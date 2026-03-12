package org.example;

import org.example.task2.Edge;
import org.example.task2.Graph;
import org.example.task2.Vertex;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GraphTest {

    private Graph graph;

    @BeforeEach
    void setUp() {
        graph = new Graph();
    }

    @Test
    void pathWhenStartEqualsTarget() {
        Vertex v1 = new Vertex("1");

        graph.addVertex(v1);

        //assert sth;
    }

    @Test
    void pathDoesNotExist() {
        Vertex v1 = new Vertex("1");
        Vertex v2 = new Vertex("2");
        Vertex v3 = new Vertex("3");
        Vertex v4 = new Vertex("4");

        Edge e1 = new Edge(3, v1, v2);
        Edge e2 = new Edge(23, v3, v4);


        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);
        graph.addVertex(v4);

        graph.addEdge(e1);
        graph.addEdge(e2);

        //assert sth;

    }

    @Test
    void pathDoesNotUpdate() {
        Vertex v1 = new Vertex("1");
        Vertex v2 = new Vertex("2");
        Vertex v3 = new Vertex("3");

        Edge e1 = new Edge(3, v1, v2);
        Edge e2 = new Edge(23, v1, v3);
        Edge e3 = new Edge(23, v2, v3);



        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);

        graph.addEdge(e1);
        graph.addEdge(e2);
        graph.addEdge(e3);

        //assert sth;

    }

     @Test
    void pathUpdates() {
        Vertex v1 = new Vertex("1");
        Vertex v2 = new Vertex("2");
        Vertex v3 = new Vertex("3");

        Edge e1 = new Edge(300, v1, v2);
        Edge e2 = new Edge(23, v1, v3);
        Edge e3 = new Edge(23, v2, v3);



        graph.addVertex(v1);
        graph.addVertex(v2);
        graph.addVertex(v3);

        graph.addEdge(e1);
        graph.addEdge(e2);
        graph.addEdge(e3);

        //assert sth;
    }
}
