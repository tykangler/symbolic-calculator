package datastructures.concrete.dictionaries;

import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;
import misc.exceptions.NotYetImplementedException;

/**
 * @see datastructures.interfaces.IDictionary
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    
    private Pair<K, V>[] pairs;
    private static final int INIT_CAPACITY = 16;
    int size;

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
        for (Pair<K, V> pair : pairs) {
            if (pair.key == key) {
                return pair.value;
            }
        }
        throw new NoSuchKeyException();
    }

    private void increaseCapacity() {
        Pair<K, V>[] newArray = makeArrayOfPairs(pairs.length * 2);
        for (int i = 0; i < pairs.length; i++) {
            newArray[i] = pairs[i];
        }
        pairs = newArray;
    }

    private int indexOf(K key) {
        for (int i = 0; i < size; i++) {
            K currKey = pairs[i].key;
            if (key != null ? currKey.equals(key) : currKey == key) {
                return i;
            }
        }
        return -1;
    } 

    @Override
    public void put(K key, V value) {
        if (size >= pairs.length) {
            increaseCapacity();
        }
        int keyIndex = indexOf(key);
        if (keyIndex < 0) {
            pairs[size] = new Pair<K, V>(key, value);
            size++;
        } else {
            pairs[keyIndex].value = value;
        }
    }

    @Override
    public V remove(K key) {
        throw new NotYetImplementedException();
    }

    @Override
    public boolean containsKey(K key) {
        return indexOf(key) >= 0;
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
