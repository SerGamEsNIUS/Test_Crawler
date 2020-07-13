package by.serg.services;

import au.com.bytecode.opencsv.CSVReader;
import by.serg.bean.Crawler;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Class represents UI to work with {@link by.serg.bean.Crawler},
 * change it's options and write or read results from CSV file "results.csv".
 *
 *
 * @author      Eugene Sergeev
 */

public class Console {
    private static Scanner defaultScanner = new Scanner(System.in);
    private static Crawler crawler = new Crawler();

    /**
     *  Method initiates starting menu of application and offers simple navigation to start,
     *  change options, or print results restored in "results.csv"
     */
    public static void initiate() {
        System.out.println(
                "   Welcome to crawler by sergamesnius \n" +
                        "   1. Start \n" +
                        "   2. Options \n" +
                        "   3. Print top pages by total hits \n" +
                        "   4. Exit \n" +
                        "------======------"


        );
        try {
            int input = defaultScanner.nextInt();
            switch (input) {
                case 1:
                    System.out.println("Input URL link");
                    Console.start(defaultScanner.next());
                    break;
                case 2:
                    Console.options();
                    break;
                case 3:
                    Console.printResultsFromCSV();
                    break;
                case 4:
                    return;
            }
        } catch (NoSuchElementException e) {
            System.err.println("Incorrect input");
            defaultScanner.next();
        }
        Console.initiate();
    }

    /**
     * Method offers simple UI to set variables in {@code Crawler}
     */
    private static void options() {
        System.out.println(
                "   1. Set pages limit \n" +
                        "   2. Set link depth \n" +
                        "   3. Set key words \n" +
                        "   4. Back \n" +
                        "------======------");
        int input = defaultScanner.nextInt();
        switch (input) {
            case 1:
                System.out.println(
                        "   Current pages limit: " + Crawler.getPagesLimit() + "\n" +
                                "   Input pages limit");
                setPagesLimit();
                break;

            case 2:
                System.out.println(
                        "   Current link depth: " + Crawler.getLinkDepth() + "\n" +
                                "   Input link depth");
                setLinkDepth();
                break;

            case 3:
                System.out.println("  Current key words: ");
                Crawler.getKeyWords().forEach(keyWord -> System.out.println(keyWord + ";"));
                setKeyWords();
                break;
            case 4:
                return;
        }
        options();
    }

    /**
     * Method uses given starting url to crawl and prints results received from run.
     * @param url represents starting point for {@code Crawler} to begin with.
     */
    public static void start(String url) {
        crawler.crawl(url);
        Crawler.setUrl(url);
        printResults();
        if(!Crawler.getKeyWordsHits().isEmpty()) {
            writeToCSV();
        }
    }

    /**
     * Method prints results received from run.
     */

    public static void printResults() {
        System.out.println("    Results: ");
        Crawler.getKeyWordsHits().forEach((keyWord, hitsNumber) -> System.out.println(keyWord + " : " + hitsNumber + ";"));
        System.out.println("    Total hits: " + Crawler.getTotalHits());

    }

    public static void setKeyWords() {
        System.out.println("   Type key word and press ENTER to add (Current words will be deleted). Type EXIT to return");
        Set<String> keyWords = new HashSet<>();
        while (true) {
            String input = defaultScanner.nextLine();
            if (input.equals("EXIT") && !keyWords.isEmpty()) {
                Crawler.setKeyWords(keyWords);
                return;
            } else if (input.equals("EXIT")) {
                return;
            }
            if(!input.equals("")) {
                keyWords.add(input);
            }
        }
    }

    public static void setLinkDepth() {
        try {
            int input = defaultScanner.nextInt();
            if (input <= 0) {
                throw new IllegalArgumentException();
            } else {
                Crawler.setLinkDepth(input);
                System.out.println("   Link depth = " + Crawler.getLinkDepth() + " now");
                return;
            }
        } catch (IllegalArgumentException ex) {
            System.err.println("Input positive number");
        }
        setLinkDepth();
    }

    public static void setPagesLimit() {
        try {
            int input = defaultScanner.nextInt();
            if (input <= 0) {
                throw new IllegalArgumentException();
            } else {
                Crawler.setPagesLimit(input);
                System.out.println("   Pages limit = " + Crawler.getPagesLimit() + " now");
                return;
            }
        } catch (IllegalArgumentException ex) {
            System.err.println("Input positive number");
        }
        setPagesLimit();
    }

    /**
     * Method writes results received from run into "results.csv"
     */

    public static void writeToCSV() {
        final String CSV_SEPARATOR = ",";
        final String QUOTE = "\"";
        if (!Crawler.getKeyWordsHits().isEmpty()) {
            try (BufferedWriter bufferedWriter =
                         new BufferedWriter(
                                 new OutputStreamWriter(
                                         new FileOutputStream("results.csv", true), StandardCharsets.UTF_8))) {

                Crawler.getKeyWordsHits().forEach((keyWord, hitsNumber) -> {
                    String oneLine = QUOTE + keyWord + QUOTE +
                            CSV_SEPARATOR +
                            hitsNumber;
                    try {
                        bufferedWriter.write(oneLine);
                        bufferedWriter.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                String lastLine = QUOTE + Crawler.getUrl() + QUOTE + CSV_SEPARATOR + Crawler.getTotalHits();
                bufferedWriter.write(lastLine);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Crawler.setKeyWordsHits(new HashMap<>());
        Crawler.setTotalHits(0);
    }

    /**
     * Method reads "result.csv" and prints URL links
     * of all success runs sorted by total hits.
     */

    public static void printResultsFromCSV() {
        try (CSVReader reader = new CSVReader(new FileReader("results.csv"), ',', '"', 0)) {
            Map<String, Integer> results = new HashMap<>();
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                if (nextLine[0].contains("http")) {
                    results.put(nextLine[0], Integer.valueOf(nextLine[1]));
                }
            }
            results.
                    entrySet().
                    stream().
                    sorted(Map.Entry.<String, Integer>comparingByValue().reversed()).
                    forEach(entry -> System.out.println(entry.getKey() + ", " + entry.getValue()));
        } catch (IOException e) {
            System.err.println("Can't find results.csv");
        }
    }

    private Console() {
    }
}
