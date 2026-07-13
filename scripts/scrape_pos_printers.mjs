#!/usr/bin/env node
/**
 * scrape_pos_printers.mjs
 *
 * Scrapes the names and details of every POS printer listed on the hardware
 * page at:
 *
 *     http://localhost:3000/en/hardware
 *
 * HOW THIS WORKS
 * --------------
 * That page is a client-rendered Next.js app: its server HTML is just a shell,
 * and the printer cards are fetched in the browser from this project's public
 * backend endpoint:
 *
 *     GET /hardware      ->  { data: [ { name, brand, description, ... } ] }   (no auth)
 *
 * So instead of driving a headless browser to render JavaScript, this script
 * pulls the exact same JSON the page renders from, then keeps only the items
 * whose category is "POS Printers". Same data the page shows, but robust and
 * fast (no browser, no HTML parsing).
 *
 * If you specifically need to scrape the rendered DOM of the :3000 page instead
 * (e.g. the API is not reachable), see the note at the bottom of this file for a
 * Playwright variant.
 *
 * USAGE
 * -----
 *   node scripts/scrape_pos_printers.mjs                 # pretty table (default)
 *   node scripts/scrape_pos_printers.mjs --format json   # full JSON
 *   node scripts/scrape_pos_printers.mjs --format csv     > printers.csv
 *   node scripts/scrape_pos_printers.mjs --api http://localhost:26022
 *   node scripts/scrape_pos_printers.mjs --category "POS Printers"
 *   node scripts/scrape_pos_printers.mjs --lang km       # Khmer name/description
 *
 * Requirements: Node 18+ (uses built-in global fetch). No npm install needed.
 */

import { parseArgs } from "node:util";

const PAGE_URL = "http://localhost:3000/en/hardware"; // the page these items appear on
const DEFAULT_API = "http://localhost:26022"; // backend that the page fetches from
const DEFAULT_CATEGORY = "POS Printers";

const { values: opts } = parseArgs({
  options: {
    api: { type: "string", default: DEFAULT_API },
    category: { type: "string", default: DEFAULT_CATEGORY },
    format: { type: "string", default: "table" }, // table | json | csv
    lang: { type: "string", default: "en" }, // en | km
    help: { type: "boolean", default: false },
  },
});

if (opts.help) {
  console.log(
    `Scrape POS printers listed on ${PAGE_URL}\n\n` +
      `Options:\n` +
      `  --api <url>        Backend base URL (default: ${DEFAULT_API})\n` +
      `  --category <name>  Category to keep (default: "${DEFAULT_CATEGORY}")\n` +
      `  --format <fmt>     table | json | csv (default: table)\n` +
      `  --lang <en|km>     Language for name/description (default: en)\n` +
      `  --help             Show this help\n`,
  );
  process.exit(0);
}

const apiBase = opts.api.replace(/\/+$/, "");
const endpoint = `${apiBase}/hardware`;

/** Fetch the public hardware list that backs the page. */
async function fetchHardware() {
  let res;
  try {
    res = await fetch(endpoint, {
      headers: { Accept: "application/json" },
    });
  } catch (err) {
    throw new Error(
      `Could not reach ${endpoint} (${err.message}).\n` +
        `Is the backend running? The ${PAGE_URL} page loads its data from there.`,
    );
  }
  if (!res.ok) {
    throw new Error(`GET ${endpoint} returned HTTP ${res.status}`);
  }
  const body = await res.json();
  const items = Array.isArray(body) ? body : (body.data ?? body.content ?? []);
  if (!Array.isArray(items)) {
    throw new Error(`Unexpected response shape from ${endpoint}`);
  }
  return items;
}

/** Reduce a raw hardware record to the fields worth reporting. */
function toPrinter(item, lang) {
  const kh = lang === "km";
  return {
    name: (kh && item.nameKh) || item.name || null,
    brand: item.brand || null,
    description: (kh && item.descriptionKh) || item.description || null,
    connectivity: item.connectivity || null,
    platform: item.platform || null,
    countries: item.countries || null,
    releaseDate: item.releaseDate || null,
    category: item.category?.name || null,
    image: item.image?.url || item.image?.link || item.link || null,
    link: item.link || null,
    sortOrder: item.sortOrder ?? null,
    status: item.status || null,
  };
}

function csvEscape(v) {
  if (v == null) return "";
  const s = String(v);
  return /[",\n]/.test(s) ? `"${s.replace(/"/g, '""')}"` : s;
}

function printTable(printers) {
  console.log(`\nPOS printers on ${PAGE_URL}  (via ${endpoint})\n`);
  printers.forEach((p, i) => {
    console.log(`${String(i + 1).padStart(2, " ")}. ${p.name}`);
    if (p.brand) console.log(`    brand:        ${p.brand}`);
    if (p.description) console.log(`    description:  ${p.description}`);
    if (p.connectivity) console.log(`    connectivity: ${p.connectivity}`);
    if (p.platform) console.log(`    platform:     ${p.platform}`);
    if (p.countries) console.log(`    countries:    ${p.countries}`);
    if (p.releaseDate) console.log(`    released:     ${p.releaseDate}`);
    if (p.link) console.log(`    link:         ${p.link}`);
    console.log("");
  });
  console.log(`Total: ${printers.length} POS printer(s).`);
}

// ── main ────────────────────────────────────────────────────────────────────
try {
  const all = await fetchHardware();
  const wanted = opts.category.trim().toLowerCase();
  const printers = all
    .filter((it) => (it.category?.name ?? "").trim().toLowerCase() === wanted)
    .map((it) => toPrinter(it, opts.lang))
    .sort((a, b) => (a.sortOrder ?? 0) - (b.sortOrder ?? 0));

  if (printers.length === 0) {
    console.error(
      `No items found in category "${opts.category}". ` +
        `Categories seen: ${[...new Set(all.map((i) => i.category?.name).filter(Boolean))].join(", ") || "(none)"}`,
    );
    process.exit(2);
  }

  if (opts.format === "json") {
    console.log(JSON.stringify(printers, null, 2));
  } else if (opts.format === "csv") {
    const cols = [
      "name",
      "brand",
      "description",
      "connectivity",
      "platform",
      "countries",
      "releaseDate",
      "link",
    ];
    console.log(cols.join(","));
    for (const p of printers) {
      console.log(cols.map((c) => csvEscape(p[c])).join(","));
    }
  } else {
    printTable(printers);
  }
} catch (err) {
  console.error(`[error] ${err.message}`);
  process.exit(1);
}

/*
 * ─────────────────────────────────────────────────────────────────────────────
 * OPTIONAL: scrape the rendered :3000 DOM instead of the API.
 *
 * Only needed if you must read exactly what the browser paints (e.g. the API is
 * private). Requires:  npm i playwright && npx playwright install chromium
 *
 *   import { chromium } from "playwright";
 *   const browser = await chromium.launch();
 *   const page = await browser.newPage();
 *   await page.goto("http://localhost:3000/en/hardware", { waitUntil: "networkidle" });
 *   // Anchor on the "POS Printers" section heading, then read its product cards:
 *   const printers = await page.evaluate(() => {
 *     const norm = (s) => (s || "").replace(/\s+/g, " ").trim();
 *     const heading = [...document.querySelectorAll("h1,h2,h3,h4")]
 *       .find((h) => /pos printer/i.test(h.textContent));
 *     const section = heading?.closest("section") ?? document;
 *     return [...section.querySelectorAll("[class*='card'], article, li")]
 *       .map((el) => norm(el.textContent))
 *       .filter(Boolean);
 *   });
 *   console.log(printers);
 *   await browser.close();
 * ─────────────────────────────────────────────────────────────────────────────
 */
