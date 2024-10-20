package ru.nsu.basargina;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Class with tests for topological sort.
 */
public class TopologicalSortTest {
    @Test
    public void testAcyclicGraph() throws Exception {
        Graph<Integer> graph = new AdjacencyList<>();
        graph.setDirected(true);

        Vertex<Integer> v1 = new Vertex<>(1);
        Vertex<Integer> v2 = new Vertex<>(2);
        Vertex<Integer> v3 = new Vertex<>(3);
        Vertex<Integer> v4 = new Vertex<>(4);

        graph.addEdge(new Edge<>(v1, v2));
        graph.addEdge(new Edge<>(v1, v3));
        graph.addEdge(new Edge<>(v3, v4));
        graph.addEdge(new Edge<>(v2, v4));

        List<Vertex<Integer>> sortedList = TopologicalSort.sort(graph);

        assertTrue(
                sortedList.equals(Arrays.asList(v1, v2, v3, v4))
                        || sortedList.equals(Arrays.asList(v1, v3, v2, v4))
        );
    }

    @Test
    public void testGraphWithCycle() {
        Graph<Integer> graph = new AdjacencyList<>();

        Vertex<Integer> v1 = new Vertex<>(1);
        Vertex<Integer> v2 = new Vertex<>(2);
        Vertex<Integer> v3 = new Vertex<>(3);

        graph.addEdge(new Edge<>(v1, v2));
        graph.addEdge(new Edge<>(v2, v3));
        graph.addEdge(new Edge<>(v3, v1));

        Exception exception = assertThrows(Exception.class, () -> {
            TopologicalSort.sort(graph);
        });

        assertEquals("Graph contains cycles - topological sort is not possible.",
                exception.getMessage());
    }

    @Test
    public void testEmptyGraph() throws Exception {
        Graph<Integer> graph = new AdjacencyList<>();

        List<Vertex<Integer>> sortedList = TopologicalSort.sort(graph);

        assertTrue(sortedList.isEmpty());
    }
}