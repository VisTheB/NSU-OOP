package ru.nsu.basargina;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class for representing a graph as an adjacency matrix.
 *
 * @param <T> - type of vertex name
 */
public class AdjacencyMatrix<T> implements Graph<T> {
    private Map<Vertex<T>, Integer> vertexMap; // vertexes and their indexes
    private boolean[][] adjacencyMatrix;
    private boolean isDirected;
    private int verticesTotal; // current vertex quantity in the graph

    /**
     * Constructor for initialization of adjacency matrix of the graph.
     */
    public AdjacencyMatrix() {
        this.vertexMap = new HashMap<>();
        this.adjacencyMatrix = new boolean[0][0];
        this.verticesTotal = 0;
        this.isDirected = false;
    }

    /**
     * Sets the orientation of the graph.
     *
     * @param isDirected - is directed or not
     */
    @Override
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
        if (!vertexMap.containsKey(vertex)) {
            if (verticesTotal == adjacencyMatrix.length) {
                resizeMatrix(verticesTotal * 2 + 1);
            }
            vertexMap.put(vertex, verticesTotal++);
        }
    }

    /**
     * Remove vertex from the graph.
     *
     * @param vertex - vertex to be removed
     */
    @Override
    public void removeVertex(Vertex<T> vertex) {
        if (vertexMap.containsKey(vertex)) {
            // Shift rows and columns
            int index = vertexMap.get(vertex);

            for (int i = index + 1; i < verticesTotal; i++) {
                for (int j = 0; j < verticesTotal; j++) {
                    adjacencyMatrix[i - 1][j] = adjacencyMatrix[i][j];
                }
            }

            for (int i = index + 1; i < verticesTotal; i++) {
                for (int j = 0; j < verticesTotal; j++) {
                    adjacencyMatrix[j][i - 1] = adjacencyMatrix[j][i];
                }
            }

            vertexMap.remove(vertex);

            verticesTotal--;
            resizeMatrix(verticesTotal);
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

        int sourceIndex = vertexMap.get(s);
        int destinationIndex = vertexMap.get(d);
        adjacencyMatrix[sourceIndex][destinationIndex] = true;

        if (!this.isDirected) {
            adjacencyMatrix[destinationIndex][sourceIndex] = true;
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

        if (vertexMap.containsKey(s) && vertexMap.containsKey(d)) {
            int sourceIndex = vertexMap.get(s);
            int destinationIndex = vertexMap.get(d);
            adjacencyMatrix[sourceIndex][destinationIndex] = false;

            if (!this.isDirected) {
                adjacencyMatrix[destinationIndex][sourceIndex] = false;
            }
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
        List<Vertex<T>> neighbors = new ArrayList<>();

        if (vertexMap.containsKey(vertex)) {
            int sourceIndex = vertexMap.get(vertex);

            for (Map.Entry<Vertex<T>, Integer> entry : vertexMap.entrySet()) {
                if (adjacencyMatrix[sourceIndex][entry.getValue()]) {
                    neighbors.add(entry.getKey());
                }
            }
        }
        return neighbors;
    }

    /**
     * Get all vertex names in the graph.
     *
     * @return list of all vertex names
     */
    @Override
    public List<Vertex<T>> getAllVertices() {
        return new ArrayList<>(vertexMap.keySet());
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
            throw new Exception("Error reading from file", e);
        }
    }

    /**
     * Modify adjacency matrix for new size.
     *
     * @param newSize - new size of the matrix
     */
    private void resizeMatrix(int newSize) {
        boolean[][] newMatrix = new boolean[newSize][newSize];
        // Relevant size will depend on whether we want to increase or decrease the matrix
        int relevantSize = Math.min(newSize, adjacencyMatrix.length);

        for (int i = 0; i < relevantSize; i++) {
            for (int j = 0; j <  relevantSize; j++) {
                newMatrix[i][j] = adjacencyMatrix[i][j];
            }
        }
        adjacencyMatrix = newMatrix;
    }

    /**
     * Get string representation of the graph.
     *
     * @return graph in string form
     */
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        int neighboursCnt;

        for (Vertex<T> vertex : vertexMap.keySet()) {
            neighboursCnt = 0;
            str.append(vertex).append(": [");
            for (Vertex<T> neighbor : getNeighbors(vertex)) {
                neighboursCnt++;
                str.append(neighbor).append(", ");
            }
            int len = str.length();
            if (neighboursCnt > 0) {
                str.delete(len - 2, len); // delete last ", "
            }
            str.append("]\n");
        }
        return str.toString();
    }
}