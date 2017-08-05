import java.util.HashMap;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.ArrayList;
/**
 * Implements autocomplete on prefixes for a given dictionary of terms and weights.
 * @author Yunan Zhang
 */
public class Autocomplete {
    TSTNodeComparator tstNC = new TSTNodeComparator();
    WeightComparator mpc = new WeightComparator();
    TSTNode dummy = new TSTNode();
    private TST tst;
    private String[] terms;
    private double[] weights;
    private HashMap<String, Double> map;
    private MaxPQ<MyPair> maxPQMP;
    /**
     * Initializes required data structures from parallel arrays.
     * @param terms Array of terms.
     * @param weights Array of weights.
     */
    public Autocomplete(String[] terms, double[] weights) {
        tst = new TST();
        map = new HashMap<String, Double>();
        if (terms.length != weights.length) {
            throw new IllegalArgumentException("The length of the terms" 
                + "and weight arrays are different.");
        }
        for (int i = 0; i < terms.length; i += 1) {
            if (weights[i] < 0) {
                throw new IllegalArgumentException("Weght is non-negative.");
            }
            if (map.containsKey(terms[i])) {
                throw new IllegalArgumentException("Array contains duplicates.");
            }
            tst.put(terms[i], weights[i]);
            map.put(terms[i], weights[i]);
        }
        this.terms = terms;
        this.weights = weights;
    }

    /**
     * Find the weight of a given term. If it is not in the dictionary, return 0.0
     * @param term Inputted term.
     * @return Weight of term.
     */
    public double weightOf(String term) {
        return map.get(term);
    }

    /**
     * Return the top match for given prefix, or null if there is no matching term.
     * @param prefix Input prefix to match against.
     * @return Best (highest weight) matching string in the dictionary.
     */
    public String topMatch(String prefix) {
        Iterable x = topMatches(prefix, 1);
        Iterator y = x.iterator();
        return (String) y.next();
    }

    /**
     * Returns the top k matching terms (in descending order of weight) as an iterable.
     * If there are less than k matches, return all the matching terms.
     * @param prefix Prefix that was inputted.
     * @param k Number of terms wanted.
     * @return iterable of the top matching terms
     */
    public Iterable<String> topMatches(String prefix, int k) {
        if (k < 0) {
            throw new IllegalArgumentException();
        }
        MaxPQ<TSTNode> maxPQTST = new MaxPQ<TSTNode>(1, tstNC);
        MinPQ<TSTNode> minPQWord = new MinPQ<TSTNode>(k, mpc);
        MaxPQ<TSTNode> maxPQWord = new MaxPQ<TSTNode>(1, mpc);
        minPQWord.insert(dummy);
        char[] charArray = prefix.toCharArray();
        TSTNode temp = tst.root();
        if (!prefix.isEmpty()) {
            temp = tst.get(tst.root(), prefix, 0);
        }
        if (prefix.isEmpty()) {
            if (temp.left != null) {
                maxPQTST.insert(temp.left);                    
            }
            if (temp.right != null) {
                maxPQTST.insert(temp.right);
            }
        }
        if (temp == null) {
            return new ArrayList();
        }
        if (temp.val != 0) {
            maxPQWord.insert(temp);
        }
        if (temp.mid != null) {
            temp = temp.mid;
            if (temp.val != 0) {
                maxPQWord.insert(temp);
            }
            if (temp.left != null) {
                maxPQTST.insert(temp.left);                    
            }
            if (temp.right != null) {
                maxPQTST.insert(temp.right);
            }
            if (temp.mid != null) {
                maxPQTST.insert(temp.mid);
            }
            while (maxPQWord.size() < k || (!maxPQTST.isEmpty() 
                && minPQWord.min().val < maxPQTST.max().maxVal)) {
                if (maxPQTST.isEmpty()) {
                    break;
                }
                TSTNode n = maxPQTST.delMax();
                if (n == null) {
                    break;
                }
                if (n.mid != null) {
                    maxPQTST.insert(n.mid);
                }
                if (n.val != 0) {

                    maxPQWord.insert(n);
                    minPQWord.insert(n);
                }
                if (n.left != null) {
                    maxPQTST.insert(n.left);                    
                }
                if (n.right != null) {
                    maxPQTST.insert(n.right);
                }
                if (maxPQTST.isEmpty()) {
                    break;
                }
            }
        }
        ArrayList<String> ret = new ArrayList<String>();
        while (ret.size() < k && !maxPQWord.isEmpty()) {
            TSTNode max = maxPQWord.delMax();
            ret.add(max.fullWord);
        }
        return ret;
    }

    /**
     * Returns the highest weighted matches within k edit distance of the word.
     * If the word is in the dictionary, then return an empty list.
     * @param word The word to spell-check
     * @param dist Maximum edit distance to search
     * @param k    Number of results to return 
     * @return Iterable in descending weight order of the matches
     */
    public Iterable<String> spellCheck(String word, int dist, int k) {
        LinkedList<String> results = new LinkedList<String>();  
        /* YOUR CODE HERE; LEAVE BLANK IF NOT PURSUING BONUS */
        return results;
    }
    /**
     * Test client. Reads the data from the file, 
     * then repeatedly reads autocomplete queries 
     * from standard input and prints out the top k matching terms.
     * @param args takes the name of an input file and an integer k as command-line arguments
     */
    public static void main(String[] args) {
        // initialize autocomplete data structure
        In in = new In(args[0]);
        int N = in.readInt();
        String[] terms = new String[N];
        double[] weights = new double[N];
        for (int i = 0; i < N; i++) {
            weights[i] = in.readDouble();   // read the next weight
            in.readChar();                  // scan past the tab
            terms[i] = in.readLine();       // read the next term
        }

        Autocomplete autocomplete = new Autocomplete(terms, weights);
        // process queries from standard input
        int k = Integer.parseInt(args[1]);
        while (StdIn.hasNextLine()) {
            String prefix = StdIn.readLine();
            for (String term : autocomplete.topMatches(prefix, k)) {
                StdOut.printf("%14.1f  %s\n", autocomplete.weightOf(term), term);
            }
        }
    }
}

