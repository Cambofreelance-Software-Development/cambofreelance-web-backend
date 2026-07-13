package com.cambofreelance.webbackend.scripts;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * One-off scraper. Pulls the product catalog from https://loyverse.com/hardware and
 * writes a Flyway migration (V39) that inserts each product — name, description,
 * brand, connectivity, price, image URL, link — into public.hardware, following
 * the exact pattern established in V25__insert_hardware_dummy_data.sql
 * (VALUES(...) JOIN public.category_hardware c ON c.name = v.category_name).
 *
 * Depends on V38__extend_hardware_for_loyverse.sql having run first — that migration
 * adds the image_url column and seeds the extra categories (Mobile Printers,
 * Label Printers, Android POS Terminals, Tablet Stands) that Loyverse groups by.
 *
 * Run once from a terminal, then start the app so Flyway applies V39:
 *   ./gradlew -q compileJava
 *   java -cp build/classes/java/main;build/libs/*  com.cambofreelance.webbackend.scripts.LoyverseHardwareScraper
 *
 * Not a Spring bean and not invoked at app startup on purpose — Flyway must not
 * depend on network access at migration time.
 */
public final class LoyverseHardwareScraper {

    private static final String SOURCE_URL = "https://loyverse.com/hardware";
    private static final Path OUTPUT_SQL = Paths.get(
        "src", "main", "resources", "db", "migration", "V39__insert_loyverse_hardware.sql");

    /** Map from a Loyverse heading (lower-cased, contains-match) to the category_hardware.name
     *  seeded in V25 or V38. Order matters — first match wins, so more specific keys come first. */
    private static final Map<String, String> CATEGORY_ALIASES = new LinkedHashMap<>() {{
        put("mobile printer",        "Mobile Printers");
        put("label printer",         "Label Printers");
        put("pos printer",           "POS Printers");
        put("printers for pos",      "POS Printers");
        put("barcode scanner",       "Barcode Scanners");
        put("cash drawer",           "Cash Drawers");
        put("android pos terminal",  "Android POS Terminals");
        put("android pos",           "Android POS Terminals");
        put("card reader",           "Card Readers");
        put("tablet stand",          "Tablet Stands");
    }};

    private static final List<String> KNOWN_BRANDS = List.of(
        "Star Micronics", "Star", "Epson", "Sam4s", "Citizen", "Seiko", "SPRT",
        "Xprinter", "Posiflex", "Bematech", "Zebra", "Honeywell", "Socket Mobile", "SumUp");

    private static final List<String> CONNECTIVITY_KEYWORDS = List.of(
        "Bluetooth", "USB", "Ethernet", "Wi-Fi", "WiFi", "LAN");

    /** Matches "$149", "$149.99", "USD 149", "EUR 99,90", "£79", "Starting from $199" etc. */
    private static final Pattern PRICE_PATTERN = Pattern.compile(
        "(?:(?:USD|EUR|GBP|CAD|AUD)\\s*|[$€£¥])\\s*\\d{1,6}(?:[.,]\\d{1,2})?",
        Pattern.CASE_INSENSITIVE);

    private LoyverseHardwareScraper() {}

    public static void main(String[] args) throws IOException {
        System.out.println("[scrape] GET " + SOURCE_URL);
        Document doc = Jsoup.connect(SOURCE_URL)
            .userAgent("Mozilla/5.0 (compatible; CamboFreelanceCMS/1.0)")
            .timeout(30_000)
            .get();

        List<ScrapedProduct> products = parse(doc);
        System.out.println("[scrape] " + products.size() + " products across "
            + products.stream().map(p -> p.category).distinct().count() + " categories");

        String sql = renderMigration(products);
        Files.createDirectories(OUTPUT_SQL.getParent());
        Files.writeString(OUTPUT_SQL, sql, StandardCharsets.UTF_8);
        System.out.println("[write] " + OUTPUT_SQL.toAbsolutePath());
    }

    // ── Parsing ──────────────────────────────────────────────────────────────

    static List<ScrapedProduct> parse(Document doc) {
        List<ScrapedProduct> out = new ArrayList<>();
        Elements all = doc.getAllElements();
        Elements headings = doc.select("h1, h2, h3, h4");

        for (int i = 0; i < headings.size(); i++) {
            Element heading = headings.get(i);
            String canonical = matchCategory(heading.text());
            if (canonical == null) continue;

            int startIdx = all.indexOf(heading);
            Element endHeading = nextSameOrHigherLevel(headings, i);
            int endIdx = endHeading == null ? all.size() : all.indexOf(endHeading);

            Set<String> seenNames = new LinkedHashSet<>();
            int sort = 1;

            for (int j = startIdx + 1; j < endIdx; j++) {
                Element el = all.get(j);
                if (!"img".equals(el.tagName())) continue;

                Element card = findCard(el);
                if (card == null) continue;

                String name = pickName(card, el);
                if (name == null || name.isBlank() || !seenNames.add(name)) continue;

                out.add(ScrapedProduct.builder()
                    .name(name)
                    .brand(inferBrand(name, card.text()))
                    .description(pickDescription(card, name))
                    .price(extractPrice(card.text()))
                    .connectivity(extractConnectivity(card.text()))
                    .imageUrl(absoluteUrl(el, "src"))
                    .link(pickLink(card))
                    .category(canonical)
                    .sortOrder(sort++)
                    .build());
            }
        }
        return out;
    }

    private static String matchCategory(String headingText) {
        String lower = headingText.toLowerCase(Locale.ROOT);
        for (Map.Entry<String, String> e : CATEGORY_ALIASES.entrySet()) {
            if (lower.contains(e.getKey())) return e.getValue();
        }
        return null;
    }

    private static Element nextSameOrHigherLevel(Elements headings, int fromIdx) {
        int level = Integer.parseInt(headings.get(fromIdx).tagName().substring(1));
        for (int j = fromIdx + 1; j < headings.size(); j++) {
            int lvl = Integer.parseInt(headings.get(j).tagName().substring(1));
            if (lvl <= level) return headings.get(j);
        }
        return null;
    }

    private static Element findCard(Element img) {
        Element p = img.parent();
        while (p != null && !List.of("li", "article", "div", "a").contains(p.tagName())) {
            p = p.parent();
        }
        return p;
    }

    private static String pickName(Element card, Element img) {
        Element title = card.selectFirst("h3, h4, h5");
        if (title != null && !title.text().isBlank()) return title.text().trim();
        Element byClass = card.selectFirst("[class*=title], [class*=name], [class*=product]");
        if (byClass != null && !byClass.text().isBlank()) return byClass.text().trim();
        String alt = img.attr("alt");
        return alt == null || alt.isBlank() ? null : alt.trim();
    }

    private static String pickDescription(Element card, String name) {
        for (Element p : card.select("p, span, div")) {
            String text = p.text().trim();
            if (text.length() > 30 && !text.equals(name)) return text;
        }
        return null;
    }

    private static String pickLink(Element card) {
        Element a = "a".equals(card.tagName()) ? card : card.selectFirst("a[href]");
        return a == null ? null : absoluteUrl(a, "href");
    }

    private static String absoluteUrl(Element el, String attr) {
        String abs = el.absUrl(attr);
        if (!abs.isBlank()) return abs;
        String raw = el.attr(attr);
        return raw.isBlank() ? null : raw;
    }

    private static String inferBrand(String name, String extra) {
        String haystack = (name + " " + extra);
        for (String b : KNOWN_BRANDS) {
            if (Pattern.compile("\\b" + Pattern.quote(b) + "\\b").matcher(haystack).find()) {
                return b;
            }
        }
        return null;
    }

    private static String extractPrice(String text) {
        Matcher m = PRICE_PATTERN.matcher(text);
        return m.find() ? m.group().trim().replaceAll("\\s+", " ") : null;
    }

    private static String extractConnectivity(String text) {
        Set<String> found = new LinkedHashSet<>();
        for (String kw : CONNECTIVITY_KEYWORDS) {
            if (Pattern.compile("\\b" + Pattern.quote(kw) + "\\b", Pattern.CASE_INSENSITIVE).matcher(text).find()) {
                found.add(kw.equalsIgnoreCase("wifi") ? "Wi-Fi" : kw);
            }
        }
        return found.isEmpty() ? null : String.join(",", found);
    }

    // ── SQL rendering ────────────────────────────────────────────────────────

    static String renderMigration(List<ScrapedProduct> products) {
        StringBuilder sb = new StringBuilder(4096);
        sb.append("-- Seed hardware products scraped from ").append(SOURCE_URL).append('\n');
        sb.append("-- Generated by com.cambofreelance.webbackend.scripts.LoyverseHardwareScraper\n\n");

        if (products.isEmpty()) {
            sb.append("-- No products scraped. Migration intentionally left empty.\n");
            return sb.toString();
        }

        sb.append("INSERT INTO public.hardware (id, name, name_kh, brand, description, description_kh, connectivity, price, image_url, category_id, icon, link, sort_order)\n");
        sb.append("SELECT gen_random_uuid()::text, v.name, v.name_kh, v.brand, v.description, v.description_kh, v.connectivity, v.price, v.image_url, c.id, v.icon, v.link, v.sort_order\n");
        sb.append("FROM (VALUES\n");
        for (int i = 0; i < products.size(); i++) {
            ScrapedProduct p = products.get(i);
            sb.append("    (")
                .append(sqlString(p.name)).append(", ")
                .append("NULL").append(", ")            // name_kh
                .append(sqlString(p.brand)).append(", ")
                .append(sqlString(p.description)).append(", ")
                .append("NULL").append(", ")            // description_kh
                .append(sqlString(p.connectivity)).append(", ")
                .append(sqlString(p.price)).append(", ")
                .append(sqlString(p.imageUrl)).append(", ")
                .append(sqlString(p.category)).append(", ")
                .append("NULL").append(", ")            // icon
                .append(sqlString(p.link)).append(", ")
                .append(p.sortOrder)
                .append(')');
            sb.append(i == products.size() - 1 ? "\n" : ",\n");
        }
        sb.append(") AS v(name, name_kh, brand, description, description_kh, connectivity, price, image_url, category_name, icon, link, sort_order)\n");
        sb.append("JOIN public.category_hardware c ON c.name = v.category_name;\n");
        return sb.toString();
    }

    private static String sqlString(String value) {
        if (value == null) return "NULL";
        return "'" + value.replace("'", "''") + "'";
    }

    // ── Model ────────────────────────────────────────────────────────────────

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static class ScrapedProduct {
        private String name;
        private String brand;
        private String description;
        private String price;
        private String connectivity;
        private String imageUrl;
        private String link;
        private String category;
        private int sortOrder;
    }
}
