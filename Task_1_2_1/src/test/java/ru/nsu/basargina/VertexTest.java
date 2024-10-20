package ru.nsu.basargina;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Class with tests for Vertex.
 */
class VertexTest {

    @Test
    void testGetName() {
        Vertex<String> v = new Vertex<>("LOL");
        assertEquals("LOL", v.getName());
    }
}