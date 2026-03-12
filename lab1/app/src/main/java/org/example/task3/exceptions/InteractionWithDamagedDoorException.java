package org.example.task3.exceptions;

public class InteractionWithDamagedDoorException extends Exception {
    public InteractionWithDamagedDoorException() {
        super("Can not interract with damaged door");
    }
}
