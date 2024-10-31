package ru.nsu.basargina;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Class for representing hash-table with "key: value" pairs.
 *
 * @param <K> key type
 * @param <V> value type
 */
public class HashTable<K, V> implements Iterable<Entry<K, V>> {
    private static final int INIT_CAPACITY = 15;
    private static final double LOAD_FACTOR = 0.75; // when the table have to be enlarged

    private List<Entry<K, V>>[] table;
    private int tableSize;
    private int modifCnt; // modification counter for ConcurrentModificationException

    /**
     * Constructor for creating hashtable.
     */
    public HashTable() {
        this.table = new ArrayList[INIT_CAPACITY];
        for (int i = 0; i < INIT_CAPACITY; i++) {
            table[i] = new ArrayList<>();
        }

        this.tableSize = 0;
        this.modifCnt = 0;
    }

    /**
     * Create hash less than table length for the key.
     *
     * @param key key
     * @return hash
     */
    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    /**
     * Get table size (i.e. entries quantity).
     *
     * @return current size of the table.
     */
    public int getTableSize() {
        return this.tableSize;
    }

    /**
     * Get table length (i.e. buckets quantity).
     *
     * @return current length of the table
     */
    public int getTableLength() {
        return table.length;
    }

    /**
     * Add "key: value" pair to the table.
     *
     * @param key key to be added
     * @param value value to be added
     */
    public void put(K key, V value) {
        if (tableSize >= LOAD_FACTOR * table.length) {
            resize();
        }

        int index = hash(key);
        for (Entry<K, V> entry : table[index]) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }

        table[index].add(new Entry<>(key, value));
        tableSize++;
        modifCnt++;
    }

    /**
     * Remove entry from the bucket.
     *
     * @param key entry key
     */
    public void remove(K key) {
        int index = hash(key);

        for (Entry<K, V> entry : table[index]) {
            if (entry.key.equals(key)) {
                table[index].remove(entry);
                tableSize--;
                modifCnt++;
                return;
            }
        }
    }

    /**
     * Get entry value by the key.
     *
     * @param key entry key
     * @return entry value or null if entry doesn't exist
     */
    public V get(K key) {
        int index = hash(key);

        for (Entry<K, V> entry : table[index]) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;
    }

    /**
     * Checks if table contains entry with key.
     *
     * @param key key to be searched
     * @return true if contains
     */
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    /**
     * Update value by the key.
     *
     * @param key key
     * @param value value to be updated
     */
    public void update(K key, V value) {
        int index = hash(key);

        for (Entry<K, V> entry : table[index]) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }
    }

    /**
     * Enlarge table size.
     */
    public void resize() {
        List<Entry<K, V>>[] newTable = new ArrayList[table.length * 2];
        for (int i = 0; i < newTable.length; i++) {
            newTable[i] = new ArrayList<>();
        }

        for (List<Entry<K, V>> bucket : table) {
            for (Entry<K, V> entry : bucket) {
                int index = (entry.key == null) ? 0
                        : Math.abs(entry.key.hashCode() % newTable.length);
                newTable[index].add(entry);
            }
        }

        table = newTable;
    }

    /**
     * Creates iterator for the bucket.
     *
     * @return iterator
     */
    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new HashTableIterator();
    }

    /**
     * Class for iterating through the hash table elements.
     */
    private class HashTableIterator implements Iterator<Entry<K, V>> {
        private int currentBucket;
        private Iterator<Entry<K, V>> bucketIterator;
        private int expectedModifCnt;

        /**
         * Constructor for iterator.
         */
        HashTableIterator() {
            this.expectedModifCnt = modifCnt;
            this.currentBucket = -1;
            this.bucketIterator = Collections.emptyIterator();
        }

        /**
         * Moves iterator to the next not empty bucket.
         */
        private void move() {
            while (!bucketIterator.hasNext() && currentBucket + 1 < table.length) {
                bucketIterator = table[++currentBucket].iterator();
            }
        }

        /**
         * Checks if iterator has next element.
         *
         * @return true if has next element
         */
        @Override
        public boolean hasNext() {
            move();
            return bucketIterator.hasNext();
        }

        /**
         * Get iterator's next element.
         *
         * @return next entry
         */
        @Override
        public Entry<K, V> next() {
            if (modifCnt != expectedModifCnt) {
                throw new ConcurrentModificationException();
            }

            move();
            if (!bucketIterator.hasNext()) {
                throw new NoSuchElementException();
            }

            return bucketIterator.next();
        }
    }

    /**
     * Compares current hash-table to given object.
     *
     * @param obj - object to be compared
     * @return true if object is equal to current hash-table
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        HashTable<K, V> other = (HashTable<K, V>) obj;

        if (this.tableSize != other.tableSize) {
            return false;
        }

        // Compare elements in buckets
        for (int i = 0; i < this.table.length; i++) {
            List<Entry<K, V>> thisBucket = this.table[i];
            List<Entry<K, V>> otherBucket = other.table[i];

            if (thisBucket.size() != otherBucket.size()) {
                return false;
            }

            for (Entry<K, V> entry : thisBucket) {
                // Find the same entry in otherBucket
                boolean found = false;
                for (Entry<K, V> otherEntry : otherBucket) {
                    if (entry.key.equals(otherEntry.key)
                            && entry.value.equals(otherEntry.value)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Creates hash-table hash code.
     *
     * @return hash code for hash-table
     */
    @Override
    public int hashCode() {
        int result = 31; // this multiplier is usually used for hashCode
        result += tableSize;

        for (List<Entry<K, V>> bucket : table) {
            for (Entry<K, V> entry : bucket) {
                result = 31 * result + (entry.key != null ? entry.key.hashCode() : 0);
                result = 31 * result + (entry.value != null ? entry.value.hashCode() : 0);
            }
        }

        return result;
    }

    /**
     * Represents hash-table in a string format.
     *
     * @return string
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("{");
        for (Entry<K, V> entry : this) {
            str.append(entry.key).append(": ").append(entry.value).append(", ");
        }
        str.setLength(str.isEmpty() ? 1 : str.length() - 2); // delete last ", "
        str.append("}");
        return str.toString();
    }
}