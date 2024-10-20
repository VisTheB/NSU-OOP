package ru.nsu.basargina;

/**
 * Class that represents an edge in the graph.
 *
 * @param <T> - type of vertexes
 */
public class Edge<T> {
    private final Vertex<T> source;
    private final Vertex<T> destination;

    /**
     * Constructor for creating an edge.
     *
     * @param source - source vertex
     * @param destination - destination vertex
     */
    public Edge(Vertex<T> source, Vertex<T> destination) {
        this.source = source;
        this.destination = destination;
    }

    /**
     * Get source vertex of an edge.
     *
     * @return source vertex
     */
    public Vertex<T> getSource() {
        return source;
    }

    /**
     * Get destination vertex of an edge.
     *
     * @return destination vertex
     */
    public Vertex<T> getDestination() {
        return destination;
    }

    /**
     * Get string representation of the edge.
     *
     * @return string edge
     */
    @Override
    public String toString() {
        return "(" + source + ", " + destination + ")";
    }
}