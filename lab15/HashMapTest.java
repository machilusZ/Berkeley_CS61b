import static org.junit.Assert.*;
import org.junit.Test;

import java.awt.Point;
import java.util.HashSet;

public class HashMapTest {

    @Test
    public void testConstructor() {
        // assert constructors are initialized, seem to work correctly, and
        // don't cause errors
        HashMap<String, String> dictionary = new HashMap<String, String>();
        assertEquals(0, dictionary.size());
        assertEquals(16, dictionary.capacity());

        dictionary = new HashMap<String, String>(10);
        assertEquals(0, dictionary.size());
        assertEquals(10, dictionary.capacity());

        // simply test that the constructor exists, resizeTest will do the rest
        dictionary = new HashMap<String, String>(10, 1);
        assertEquals(0, dictionary.size());
        assertEquals(10, dictionary.capacity());
    }

    @Test
    public void testClear() {
        HashMap<String, String> h = new HashMap<String, String>();
        h.put("key", "value");
        assertTrue(h.containsKey("key"));
        assertEquals(1, h.size());
        h.clear();
        assertFalse(h.containsKey("key"));
        assertEquals(0, h.size());
    }

    @Test
    public void testPut() {
        HashMap<String, String> h = new HashMap<String, String>();
        h.put("key", "value");
        assertTrue(h.containsKey("key"));
        assertFalse(h.containsKey("trash"));
    }

    @Test
    public void testGet() {
        HashMap<String, String> h = new HashMap<String, String>();
        h.put("key", "value");
        assertTrue(h.get("key").equals("value"));
    }

    @Test
    public void testRemove() {
        HashMap<String, String> h = new HashMap<String, String>();
        h.put("key", "value");
        assertTrue(h.containsKey("key"));
        h.remove("key");
        assertFalse(h.containsKey("key"));
    }

    @Test
    public void testResize() {
        HashMap<String, String> h = new HashMap<String, String>(2);
        assertEquals(h.capacity(), 2);
        h.put("key", "value");
        h.put("key2", "value2");
        assertEquals(h.capacity(), 4);

        h = new HashMap<String, String>(10, 1);
        for (int i = 1; i <= 10; i += 1) {
            h.put(Integer.toString(i), Integer.toString(i));
        }
        assertEquals(10, h.size());
        assertEquals(10, h.capacity());
        h.put("11", "11");
        assertEquals(11, h.size());
        assertEquals(20, h.capacity());
        h.remove("11");
        assertEquals(10, h.size());
        assertEquals(20, h.capacity());
    }

    @Test
    public void basicFunctionalityTest() {
        HashMap<String, String> dictionary = new HashMap<String, String>();
        assertEquals(0, dictionary.size());
        assertEquals(16, dictionary.capacity());

        // can put objects in dictionary and get them
        dictionary.put("hello", "world");
        assertTrue(dictionary.containsKey("hello"));
        assertEquals("world", dictionary.get("hello"));
        assertEquals(1, dictionary.size());

        // putting with existing key replaces key
        dictionary.put("hello", "kevin");
        assertEquals(1, dictionary.size());
        assertEquals("kevin", dictionary.get("hello"));
        assertEquals("kevin", dictionary.remove("hello"));
        assertEquals(null, dictionary.get("hello"));
        assertEquals(0, dictionary.size());

        // placing key in multiple times does not affect behavior
        HashMap<String, Integer> studentIDs = new HashMap<String, Integer>();
        studentIDs.put("sarah", 12345);
        assertEquals(1, studentIDs.size());
        assertEquals(12345, studentIDs.get("sarah").intValue());
        studentIDs.put("alan", 345);
        assertEquals(2, studentIDs.size());
        assertEquals(12345, studentIDs.get("sarah").intValue());
        assertEquals(345, studentIDs.get("alan").intValue());
        studentIDs.put("alan", 345);
        assertEquals(2, studentIDs.size());
        assertEquals(12345, studentIDs.get("sarah").intValue());
        assertEquals(345, studentIDs.get("alan").intValue());
        studentIDs.put("alan", 345);
        assertEquals(2, studentIDs.size());
        assertEquals(12345, studentIDs.get("sarah").intValue());
        assertEquals(345, studentIDs.get("alan").intValue());
        assertTrue(studentIDs.containsKey("sarah"));
        assertTrue(studentIDs.containsKey("alan"));

        // ensure that containsKey does not always return true
        assertFalse(studentIDs.containsKey("brijen"));
        assertFalse(studentIDs.containsKey("kaylee"));
        studentIDs.put("alex", 612);

        // confirm hash map can handle values being the same
        assertEquals(345, studentIDs.get("alan").intValue());
        studentIDs.put("evil alan", 345);
        assertEquals(345, studentIDs.get("evil alan").intValue());
        assertEquals(studentIDs.get("evil alan"), studentIDs.get("alan"));
    }

    @Test
    public void iteratorTest() {
        // replicate basic functionality test while building database
        HashMap<String, Integer> studentIDs = new HashMap<String, Integer>();
        studentIDs.put("sarah", 12345);
        studentIDs.put("alan", 345);
        assertTrue(studentIDs.containsKey("sarah"));
        assertTrue(studentIDs.containsKey("alan"));

        // ensure that containsKey does not always return true
        assertFalse(studentIDs.containsKey("brijen"));
        assertFalse(studentIDs.containsKey("kaylee"));
        assertFalse(studentIDs.containsKey("alex"));
        studentIDs.put("alex", 612);
        assertTrue(studentIDs.containsKey("alex"));

        // confirm hashMap can handle values being the same
        studentIDs.put("kevin", 12345);
        assertEquals(studentIDs.get("kevin"), studentIDs.get("sarah"));

        HashSet<String> expected = new HashSet<String>();
        expected.add("sarah");
        expected.add("alan");
        expected.add("alex");
        expected.add("kevin");

        HashSet<String> output = new HashSet<String>();
        for (String name : studentIDs) {
            output.add(name);
        }
        assertEquals(expected, output);
    }

}
