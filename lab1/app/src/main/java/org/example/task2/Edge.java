package org.example.task2;

public class Edge {
    
    private double weight;

    private Vertex from;

    private Vertex to;

    public Edge(double weight, Vertex from, Vertex to) {
        this.weight = weight;
        this.from = from;
        this.to = to;
    }

    public double getWeight() {
        return weight;
    }

    public Vertex getFrom() {
        return from;
    }

    public Vertex getTo() {
        return to;
    }
}
