/*SplayTree.java*/

/**
 * Simple Splay Tree Implementation.  This Splay Tree is used
 * as a map.
 *
 * @param <K> type of key, must be Comparable
 * @param <V> type of value
 * @author Daniel Nguyen
 */

public class SplayTree<K extends Comparable<K>, V> {

    /**
     * root is the TreeNode that is the root of the tree
     * size is the number of items stored in this tree
     */
    TreeNode<K, V> root;
    private int size;

    /**
     * SplayTree Node
     */
    static class TreeNode<K extends Comparable<K>, V> {
        /**
         * item is the key-value pair stored in this node
         * left is the left child of this node
         * right is the right child of this node
         * parent is the parent of this node
         */
        Entry<K,V> item;
        TreeNode<K, V> left;
        TreeNode<K, V> right;
        TreeNode<K, V> parent;

        /**
         * Basic constructor with item
         * @param item
         */
        TreeNode(Entry<K,V> item) {
            this.item = item;
        }

        /**
         * Basic constructor with item and parent
         * @param item
         * @param parent
         */
        TreeNode(Entry<K, V> item, TreeNode<K, V> parent) {
            this.item = item;
            this.parent = parent;
        }

        /**
         * Returns a string representation of the subtree rooted at this node
         * @return the string representation
         */
        public String toString() {
            String repr = "";
            if (left != null) {
                repr = "(" + left + ")";
            }
            repr += item;
            if (right != null) {
                repr += "(" + right + ")";
            }
            return repr;
        }
    }

    /**
     * A key-value pair
     */
    static class Entry<K extends Comparable<K>, V> {

        K key;
        V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public String toString() {
            return key.toString() + value.toString();
        }
    }

    /**
     * Empty Constructor
     */
    public SplayTree() {
        size = 0;
        root = null;
    }

    /**
     * Returns the number of entries in this tree
     * @return the size of this SplayTree
     */
    public int size() {
        return size;
    }

    /**
     * Sees if the tree is empty
     * @return true if the tree has no entries; false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Rotates the node right through its parent. Can only work if node is a left child
     * If rotation is not possible, immediately returns
     *
     * @param node the node to rotate
     */
    void rotateRight(TreeNode<K, V> node) {
        if (node == null || node.parent == null || node.parent.left != node) {
            throw new IllegalArgumentException("Illegal call to rotateRight().");
        }

        TreeNode exParent = node.parent;
        TreeNode subtreeParent = exParent.parent;

        // Move "node"'s right subtree to its former parent.
        exParent.left = node.right;
        if (node.right != null) {
            node.right.parent = exParent;
        }

        // Make exParent become a child of "node".
        node.right = exParent;
        exParent.parent = node;

        // Make "node" become a child of exParent's former parent.
        node.parent = subtreeParent;
        if (subtreeParent == null) {
            root = node;
        } else if (subtreeParent.right == exParent) {
            subtreeParent.right = node;
        } else {
            subtreeParent.left = node;
        }

        }




    /**
     * Rotates the node left through its parent. Can only work if node is a right child
     * If rotation is not possible, immediately returns
     *
     * @param node the node to rotate
     */
    void rotateLeft(TreeNode<K, V> node) {
        if (node == null || node.parent == null || node.parent.right != node) {
            throw new IllegalArgumentException("Illegal call to rotateLeft().");
        }

        TreeNode exParent = node.parent;
        TreeNode subtreeParent = exParent.parent;

        // Move "node"'s left subtree to its former parent.
        exParent.right = node.left;
        if (node.left != null) {
            node.left.parent = exParent;
        }

        // Make exParent become a child of "node".
        node.left = exParent;
        exParent.parent = node;

        // Make "node" become a child of exParent's former parent.
        node.parent = subtreeParent;
        if (subtreeParent == null) {
            root = node;
        } else if (subtreeParent.right == exParent) {
            subtreeParent.right = node;
        } else {
            subtreeParent.left = node;
        }
    }

    /**
     * Rotates node up through its parent (could be either a left or right rotation)
     * @param node the node to splay one step up the tree
     */
    private void zig(TreeNode<K, V> node) {
        // YOUR CODE HERE
    }

    /**
     * Performs a zig-zag operation, splaying the node up two steps towards the root
     * Rotates the node twice
     * @param node the node to splay two steps up the tree
     */
    private void zigZag(TreeNode<K, V> node) {
        // YOUR CODE HERE
    }

    /**
     * Performs a zig-zig operation, splaying the node up two steps towards the root
     * First rotates node's parent through its grandparent,
     * then rotates node through its parent
     * @param node the node to splay two steps up the tree
     */
    private void zigZig(TreeNode<K, V> node) {
        // YOUR CODE HERE
    }

    /**
     * Splays the node up to the root of the tree with zig-zag, zig-zig, and zig operations
     * @param node the node to splay to the root
     */
    private void splayNode(TreeNode<K, V> node) {
        while (node.parent != null) {
            if (node.parent == root) {
                zig(node);
            } else if ((node.parent == node.parent.parent.left && node == node.parent.right)
                    || (node.parent == node.parent.parent.right && node == node.parent.left)) {
                zigZag(node);
            } else {
                zigZig(node);
            }
        }
        root = node;
    }

    /**
     * Puts the key-value pair into this tree. If the key is already in the tree
     * replaces the current value stored with that key
     * @param key key to put into this tree
     * @param value value associated with key
     */
    public void put(K key, V value) {
        if (root == null) {
            root = new TreeNode<>(new Entry<>(key, value));
            size++;
        } else {
            put(key, value, root);
        }
    }

    /**
     * Internal helper for put, doing all the recursion
     * Splays the node that is inserted
     * @param key key to put into this tree
     * @param value value associated with key
     * @param node root of the current subtree being recursed on
     */
    private void put(K key, V value, TreeNode<K, V> node) {
        int comp = key.compareTo(node.item.key);
        if (comp < 0) {
            if (node.left == null) {
                node.left = new TreeNode<>(new Entry<>(key, value), node);
                splayNode(node.left);
                size++;
            } else {
                put(key, value, node.left);
            }
        } else if (comp > 0) {
            if (node.right == null) {
                node.right = new TreeNode<>(new Entry<>(key, value), node);
                splayNode(node.right);
                size++;
            } else {
                put(key, value, node.right);
            }
        } else {
            node.item.value = value;
        }
    }

    /**
     * Searches for the node that contains the specified key and returns the value
     * associated with it. Returns null if not in the tree
     * @param key the search key
     * @return the value associated with key if it exists; null otherwise
     */
    public V get(K key) {
        TreeNode<K, V> node = get(key, root);
        if (node == null) {
            return null;
        } else {
            return node.item.value;
        }
    }

    /**
     * Internal helper for get, doing all the recursion
     * @param key the search key
     * @param node the current subtree that is being recursed on
     * @return the TreeNode that contains the Entry that has key
     */
    private TreeNode<K, V> get(K key, TreeNode<K, V> node) {
        if (node == null) {
            return null;
        }
        int comp = key.compareTo(node.item.key);
        if (comp < 0) {
            if (node.left == null) {
                splayNode(node);  // Splay the last node visited to the root.
                return null;
            }
            return get(key, node.left);
        } else if (comp > 0) {
            if (node.right == null) {
                splayNode(node);  // Splay the last node visited to the root.
                return null;
            }
            return get(key, node.right);
        } else {
            splayNode(node);  // Splay the found node to the root.
            return node;
        }
    }

    /**
     * Returns a String representation for this SplayTree
     * @return the String representation
     */
    public String toString() {
        if (root == null) {
            return "";
        } else {
            return root.toString();
        }
    }

}
