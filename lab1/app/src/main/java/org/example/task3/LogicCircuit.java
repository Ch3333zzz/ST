package org.example.task3;

public class LogicCircuit {
    private Mood mood = Mood.JOY;
    private boolean on = false;

    public void turnOff() {
        on = false;
    }

    public void turnOn() {
        on = true;
    }

    public void changeMood(Mood mood) {
        this.mood = mood;
    }

    public String analysisOfMolecularComponents(Door door) {
        return door.toString();
    }

    public long analysisOfMolecularComponents(Brain brain) {
        return brain.getNumberOfNeuralConnections();
    }

    public short spaceAnalysis(ParsecOfSpace parsecOfSpace) {
        return parsecOfSpace.getHydrogenLvl();
    }

    public boolean isOn() {
        return this.on;
    }

    public Mood getMood() {
        return this.mood;
    }
}
