package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

class CalculatorTest {

    private Calculator calculator;

    private final double ERROR = 0.0001;

    @BeforeEach
    public void testSetUp() {
        calculator = new Calculator();
    }

    @Test 
    void testNegativeOutOfRange() {
        assertTrue(Double.isNaN(calculator.asin(-2)));
    }

    @Test 
    void testLeftBoundaryPoint() {
        assertEquals(-Math.PI/2, calculator.asin(-1), ERROR);
    } 

    @Test 
    void testNegativeInRange() {
        assertEquals(-Math.PI/6, calculator.asin(-0.5), ERROR);
    }

    @Test 
    void testZero() {
        assertEquals(0, calculator.asin(0), ERROR);
    }

    @Test 
    void testPositiveInRange() {
        assertEquals(Math.PI/6, calculator.asin(0.5), ERROR);
    }

    @Test 
    void testRightBoundaryPoint() {
        assertEquals(Math.PI/2, calculator.asin(1), ERROR);
    } 

    @Test 
    void testPositiveOutOfRange() {
        assertTrue(Double.isNaN(calculator.asin(2)));
    }

}
