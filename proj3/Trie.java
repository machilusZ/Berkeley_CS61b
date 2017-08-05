import java.util.HashMap;
/**
 * Prefix-Trie. Supports linear time find() and insert(). 
 * Should support determining whether a word is a full word in the 
 * Trie or a prefix.
 * @author Yunan Zhang
 */
// it'd be easier to just use a Trie for AlphabetSort and a TST for Autocomplete. 
public class Trie {
    private Node root = new Node();
    /**
     * Node contains a HashMap of with Characters and Keys and Nodes as values
     * Links letters to the subsequent letter
     */
    private class Node {
        boolean exists;
        HashMap<Character, Node> links;
        /**
         * Puts a Character and Node pair into the HashMap.
         * @param c The character to put into the map.
         * @param n The node to put into the map.
         */
        public void putChar(Character c, Node n) {
            links.put(c, n);
        }
        /**
         * Initializes required data structures for Node
         */
        public Node() {
            links = new HashMap<Character, Node>();
            exists = false;
        }
        /**
         * Sets exists boolean of a node to true, meaning its the last letter of an inserted word
         */
        private void doesExist() {
            exists = true;
        }
        /**
         * Returns whether or not a node represents the last letter of a word.
         * @return Boolean on whether the node represents the last letter of a word.
         */
        private boolean exists() {
            return exists;
        }
        /**
         * Returns the HashMap of Characters and Nodes which links words together.
         * @return HashMap of Characters and Nodes representing subsequent letters in a word.
         */
        public HashMap<Character, Node> links() {
            return links;
        }
    }
    /**
     * Returns the root of the Trie.
     * @return Node root.
     */
    public Node root() {
        return root;
    }
    /**
     * Inserts string into the Trie
     * @param s Word being inserted into Trie.
     * @param isFullWord If isFullWord is false, then partial prefix matches should return true; 
     * if isFullWord is true, then only full word matches should return true.
     * @return Boolean telling whether a string was found in the Trie
     */
    public boolean find(String s, boolean isFullWord) {
        Node pointer = root;
        if (isFullWord) {
            char[] charArray = s.toCharArray();
            for (char c : charArray) {
                if (pointer.links().get(c) == null) {
                    return false;
                } else {
                    pointer = pointer.links().get(c);
                }
            }
            if (pointer == null) {
                return false;
            } else {
                return pointer.exists;
            }
        } else {
            char[] charArray = s.toCharArray();
            for (char c : charArray) {
                if (pointer.links().get(c) == null) {
                    return false;
                } else {
                    pointer = pointer.links().get(c);
                }
            }
            return pointer != null;
        }
    }
    /**
     * Inserts string into the Trie
     * @param s Word being inserted into Trie.
     */
    public void insert(String s) {
        if (s.equals(null) || s.equals("")) {
            throw new IllegalArgumentException("Null and empty are never in the Trie.");
        }
        char[] charArray = s.toCharArray();
        Node pointer = root;
        for (char c : charArray) {
            if (pointer.links().containsKey(c)) {
                pointer = pointer.links().get(c);   
            } else {
                Node n = new Node();
                pointer.putChar(c, n);
                pointer = pointer.links().get(c);
            }
        }
        pointer.doesExist();
    }
    /**
     * Traverses through the Word list and prints words based on the new alphabet order.
     * @param n Current Node whose letters are being traversed through.
     * @param s String which is being added to and will be printed when full word.
     * @param charArray foreign alphabet.
     */
    public void wordTraverse(char[] charArray, Node n, String s) {
        Node nTemp = n;
        for (char c : charArray) {
            if (n.links().containsKey(c)) {
                if (n.links.get(c).exists()) {
                    nTemp = n.links().get(c);
                    String temp = s;
                    temp = temp + c;
                    System.out.println(temp);
                    wordTraverse(charArray, nTemp, temp);
                } else {
                    String temp = s;
                    temp = temp + c;
                    nTemp = n.links().get(c);
                    wordTraverse(charArray, nTemp, temp);
                }                    
            }
        }
    }
}

