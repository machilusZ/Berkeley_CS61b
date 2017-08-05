import java.util.ArrayList;
import java.util.Scanner;
    /**
     * Initializes required data structures from parallel arrays.
     * @param terms Array of terms.
     * @param weights Array of weights.
     */
/**
 * AlphabetSort sorts a list of words into alphabetical order, 
 * according to a given permutation of some alphabet.
 * @author Yunan Zhang
 */
public class AlphabetSort {
    private Trie t;
    /**
     * Initializes an empty Trie.
     */
    public AlphabetSort() {
        t = new Trie();
    }
    /**
     * Sorts the words.
     * @param args txt or in file with a foreign alphabet followed by words
     */
    public static void main(String[] args) {
        AlphabetSort aS = new AlphabetSort(); 
        Scanner sc = new Scanner(System.in);
        if (!sc.hasNextLine()) {
            throw new IllegalArgumentException("No words or Alphabet given.");
        }
        String order = sc.nextLine();
        if (!sc.hasNextLine()) {
            throw new IllegalArgumentException("No words or Alphabet given.");
        }
        while (sc.hasNextLine()) {
            aS.t.insert(sc.nextLine());
        }
        char[] charArray = order.toCharArray();
        ArrayList<Character> dupCheck = new ArrayList<Character>();
        for (char c: charArray) {
            if (dupCheck.contains(c)) {
                throw new IllegalArgumentException("A letter "
                    + "appears multiple times in the alphabet.");
            } else {
                dupCheck.add(c);
            }
        }
        aS.t.wordTraverse(charArray, aS.t.root(), "");
    }
}

