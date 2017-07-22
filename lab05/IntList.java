/** A data structure to represent a Linked List of Integers.
 * Each IntList represents one node in the overall Linked List.
 *
 * @author Maurice Lee and Wan Fung Chui
 */

public class IntList {

    /** The integer stored by this node. */
    private int item;
    /** The next node in this IntList. */
    private IntList next;

    /** Constructs an IntList storing ITEM and next node NEXT. */
    public IntList(int item, IntList next) {
        this.item = item;
        this.next = next;
    }

    /** Constructs an IntList storing ITEM and no next node. */
    public IntList(int item) {
        this(item, null);
    }

    /** Returns an IntList consisting of the elements in ITEMS.
     * IntList L = IntList.list(1, 2, 3);
     * System.out.println(L.toString()) // Prints (1 2 3) */
    public static IntList list(int... items) {
        /** Check for cases when we have no element given. */
        if (items.length == 0) {
            return null;
        }
        /** Create the first element. */
        IntList head = new IntList(items[0]);
        IntList last = head;
        /** Create rest of the list. */
        for (int i = 1; i < items.length; i++) {
            last.next = new IntList(items[i]);
            last = last.next;
        }
        return head;
    }

    /** Returns the integer stored by this IntList. */
    public int item() {
        return item;
    }

    /** Returns the next node stored by this IntList. */
    public IntList next() {
        return next;
    }

    /**
     * Returns [position]th item in this list. Throws IllegalArgumentException
     * if index out of bounds.
     *
     * @param position, the position of element.
     * @return The element at [position]
     */
    public int get(int position) {
        IntList pointer=this;
        if(position>=this.size()||position<0) {
            throw new IllegalArgumentException() ;
        }
        else{
            for(int i=0;i<position;i++){
                pointer=pointer.next;
            }

        }
        return pointer.item();
    }

    /**
     * Returns the size of the list.
     *
     * @return The size of the list.
     */
    public int size() {
        int length=1;
        IntList pointer=this;
        while(pointer.next()!=null){
            length++;
            pointer=pointer.next;
        }
        return length;
    }

    /**
     * Returns the string representation of the list. For the list (1, 2, 3),
     * returns "( 1 2 3 )".
     *
     * @return The String representation of the list.
     */
    public String toString() {
        String result="( ";
       IntList pointer=this;
       for(int i=0;i<this.size();i++){
           result+=pointer.item()+" ";
           pointer=pointer.next();
       }
       result+=")";
        return  result;
    }

    /**
     * Returns whether this and the given list or object are equal.
     *
     * @param obj, another list (object)
     * @return Whether the two lists are equal.
     */
    public boolean equals(Object obj) {
        boolean result=true;
        IntList pointer=this;


        if(!(obj instanceof IntList))
            return false;
        IntList o=(IntList) obj;
        if(o.size()!=this.size()){
            return false;
        }
        while(pointer!=null&&o!=null) {
            if (pointer.item() != o.item())
                result = false;
            pointer = pointer.next;
            o = o.next;
        }

        return result;
    }

    /**
     * Adds the given item at the end of the list.
     *
     * @param item, the int to be added.
     */
    public void add(int item) {
        IntList pointer=this;
        for(int i=0;i<this.size()-1;i++){
            pointer=pointer.next;
        }
        pointer.next=new IntList(item);//I try to reconstruct this, but failed; nor can I directly assign a variable to it.
    }

    /**
     * Returns the smallest element in the list.
     *
     * @return smallest element in the list
     */
    public int smallest() {
        int smallest=this.item;
        IntList pointer=this;
        while(pointer!=null){
            if(pointer.item()<smallest)
                smallest=pointer.item();
            pointer=pointer.next();
        }
        return smallest;
    }

    /**
     * Returns the sum of squares of all elements in the list.
     *
     * @return The sum of squares of all elements.
     */
    public int squaredSum() {
        int result=0;
        IntList pointer=this;
        while(pointer!=null){
            result+=pointer.item()*pointer.item();
            pointer=pointer.next();
        }
        return result;
    }

    /**
     * Returns a new IntList consisting of L1 followed by L2,
     * non-destructively.
     *
     * @param l1 list to be on the front of the new list.
     * @param l2 list to be on the back of the new list.
     * @return new list with L1 followed by L2.
     */
    public static IntList append(IntList l1, IntList l2) {
        IntList result=new IntList(1);
        if(l1!=null&&l2!=null) {
            int size = l1.size() + l2.size();
            IntList pointer1 = l1;
            IntList pointer2 = l2;

            int[] arr = new int[size];
            for (int i = 0; i < size; i++) {
                if (i < l1.size()) {
                    arr[i] = pointer1.item();
                    pointer1 = pointer1.next();
                } else {
                    arr[i] = pointer2.item();
                    pointer2 = pointer2.next();
                }
            }
            result = list(arr);
        }
        else if(l2==null){
            result=l1;
        }else{
            result=l2;
        }
        return result;
    }
}