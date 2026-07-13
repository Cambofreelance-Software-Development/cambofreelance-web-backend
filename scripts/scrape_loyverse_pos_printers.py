"""
scrape_loyverse_pos_printers.py

Scrapes the "POS Printers" section from https://loyverse.com/hardware and
posts each printer as a hardware item into this project's CMS via the
existing REST endpoints:

    POST /oauth/token                 - auth
    GET  /cms/hardware-categories     - find or create the "POS Printers" category
    POST /cms/hardware-categories     -   ...create if missing
    POST /cms/hardware                - one call per printer

Usage:
    python scrape_loyverse_pos_printers.py \\
        --api http://localhost:26022 \\
        --username admin@cambofreelance.com \\
        --password Admin@1234

    # inspect scraped items without hitting the API
    python scrape_loyverse_pos_printers.py --dry-run

Requirements:
    pip install requests beautifulsoup4

Notes:
- The script only imports the "POS Printers" category to keep scope small.
  Add extra categories to CATEGORY_HEADINGS to expand.
- Uses each product's loyverse image URL as the `link` field. If you want the
  image stored in your Media library, first POST it through /cms/media then
  set `imageId` on the payload.
- Loyverse's HTML class names change over time. The parser targets stable
  hooks (headings + adjacent product blocks) rather than specific class names.
"""

from __future__ import annotations

import argparse
import json
import re
import sys
import uuid
from dataclasses import dataclass, field, asdict
from typing import Iterable, List, Optional
from urllib.parse import urljoin

import requests
from bs4 import BeautifulSoup, Tag


LOYVERSE_URL = "https://loyverse.com/hardware"
CATEGORY_NAME = "POS Printers"
# Headings on loyverse that identify the section to scrape.
CATEGORY_HEADINGS = ("pos printer", "printers for pos")

DEFAULT_CLIENT_ID = "cambofreelance-cms"
DEFAULT_CLIENT_SECRET = "secret"
CONNECTIVITY_KEYWORDS = ("Bluetooth", "USB", "Ethernet", "Wi-Fi", "WiFi", "LAN")


# ── Data model ────────────────────────────────────────────────────────────────

@dataclass
class Printer:
    name: str
    brand: Optional[str] = None
    description: Optional[str] = None
    connectivity: Optional[str] = None
    image_url: Optional[str] = None
    link: Optional[str] = None


# ── Scraping ──────────────────────────────────────────────────────────────────

def fetch_html(url: str) -> str:
    r = requests.get(url, timeout=30, headers={
        "User-Agent": "Mozilla/5.0 (compatible; CamboFreelanceCMS/1.0)",
        "Accept": "text/html,application/xhtml+xml",
    })
    r.raise_for_status()
    return r.text


def find_section(soup: BeautifulSoup, keywords: Iterable[str]) -> Optional[Tag]:
    """Locate the heading tag introducing the target section."""
    for tag in soup.find_all(re.compile(r"^h[1-4]$")):
        text = tag.get_text(" ", strip=True).lower()
        if any(k in text for k in keywords):
            return tag
    return None


def next_section_boundary(heading: Tag) -> Optional[Tag]:
    """The heading (of the same or higher level) that ends the current section."""
    level = int(heading.name[1])
    for sib in heading.find_all_next(re.compile(r"^h[1-4]$")):
        if sib is heading:
            continue
        if int(sib.name[1]) <= level:
            return sib
    return None


def between(start: Tag, end: Optional[Tag]) -> List[Tag]:
    """All descendants after `start` up to (but not including) `end`."""
    out: List[Tag] = []
    for node in start.find_all_next():
        if end is not None and node is end:
            break
        out.append(node)
    return out


def extract_connectivity(text: str) -> Optional[str]:
    found = []
    for kw in CONNECTIVITY_KEYWORDS:
        if re.search(rf"\b{re.escape(kw)}\b", text, flags=re.IGNORECASE):
            found.append("Wi-Fi" if kw.lower() in ("wi-fi", "wifi") else kw)
    # De-dup while preserving order
    seen, dedup = set(), []
    for c in found:
        if c not in seen:
            seen.add(c); dedup.append(c)
    return ",".join(dedup) if dedup else None


def parse_printers(html: str, base_url: str = LOYVERSE_URL) -> List[Printer]:
    soup = BeautifulSoup(html, "html.parser")
    heading = find_section(soup, CATEGORY_HEADINGS)
    if heading is None:
        raise RuntimeError("Could not locate the POS Printers section on the page.")
    end = next_section_boundary(heading)
    section_nodes = between(heading, end)

    printers: List[Printer] = []
    seen_names: set[str] = set()

    for img in [n for n in section_nodes if isinstance(n, Tag) and n.name == "img"]:
        card = img.find_parent(["li", "article", "div", "a"]) or img.parent
        if card is None:
            continue

        name_tag = (
            card.find(["h3", "h4", "h5"])
            or card.find(attrs={"class": re.compile(r"(title|name|product)", re.I)})
        )
        name = name_tag.get_text(" ", strip=True) if name_tag else None
        if not name:
            alt = img.get("alt")
            name = alt.strip() if alt else None
        if not name or name in seen_names:
            continue
        seen_names.add(name)

        # Description: first paragraph in the card that isn't the title
        desc = None
        for p in card.find_all(["p", "span", "div"]):
            text = p.get_text(" ", strip=True)
            if text and text != name and len(text) > 30:
                desc = text
                break

        # Brand: infer from the leading word of the name if it matches a known brand
        brand = infer_brand(name, desc or "")

        card_text = card.get_text(" ", strip=True)
        connectivity = extract_connectivity(card_text)

        image_src = img.get("src") or img.get("data-src") or ""
        if image_src.startswith("//"):
            image_src = "https:" + image_src
        elif image_src.startswith("/"):
            image_src = urljoin(base_url, image_src)

        link = None
        anchor = card if card.name == "a" else card.find("a", href=True)
        if isinstance(anchor, Tag) and anchor.has_attr("href"):
            link = urljoin(base_url, anchor["href"])

        printers.append(Printer(
            name=name,
            brand=brand,
            description=desc,
            connectivity=connectivity,
            image_url=image_src or None,
            link=link,
        ))

    return printers


KNOWN_BRANDS = (
    "Star Micronics", "Star", "Epson", "Sam4s", "Citizen", "Seiko", "SPRT",
    "GP", "Xprinter", "Posiflex", "Bematech", "Zebra", "Honeywell",
)


def infer_brand(name: str, description: str) -> Optional[str]:
    haystack = f"{name} {description}"
    for b in KNOWN_BRANDS:
        if re.search(rf"\b{re.escape(b)}\b", haystack):
            return b
    return None


# ── Backend client ────────────────────────────────────────────────────────────

class CmsClient:
    def __init__(self, base: str, token: str):
        self.base = base.rstrip("/")
        self.token = token

    @classmethod
    def login(cls, base: str, username: str, password: str,
              client_id: str = DEFAULT_CLIENT_ID,
              client_secret: str = DEFAULT_CLIENT_SECRET) -> "CmsClient":
        r = requests.post(f"{base.rstrip('/')}/oauth/token", timeout=30, json={
            "grant_type": "password",
            "username": username,
            "password": password,
            "client_id": client_id,
            "client_secret": client_secret,
            "scope": "openid",
            "application_type": "WEB",
            "device_id": str(uuid.uuid4()),
        })
        r.raise_for_status()
        payload = r.json()
        data = payload.get("data") or {}
        token = data.get("access_token")
        if not token:
            raise RuntimeError(f"No access_token in login response: {payload}")
        return cls(base, token)

    def _headers(self) -> dict:
        return {"Authorization": f"Bearer {self.token}"}

    def ensure_category(self, name: str) -> str:
        r = requests.get(f"{self.base}/cms/hardware-categories?all=true",
                         headers=self._headers(), timeout=30)
        r.raise_for_status()
        items = r.json().get("data") or []
        for c in items:
            if c.get("name", "").strip().lower() == name.lower():
                return c["id"]
        r = requests.post(f"{self.base}/cms/hardware-categories",
                          headers=self._headers(), timeout=30,
                          json={"name": name, "sortOrder": 1})
        r.raise_for_status()
        return r.json()["data"]["id"]

    def create_hardware(self, category_id: str, p: Printer, sort_order: int) -> dict:
        payload = {
            "name": p.name,
            "brand": p.brand,
            "description": p.description,
            "connectivity": p.connectivity,
            "platform": "iOS,Android",
            "categoryId": category_id,
            "link": p.link or p.image_url,
            "sortOrder": sort_order,
        }
        r = requests.post(f"{self.base}/cms/hardware",
                          headers=self._headers(), timeout=30, json=payload)
        r.raise_for_status()
        return r.json()


# ── CLI ───────────────────────────────────────────────────────────────────────

def main() -> int:
    ap = argparse.ArgumentParser(description=__doc__)
    ap.add_argument("--api", default="http://localhost:26022",
                    help="Backend base URL (default: %(default)s)")
    ap.add_argument("--username", help="Admin username / email")
    ap.add_argument("--password", help="Admin password")
    ap.add_argument("--client-id", default=DEFAULT_CLIENT_ID)
    ap.add_argument("--client-secret", default=DEFAULT_CLIENT_SECRET)
    ap.add_argument("--dry-run", action="store_true",
                    help="Print scraped items instead of POSTing to the API")
    ap.add_argument("--source-url", default=LOYVERSE_URL,
                    help="Override the page to scrape (default: %(default)s)")
    args = ap.parse_args()

    print(f"[scrape] Fetching {args.source_url}")
    html = fetch_html(args.source_url)
    printers = parse_printers(html, base_url=args.source_url)
    print(f"[scrape] Found {len(printers)} POS printer items")

    if args.dry_run:
        print(json.dumps([asdict(p) for p in printers], indent=2, ensure_ascii=False))
        return 0

    if not args.username or not args.password:
        ap.error("--username and --password are required unless --dry-run is set")

    print(f"[api] Logging in to {args.api}")
    client = CmsClient.login(args.api, args.username, args.password,
                             args.client_id, args.client_secret)
    category_id = client.ensure_category(CATEGORY_NAME)
    print(f"[api] Category '{CATEGORY_NAME}' -> {category_id}")

    ok = fail = 0
    for i, p in enumerate(printers, start=1):
        try:
            client.create_hardware(category_id, p, sort_order=i)
            ok += 1
            print(f"  + [{i:02d}] {p.name}")
        except Exception as e:
            fail += 1
            print(f"  ! [{i:02d}] {p.name} -> {e}", file=sys.stderr)

    print(f"[done] created={ok} failed={fail}")
    return 0 if fail == 0 else 1


if __name__ == "__main__":
    raise SystemExit(main())
