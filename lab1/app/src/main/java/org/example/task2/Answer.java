package org.example.task2;

import java.util.ArrayList;
import java.util.List;

public class Answer {
    private double finalLength;
    private String path;

    /*
    START - algorythm started
    EXTRACT_MIN - choose the smallest weight
    UPDATE - found a shorter path
    SKIP - found an equal or bigger path
    END - end of algorythm
     */
    private final List<String> executionLog = new ArrayList<>();

    public double getFinalLength() {
        return finalLength;
    }
    public void setFinalLength(double finalLength) {
        this.finalLength = finalLength;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public List<String> getExecutionLog() {
        return executionLog;
    }

}
