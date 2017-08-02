import java.util.ArrayList;

/** A Generic heap class. Unlike Java's priority queue, this heap doesn't just
 * store Comparable objects. Instead, it can store any type of object
 * (represented by type T) and an associated priority value.
 * @author CS 61BL Staff */
public class ArrayHeap<T> {

	/* DO NOT CHANGE THESE METHODS. */

    /* An ArrayList that stores the nodes in this binary heap. */
    private ArrayList<Node> contents;

    /* A constructor that initializes an empty ArrayHeap. */
    public ArrayHeap() {
        contents = new ArrayList<>();
        contents.add(null);
    }

    /* Returns the node at index INDEX. */
    private Node getNode(int index) {
        if (index >= contents.size()) {
            return null;
        } else {
            return contents.get(index);
        }
    }

    private void setNode(int index, Node n) {
        // In the case that the ArrayList is not big enough
        // add null elements until it is the right size
        while (index + 1 >= contents.size()) {
            contents.add(null);
        }
        contents.set(index, n);
    }

    /* Swap the nodes at the two indices. */
    private void swap(int index1, int index2) {
        Node node1 = getNode(index1);
        Node node2 = getNode(index2);
        this.contents.set(index1, node2);
        this.contents.set(index2, node1);
    }

    /* Prints out the heap sideways. Use for debugging. */
    @Override
    public String toString() {
        return toStringHelper(1, "");
    }

    /* Recursive helper method for toString. */
    private String toStringHelper(int index, String soFar) {
        if (getNode(index) == null) {
            return "";
        } else {
            String toReturn = "";
            int rightChild = getRightOf(index);
            toReturn += toStringHelper(rightChild, "        " + soFar);
            if (getNode(rightChild) != null) {
                toReturn += soFar + "    /";
            }
            toReturn += "\n" + soFar + getNode(index) + "\n";
            int leftChild = getLeftOf(index);
            if (getNode(leftChild) != null) {
                toReturn += soFar + "    \\";
            }
            toReturn += toStringHelper(leftChild, "        " + soFar);
            return toReturn;
        }
    }

    /* A Node class that stores items and their associated priorities. */
    public class Node {
        private T item;
        private double priority;

        private Node(T item, double priority) {
            this.item = item;
            this.priority = priority;
        }

        public T item(){
            return this.item;
        }

        public double priority() {
            return this.priority;
        }

        @Override
        public String toString() {
            return this.item.toString() + ", " + this.priority;
        }
    }



	/* FILL IN THE METHODS BELOW. */

    /* Returns the index of the node to the left of the node at i. */
    private int getLeftOf(int i) {
        //YOUR CODE HERE
        return i * 2;
    }

    /* Returns the index of the node to the right of the node at i. */
    private int getRightOf(int i) {
        //YOUR CODE HERE
        return i * 2 + 1;
    }

    /* Returns the index of the node that is the parent of the node at i. */
    private int getParentOf(int i) {
        //YOUR CODE HERE
        return i / 2;
    }

    /* Adds the given node as a left child of the node at the given index. */
    private void setLeft(int index, Node n) {
        //YOUR CODE HERE
        contents.set(index * 2, n);
    }

    /* Adds the given node as the right child of the node at the given index. */
    private void setRight(int index, Node n) {
        //YOUR CODE HERE
        contents.set(index * 2 + 1, n);
    }

    /** Returns the index of the node with smaller priority. Precondition: not
     * both nodes are null. */
    private int min(int index1, int index2) {
        //YOUR CODE HERE
        if (index2 >= contents.size()) {
            return index1;
        }
        return contents.get(index1).priority < contents.get(index2).priority ? index1 : index2;
    }

    /* Returns the Node with the smallest priority value, but does not remove it
     * from the heap. */
    public Node peek() {
        //YOUR CODE HERE
        return contents.get(0);
    }

    /* Bubbles up the node currently at the given index. */
    private void bubbleUp(int index) {
        //YOUR CODE HERE
        int parIndex = getParentOf(index);
        if (parIndex != 0) {
            if (contents.get(index).priority < contents.get(parIndex).priority) {
                swap(index, parIndex);
                bubbleUp(parIndex);
            }
        }
    }

    /* Bubbles down the node currently at the given index. */
    private void bubbleDown(int index) {
        //YOUR CODE HERE
        int leftIndex = getLeftOf(index);
        int rightIndex = getRightOf(index);
        double curPriority = contents.get(index).priority;
        if (leftIndex >= contents.size()) {
            return;
        } else {
            int minIndex = min(leftIndex, rightIndex);
            if (curPriority > contents.get(minIndex).priority) {
                swap(index, minIndex);
                bubbleDown(minIndex);
            }
        }
    }

    /* Inserts an item with the given priority value. Same as enqueue, or offer. */
    public void insert(T item, double priority) {
        //YOUR CODE HERE
        contents.add(new Node(item, priority));
        bubbleUp(contents.size() - 1);
    }

    /* Returns the Node with the smallest priority value, and removes it from
     * the heap. Same as dequeue, or poll. */
    public Node removeMin() {
        //YOUR CODE HERE
        Node result = contents.get(1);
        swap(1, contents.size() - 1);
        contents.remove(contents.size() - 1);
        if (contents.size() > 1) {
            bubbleDown(1);
        }
        return result;
    }

    /* Changes the node in this heap with the given item to have the given
     * priority. You can assume the heap will not have two nodes with the same
     * item. Check for item equality with .equals(), not == */
    public void changePriority(T item, double priority) {
        //YOUR CODE HERE
        int curIndex = 0;
        for (Node node : contents) {
            if (node != null && node.item.equals(item)) {
                node.priority = priority;
                curIndex = contents.indexOf(node);
            }
        }
        if (curIndex != 0) {
            int parIndex = getParentOf(curIndex);
            if (parIndex != 0 && contents.get(curIndex).priority < contents.get(parIndex).priority) {
                bubbleUp(curIndex);
            } else {
                bubbleDown(curIndex);
            }
        }
    }
}
