/** A data structure to represent a Linked List of Integers.
 * Each IntList represents one node in the overall Linked List.
 * Encapsulated version.
 */
import java.util.*;
public class IntList {

    /**
     * The head of the list is the first node in the list. 
     * If the list is empty, head is null 
     */
    private IntListNode head;
    private int size;

    @Override
    public String toString() {
        return "IntList{" +
                "head=" + head +
                ", size=" + size +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IntList)) return false;

        IntList intList = (IntList) o;

        if (getSize() != intList.getSize()) return false;
        return head != null ? head.equals(intList.head) : intList.head == null;
    }

    @Override
    public int hashCode() {
        int result = head != null ? head.hashCode() : 0;
        result = 31 * result + getSize();
        return result;
    }

    /**
     * IntListNode is a nested class. It can be instantiated
     * when associated with an instance of IntList.
     */
    public class IntListNode {
        @Override
        public String toString() {
            return "IntListNode{" +
                    "item=" + item +
                    ", next=" + next +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof IntListNode)) return false;

            IntListNode that = (IntListNode) o;

            if (item != that.item) return false;
            return next != null ? next.equals(that.next) : that.next == null;
        }

        @Override
        public int hashCode() {
            int result = item;
            result = 31 * result + (next != null ? next.hashCode() : 0);
            return result;
        }

        int item;
        IntListNode next;

        public IntListNode(int item, IntListNode next) {
            this.item = item;
            this.next = next;
        }
    }

    public int getSize() {
        return size;
    }

    public IntList() {}

    public IntList(int[] initial) {
        for (int i = initial.length - 1; i >= 0; i--) {
            head = new IntListNode(initial[i], head);
        }
        size = initial.length;
    }

    /**
     * Get the value at position pos. If the position does not exist, throw an
     * IndexOutOfBoundsException.
     * @param position to get from
     * @return the int at the position in the list.
     */
    public int get(int position) {
        if (position >= size||position<0) throw new IndexOutOfBoundsException("Position larger than size of list.");
        IntListNode curr = head;
        while (position > 0) {
            curr = curr.next;
            position--;
        }
        return curr.item;
    }

    /* Fill in below! */

    /**
     * Insert a new node into the IntList.
     * @param x value to insert
     * @param position position to insert into. If position exceeds the size of the list, insert into
     *            the end of the list.
     */
    public void insert(int x, int position) {
        IntListNode curr=head;
        if(position>=size){
            if(head==null){
                head=new IntListNode(x,null);
                size++;
            }else{
                for(int i=0;i<size-1;i++){
                    curr=curr.next;
                }
                curr.next=new IntListNode(x,null);
                size++;
            }
        }else{
            if(position==0){
                head=new IntListNode(x,curr);
                size++;
            }else{
                for(int i=0;i<position-1;i++){
                    curr=curr.next;
                }
                IntListNode next=curr.next;
                curr.next=new IntListNode(x,next);
                size++;
            }
        }
    }

    /**
     * Merge two sorted IntLists a and b into one sorted IntList containing all of their elements.
     * @return a new IntList without modifying either parameter
     */
    public static IntList merge(IntList a, IntList b) {
        IntList result = new IntList();
        if (a.head == null && b.head != null) {
            result = b;
        } else if (a.head != null && b.head == null) {
            result = a;
        } else {
            int[] arr=new int[a.size+b.size];
            for(int i=0;i<a.size;i++){
                arr[i]=a.get(i);
            }
            for(int j=0;j<b.size;j++){
                arr[j+a.size]=b.get(j);
            }
            Arrays.sort(arr);
            result=new IntList(arr);
        }
        return  result;
    }

    /**
     * Reverse the current list recursively, using a helper method.
     */
    public void reverse() {
        head=reverseHelper(head);

    }
    public static IntListNode reverseHelper(IntListNode head){
        if(head==null||head.next==null)
            return head;
        IntListNode nextNode=head.next;
        IntListNode newHead=reverseHelper(nextNode);
        nextNode.next=head;
        head.next=null;
        return newHead;
    }

    /* Optional! */

    /**
     * Remove the node at position from this list.
     * @param position int representing the index of the node to remove. If greater than the size
     *                 of this list, throw an IndexOutOfBoundsException.
     */
    public void remove(int position) {
        if (position >= size) throw new IndexOutOfBoundsException();
        IntListNode pointer=this.head;
        for (int i=0;i<position-1;i++){
            pointer=pointer.next;
        }
        IntListNode p2=pointer.next;
        pointer.next=p2.next;
        pointer=p2;
        this.size-=1;
    }
}
