package ru.nsu.basargina;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class for representing a graph as an incidence matrix.
 *
 * @param <T> - type of vertex name
 */
public class IncidenceMatrix<T> implements Graph<T> {
    private Map<T, Integer> vertexMap;
    private List<int[]> incidenceMatrix;
    private boolean isDirected;
    private int verticesTotal; // current vertex quantity in the graph
    private int edgesTotal;

    /**
     * Constructor for initialization of incidence matrix of the graph.
     */
    public IncidenceMatrix() {
        this.vertexMap = new HashMap<>();
        this.incidenceMatrix = new ArrayList<>();
        this.isDirected = false;
        this.verticesTotal = 0;
        this.edgesTotal = 0;
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
    public void addVertex(T vertex) {
        vertexMap.putIfAbsent(vertex, verticesTotal++);
    }

    /**
     * Remove vertex from the graph.
     *
     * @param vertex - vertex to be removed
     */
    @Override
    public void removeVertex(T vertex) {
        if (vertexMap.containsKey(vertex)) {
            int index = vertexMap.get(vertex);
            vertexMap.remove(vertex);
            verticesTotal--;

            for (int[] edge : incidenceMatrix) {
                edge[index] = 0; // Zero incidence for deleted vertex
            }
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

        int[] edgeVector = new int[verticesTotal];
        edgeVector[vertexMap.get(source)] = 1;

        if (this.isDirected) {
            edgeVector[vertexMap.get(destination)] = -1;
        } else {
            edgeVector[vertexMap.get(destination)] = 1;
        }

        edgesTotal++;
        incidenceMatrix.add(edgeVector);
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
            int destIndex = vertexMap.get(destination);

            // Look for the edge that is incident to given vertexes
            for (int i = 0; i < edgesTotal; i++) {
                int[] edge = incidenceMatrix.get(i);

                boolean isEdgeToRemove = (this.isDirected && edge[sourceIndex] == 1 && edge[destIndex] == -1) ||
                        (!this.isDirected && edge[sourceIndex] == 1 && edge[destIndex] == 1);

                if (isEdgeToRemove) {
                    if (edgesTotal == 1) {
                        incidenceMatrix.remove(0);
                    } else {
                        // Remove edge and replace it with the last edge
                        incidenceMatrix.set(i, incidenceMatrix.get(edgesTotal - 1));
                        // Remove last edge
                        incidenceMatrix.remove(edgesTotal - 1);
                    }
                    edgesTotal--;
                    return;
                }
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

        int vertexIndex = vertexMap.get(vertex);

        for (int[] edge : incidenceMatrix) {
            if (edge[vertexIndex] != 0) {
                for (Map.Entry<T, Integer> entry : vertexMap.entrySet()) {
                    T possibleNeighbor = entry.getKey();
                    int neighborIndex = entry.getValue();

                    if (edge[neighborIndex] != 0 && neighborIndex != vertexIndex) {
                        neighbors.add(possibleNeighbor);
                    }
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