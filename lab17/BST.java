import java.util.*;

public class BST {
    BSTNode root;

    public BST(LinkedList list) {
        root = linkedListToTree(list.iterator(), list.size());
    }

    /**
     * Returns the root node of a BST (Binary Search Tree) built from given
     * sorted linked list iterator of n items. The iterator will contain objects
     * that will be the item of each BSTNode.
     *
     * @param iter iterator initialized to the first element.
     * @param n number of items in the linked list.
     * @return root node of a BST from given items.
     */
    private BSTNode linkedListToTree (Iterator iter, int n) {
        if(n==0)return null;
        else if(n==1)return new BSTNode(iter.next());
        else{
            return new BSTNode(linkedListToTree(iter, n/2), iter.next(), linkedListToTree(iter, n - n / 2 - 1));
        }
    }

    /**
     * Prints the tree to the console.
     */
    private void print() {
        print(root, 0);
    }

    private void print(BSTNode node, int d) {
        if (node == null) {
            return;
        }
        for (int i = 0; i < d; i++) {
            System.out.print("  ");
        }
        System.out.println(node.item);
        print(node.left, d + 1);
        print(node.right, d + 1);
    }

    /**
     * Node for BST.
     */
    static class BSTNode {

        /** Item. */
        Object item;

        /** Left child. */
        BSTNode left;

        /** Right child. */
        BSTNode right;

        public BSTNode(Object item){
            this.item=item;
        }
        public BSTNode(BSTNode left,Object item, BSTNode right){
            this.item=item;
            this.left=left;
            this.right=right;
        }
    }
}
