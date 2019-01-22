package datastructures;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;

import static org.junit.Assert.assertTrue;

/**
 * This file should contain any tests that check and make sure your
 * delete method is efficient.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDeleteStress extends TestDoubleLinkedList {

    public IList<Integer> makeIntList(int cap) {
        IList<Integer> list = new DoubleLinkedList<Integer>();
        for (int i = 0; i < cap; i++) {
            list.add(i);
        }
        return list;
    }

    @Test(timeout=SECOND)
    public void testDeleteMany() {
        IList<Integer> list = makeIntList(50);
        IList<Integer> list2 = new DoubleLinkedList<Integer>();
        for (int i = 0; i < 30; i++) {
            list2.add(i + 10);
        }
        assertTrue(list.size() == 50);
        for (int i = 0; i < 10; i++) {
            list.delete(list.size() - 1);
        }
        assertTrue(list.size() == 40);
        for (int i = 0; i < 10; i++) {
            list.delete(0);
        }
        assertTrue(list.size() == 30);
        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i), list2.get(i));
        }
    }

    @Test(timeout=15*SECOND)
    public void testDeleteAtFrontIsEfficient() {
        int cap = 1000000;
        IList<Integer> list = makeIntList(cap);
        int deleteAmount = 20;
        for (int i = 0; i < deleteAmount; i++) {
            list.delete(0);
        }
        assertTrue(list.size() == cap - deleteAmount);
    }

    @Test(timeout=15*SECOND)
    public void testDeleteNearFrontIsEfficient() {
        int cap = 1000000;
        IList<Integer> list = makeIntList(cap);
        int deleteAmount = 20;
        for (int i = 0; i < deleteAmount; i++) {
            list.delete(10);
        }
        assertTrue(list.size() == cap - deleteAmount);
    }

    @Test(timeout=15*SECOND) 
    public void testDeleteAtEndIsEfficient() {
        int cap = 1000000;
        IList<Integer> list = makeIntList(cap);
        int deleteAmount = 20;
        for (int i = 0; i < deleteAmount; i++) {
            list.delete(list.size() - 1);
        }
        assertTrue(list.size() == cap - deleteAmount);
    }

    @Test(timeout=15*SECOND)
    public void testDeleteNearEndIsEfficient() {
        int cap = 1000000;
        IList<Integer> list= makeIntList(cap);
        int deleteAmount = 20;
        for (int i = 0; i < deleteAmount; i++) {
            list.delete(list.size() - 10);
        }
        assertTrue(list.size() == cap - deleteAmount);
    }

}
