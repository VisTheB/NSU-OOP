package ru.nsu.basargina;

/**
 * Class for representing "key: value" pair in a hashtable.
 *
 * @param <K> - key type
 * @param <V> - value type
 */
public class Entry<K, V> {
    K key;
    V value;

    /**
     * Constructor for an entry.
     *
     * @param key - key
     * @param value - value
     */
    Entry(K key, V value) {
        this.key = key;
        this.value = value;
    }
}
