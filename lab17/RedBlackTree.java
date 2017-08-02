/**
 * Simple Red-Black tree implementation.
 *
 * @param <T> type of items.
 */
public class RedBlackTree<T extends Comparable<T>> {

    /** Root of the tree. */
    RBTreeNode<T> root;

    /**
     * Empty constructor.
     */
    public RedBlackTree() {
        root = null;
    }

    /**
     * Constructor that builds this from given BTree (2-3-4) tree.
     *
     * @param tree BTree (2-3-4 tree).
     */
    public RedBlackTree(BTree<T> tree) {
        BTree.Node<T> btreeRoot = tree.root;
        root = buildRedBlackTree(btreeRoot);
    }

    /**
     * Builds a RedBlack tree that has isometry with given 2-3-4 tree rooted at
     * given node r, and returns the root node.
     *
     * @param r root of the 2-3-4 tree.
     * @return root of the Red-Black tree for given 2-3-4 tree.
     */
    RBTreeNode<T> buildRedBlackTree(BTree.Node<T> r) {
        if(r==null){
            return null;
        }
        if(r.getItemCount()==3){
            RBTreeNode red=new RBTreeNode(true,r.getItemAt(1));
            red.left=new RBTreeNode(false,r.getItemAt(0));
            red.right=new RBTreeNode(false,r.getItemAt(2));
            if(r.getItemCount()!=0){
                red.left.left=buildRedBlackTree(r.getChildAt(0));
                red.left.right=buildRedBlackTree(r.getChildAt(1));
                red.right.left=buildRedBlackTree(r.getChildAt(2));
                red.right.right=buildRedBlackTree(r.getChildAt(3));
            }
            return  red;
        }else if(r.getItemCount()==2){
            RBTreeNode red=new RBTreeNode(true,r.getItemAt(0));
            red.right=new RBTreeNode(false,r.getItemAt(1));
            if(!(r.getItemCount()==0)){
                red.left=buildRedBlackTree(r.getChildAt(0));
                red.right.left=buildRedBlackTree(r.getChildAt(1));
                red.right.right=buildRedBlackTree(r.getChildAt(2));
            }
            return  red;
        }else{
            RBTreeNode red=new RBTreeNode(true,r.getItemAt(0));
            if(r.getItemCount()!=0){
                red.left=buildRedBlackTree(r.getChildAt(0));
                red.right=buildRedBlackTree(r.getChildAt(1));
            }
            return red;
        }
    }

    /**
     * Flips the color of the node and its children. Assume that the node has
     * both left and right children.
     *
     * @param node tree node
     */
    void flipColors(RBTreeNode<T> node) {
        node.isBlack = !node.isBlack;
        node.left.isBlack = !node.left.isBlack;
        node.right.isBlack = !node.right.isBlack;
    }

    /**
     * Returns whether a given node is red. null nodes (children of leaf) are
     * automatically considered black.
     *
     * @param node node
     * @return node is red.
     */
    private boolean isRed(RBTreeNode<T> node) {
        return node != null && !node.isBlack;
    }

    /**
     * RedBlack tree node.
     *
     * @param <T> type of item.
     */
    static class RBTreeNode<T> {

        /** Item. */
        final T item;

        /** True if the node is black. */
        boolean isBlack;

        /** Pointer to left child. */
        RBTreeNode<T> left;

        /** Pointer to right child. */
        RBTreeNode<T> right;

        /**
         * Constructor with color and item.
         */
        RBTreeNode(boolean isBlack, T item) {
            this(isBlack, item, null, null);
        }

        /**
         * Constructor with color, item, and pointers to children (can be
         * null).
         */
        RBTreeNode(boolean isBlack, T item, RBTreeNode<T> left,
            RBTreeNode<T> right) {
            this.isBlack = isBlack;
            this.item = item;
            this.left = left;
            this.right = right;
        }
    }

}
