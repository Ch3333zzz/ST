package org.example.task3;

import java.util.ArrayList;
import java.util.List;

public class Robot {
    private Mood mood;
    private List<LogicCircuit> circuits = new ArrayList<>();

    public void addCircuit(LogicCircuit circuit) {
        this.circuits.add(circuit);
    }
    public void removeCircuit(LogicCircuit circuit) {
        this.circuits.remove(this.circuits.indexOf(circuit));
    }

    public void turnOffCircuit(LogicCircuit circuit) {
        this.circuits.get(this.circuits.indexOf(circuit)).turnOff();
    }
    public void turnOnCircuit(LogicCircuit circuit) {
        this.circuits.get(this.circuits.indexOf(circuit)).turnOn();
    }

    public void changeMood(Mood mood) {
        this.mood = mood;
    }

    public Mood getMood() {
        return this.mood;
    }

    public List<LogicCircuit> geCircuits() {
        return circuits;
    }
}
