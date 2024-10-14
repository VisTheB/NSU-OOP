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
    private Map<T, Integer> vertexMap; // vertexes and their indexes
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
    public void addVertex(T vertex) {
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
    public void removeVertex(T vertex) {
        if (vertexMap.containsKey(vertex)) {
            // Shift rows and columns
            int index = vertexMap.get(vertex);

            for (int i = index + 1; i < verticesTotal; i++) {
                for (int j = 0; j < verticesTotal; j++) {
                    adjacencyMatrix[i - 1][j] = adjacencyMatrix[i][j];
                }
            }

            for (int i = index + 1; i < verticesTotal; i++) {
                for(int j = 0; j < verticesTotal; j++) {
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
     * @param source - start vertex
     * @param destination - end vertex
     */
    @Override
    public void addEdge(T source, T destination) {
        addVertex(source);
        addVertex(destination);

        int sourceIndex = vertexMap.get(source);
        int destinationIndex = vertexMap.get(destination);
        adjacencyMatrix[sourceIndex][destinationIndex] = true;

        if (!this.isDirected) {
            adjacencyMatrix[destinationIndex][sourceIndex] = true;
        }
    }

    /**
     * Remove edge from the graph.
     *
     * @param source - start vertex
     * @param destination - end vertex
     */
    @Override
    public void removeEdge(T source, T destination) {
        if(vertexMap.containsKey(source) && vertexMap.containsKey(destination)) {
            int sourceIndex = vertexMap.get(source);
            int destinationIndex = vertexMap.get(destination);
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
    public List<T> getNeighbors(T vertex) {
        List<T> neighbors = new ArrayList<>();

        if (vertexMap.containsKey(vertex)) {
            int sourceIndex = vertexMap.get(vertex);

            for (Map.Entry<T, Integer> entry : vertexMap.entrySet()) {
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
    public List<T> getAllVertices() {
        return new ArrayList<>(vertexMap.keySet());
    }

    /**
     * Reading a graph from a file and making a graph.
     *
     * @param filename - file name
     */
    @Override
    public void readFromFile(String filename) {
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
                addVertex((T) line.trim());
            }

            // Reading edges number
            line = br.readLine();
            int numberOfEdges = Integer.parseInt(line.trim());

            // Reading edges
            for (int i = 0; i < numberOfEdges; i++) {
                line = br.readLine();
                String[] vertices = line.trim().split(" ");
                if (vertices.length == 2) {
                    addEdge((T) vertices[0], (T) vertices[1]);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Error reading from file: " + e.getMessage());
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

        for (T vertex : vertexMap.keySet()) {
            str.append(vertex).append(": ");
            for (T neighbor : getNeighbors(vertex)) {
                str.append(neighbor).append(" ");
            }
            str.append("\n");
        }
        return str.toString();
    }
}
