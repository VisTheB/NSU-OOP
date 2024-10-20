package ru.nsu.basargina;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Class with tests for 3 graph representations.
 */
public class GraphTest {
    private Graph<String> adjacencyListGraph;
    private Graph<String> adjacencyMatrixGraph;
    private Graph<String> incidenceMatrixGraph;

    /**
     * Set up 3 graph representations.
     */
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
        Vertex<String> s = new Vertex<>(source);
        Vertex<String> d = new Vertex<>(destination);
        Edge<String> e = new Edge<>(s, d);

        adjacencyListGraph.addEdge(e);
        adjacencyMatrixGraph.addEdge(e);
        incidenceMatrixGraph.addEdge(e);

        assertTrue(adjacencyListGraph.getNeighbors(s).contains(d));
        assertTrue(adjacencyMatrixGraph.getNeighbors(s).contains(d));
        assertTrue(incidenceMatrixGraph.getNeighbors(s).contains(d));
    }

    @ParameterizedTest
    @CsvSource({
            "A, B, A, C"
    })
    void testRemoveEdge(String source1, String destination1, String source2, String destination2) {

        Vertex<String> s1 = new Vertex<>(source1);
        Vertex<String> d1 = new Vertex<>(destination1);
        Edge<String> e1 = new Edge<>(s1, d1);
        adjacencyListGraph.addEdge(e1);
        adjacencyMatrixGraph.addEdge(e1);
        incidenceMatrixGraph.addEdge(e1);

        Vertex<String> s2 = new Vertex<>(source2);
        Vertex<String> d2 = new Vertex<>(destination2);
        Edge<String> e2 = new Edge<>(s2, d2);
        adjacencyListGraph.addEdge(e2);
        adjacencyMatrixGraph.addEdge(e2);
        incidenceMatrixGraph.addEdge(e2);

        adjacencyListGraph.removeEdge(e1);
        adjacencyMatrixGraph.removeEdge(e1);
        incidenceMatrixGraph.removeEdge(e1);

        assertFalse(adjacencyListGraph.getNeighbors(s1).contains(d1));
        assertFalse(adjacencyMatrixGraph.getNeighbors(s1).contains(d1));
        assertFalse(incidenceMatrixGraph.getNeighbors(s1).contains(d1));
    }

    @ParameterizedTest
    @CsvSource({
            "A, B",
            "C, D"
    })
    void testAddVertex(String v1, String v2) {
        Vertex<String> vertex1 = new Vertex<>(v1);
        Vertex<String> vertex2 = new Vertex<>(v2);

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
            "A, B, A, C"
    })
    void testRemoveVertex(String source1, String destination1,
                          String source2, String destination2) {

        Vertex<String> s1 = new Vertex<>(source1);
        Vertex<String> d1 = new Vertex<>(destination1);
        Edge<String> e1 = new Edge<>(s1, d1);
        adjacencyListGraph.addEdge(e1);
        adjacencyMatrixGraph.addEdge(e1);
        incidenceMatrixGraph.addEdge(e1);

        Vertex<String> s2 = new Vertex<>(source2);
        Vertex<String> d2 = new Vertex<>(destination2);
        Edge<String> e2 = new Edge<>(s2, d2);
        adjacencyListGraph.addEdge(e2);
        adjacencyMatrixGraph.addEdge(e2);
        incidenceMatrixGraph.addEdge(e2);

        adjacencyListGraph.removeVertex(s1);
        adjacencyMatrixGraph.removeVertex(s1);
        incidenceMatrixGraph.removeVertex(s1);

        assertEquals(2, adjacencyListGraph.getAllVertices().size());
        assertEquals(2, adjacencyMatrixGraph.getAllVertices().size());
        assertEquals(2, incidenceMatrixGraph.getAllVertices().size());
    }

    @ParameterizedTest
    @CsvSource({
            "A, B",
            "A, C",
    })
    void testGetNeighbors(String source, String destination) {
        Vertex<String> s = new Vertex<>(source);
        Vertex<String> d = new Vertex<>(destination);
        Edge<String> e = new Edge<>(s, d);

        adjacencyListGraph.addEdge(e);
        adjacencyMatrixGraph.addEdge(e);
        incidenceMatrixGraph.addEdge(e);

        assertTrue(adjacencyListGraph.getNeighbors(s).contains(d));
        assertTrue(adjacencyMatrixGraph.getNeighbors(s).contains(d));
        assertTrue(incidenceMatrixGraph.getNeighbors(s).contains(d));
    }

    @ParameterizedTest
    @CsvSource({
            "A, B",
            "C, D",
    })
    void testGetAllVertices(String v1, String v2) {
        Vertex<String> vertex1 = new Vertex<>(v1);
        Vertex<String> vertex2 = new Vertex<>(v2);

        adjacencyListGraph.addVertex(vertex1);
        adjacencyListGraph.addVertex(vertex2);
        adjacencyMatrixGraph.addVertex(vertex1);
        adjacencyMatrixGraph.addVertex(vertex2);
        incidenceMatrixGraph.addVertex(vertex1);
        incidenceMatrixGraph.addVertex(vertex2);

        ArrayList<Vertex<String>> expected = new ArrayList<>();
        expected.add(vertex1);
        expected.add(vertex2);

        assertEquals(expected, adjacencyListGraph.getAllVertices());
        assertEquals(expected, adjacencyMatrixGraph.getAllVertices());
        assertEquals(expected, incidenceMatrixGraph.getAllVertices());
    }

    @ParameterizedTest
    @CsvSource({
            "testGraph1.txt"
    })
    void testReadFromFile(String filename) throws Exception {
        adjacencyListGraph.readFromFile(filename);
        adjacencyMatrixGraph.readFromFile(filename);
        incidenceMatrixGraph.readFromFile(filename);

        ArrayList<Vertex<String>> expectedVertexes = new ArrayList<>();
        Vertex<String> a = new Vertex<>("A");
        expectedVertexes.add(a);
        expectedVertexes.add(new Vertex<>("B"));
        expectedVertexes.add(new Vertex<>("C"));

        ArrayList<Vertex<String>> expectedNeighbours = new ArrayList<>();
        expectedNeighbours.add(new Vertex<>("B"));
        expectedNeighbours.add(new Vertex<>("C"));

        assertEquals(3, adjacencyListGraph.getAllVertices().size());
        assertEquals(expectedVertexes, adjacencyListGraph.getAllVertices());
        assertEquals(expectedNeighbours, adjacencyListGraph.getNeighbors(a));

        assertEquals(3, adjacencyMatrixGraph.getAllVertices().size());
        assertEquals(expectedVertexes, adjacencyMatrixGraph.getAllVertices());
        assertEquals(expectedNeighbours, adjacencyMatrixGraph.getNeighbors(a));

        assertEquals(3, incidenceMatrixGraph.getAllVertices().size());
        assertEquals(expectedVertexes, incidenceMatrixGraph.getAllVertices());
        assertEquals(expectedNeighbours, incidenceMatrixGraph.getNeighbors(a));
    }

    @ParameterizedTest
    @CsvSource({
            "testGraph2.txt"
    })
    void testToStringNotDir(String filename) throws Exception {
        adjacencyListGraph.readFromFile(filename);
        adjacencyMatrixGraph.readFromFile(filename);
        incidenceMatrixGraph.readFromFile(filename);

        String expected1 = "A: [B, C]\nB: [A, C]\nC: [B, A]\n";
        String expected2 = "A: [B, C]\nB: [A, C]\nC: [A, B]\n";

        assertEquals(expected1, adjacencyListGraph.toString());
        assertEquals(expected2, adjacencyMatrixGraph.toString());
        assertEquals(expected1, incidenceMatrixGraph.toString());
    }

    @ParameterizedTest
    @CsvSource({
            "testGraph1.txt"
    })
    void testToStringDir(String filename) throws Exception {
        adjacencyListGraph.readFromFile(filename);
        adjacencyMatrixGraph.readFromFile(filename);
        incidenceMatrixGraph.readFromFile(filename);

        String expected = "A: [B, C]\nB: [C]\nC: []\n";

        assertEquals(expected, adjacencyListGraph.toString());
        assertEquals(expected, adjacencyMatrixGraph.toString());
        assertEquals(expected, incidenceMatrixGraph.toString());
    }

    @ParameterizedTest
    @CsvSource({
            "testGraph12.txt"
    })
    void testException(String filename) {
        try {
            adjacencyListGraph.readFromFile(filename);
        } catch (Exception e) {
            String expected = "Error reading from file";
            assertEquals(expected, e.getMessage());
        }

        try {
            adjacencyMatrixGraph.readFromFile(filename);
        } catch (Exception e) {
            String expected = "Error reading from file";
            assertEquals(expected, e.getMessage());
        }

        try {
            incidenceMatrixGraph.readFromFile(filename);
        } catch (Exception e) {
            String expected = "Error reading from file";
            assertEquals(expected, e.getMessage());
        }
    }
}