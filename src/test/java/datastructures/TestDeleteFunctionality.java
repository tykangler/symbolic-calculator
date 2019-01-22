package datastructures;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

/**
 * This class should contain all the tests you implement to verify that
 * your 'delete' method behaves as specified.
 *
 * This test _extends_ your TestDoubleLinkedList class. This means that when
 * you run this test, not only will your tests run, all of the ones in
 * TestDoubleLinkedList will also run.
 *
 * This also means that you can use any helper methods defined within
 * TestDoubleLinkedList here. In particular, you may find using the
 * 'assertListMatches' and 'makeBasicList' helper methods to be useful.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDeleteFunctionality extends TestDoubleLinkedList {
    @Test(timeout=SECOND)
    public void testExample() {
        // Feel free to modify or delete this dummy test.
        assertTrue(true);
        assertEquals(3, 3);
    }

    protected IList<String> makeBasicList() {
        IList<String> list = new DoubleLinkedList<>();

        list.add("a");
        list.add("b");
        list.add("c");

        return list;
    }

    /**
     * This function will check if a list contains exactly the same elements as
     * the "expected" array. See the tests provided for example usage.
     */
    protected <T> void assertListMatches(T[] expected, IList<T> actual) {
        assertEquals(expected.length, actual.size());
        assertEquals(expected.length == 0, actual.isEmpty());

        for (int i = 0; i < expected.length; i++) {
            try {
                assertEquals("Item at index " + i + " does not match", expected[i], actual.get(i));
            } catch (Exception ex) {
                String errorMessage = String.format(
                        "Got %s when getting item at index %d (expected '%s')",
                        ex.getClass().getSimpleName(),
                        i,
                        expected[i]);
                throw new AssertionError(errorMessage, ex);
            }
        }
    }

    @Test(timeout = SECOND)
    public void basicTestDelete() {
        IList<String> list = makeBasicList();
        int initSize = list.size();

        String test = list.delete(0);

        assertEquals("a", test);
        assertEquals("b", list.get(0));
        assertEquals(initSize - 1, list.size());
    }

    @Test(timeout = SECOND)
    public void testDeleteSingleElement() {
        IList<String> list = new DoubleLinkedList<>();
        int initSize = list.size();
        list.add("a");

        String test = list.delete(0);

        assertEquals("a", test);
        assertEquals(initSize, list.size());
    }

    @Test(timeout = SECOND)
    public void testDeleteAtEnd() {
        IList<String> list = makeBasicList();
        int initSize = list.size();

        String test = list.delete(list.size() - 1);

        assertEquals("c", test);
        assertEquals(initSize - 1, list.size());
    }

    @Test(timeout = SECOND)
    public void testIndexOutOfBoundThrowsException() {
        IList<String> list = new DoubleLinkedList<>();
        int initSize = list.size();

        try {
            list.delete(0);
            // Didn't throw anything? Fail now.
            fail("Expected IndexOutOfBoundException");
        } catch (IndexOutOfBoundsException ex) {
            // Do nothing. This is a-ok.
        }

        // make sure size is still the same
        assertEquals(initSize, list.size());

        try {
            list.delete(-2);
            fail("Expected IndexOutOfBoundException");
        } catch (IndexOutOfBoundsException ex) {
            // Do nothing.
        }
    }

    @Test(timeout = SECOND)
    public void testDeleteNullElement() {
        IList<String> list = makeBasicList();
        int initSize = list.size();

        list.add(null);

        assertEquals(null, list.delete(3));
        assertEquals(initSize, list.size());
    }

    @Test(timeout = SECOND)
    public void testAlternatingAddAndDelete() {
        int iterators = 1000;

        IList<String> list = new DoubleLinkedList<>();

        for (int i = 0; i < iterators; i++) {
            String entry = "" + i;
            list.add(entry);
            assertEquals(1, list.size());

            String out = list.delete(0);
            assertEquals(entry, out);
            assertEquals(0, list.size());
        }
    }

    @Test(timeout = SECOND)
    public void testDeleteWithContainsAndIndexOf() {
        IList<String> list = makeBasicList();
        list.add("foo");
        list.add("bar");
        list.add("baz");
        int initSize = list.size();

        assertEquals("foo", list.delete(3));
        assertFalse(list.contains("foo"));
        assertEquals(3, list.indexOf("bar"));

        assertEquals("a", list.delete(0));
        assertFalse(list.contains("a"));
        assertEquals(0, list.indexOf("b"));

        assertEquals(initSize - 2, list.size());
    }

}
