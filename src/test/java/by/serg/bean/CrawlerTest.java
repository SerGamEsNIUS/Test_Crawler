package by.serg.bean;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


class CrawlerTest {

    @Test
    void getLinkDepth() {
        Crawler crawler = new Crawler();

        try {
            Field field = crawler.getClass().getDeclaredField("linkDepth");
            field.setAccessible(true);
            field.set(crawler, 10);

            assertEquals(10, Crawler.getLinkDepth(), "Field wasn't retrieved properly");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @Test
    void setLinkDepth() {
        Crawler crawler = new Crawler();

        Crawler.setLinkDepth(10);
        try {
            Field field = crawler.getClass().getDeclaredField("linkDepth");
            field.setAccessible(true);

            assertEquals(10, field.getInt(crawler), "Fields didn't match");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getPagesLimit() {
        Crawler crawler = new Crawler();

        try {
            Field field = crawler.getClass().getDeclaredField("pagesLimit");
            field.setAccessible(true);
            field.set(crawler, 30000);

            assertEquals(30000, Crawler.getPagesLimit(), "Field wasn't retrieved properly");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @Test
    void setPagesLimit() {
        Crawler crawler = new Crawler();

        Crawler.setPagesLimit(30000);
        try {
            Field field = crawler.getClass().getDeclaredField("pagesLimit");
            field.setAccessible(true);

            assertEquals(30000, field.getInt(crawler), "Fields didn't match");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getKeyWords() {
        Crawler crawler = new Crawler();
        Set<String> testSet = new HashSet<>(Arrays.asList("A", "B", "C", "D"));

        try {
            Field field = crawler.getClass().getDeclaredField("keyWords");
            field.setAccessible(true);
            field.set(crawler, testSet);

            assertEquals(testSet, Crawler.getKeyWords(), "Field wasn't retrieved properly");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @Test
    void setKeyWords() {
        Crawler crawler = new Crawler();
        Set<String> testSet = new HashSet<>(Arrays.asList("A", "B", "C", "D"));

        Crawler.setKeyWords(testSet);
        try {
            Field field = crawler.getClass().getDeclaredField("keyWords");
            field.setAccessible(true);

            assertEquals(testSet, field.get(crawler), "Fields didn't match");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getKeyWordsHits() {
        Crawler crawler = new Crawler();
        Map<String, Integer> testMap = new HashMap<>() {
            {
                put("A", 1);
                put("B", 2);
                put("C", 3);
                put("D", 4);
            }
        };


        try {
            Field field = crawler.getClass().getDeclaredField("keyWordsHits");
            field.setAccessible(true);
            field.set(crawler, testMap);

            assertEquals(testMap, Crawler.getKeyWordsHits(), "Field wasn't retrieved properly");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @Test
    void setKeyWordsHits() {
        Crawler crawler = new Crawler();
        Map<String, Integer> testMap = new HashMap<>() {
            {
                put("A", 1);
                put("B", 2);
                put("C", 3);
                put("D", 4);
            }
        };

        Crawler.setKeyWordsHits(testMap);
        try {
            Field field = crawler.getClass().getDeclaredField("keyWordsHits");
            field.setAccessible(true);

            assertEquals(testMap, field.get(crawler), "Fields didn't match");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getTotalHits() {
        Crawler crawler = new Crawler();

        try {
            Field field = crawler.getClass().getDeclaredField("totalHits");
            field.setAccessible(true);
            field.set(crawler, 1000);

            assertEquals(1000, Crawler.getTotalHits(), "Field wasn't retrieved properly");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @Test
    void setTotalHits() {
        Crawler crawler = new Crawler();

        Crawler.setTotalHits(1000);
        try {
            Field field = crawler.getClass().getDeclaredField("totalHits");
            field.setAccessible(true);

            assertEquals(1000, field.getInt(crawler), "Fields didn't match");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    void getUrl() {
        Crawler crawler = new Crawler();

        try {
            Field field = crawler.getClass().getDeclaredField("url");
            field.setAccessible(true);
            field.set(crawler, "Test string");

            assertEquals("Test string", Crawler.getUrl(), "Field wasn't retrieved properly");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    void setUrl() {
        Crawler crawler = new Crawler();

        Crawler.setUrl("Test string");
        try {
            Field field = crawler.getClass().getDeclaredField("url");
            field.setAccessible(true);

            assertEquals("Test string", field.get(crawler), "Fields didn't match");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    void checkDocumentTextForKeyWords() {
        Crawler crawler = new Crawler();
        Set<String> testSet = new HashSet<>(Arrays.asList("A", "B", "C", "D"));
        Map<String, Integer> expectedMap = new HashMap<>() {
            {
                put("A", 4);
                put("B", 4);
                put("C", 3);
                put("D", 0);
            }
        };
        String testString = "Abcd BB ACd ABCdCBA";
        int totalHitsExpected = 11;

        try {
            Field keyWordsField = crawler.getClass().getDeclaredField("keyWords");
            keyWordsField.setAccessible(true);
            keyWordsField.set(crawler, testSet);

            Field keyWordsHitsField = crawler.getClass().getDeclaredField("keyWordsHits");
            keyWordsHitsField.setAccessible(true);

            Field totalHitsField = crawler.getClass().getDeclaredField("totalHits");
            totalHitsField.setAccessible(true);

            Crawler.checkDocumentTextForKeyWords(testString);


            assertEquals(expectedMap, keyWordsHitsField.get(crawler), "keyWordsHits maps didn't match");
            assertEquals(totalHitsExpected, totalHitsField.getInt(crawler), "TotalHits field didn't match");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    @Test
    void crawl() {
        Crawler crawler = new Crawler();
        Set<String> testSet = new HashSet<>(Arrays.asList("Musk", "Elon Mask", "Tesla", "Gigafactory"));
        Map<String, Integer> expectedMap = new HashMap<>() {
            {
                put("Musk", 9);
                put("Elon Mask", 0);
                put("Tesla", 10);
                put("Gigafactory", 0);
            }
        };
        int totalHitsExpected = 19;

        try {
            Field keyWordsField = crawler.getClass().getDeclaredField("keyWords");
            keyWordsField.setAccessible(true);
            keyWordsField.set(crawler, testSet);

            Field keyWordsHitsField = crawler.getClass().getDeclaredField("keyWordsHits");
            keyWordsHitsField.setAccessible(true);

            Field totalHitsField = crawler.getClass().getDeclaredField("totalHits");
            totalHitsField.setAccessible(true);

            crawler.crawl("https://www.cnbc.com/elon-musk/");


            assertEquals(expectedMap, keyWordsHitsField.get(crawler), "keyWordsHits maps didn't match");
            assertEquals(totalHitsExpected, totalHitsField.getInt(crawler), "TotalHits field didn't match");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}