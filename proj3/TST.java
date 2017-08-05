/**
 * Ternary Search Trie which keeps track of char.
 * @author Larry Zhou (With aid of Princeton)
 */
public class TST {
    private int N;              // size
    TSTNode root;   // root of TST

    /**
     * Initializes an empty string symbol table.
     */
    public TST() {
    }
    /**
     * Returns the root of the Trie.
     * @return Node root.
     */
    public TSTNode root() {
        return root;
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return N;
    }

    /**
     * Does this symbol table contain the given key?
     * @param key the key
     * @return <tt>true</tt> if this symbol table contains <tt>key</tt> and
     *     <tt>false</tt> otherwise
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     */
    public boolean contains(String key) {
        return get(key) == 0;
    }

    /**
     * Returns the value associated with the given key.
     * @param key the key
     * @return the value associated with the given key if the key is in the symbol table
     *     and <tt>null</tt> if the key is not in the symbol table
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     */
    public double get(String key) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (key.length() == 0) {
            throw new IllegalArgumentException("key must have length >= 1");
        }
        TSTNode x = get(root, key, 0);
        if (x == null) {
            return 0;
        }
        return x.val;
    }

    /**
     * Returns the TSTNode at the end of the given key
     * @param x Starting node.
     * @param key Prefix that is looked for.
     * @param d Initially 0 to iterate through word.
     * @return TSTNode up to given key.
     */
    public TSTNode get(TSTNode x, String key, int d) {
        if (key == null) {
            throw new NullPointerException();
        }
        if (key.length() == 0) {
            throw new IllegalArgumentException("key must have length >= 1");
        }
        if (x == null) {
            return null;
        }
        char c = key.charAt(d);
        if (c < x.c) {
            return get(x.left,  key, d);
        } else if (c > x.c) {
            return get(x.right, key, d);
        } else if (d < key.length() - 1) {
            return get(x.mid, key, d + 1);
        } else {
            return x;
        }
    }

    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     * If the value is <tt>null</tt>, this effectively deletes the key from the symbol table.
     * @param key the key
     * @param val the value
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     */
    public void put(String key, double val) {
        if (!contains(key)) {
            N += 1;
        }
        root = put(root, key, val, 0);
    }
    /**
     * Returns the TSTNode at the end of the given key
     * @param x Starting node.
     * @param key Prefix that is looked for.
     * @param val Weight of word.
     * @param d Initially 0 to iterate through word.
     * @return TSTNode that you just put into.
     */
    private TSTNode put(TSTNode x, String key, double val, int d) {
        char c = key.charAt(d);
        if (x == null) {
            x = new TSTNode();
            x.c = c;
        }
        if (x.maxVal < val) {
            x.maxVal = val;
        }
        if (c < x.c) {            
            x.left  = put(x.left,  key, val, d);
        } else if (c > x.c) {
            x.right = put(x.right, key, val, d);
        } else if (d < key.length() - 1) {
            x.mid   = put(x.mid,   key, val, d + 1);
        } else {  
            x.fullWord = key;
            x.val = val;
            if (x.maxVal < x.val) {
                x.maxVal = x.val;
            }
        }
        return x;
    }

}

