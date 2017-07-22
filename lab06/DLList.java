public class DLList {
    DLNode sentinel;
    int size;

    public class DLNode {
        Object item;
        DLNode prev, next;

        public DLNode(Object item, DLNode prev, DLNode next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    /**
     * Construct a new DLList with a sentinel that points to itself.
     */
    public DLList() {
        sentinel = new DLNode(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
    }

    /**
     * Insert into the end of this list
     * @param o Object to insert
     */
    public void insertBack(Object o) {
        DLNode n = new DLNode(o, sentinel.prev, sentinel);
        n.next.prev = n;
        n.prev.next = n;
        size++;
    }


    /**
     * Get the value at position pos. If the position does not exist, return null (the item of
     * the sentinel).
     * @param position to get from
     * @return the Object at the position in the list.
     */
    public Object get(int position) {
        DLNode curr = sentinel.next;
        while (position > 0 && curr != sentinel) {
            curr = curr.next;
            position--;
        }
        return curr.item;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("DLList(");
        DLNode curr = sentinel.next;
        while (curr != sentinel) {
            s.append(curr.item.toString());
            if (curr.next != sentinel) s.append(", ");
            curr = curr.next;
        }
        s.append(')');
        return s.toString();
    }

    /* Fill these in! */

    /**
     * Insert a new node into the DLList.
     * @param o Object to insert
     * @param position position to insert into. If position exceeds the size of the list, insert into
     *            the end of the list.
     */
    public void insert(Object o, int position) {
        if(position>=size){
            if(sentinel.next==sentinel){
                sentinel.next=new DLNode(o,sentinel,sentinel);
                sentinel.prev=sentinel.next;
                size++;
            }else {
                DLNode last=sentinel.prev;
                DLNode newLast=new DLNode(o,last,sentinel);
                last.next=newLast;
                sentinel.prev=newLast;
                size++;
            }
        }else {
            if(position==0){
                DLNode head=sentinel.next;
                DLNode newHead=new DLNode(o,sentinel,head);
                head.prev=newHead;
                sentinel.next=newHead;
                size++;
            }else{
                DLNode pointer=sentinel.next;
                for(int i=0;i<position-1;i++){
                    pointer=pointer.next;
                }
                DLNode next=pointer.next;
                DLNode newNext=new DLNode(o,pointer,next);
                pointer.next=newNext;
                next.prev=newNext;
                size++;
            }
        }
    }

    /**
     * Insert into the front of this list. You should can do this with a single call to insert().
     * @param o Object to insert
     */
    public void insertFront(Object o) {
        insert(o,0);

    }

    /**
     * Remove all copies of Object o in this list
     * @param o Object to remove
     */
    public void remove(Object o) {
        if(sentinel.next==sentinel){
            return;
        }
        DLNode curr=sentinel.next;
        for(int i=0;i<size-1;i++){
            if(curr.item.equals(o)){
                DLNode p=curr.prev;
                DLNode n=curr.next;
                p.next=n;
                n.prev=p;
                size--;
            }
            curr=curr.next;
        }

    }

    /**
     * Remove a DLNode from this list. Does not error-check to make sure that the node actually
     * belongs to this list.
     * @param n DLNode to remove
     */
    public void remove(DLNode n) {
        if(sentinel.next==sentinel){
            return;
        }
        DLNode curr=sentinel.next;
        for(int i=0;i<size-1;i++){
            if(curr.equals(n)){
                DLNode p=curr.prev;
                DLNode ne=curr.next;
                p.next=ne;
                n.prev=p;
                size--;
            }
            curr=curr.next;
        }
    }


    /**
     * Duplicate each node in this linked list destructively.
     */
    public void doubleInPlace() {
       DLNode curr=this.sentinel.next;
       while (!curr.equals(sentinel)){
           DLNode newNode=new DLNode(curr.item,curr,curr.next);
           curr.next=newNode;
           curr=newNode.next;
           curr.prev=newNode;
       }
    }

    /**
     * Reverse the order of this list destructively.
     */
    public void reverse() {
        DLNode curr=sentinel.next;
        sentinel.prev=curr;
        DLNode temp=null;
        while (!curr.equals(sentinel)){
            temp=curr.prev;
            curr.prev=curr.next;
            curr.next=temp;
            curr=curr.prev;
        }
        sentinel.next= temp.prev;
    }

    public static void main(String[] args) {
        // you can add some quick tests here if you would like
    }
}
