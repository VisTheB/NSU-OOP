package ru.nsu.basargina;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Class for topological sort of the graph.
 */
public class TopologicalSort {
    /**
     * Topological sort.
     *
     * @param graph - graph for sorting
     * @return list of sorted vertexes
     * @param <T> - vertexes type
     * @throws Exception if graph contains cycles
     */
    public static <T> List<Vertex<T>> sort(Graph<T> graph) throws Exception {
        List<Vertex<T>> sortedList = new ArrayList<>();
        List<Vertex<T>> allVertices = new ArrayList<>();
        allVertices = graph.getAllVertices();
        Map<Vertex<T>, Integer> inDegree = new HashMap<>();

        for (Vertex<T> vertex : allVertices) {
            inDegree.put(vertex, 0);
        }

        for (Vertex<T> vertex : allVertices) {
            for (Vertex<T> neighbor : graph.getNeighbors(vertex)) {
                inDegree.put(neighbor, inDegree.get(neighbor) + 1);
            }
        }

        Queue<Vertex<T>> queue = new LinkedList<>();
        for (Vertex<T> vertex : allVertices) {
            if (inDegree.get(vertex) == 0) {
                queue.add(vertex);
            }
        }

        while (!queue.isEmpty()) {
            Vertex<T> vertex = queue.poll();
            sortedList.add(vertex);

            for (Vertex<T> neighbor : graph.getNeighbors(vertex)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.add(neighbor);
                }
            }
        }

        if (sortedList.size() != graph.getAllVertices().size()) {
            throw new Exception("Graph contains cycles - topological sort is not possible.");
        }

        return sortedList;
    }
}