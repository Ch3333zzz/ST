package org.example;

import org.example.task2.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GraphTest {

    @Test
    void pathWhenStartEqualsTarget() {
        Graph graph = new Graph();
        Vertex v0 = new Vertex("0");
        graph.addVertex(v0);

        List<Answer> results = graph.findAllShortestPaths(v0);
        List<String> actualLog = results.get(0).getExecutionLog();

        List<String> expectedLog = List.of(
            "Finding Cheapest Unknown Vertex",
            "Cheapest Unknown Vertex: 0",
            "Setting known field to True",
            "Updating neighbors of vertex 0",
            "Finding Cheapest Unknown Vertex"
        );

        assertEquals(expectedLog, actualLog);
    }

    @Test
    void pathDoesNotExist() {
        Graph graph = new Graph();
        Vertex v0 = new Vertex("0");
        Vertex v1 = new Vertex("1");
        graph.addVertex(v0);
        graph.addVertex(v1);

        List<Answer> results = graph.findAllShortestPaths(v0);
        List<String> actualLog = results.get(0).getExecutionLog();

        List<String> expectedLog = List.of(
            "Finding Cheapest Unknown Vertex",
            "Cheapest Unknown Vertex: 0",
            "Setting known field to True",
            "Updating neighbors of vertex 0",
            "Finding Cheapest Unknown Vertex"
        );

        assertEquals(expectedLog, actualLog);
    }

    @Test
    void pathDoesNotUpdate() {
        Graph graph = new Graph();
        Vertex v0 = new Vertex("0");
        Vertex v1 = new Vertex("1");
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addEdge(new Edge(5.0, v0, v1));

        List<Answer> results = graph.findAllShortestPaths(v0);
        List<String> actualLog = results.get(0).getExecutionLog();

        List<String> expectedLog = List.of(
            "Finding Cheapest Unknown Vertex",
            "Cheapest Unknown Vertex: 0",
            "Setting known field to True",
            "Updating neighbors of vertex 0",
            "INF > 0.0 + 5.0",
            "Finding Cheapest Unknown Vertex",
            "Cheapest Unknown Vertex: 1",
            "Setting known field to True",
            "Updating neighbors of vertex 1",
            "Finding Cheapest Unknown Vertex"
        );

        assertEquals(expectedLog, actualLog);
    }

    @Test
    void pathUpdates() {
        Graph graph = new Graph();
        Vertex v0 = new Vertex("0");
        Vertex v1 = new Vertex("1");
        Vertex v2 = new Vertex("2");
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);

        graph.addEdge(new Edge(10.0, v0, v2));
        graph.addEdge(new Edge(1.0, v0, v1));
        graph.addEdge(new Edge(1.0, v1, v2));

        List<Answer> results = graph.findAllShortestPaths(v0);
        List<String> actualLog = results.get(0).getExecutionLog();

        List<String> expectedLog = List.of(
            "Finding Cheapest Unknown Vertex",
            "Cheapest Unknown Vertex: 0",
            "Setting known field to True",
            "Updating neighbors of vertex 0",
            "INF > 0.0 + 1.0",
            "INF > 0.0 + 10.0",
            "Finding Cheapest Unknown Vertex",
            "Cheapest Unknown Vertex: 1",
            "Setting known field to True",
            "Updating neighbors of vertex 1",
            "10.0 > 1.0 + 1.0",
            "Finding Cheapest Unknown Vertex",
            "Cheapest Unknown Vertex: 2",
            "Setting known field to True",
            "Updating neighbors of vertex 2",
            "Finding Cheapest Unknown Vertex"
        );

        assertEquals(expectedLog, actualLog);
    }
}