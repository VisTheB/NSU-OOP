package ru.nsu.basargina;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Class with tests for HashTable.
 */
class HashTableTest {
    private HashTable<String, Integer> hashTable;

    @BeforeEach
    public void setup() {
        hashTable = new HashTable<>();
    }

    @Test
    public void testPutGet() {
        hashTable.put("one", 1);
        hashTable.put("two hundred", 2000);
        hashTable.put("three nines", 999);

        assertEquals(1, hashTable.get("one"));
        assertEquals(2000, hashTable.get("two hundred"));
        assertEquals(999, hashTable.get("three nines"));
        assertNull(hashTable.get("four"));
    }
}