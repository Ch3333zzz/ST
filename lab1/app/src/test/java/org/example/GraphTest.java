package org.example;

import org.example.task2.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GraphTest {

    private Graph graph;

    @BeforeEach
    void setUp() {
        graph = new Graph();
    }

    @Test
    void pathWhenStartEqualsTarget() {
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
        Vertex v0 = new Vertex("0");
        Vertex v1 = new Vertex("1");
        Vertex v2 = new Vertex("2");
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);

        graph.addEdge(new Edge(5.0, v0, v1));
        graph.addEdge(new Edge(1.0, v0, v2));
        graph.addEdge(new Edge(10.0, v2, v1));

        List<Answer> results = graph.findAllShortestPaths(v0);
        List<String> actualLog = results.get(0).getExecutionLog();

        List<String> expectedLog = List.of(
            "Finding Cheapest Unknown Vertex",
            "Cheapest Unknown Vertex: 0",
            "Setting known field to True",
            "Updating neighbors of vertex 0",
            "INF > 0.0 + 5.0",
            "INF > 0.0 + 1.0",
            "Finding Cheapest Unknown Vertex",
            "Cheapest Unknown Vertex: 2",
            "Setting known field to True",
            "Updating neighbors of vertex 2",
            "5.0 > 1.0 + 10.0", 
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

    @Test
    void testUsingOthersAnswerFields() {
        Vertex v0 = new Vertex("0");
        Vertex v1 = new Vertex("1");
        Vertex v2 = new Vertex("2");
        
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);

        graph.addEdge(new Edge(10.0, v0, v2)); 
        graph.addEdge(new Edge(1.0, v0, v1));
        graph.addEdge(new Edge(1.0, v1, v2));
        
        graph.addEdge(new Edge(1.0, v2, v0)); 

        List<Answer> results = graph.findAllShortestPaths(v0);
        
        Answer ans2 = results.stream().filter(a -> a.getPath().contains("2")).findFirst().get();
        
        assertEquals("0 -> 1 -> 2", ans2.getPath());
        assertEquals(2.0, ans2.getFinalLength());
        assertTrue(ans2.getExecutionLog().size() > 0);    
    }

    @Test
    void testPathReconstruction() {
        Vertex v0 = new Vertex("A");
        Vertex v1 = new Vertex("B");
        Vertex v2 = new Vertex("C");
        
        graph.addVertex(v0);
        graph.addVertex(v1);
        graph.addVertex(v2);

        graph.addEdge(new Edge(1.0, v0, v1));
        graph.addEdge(new Edge(1.0, v1, v2));

        List<Answer> results = graph.findAllShortestPaths(v0);

        Answer ansC = results.stream()
                            .filter(a -> a.getPath().contains("C"))
                            .findFirst()
                            .get();

        assertEquals("A -> B -> C", ansC.getPath());
    }
}