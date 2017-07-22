import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;

/** A suite of tests for IntList.
    @author Wan Fung Chui and Maurice Lee
*/

public class IntListTest {

    @Test
    public void testList() {
        IntList test = IntList.list(1, 2, 3, 4, 5);
        assertNotNull(test);
        assertEquals(1, test.item());
        assertEquals(2, test.next().item());
        assertEquals(3, test.next().next().item());
        assertEquals(4, test.next().next().next().item());
        assertEquals(5, test.next().next().next().next().item());
        assertNull(test.next().next().next().next().next());

        IntList empty = IntList.list();
        assertNull(empty);

        IntList single = IntList.list(7);
        assertNotNull(single);
        assertEquals(7, single.item());
        assertNull(single.next());
    }

    @Test
    public void testGet() {
        IntList test = IntList.list(1, 2, 3, 4, 5);
        assertEquals(1, test.get(0));
        assertEquals(2, test.get(1));
        assertEquals(3, test.get(2));
        assertEquals(4, test.get(3));
        assertEquals(5, test.get(4));
        try {
            test.get(5);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {

        } catch (Exception e) {
            fail("Should throw IllegalArgumentException");
        }

        try {
            test.get(-1);
            fail("Should throw IllegalArgumentException for negative indices");
        } catch (IllegalArgumentException e) {

        } catch (Exception e) {
            fail("Should throw IllegalArgumentException for negative indices");
        }

        IntList single = IntList.list(5);
        assertEquals(5, single.get(0));
        try {
            single.get(1);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {

        } catch (Exception e) {
            fail("Should throw IllegalArgumentException");
        }
    }

    @Test
    public void testSize() {
        IntList test = IntList.list(1, 2, 3, 4, 5);
        assertEquals(5, test.size());

        IntList single = IntList.list(1);
        assertEquals(1, single.size());
    }

    @Test
    public void testToString() {
        IntList test = IntList.list(1, 2, 3, 4, 5);
        assertEquals("( 1 2 3 4 5 )", test.toString());
        assertEquals("( 2 3 4 )", IntList.list(2, 3, 4).toString());
        assertEquals("( 1 )", IntList.list(1).toString());
    }

    @Test
    public void testEquals() {
        IntList a = IntList.list(1, 2, 3, 4, 5);
        IntList b = IntList.list(1, 2, 3, 4, 5);
        assertTrue("List should equal itself.", a.equals(a));
        assertTrue("List should equal itself.", b.equals(b));
        assertTrue("a should equal b.", a.equals(b));
        assertTrue("b should equal a.", b.equals(a));

        assertFalse(a.equals(new Object()));
        assertFalse(b.equals(234));

        assertFalse(a.equals(IntList.list(1, 2, 3, 4)));
        assertFalse(a.equals(IntList.list(1, 2, 3, 5, 6)));
    }

    @Test
    public void testAdd() {
        IntList a = IntList.list(1, 2, 3);
        assertEquals(a, IntList.list(1, 2, 3));
        a.add(4);
        assertEquals(a, IntList.list(1, 2, 3, 4));
        a.add(5);
        assertEquals(a, IntList.list(1, 2, 3, 4, 5));

        IntList single = IntList.list(1);
        single.add(2);
        assertEquals(single, IntList.list(1, 2));
    }

    @Test
    public void testSmallest() {
        assertEquals(6, IntList.list(63, 6, 6, 74, 7, 8, 52, 33, 43, 6, 6, 32).smallest());
        assertEquals(9, IntList.list(9).smallest());
        assertEquals(9, IntList.list(9, 9, 9, 9, 9, 9, 9, 9, 9, 9).smallest());
        assertEquals(1, IntList.list(10, 9, 8, 7, 6, 5, 4, 3, 2, 1).smallest());
    }

    @Test
    public void testSquaredSum() {
        assertEquals(14, IntList.list(1, 2, 3).squaredSum());
        assertEquals(1, IntList.list(1).squaredSum());
        assertEquals(5, IntList.list(1, 2).squaredSum());
        assertEquals(2, IntList.list(1, 1).squaredSum());
        assertEquals(18, IntList.list(3, 3).squaredSum());
    }

    @Test
    public void testAppend() {
        IntList test1 = IntList.list(1, 2, 3, 4, 5);
        IntList test2 = IntList.list(1, 2, 3, 4, 5);
        IntList test3 = IntList.append(test1, test2);
        assertEquals(IntList.list(1, 2, 3, 4, 5, 1, 2, 3, 4, 5), test3);
        assertEquals(IntList.list(1, 2, 3, 4, 5), test1);
        assertEquals(IntList.list(1, 2, 3, 4, 5), test2);

        IntList test4 = null;
        IntList test5 = IntList.list(1, 2, 3, 4, 5);
        IntList test6 = IntList.append(test4, test5);
        assertEquals(IntList.list(1, 2, 3, 4, 5), test6);
        assertNull(test4);
        assertEquals(IntList.list(1, 2, 3, 4, 5), test5);

        IntList test7 = IntList.list(1, 2, 3, 4, 5);
        IntList test8 = null;
        IntList test9 = IntList.append(test7, test8);
        assertEquals(IntList.list(1, 2, 3, 4, 5), test9);
        assertEquals(IntList.list(1, 2, 3, 4, 5), test7);
        assertNull(test8);
    }
}