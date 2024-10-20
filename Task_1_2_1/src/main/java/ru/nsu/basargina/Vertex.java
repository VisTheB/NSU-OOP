package ru.nsu.basargina;

/**
 * Class that represents a vertex in the graph.
 *
 * @param <T> - type of vertex
 */
public class Vertex<T> {
    private final T name;

    /**
     * Constructor for creating a vertex.
     *
     * @param name - vertex name
     */
    public Vertex(T name) {
        this.name = name;
    }

    public T getName() {
        return name;
    }

    /**
     * Get string representation of the vertex.
     *
     * @return string vertex name
     */
    @Override
    public String toString() {
        return name.toString();
    }

    /**
     * Compares current vertex to given object.
     *
     * @param obj - object to be compared
     * @return true if object is equal to current vertex
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vertex<?> vertex = (Vertex<?>) obj;
        return name.equals(vertex.name);
    }

    /**
     * Creates vertex hash code.
     *
     * @return hash code in the form of vertex name
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}