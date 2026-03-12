package org.example.task3;

import org.example.task3.exceptions.OutOfNeuralConnectionsException;

public class Brain {
    private long numberOfNeuralConnections = 100000000;
    private final String owner; 

    public Brain(String owner) {
        this.owner = owner;
    }

    public void createNeuralConnection() {
        numberOfNeuralConnections++;
    }

    public void deleteNeuralConnection() throws OutOfNeuralConnectionsException {
        if (numberOfNeuralConnections <= 0) 
            throw new OutOfNeuralConnectionsException();
        numberOfNeuralConnections--; 
    }

    public long getNumberOfNeuralConnections() {
        return numberOfNeuralConnections;
    }
}
