package ru.nsu.basargina;

import java.util.List;

/**
 * An interface for implementing various ways of representing the graph.
 *
 * @param <T> - type of vertex name
 */
public interface Graph<T> {
    void addVertex(Vertex<T> vertex);

    void removeVertex(Vertex<T> vertex);

    void addEdge(Edge<T> edge);

    void removeEdge(Edge<T> edge);

    void setDirected(boolean isDirected);

    List<Vertex<T>> getNeighbors(Vertex<T> vertex);

    List<Vertex<T>> getAllVertices();

    void readFromFile(String filename) throws Exception;

    @Override
    String toString();
}