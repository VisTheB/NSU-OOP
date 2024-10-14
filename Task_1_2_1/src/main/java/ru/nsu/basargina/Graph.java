package ru.nsu.basargina;

import java.util.List;

/**
 * An interface for implementing various ways of representing the graph.
 *
 * @param <T> - type of vertex name
 */
public interface Graph<T> {
    void addVertex(T vertex);
    void removeVertex(T vertex);
    void addEdge(T source, T destination);
    void removeEdge(T source, T destination);
    void setDirected(boolean isDirected);

    List<T> getNeighbors(T vertex);
    List<T> getAllVertices();

    void readFromFile(String filename);

    String toString();
}
