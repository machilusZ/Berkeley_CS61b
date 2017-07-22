import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestLab06 {


    @Test
    public void testIntListInsert() {
        int[] arr = {1, 3, 5};
        IntList test1 = new IntList(arr);
        IntList test2 = new IntList();
        test1.insert(2, 1);
        test1.insert(4, 3);
        assertEquals(5, test1.getSize());
        assertEquals(3, test1.get(2));
        assertEquals(4, test1.get(3));
        test2.insert(1, 1);
        assertEquals(1, test2.get(0));
        assertEquals(1, test2.getSize());
        test2.insert(10, 10);
        assertEquals(10, test2.get(1));
        test1.insert(0, 0);
        int[] arr1 = {0, 1, 2, 3, 4, 5};
        for (int i = 0; i < arr1.length; i++) {
            assertEquals(arr1[i], test1.get(i));
        }
    }

    @Test
    public void testIntListMerge() {
        int[] arr = {1, 3, 5};
        IntList test1 = new IntList(arr);
        int[] arr2 = {2, 4, 6, 8};
        IntList test2 = new IntList(arr2);
        IntList test = IntList.merge(test1, test2);
        int[] expected ={1, 2, 3, 4, 5, 6, 8};
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], test.get(i));
        }
    }

    @Test
    public void testIntListReverse() {
        int[] arr = {1, 2, 3, 4, 5, 6, 8};
        IntList test1 = new IntList(arr);
        test1.reverse();
        int[] expected = {8, 6, 5, 4, 3, 2, 1};
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], test1.get(i));
        }
    }

    @Test
    public void testDLLInsertRemove() {
        DLList l = new DLList();
        l.insertBack(2);
        l.insertFront(1);
        assertEquals(1, l.get(0));
        l.insert(4, 1);
        assertEquals(4, l.get(1));
        l.insert(1, 10);
        // List is 1, 4, 2, 1
        int[] expected = {1, 4, 2, 1};
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], l.get(i));
        }
        assertEquals(l.sentinel, l.sentinel.next.next.next.next.prev.prev.prev.prev);
        l.remove(1);
        assertEquals(2, l.size);
        l.remove(l.sentinel.next);
        assertEquals(1, l.size);
        assertEquals(2, l.sentinel.next.item);
    }

    @Test
    public void testDLLDoubleReverse() {
        DLList l = new DLList();
        l.insertBack(4);
        l.insertBack(2);
        l.doubleInPlace();
        assertEquals(4, l.size);
        assertEquals(4, l.get(0));
        assertEquals(4, l.get(1));
        assertEquals(2, l.get(2));
        assertEquals(2, l.get(3));
        assertEquals(l.sentinel, l.sentinel.next.next.next.next.prev.prev.prev.prev);
        l.reverse();
        assertEquals(4, l.size);
        assertEquals(4, l.get(3));
        assertEquals(4, l.get(2));
        assertEquals(2, l.get(1));
        assertEquals(2, l.get(0));
        assertEquals(l.sentinel, l.sentinel.next.next.next.next.prev.prev.prev.prev);
    }
}
