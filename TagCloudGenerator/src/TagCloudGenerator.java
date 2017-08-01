import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Comparator;

import components.map.Map;
import components.map.Map2;
import components.queue.Queue;
import components.queue.Queue2;
import components.sortingmachine.SortingMachine;
import components.sortingmachine.SortingMachine5;
import components.stack.Stack;
import components.stack.Stack2;

/**
 * Generates a "tag cloud" of N terms pulled from the given input file.
 *
 * @author Samuel Smith
 * @author Yunan Zhang
 */
public final class TagCloudGeneratorOSU {

    /**
     * Private constructor to prevent instantiation.
     */
    private TagCloudGeneratorOSU() {
    }
    /*
     * ------PRIVATE MEMBERS
     */

    /**
     * Maximum number of times program will prompt user to enter a positive
     * integer for N.
     */
    private static final int MAX_RETRIES = 10;

    /**
     * The base font size, in this case, 11 for f11 in CSS file.
     */
    private static final int BASE_FONT_SIZE = 11;

    /**
     * The maximum font size, in this case, 48 for f48 in CSS file.
     */
    private static final int MAX_FONT_SIZE = 48;

    /**
     * Array of characters that act as separators.
     */
    private static final char[] SEPS = " \t\n\r-.!?[]';:/()".toCharArray();

    /**
     * Comparator class to sort Map.Pair's alphabetically by their key,
     * regardless of case.
     */
    private static class Alphabetical
            implements Comparator<Map.Pair<String, Integer>>, Serializable {
        private static final long serialVersionUID = 0;

        @Override
        public int compare(Map.Pair<String, Integer> s1,
                Map.Pair<String, Integer> s2) {
            return s1.key().compareToIgnoreCase(s2.key());
        }
    }

    /**
     * Comparator class to sort Map.Pairs numerically by their value.
     */
    private static class Numeric
            implements Comparator<Map.Pair<String, Integer>>, Serializable {
        private static final long serialVersionUID = 0;

        @Override
        public int compare(Map.Pair<String, Integer> s1,
                Map.Pair<String, Integer> s2) {
            return s2.value() - s1.value();
        }
    }

    /*
     * ------PUBLIC METHODS
     */

    /**
     * Main method, prompts user for input file, output file, and number of tags
     * to generate.
     *
     * @param args
     *            Command line arguments
     */
    public static void main(String[] args) {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(System.in));

        System.out.print("Enter location of input file: ");
        String inFile = null;
        try {
            inFile = in.readLine();
        } catch (IOException e) {
            System.err.println("Error reading from input");
        }

        /*
         * Ask for output file
         */
        System.out.print("Enter location of output file: ");
        String outFile = null;
        try {
            outFile = in.readLine();
        } catch (IOException e) {
            System.err.println("Error reading from input");
        }

        /*
         * Ask for number of words to be included
         */
        int numOfWords = 0;
        boolean validInput = false;
        int retries = 0;

        /*
         * Continue asking until the input is valid or the maximum number of
         * attempts has been reached
         */
        while (!validInput && retries < MAX_RETRIES) {
            System.out.print(
                    "Enter how many words should be included in the tag cloud: ");
            String input = null;
            try {
                input = in.readLine();
            } catch (IOException e) {
                System.err.println("Error reading from input");
            }
            try {
                numOfWords = Integer.parseInt(input);
                validInput = true;
            } catch (NumberFormatException nf) {
                /*
                 * This will catch any non-integer errors thrown by
                 * Integer.parseInt()
                 */
                System.out.println("ERROR: Invalid input, \"" + input
                        + "\" is not an integer.");
                retries++;
            }

            /*
             * This will catch any negatives numbers entered
             */
            if (numOfWords < 0) {
                validInput = false;
                System.out.println(
                        "ERROR: " + numOfWords + " is not a positive integer.");
                retries++;
            }
        }

        /*
         * If the maximum attempts have not been reached, proceed with
         * processing. Otherwise, print a fatal error.
         */
        if (retries < MAX_RETRIES) {
            generateTagCloud(inFile, outFile, numOfWords);
            System.out.println("SUCCESS: Tag cloud successfully generated.");
        } else {
            System.out
                    .println("FATAL: Too many input attempts failed. Closing.");
        }

        try {
            in.close();
        } catch (IOException e) {
            System.out.println("Cannot close input stream");
        }
        System.out.close();
    }

    /**
     * Generates a tag cloud with input from {@code inputFile} with the top
     * {@code numberOfTags} words and saves it in {@code outputFile}.
     *
     * @param inputFile
     *            file to load words from
     * @param outputFile
     *            location of output html file
     * @param numberOfTags
     *            number of top words to use as tags
     */
    public static void generateTagCloud(String inputFile, String outputFile,
            int numberOfTags) {
        /*
         * Create the input and output streams
         */
        BufferedReader inFile = null;
        try {
            inFile = new BufferedReader(new FileReader(inputFile));
        } catch (IOException e) {
            System.err.println("Error opening file");
        }
        PrintWriter outFile = null;
        try {
            outFile = new PrintWriter(
                    new BufferedWriter(new FileWriter(outputFile)));

        } catch (IOException e) {
            System.err.println("Error opening file");
            System.exit(0);
        }
        /*
         * Create a map for the tags, load the words from input file
         */
        Map<String, Integer> tags = new Map2<String, Integer>();
        generateTagMap(inFile, tags);

        /*
         * Create a queue for the sorted keys
         */
        Queue<String> sortedKeys = new Queue2<String>();

        /*
         * Load numberOfTags and sort
         */
        generateSortedTags(sortedKeys, tags, numberOfTags);

        /*
         * Generate the HTML headers (including titles)
         */
        generateHTMLHeaders(outFile, numberOfTags, inputFile);

        /*
         * Print the top tags
         */
        printTags(outFile, tags, sortedKeys);

        /*
         * Print the HTML footers
         */
        generateHTMLFooters(outFile);

        outFile.close();
        try {
            inFile.close();
        } catch (IOException e) {
            System.err.println("Error closing file");
        }
    }

    /**
     * Prints the headers for the HTML file and uses "Top {@code n} words in
     * {@code source}" as the title and header.
     *
     * @param outFile
     *            outstream for the HTML file
     * @param n
     *            number of words in the tag cloud
     * @param source
     *            name of input file
     */
    public static void generateHTMLHeaders(PrintWriter outFile, int n,
            String source) {

        /*
         * The generic HTML headers
         */
        outFile.println("<!DOCTYPE html>");
        outFile.println("<html>");
        outFile.println("<head>");

        /*
         * Assign the same phrase to title and header
         */
        outFile.println("<title>Top " + n + " words in " + source + "</title>");
        outFile.println("<h1>Top " + n + " words in " + source + "</h1>");

        /*
         * Link the stylesheet
         */
        outFile.println(
                "<link href=\"tagcloud.css\" rel=\"stylesheet\" type=\"text/css\">");

        /*
         * More generic HTML headers
         */
        outFile.println("</head>");
        outFile.println("<body>");
    }

    /**
     * Generates the generic HTML footers in file {@code out}.
     *
     * @param outFile
     *            outstream to HTML file
     */
    public static void generateHTMLFooters(PrintWriter outFile) {
        outFile.println("</body>");
        outFile.println("</html>");
    }

    /**
     * Prints the tags from {@code sorted} with values from {@code tags} to
     * {@code outFile}.
     *
     * @param outFile
     *            location of HTML file
     * @param tags
     *            Map of words and number of occurrences
     * @param sorted
     *            Queue of words from tags sorted
     */
    public static void printTags(PrintWriter outFile, Map<String, Integer> tags,
            Queue<String> sorted) {
        /*
         * Find the minimum and maximum number of occurences
         */
        int max = 0;
        int min = Integer.MAX_VALUE;
        for (String entry : sorted) {
            int entryOccurence = tags.value(entry);
            if (entryOccurence > max) {
                max = entryOccurence;
            }
            if (entryOccurence < min) {
                min = entryOccurence;
            }
        }

        /*
         * Start a <p> tag with class "cbox" for the main body
         */
        outFile.println("<p class=\"cbox\">");

        /*
         * Iterate through words and print them
         */
        for (String entry : sorted) {
            /*
             * If the maximum and minimum occurrences are the same, assign
             * everyone the same base font size
             */
            int fontClass;
            if (max == min) {
                fontClass = BASE_FONT_SIZE;
            } else {
                fontClass = (tags.value(entry) - min)
                        * (MAX_FONT_SIZE - BASE_FONT_SIZE) / (max - min)
                        + BASE_FONT_SIZE;
            }

            /*
             * Print the opening of <span> tag, assign class f[fontClass] and
             * title item with count:[numberOfOccurences]
             */
            outFile.print("<span style=\"cursor:default\" class=\"f" + fontClass
                    + "\" title=\"count: " + tags.value(entry) + "\">");

            /*
             * Print word, end span
             */
            outFile.print(entry);
            outFile.print("</span>");
            outFile.println();
        }

        /*
         * End <p> tag
         */
        outFile.println("</p>");
    }

    /**
     * Returns a String generated by replacing any separator in {@code}line with
     * a comma.
     *
     * @param line
     *            Line of text
     * @return line, with every separator replaced by a comma
     */
    public static String convertToCSV(String line) {
        String res = line;

        /*
         * Iterate through every delimiter
         */
        for (char d : SEPS) {
            /*
             * Find the first occurrence of the delimiter
             */
            int loc = res.indexOf(d);

            /*
             * As long as another delimiter is present (ie indexOf >= 0 and !=
             * -1), continue looping
             */
            while (loc >= 0) {
                /*
                 * Make res the substring up to the start of the delimiter * , *
                 * the substring starting at the indexOf the delimiter + the
                 * length of the delimiter (in order to account for delimiters
                 * of length > 1, in this specific case, --
                 */
                res = res.substring(0, loc) + "," + res.substring(loc + 1);

                /*
                 * Find the next occurrence (if any) of d
                 */
                loc = res.indexOf(d, loc);
            }
        }

        return res;
    }

    /**
     * Splits {@code}line into words based on the separators in {@code} delims.
     *
     * @param line
     *            Line of text to break into words.
     *
     * @return Stack<String> containing words of {@code}line.
     */
    public static Stack<String> splitter(String line) {
        Stack<String> brokenLine = new Stack2<String>();

        /*
         * Create a new String for the CSV version of the incoming line (avoid
         * changing the parameter value)
         */
        String processing = convertToCSV(line);

        int start = 0, end = processing.indexOf(',');

        /*
         * If the first character was a , then start = end = 0
         */
        if (start == end) {
            /*
             * Update start to the index of the first none separator character
             */
            while (processing.charAt(start) == ',') {
                start++;
            }

            /*
             * Change end to the first instance of the separator after the start
             * index
             */
            end = processing.indexOf(',', start);
        }

        /*
         * Continue looping until end is out of bounds, or there are no more
         * delimiters
         */
        while (end < processing.length() && end >= 0) {
            brokenLine.push(processing.substring(start, end));

            /*
             * Set start to one past end (since end is the index of ,), and
             * continue looping while start is within bounds of the String and
             * is the index of a separator
             */
            start = end + 1;
            while (start < processing.length()
                    && processing.charAt(start) == ',') {
                start++;
            }

            /*
             * Find a new end
             */
            end = processing.indexOf(',', start);

        }

        /*
         * Since end is either greater than the String length, or -1 if there
         * were no more delimiters, if start < processing, then there is a full
         * word at the very end with no separator between it and the end; add
         * that word
         */
        if (start < processing.length()) {
            brokenLine.push(processing.substring(start));
        }

        return brokenLine;
    }

    /**
     * Reads {@code in} and stores the unique words and how often the occur in
     * {@code map}.
     *
     * @param inFile
     *            input stream of some text file
     * @param map
     *            Map to store unique words and their frequency
     */
    public static void generateTagMap(BufferedReader inFile,
            Map<String, Integer> map) {
        String line = null;
        try {
            line = inFile.readLine();
        } catch (IOException e) {
            System.err.println("Error reading from file");
        }
        while (line != null) {
            /*
             * Store the individual words from the line into a Stack
             */
            Stack<String> words = splitter(line);

            /*
             * For each word in the stack, turn it to lowercase and process it
             */
            for (String eachword : words) {
                String key = eachword.toLowerCase();

                /*
                 * If the word is already in the map, then increment the value
                 * of it, else, add it with value 1
                 */
                if (map.hasKey(key)) {
                    int newCount = map.value(key) + 1;
                    map.replaceValue(key, newCount);

                } else {
                    map.add(key, 1);
                }
            }

            try {
                line = inFile.readLine();
            } catch (IOException e) {
                System.err.println("Error reading from file");
            }
        }
    }

    /**
     * Sorts the pairs in {@code map} by frequency, and stores the top {@code n}
     * of them, sorted alphabetically, in {@code sorted}.
     *
     * @param sorted
     *            a Queue holding the sorted n keys
     * @param map
     *            a Map holding all unique words and their frequencies
     * @param n
     *            number of tags to add to tag cloud
     */
    public static void generateSortedTags(Queue<String> sorted,
            Map<String, Integer> map, int n) {

        /*
         * Declare a sorting machine for sorting numerically (declaration and
         * initialization separated due to CheckStyles warning)
         */
        SortingMachine<Map.Pair<String, Integer>> occurenceSorter;
        occurenceSorter = new SortingMachine5<Map.Pair<String, Integer>>(
                new Numeric());

        /*
         * Add each pair to the numeric sorter
         */
        for (Map.Pair<String, Integer> entry : map) {
            occurenceSorter.add(entry);
        }

        occurenceSorter.changeToExtractionMode();

        /*
         * Declare a sorting machine for sorting alphabetically
         */
        SortingMachine<Map.Pair<String, Integer>> alphabeticalSorter;
        alphabeticalSorter = new SortingMachine5<Map.Pair<String, Integer>>(
                new Alphabetical());

        /*
         * For n times, remove the first in order item from occurenceSorter to
         * add to alphabetical sorter
         */
        for (int i = 0; i < n; i++) {
            alphabeticalSorter.add(occurenceSorter.removeFirst());
        }

        alphabeticalSorter.changeToExtractionMode();

        /*
         * Remove each item from the alphabetical sorter and store the key in
         * the sorted queue
         */
        while (alphabeticalSorter.size() > 0) {
            sorted.enqueue(alphabeticalSorter.removeFirst().key());
        }
    }

}
