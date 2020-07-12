package by.serg.bean;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.util.*;
/**
 * Class that traverses websites following predefined link depth (8 by default)
 * and max visited pages limit (10000 by default)
 * collecting information of how many times given keywords appeared on web-pages.
 * The {@code Crawler} starts from predefined URL and follows links found to dive deeper.
 *
 *
 * @author      Eugene Sergeev
 */

public class Crawler {
    private static String url;
    /**
     * Amount of hyperlinks one crawler can go through.
     */
    private static int linkDepth = 8;
    /**
     * Amount of pages can be visited in one run.
     */
    private static int pagesLimit = 10000;
    private int currentLinkDepth = 0;
    private static int currentPages = 0;
    /**
     * Default keywords the crawler looking for.
     */
    private static Set<String> keyWords = new HashSet<>(Arrays.asList("Musk", "Tesla", "Gigafactory", "Elon Mask"));
    private static Map<String, Integer> keyWordsHits = new HashMap<>();
    private static int totalHits = 0;

    /**
     * Crawler starts from given URL and counts amount of {@keyWords} on page of given address
     * and pages available via hyperlinks on current page.
     * @param httpLink means starting page from which crawler begins to search given key words.
     */
    public void crawl(String httpLink) {
        currentLinkDepth++;
        if (this.currentLinkDepth <= linkDepth && currentPages <= pagesLimit) {
            Document document;
            try {
                document = Jsoup.connect(httpLink).get();
            } catch (IOException e) {
                System.err.println("Current Url is unavailable");
                return;
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid link");
                return;
            }
            String documentText = document.body().wholeText();
            checkDocumentTextForKeyWords(documentText);
            currentPages++;
            Set<Element> links = new HashSet<>();
            document.select("a").addAll(links);
            links.forEach(link -> new Crawler(this.currentLinkDepth).crawl(link.toString()));
        }
    }

    public Crawler(int currentLinkDepth) {
        this.currentLinkDepth = currentLinkDepth;
    }

    public Crawler() {
    }

    public static void checkDocumentTextForKeyWords(String documentText) {
        keyWords.forEach(keyWord -> {
            totalHits += StringUtils.countMatches(documentText, keyWord);
            keyWordsHits.put(keyWord, StringUtils.countMatches(documentText, keyWord));
        });
    }

    public static int getLinkDepth() {
        return linkDepth;
    }

    public static void setLinkDepth(int linkDepth) {
        Crawler.linkDepth = linkDepth;
    }

    public static int getPagesLimit() {
        return pagesLimit;
    }

    public static void setPagesLimit(int pagesLimit) {
        Crawler.pagesLimit = pagesLimit;
    }

    public static Set<String> getKeyWords() {
        return keyWords;
    }

    public static void setKeyWords(Set<String> keyWords) {
        Crawler.keyWords = keyWords;
    }


    public static Map<String, Integer> getKeyWordsHits() {
        return keyWordsHits;
    }

    public static void setKeyWordsHits(Map<String, Integer> keyWordsHits) {
        Crawler.keyWordsHits = keyWordsHits;
    }

    public static int getTotalHits() {
        return totalHits;
    }

    public static void setTotalHits(int totalHits) {
        Crawler.totalHits = totalHits;
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        Crawler.url = url;
    }
}
