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
        hashTable.put("three nines", 666);

        assertEquals(1, hashTable.get("one"));
        assertEquals(2000, hashTable.get("two hundred"));
        assertEquals(666, hashTable.get("three nines"));
        assertNull(hashTable.get("four"));
    }

    @Test
    public void testRemove() {
        hashTable.put("one", 1);
        hashTable.put("two", 2);

        hashTable.remove("one");
        hashTable.remove("three");

        assertEquals(1, hashTable.getTableSize());
        assertNull(hashTable.get("one"));
    }

    @Test
    public void testUpdate() {
        hashTable.put("one", 1);
        hashTable.put("two", 2);

        hashTable.update("one", 11);
        assertEquals(11, hashTable.get("one"));

        hashTable.update("two", 22);
        assertEquals(22, hashTable.get("two"));

        hashTable.update("three", 3);
    }

    @Test
    public void testResize() {
        hashTable.put("one", 1);
        hashTable.put("two", 2);
        hashTable.put("four", 4);

        hashTable.resize();

        assertEquals(30, hashTable.getTableLength());
    }

    @Test
    public void testContainsKey() {
        hashTable.put("lol", 101);

        assertTrue(hashTable.containsKey("lol"));
        assertFalse(hashTable.containsKey("two"));
    }

    @Test
    public void testIterator() {
        hashTable.put("three", 3);
        hashTable.put("four", 4);

        int sum = 0;
        for (Entry<String, Integer> entry : hashTable) {
            sum += entry.value;
        }

        assertEquals(7, sum);
    }

    @Test
    public void testEquals() {
        hashTable.put("one", 1);
        hashTable.put("two", 2);
        hashTable.put("four", 4);

        HashTable<String, Integer> anotherTable = new HashTable<>();
        anotherTable.put("one", 1);
        anotherTable.put("two", 2);
        anotherTable.put("four", 4);

        assertTrue(hashTable.equals(anotherTable));
        anotherTable.put("three", 3);
        assertFalse(hashTable.equals(anotherTable));
    }

    @Test
    public void testHashCode() {
        hashTable.put("one", 1);
        hashTable.put("two hundred", 2000);
        hashTable.put("three nines", 999);

        int hash = hashTable.hashCode();
        int expected = 1763215547;

        assertEquals(expected, hash);
    }

    @Test
    public void testToString() {
        hashTable.put("one", 1);
        hashTable.put("two hundred", 2000);
        hashTable.put("three nines", 999);

        String expected = "{two hundred: 2000, one: 1, three nines: 999}";

        assertEquals(expected, hashTable.toString());
    }
}