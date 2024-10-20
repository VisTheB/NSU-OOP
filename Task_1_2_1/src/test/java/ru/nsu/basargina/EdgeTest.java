package ru.nsu.basargina;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Class with tests for Edge.
 */
class EdgeTest {

    @Test
    void testToString() {
        Edge<String> e = new Edge<>(new Vertex<>("F"), new Vertex<>("N"));

        assertEquals("(F, N)", e.toString());
    }
}