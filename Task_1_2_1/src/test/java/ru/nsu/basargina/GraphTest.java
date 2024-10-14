package ru.nsu.basargina;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class with tests for 3 graph representations.
 */
public class GraphTest {
    private Graph<String> adjacencyListGraph;
    private Graph<String> adjacencyMatrixGraph;
    private Graph<String> incidenceMatrixGraph;

    @BeforeEach
    public void setUp() {
        adjacencyListGraph = new AdjacencyList<>();
        adjacencyMatrixGraph = new AdjacencyMatrix<>();
        incidenceMatrixGraph = new IncidenceMatrix<>();

        adjacencyListGraph.setDirected(true);
        adjacencyMatrixGraph.setDirected(true);
        incidenceMatrixGraph.setDirected(true);
    }

    @ParameterizedTest
    @CsvSource({
            "A, B",
            "A, C",
            "B, D"
    })
    void testAddEdge(String source, String destination) {
        adjacencyListGraph.addEdge(source, destination);
        adjacencyMatrixGraph.addEdge(source, destination);
        incidenceMatrixGraph.addEdge(source, destination);

        assertTrue(adjacencyListGraph.getNeighbors(source).contains(destination));
        assertTrue(adjacencyMatrixGraph.getNeighbors(source).contains(destination));
        assertTrue(incidenceMatrixGraph.getNeighbors(source).contains(destination));
    }

    @ParameterizedTest
    @CsvSource({
            "A, B",
            "A, C",
            "B, D"
    })
    void testRemoveEdge(String source, String destination) {
        adjacencyListGraph.addEdge(source, destination);
        adjacencyMatrixGraph.addEdge(source, destination);
        incidenceMatrixGraph.addEdge(source, destination);

        adjacencyListGraph.removeEdge(source, destination);
        adjacencyMatrixGraph.removeEdge(source, destination);
        incidenceMatrixGraph.removeEdge(source, destination);

        assertFalse(adjacencyListGraph.getNeighbors(source).contains(destination));
        assertFalse(adjacencyMatrixGraph.getNeighbors(source).contains(destination));
        assertFalse(incidenceMatrixGraph.getNeighbors(source).contains(destination));
    }

    @ParameterizedTest
    @CsvSource({
            "A, B",
            "C, D"
    })
    void testAddVertex(String vertex1, String vertex2) {
        adjacencyListGraph.addVertex(vertex1);
        adjacencyListGraph.addVertex(vertex2);
        adjacencyMatrixGraph.addVertex(vertex1);
        adjacencyMatrixGraph.addVertex(vertex2);
        incidenceMatrixGraph.addVertex(vertex1);
        incidenceMatrixGraph.addVertex(vertex2);

        assertEquals(2, adjacencyListGraph.getAllVertices().size());
        assertEquals(2, adjacencyMatrixGraph.getAllVertices().size());
        assertEquals(2, incidenceMatrixGraph.getAllVertices().size());
    }

    @ParameterizedTest
    @CsvSource({
            "A",
            "B",
    })
    void testRemoveVertex(String vertex) {
        adjacencyListGraph.addVertex(vertex);
        adjacencyMatrixGraph.addVertex(vertex);
        incidenceMatrixGraph.addVertex(vertex);

        adjacencyListGraph.removeVertex(vertex);
        adjacencyMatrixGraph.removeVertex(vertex);
        incidenceMatrixGraph.removeVertex(vertex);

        assertEquals(0, adjacencyListGraph.getAllVertices().size());
        assertEquals(0, adjacencyMatrixGraph.getAllVertices().size());
        assertEquals(0, incidenceMatrixGraph.getAllVertices().size());
    }

    @ParameterizedTest
    @CsvSource({
            "A, B",
            "A, C",
    })
    void testGetNeighbors(String source, String destination) {
        adjacencyListGraph.addEdge(source, destination);
        adjacencyMatrixGraph.addEdge(source, destination);
        incidenceMatrixGraph.addEdge(source, destination);

        assertTrue(adjacencyListGraph.getNeighbors(source).contains(destination));
        assertTrue(adjacencyMatrixGraph.getNeighbors(source).contains(destination));
        assertTrue(incidenceMatrixGraph.getNeighbors(source).contains(destination));
    }

    @ParameterizedTest
    @CsvSource({
            "A, B",
            "C, D",
    })
    void testGetAllVertices(String vertex1, String vertex2) {
        adjacencyListGraph.addVertex(vertex1);
        adjacencyListGraph.addVertex(vertex2);
        adjacencyMatrixGraph.addVertex(vertex1);
        adjacencyMatrixGraph.addVertex(vertex2);
        incidenceMatrixGraph.addVertex(vertex1);
        incidenceMatrixGraph.addVertex(vertex2);

        ArrayList<String> expected = new ArrayList<>(Arrays.asList(vertex1, vertex2));

        assertEquals(expected, adjacencyListGraph.getAllVertices());
        assertEquals(expected, adjacencyMatrixGraph.getAllVertices());
        assertEquals(expected, incidenceMatrixGraph.getAllVertices());
    }

    @ParameterizedTest
    @CsvSource({
            "testGraph1.txt"
    })
    void testReadFromFile(String filename) throws IOException {
        adjacencyListGraph.readFromFile(filename);
        adjacencyMatrixGraph.readFromFile(filename);
        incidenceMatrixGraph.readFromFile(filename);

        ArrayList<String> expectedVertexes = new ArrayList<>(Arrays.asList("A", "B", "C"));
        ArrayList<String> expectedNeighbours = new ArrayList<>(Arrays.asList("B", "C"));

        assertEquals(3, adjacencyListGraph.getAllVertices().size());
        assertEquals(expectedVertexes, adjacencyListGraph.getAllVertices());
        assertEquals(expectedNeighbours, adjacencyListGraph.getNeighbors("A"));

        assertEquals(3, adjacencyMatrixGraph.getAllVertices().size());
        assertEquals(expectedVertexes, adjacencyMatrixGraph.getAllVertices());
        assertEquals(expectedNeighbours, adjacencyMatrixGraph.getNeighbors("A"));

        assertEquals(3, incidenceMatrixGraph.getAllVertices().size());
        assertEquals(expectedVertexes, incidenceMatrixGraph.getAllVertices());
        assertEquals(expectedNeighbours, incidenceMatrixGraph.getNeighbors("A"));
    }
}