package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;
import misc.exceptions.NotYetImplementedException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Note: For more info on the expected behavior of your methods:
 * @see datastructures.interfaces.IList
 * (You should be able to control/command+click "IList" above to open the file from IntelliJ.)
 */
public class DoubleLinkedList<T> implements IList<T> {
    
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    @Override
    public void add(T item) {
        if (size == 0) {
            front = new Node<T>(item);
            back = front;
        } else {
            back.next = new Node<T>(item);
        }
        size++;
    }

    @Override
    public T remove() {
        Node<T> curr = back;
        if (size == 0 || front == null && back == null) {
            throw new EmptyContainerException();
        } else {
            back = null;
            return curr.data;
        }
    }

    // helper method, gets node at index
    // exceptions are handled in caller
    private Node<T> getNode(int index) {
        Node<T> curr = front;
        if (index == size - 1) {
            return back;
        }
        while (index > 0) {
            index--;    
            curr = curr.next;
        }
        return curr;    
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return getNode(index).data;
    }

    @Override
    public void set(int index, T item) {
        if (index < 0 || index >= this.size()) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> newNode = new Node(null, item, null);
        Node<T> curr = front;
        while (index != 0) {
            curr = curr.next;
            index--;
        }
        newNode.prev = curr.prev;
        newNode.next = curr.next;
        curr = newNode;
    }

    @Override
    public void insert(int index, T item) {
        if (index < 0 || index >= size + 1) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> newNode = new Node<T>(item);
        Node<T> nodeAtIndex = getNode(index);
        if (nodeAtIndex == null) { // inserting in empty list or past back
            if (index == 0) { // empty list
                front = newNode;
            } else { // past back
                newNode.prev = back;
                back.next = newNode;
            }
            back = newNode; // set new back pointer regardless
        } else { // non empty list or inserting in middle
            newNode.next = nodeAtIndex;
            if (nodeAtIndex == front) { // insert node at front
                front = newNode;
            } else { // insert node in middle
                newNode.prev = nodeAtIndex.prev;
                newNode.prev.next = newNode;
            }
            nodeAtIndex.prev = newNode;
        }
        size++;
    }

    @Override
    public T delete(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> nodeToDelete = getNode(index);
        if (nodeToDelete == front && nodeToDelete == back) {
            front = null;
            back = null;
        } else if (nodeToDelete == front) {
            front = nodeToDelete.next;
            front.prev = null;
        } else if (nodeToDelete == back) {
            back = nodeToDelete.prev;
            back.next = null;
        } else {
            nodeToDelete.next.prev = nodeToDelete.prev;
            nodeToDelete.prev.next = nodeToDelete.next;
        }
        size--;
        return nodeToDelete.data;
    }

    @Override
    public int indexOf(T item) {
        Node<T> curr = front;
        int index = 0;
        while (index != 0) {
            if (item == curr.data) {
                return index;
            }
            curr = curr.next;
            index++;
        }
        return -1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(T other) {
        Node<T> curr = front;
        int index = 0;
        while (index != 0) {
            if (other == curr) {
                return true;
            }
            curr = curr.next;
            index++;
        }
        return false;

    }

    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
            throw new NotYetImplementedException();
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() {
            throw new NotYetImplementedException();
        }
    }
}
