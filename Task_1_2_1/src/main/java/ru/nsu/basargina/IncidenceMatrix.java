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
    private Map<Vertex<T>, Integer> vertexMap;
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
    public void addVertex(Vertex<T> vertex) {
        vertexMap.putIfAbsent(vertex, verticesTotal++);
    }

    /**
     * Remove vertex from the graph.
     *
     * @param vertex - vertex to be removed
     */
    @Override
    public void removeVertex(Vertex<T> vertex) {
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
     * @param edge - edge to be added
     */
    @Override
    public void addEdge(Edge<T> edge) {
        Vertex<T> s = edge.getSource();
        Vertex<T> d = edge.getDestination();

        addVertex(s);
        addVertex(d);

        int[] edgeVector = new int[verticesTotal];
        edgeVector[vertexMap.get(s)] = 1;

        if (this.isDirected) {
            edgeVector[vertexMap.get(d)] = -1;
        } else {
            edgeVector[vertexMap.get(d)] = 1;
        }

        edgesTotal++;
        incidenceMatrix.add(edgeVector);
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
            int destIndex = vertexMap.get(d);

            // Look for the edge that is incident to given vertexes
            for (int i = 0; i < edgesTotal; i++) {
                int[] matEdge = incidenceMatrix.get(i);

                boolean isEdgeToRemove = (this.isDirected && matEdge[sourceIndex] == 1 && matEdge[destIndex] == -1)
                        || (!this.isDirected && matEdge[sourceIndex] == 1 && matEdge[destIndex] == 1);

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
    public List<Vertex<T>> getNeighbors(Vertex<T> vertex) {
        List<Vertex<T>> neighbors = new ArrayList<>();

        int vertexIndex = vertexMap.get(vertex);

        for (int[] edge : incidenceMatrix) {
            if (edge[vertexIndex] != 0) {
                for (Map.Entry<Vertex<T>, Integer> entry : vertexMap.entrySet()) {
                    Vertex<T> possibleNeighbor = entry.getKey();
                    int neighborIndex = entry.getValue();

                    if (!this.isDirected) {
                        if (edge[neighborIndex] == 1 && neighborIndex != vertexIndex) {
                            neighbors.add(possibleNeighbor);
                        }
                    } else {
                        if (edge[neighborIndex] == -1 && neighborIndex != vertexIndex) {
                            neighbors.add(possibleNeighbor);
                        }
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