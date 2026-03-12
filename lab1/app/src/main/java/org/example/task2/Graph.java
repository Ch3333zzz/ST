package org.example.task2;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private final List<Vertex> vertices = new ArrayList<>();
    private final List<Edge> edges = new ArrayList<>();

    public void addVertex(Vertex v) {
        vertices.add(v);
    }
    public void addEdge(Edge e) {
        edges.add(e);
    }

    public Answer findShortestPath(Vertex start, Vertex end) {
        return null;
    }

}
