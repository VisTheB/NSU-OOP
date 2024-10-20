package ru.nsu.basargina;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class for representing a graph as an adjacency list.
 *
 * @param <T> - type of vertex name
 */
class AdjacencyList<T> implements Graph<T> {
    private Map<Vertex<T>, List<Vertex<T>>> adjacencyList;
    private boolean isDirected;

    /**
     * Constructor for initialization of adjacency list of the graph.
     */
    public AdjacencyList() {
        this.adjacencyList = new HashMap<>();
        this.isDirected = false;
    }

    /**
     * Sets the orientation of the graph.
     *
     * @param isDirected - is directed or not
     */
    public void setDirected(boolean isDirected) {
        this.isDirected = isDirected;
    }

    /**
     * Add vertex to the graph.
     *
     * @param vertex - vertex to be added
     */
    @Override
    public void addVertex(Vertex<T> vertex) {
        adjacencyList.putIfAbsent(vertex, new ArrayList<>());
    }

    /**
     * Remove vertex from the graph.
     *
     * @param vertex - vertex to be removed
     */
    @Override
    public void removeVertex(Vertex<T> vertex) {
        adjacencyList.remove(vertex);

        // Delete vertex in other lists
        for (List<Vertex<T>> neighbors : adjacencyList.values()) {
            neighbors.remove(vertex);
        }
    }

    /**
     * Add edge to the graph.
     *
     * @param edge - edge to be added
     */
    @Override
    public void addEdge(Edge<T> edge) {
        Vertex<T> s = edge.getSource();
        Vertex<T> d = edge.getDestination();

        addVertex(s);
        addVertex(d);

        adjacencyList.get(s).add(d);

        if (!isDirected) {
            adjacencyList.get(d).add(s);
        }
    }

    /**
     * Remove edge from the graph.
     *
     * @param edge - edge to be removed
     */
    @Override
    public void removeEdge(Edge<T> edge) {
        Vertex<T> s = edge.getSource();
        Vertex<T> d = edge.getDestination();

        adjacencyList.get(s).remove(d);

        if (!isDirected) {
            adjacencyList.get(d).remove(s);
        }
    }

    /**
     * Get neighboring vertices.
     *
     * @param vertex - vertex where the neighbors are being searched for
     * @return - list of neighboring vertices
     */
    @Override
    public List<Vertex<T>> getNeighbors(Vertex<T> vertex) {
        return adjacencyList.getOrDefault(vertex, new ArrayList<>());
    }

    /**
     * Get all vertex names in the graph.
     *
     * @return list of all vertex names
     */
    @Override
    public List<Vertex<T>> getAllVertices() {
        return new ArrayList<>(adjacencyList.keySet());
    }

    /**
     * Reading a graph from a file and making a graph.
     *
     * @param filename - file name
     */
    @Override
    public void readFromFile(String filename) throws Exception {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));

            String line;

            // Check if graph directed or not
            line = br.readLine();
            this.isDirected = Boolean.parseBoolean(line.trim());

            // Reading vertices number
            line = br.readLine();
            int numberOfVertices = Integer.parseInt(line.trim());

            // Reading vertices
            for (int i = 0; i < numberOfVertices; i++) {
                line = br.readLine();
                addVertex(new Vertex<>((T) line.trim()));
            }

            // Reading edges number
            line = br.readLine();
            int numberOfEdges = Integer.parseInt(line.trim());

            // Reading edges
            for (int i = 0; i < numberOfEdges; i++) {
                line = br.readLine();
                String[] vertices = line.trim().split(" ");
                if (vertices.length == 2) {
                    Vertex<T> source = new Vertex<>((T) vertices[0]);
                    Vertex<T> destination = new Vertex<>((T) vertices[1]);
                    addEdge((new Edge<>(source, destination)));
                }
            }
        } catch (IOException | NumberFormatException e) {
            throw new Exception("Error reading from file");
        }
    }

    /**
     * Get string representation of the graph.
     *
     * @return graph in string form
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        for (Vertex<T> vertex : adjacencyList.keySet()) {
            str.append(vertex).append(": ");
            str.append(adjacencyList.get(vertex));
            str.append("\n");
        }
        return str.toString();
    }
}