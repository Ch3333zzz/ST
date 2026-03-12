package org.example.task2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Graph {
    private final List<Vertex> vertices = new ArrayList<>();
    private final List<Edge> edges = new ArrayList<>();

    public void addVertex(Vertex v) {
        vertices.add(v);
    }
    public void addEdge(Edge e) {
        edges.add(e);
    }

    public List<Answer> findAllShortestPaths(Vertex start) {
        Map<Vertex, Double> costs = new HashMap<>();
        Map<Vertex, Vertex> paths = new HashMap<>();
        Map<Vertex, Boolean> known = new HashMap<>();
        List<String> logs = new ArrayList<>();

        for (Vertex v : vertices) {
            costs.put(v, Double.POSITIVE_INFINITY);
            paths.put(v, null);
            known.put(v, false);
        }
        costs.put(start, 0.0);

        while (true) {
            logs.add("Finding Cheapest Unknown Vertex");
            
            Vertex v = null;
            double minCost = Double.POSITIVE_INFINITY;
            
            List<Vertex> sortedVertices = vertices.stream()
                .sorted(Comparator.comparing(Vertex::getName))
                .collect(Collectors.toList());

            for (Vertex curr : sortedVertices) {
                if (!known.get(curr) && costs.get(curr) < minCost) {
                    minCost = costs.get(curr);
                    v = curr;
                }
            }

            if (v == null) break;

            logs.add("Cheapest Unknown Vertex: " + v.getName());
            known.put(v, true);
            logs.add("Setting known field to True");
            logs.add("Updating neighbors of vertex " + v.getName());
            
            Vertex finalV = v;
            List<Edge> sortedEdges = edges.stream()
                .filter(e -> e.getFrom().equals(finalV))
                .sorted(Comparator.comparing(e -> e.getTo().getName()))
                .collect(Collectors.toList());

            for (Edge edge : sortedEdges) {
                Vertex w = edge.getTo();
                if (!known.get(w)) {
                    double weight = edge.getWeight();
                    double oldCost = costs.get(w);
                    double newCost = costs.get(v) + weight;

                    String oldCostStr = (oldCost == Double.POSITIVE_INFINITY) ? "INF" : String.valueOf(oldCost);
                    logs.add(oldCostStr + " > " + costs.get(v) + " + " + weight);

                    if (newCost < oldCost) {
                        costs.put(w, newCost);
                        paths.put(w, v);
                    }
                }
            }
        }

        List<Answer> results = new ArrayList<>();
        for (Vertex v : vertices) {
            Answer a = new Answer();
            a.setFinalLength(costs.get(v));
            a.setPath(reconstructPath(paths, start, v));
            a.getExecutionLog().addAll(logs); 
            results.add(a);
        }
        return results;
    }

    private String reconstructPath(Map<Vertex, Vertex> predecessors, Vertex start, Vertex end) {
        if (predecessors.get(end) == null && !start.equals(end)) return "No path";
        List<String> pathNodes = new ArrayList<>();
        for (Vertex v = end; v != null; v = predecessors.get(v)) {
            pathNodes.add(v.getName());
            if (v.equals(start)) break;
        }
        Collections.reverse(pathNodes);
        return String.join(" -> ", pathNodes);
    }

}
