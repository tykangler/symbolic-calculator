package datastructures.concrete.dictionaries;

import datastructures.interfaces.IDictionary;
import misc.exceptions.NotYetImplementedException;

/**
 * @see datastructures.interfaces.IDictionary
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field.
    // We will be inspecting it in our private tests.
    private Pair<K, V>[] pairs;
    private static final int INIT_CAPACITY = 16;
    int size;

    // You may add extra fields or helper methods though!

    public ArrayDictionary() {
        pairs = makeArrayOfPairs(INIT_CAPACITY);
        size = 0;
    }

    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        return (Pair<K, V>[]) (new Pair[arraySize]);
    }

    @Override
    public V get(K key) {
        throw new NotYetImplementedException();
    }

    @Override
    public void put(K key, V value) {
        if (size >= pairs.length) {
            Pair<K, V>[] newArray = makeArrayOfPairs(pairs.length * 2);
            for (int i = 0; i < pairs.length; i++) {
                newArray[i] = pairs[i];
            }
            pairs = newArray;
        }
        pairs[size] = new Pair<K, V>(key, value);
        size++;
    }

    @Override
    public V remove(K key) {
        throw new NotYetImplementedException();
    }

    @Override
    public boolean containsKey(K key) {
        for (int i = 0; i < size; i++) {
            K currKey = pairs[i].key;
            if (key != null ? currKey.equals(key) : currKey == key) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    private static class Pair<K, V> {
        public K key;
        public V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }
}
