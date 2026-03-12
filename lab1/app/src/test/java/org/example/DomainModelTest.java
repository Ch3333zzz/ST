package org.example;

import org.example.task3.*;
import org.example.task3.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DomainModelTest {

    @Nested
    class ParsecOfSpaceTests {
        @Test
        void shouldClampHydrogenLevel() {
            ParsecOfSpace emptySpace = new ParsecOfSpace((short) -10);
            assertEquals(0, emptySpace.getHydrogenLvl());

            ParsecOfSpace denseSpace = new ParsecOfSpace((short) 150);
            assertEquals(100, denseSpace.getHydrogenLvl());

            ParsecOfSpace normalSpace = new ParsecOfSpace((short) 42);
            assertEquals(42, normalSpace.getHydrogenLvl());
        }
    }

    @Nested
    class DoorTests {
        private AutomaticDoor door;

        @BeforeEach
        void setUp() {
            door = new AutomaticDoor();
        }

        @Test
        void shouldOpenAndCloseNormally() throws InteractionWithDamagedDoorException {
            door.close();
            assertTrue(door.isClosed());

            door.open();
            assertFalse(door.isClosed());
        }

        @Test
        void shouldThrowExceptionWhenDoorIsDamaged() {
            door.damage();
            door.damage();
            door.damage();

            assertThrows(InteractionWithDamagedDoorException.class, () -> door.open());
            assertThrows(InteractionWithDamagedDoorException.class, () -> door.close());
        }

        @Test
        void shouldNotExceedHpLimits() {
            for (int i = 0; i < 6; i++) door.damage(); 
            assertEquals(0, door.getHP());
            
            for (int i = 0; i < 5; i++) door.repare();
            assertEquals(100, door.getHP());
        }
    }

    @Nested
    class BrainTests {
        @Test
        void shouldManageNeuralConnections() throws OutOfNeuralConnectionsException {
            Brain brain = new Brain("Human");
            long initialConnections = brain.getNumberOfNeuralConnections();

            brain.createNeuralConnection();
            assertEquals(initialConnections + 1, brain.getNumberOfNeuralConnections());

            brain.deleteNeuralConnection();
            assertEquals(initialConnections, brain.getNumberOfNeuralConnections());
        }

        @Test
        void shouldThrowExceptionWhenOutOfConnections() {
            Brain brain = new Brain("Test");
            assertThrows(OutOfNeuralConnectionsException.class, () -> {
                while (true) {
                    brain.deleteNeuralConnection();
                }
            });
        }
    }

    @Nested
    class LogicCircuitAndRobotTests {
        private LogicCircuit circuit;
        private Robot marvin;

        @BeforeEach
        void setUp() {
            circuit = new LogicCircuit();
            marvin = new Robot();
        }

        @Test
        void shouldPerformAnalysis() {
            AutomaticDoor door = new AutomaticDoor();
            Brain brain = new Brain("Human");
            ParsecOfSpace space = new ParsecOfSpace((short) 50);

            assertNotNull(circuit.analysisOfMolecularComponents(door));
            assertEquals(100000000, circuit.analysisOfMolecularComponents(brain));
            assertEquals(50, circuit.spaceAnalysis(space));
        }

        @Test
        void shouldChangeMoodAndPower() {
            circuit.turnOn();
            assertTrue(circuit.isOn());

            circuit.changeMood(Mood.DESPAIR);
            assertEquals(Mood.DESPAIR, circuit.getMood());

            circuit.turnOff();
            assertFalse(circuit.isOn());
        }

        @Test
        void robotShouldManageCircuits() {
            marvin.changeMood(Mood.CONTEMPT);
            assertEquals(Mood.CONTEMPT, marvin.getMood());;

            marvin.addCircuit(circuit);
            assertTrue(marvin.geCircuits().contains(circuit));

            marvin.turnOnCircuit(circuit);
            int idx = marvin.geCircuits().indexOf(circuit);
            assertTrue(marvin.geCircuits().get(idx).isOn());

            marvin.turnOffCircuit(circuit);
            assertFalse(marvin.geCircuits().get(idx).isOn());

            marvin.removeCircuit(circuit);
            assertFalse(marvin.geCircuits().contains(circuit));
        }
    }
}