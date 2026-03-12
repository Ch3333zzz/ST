package org.example;

import org.example.task1.Calculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    private Calculator calculator;
    private final double ERROR = 0.0001;

    @BeforeEach
    public void setUp() {
        calculator = new Calculator();
    }

    @ParameterizedTest()
    @CsvSource({
        "-1.0, -1.570796", // -PI/2
        "-0.5, -0.523598", // -PI/6
        "0.0,   0.0",
        "0.5,   0.523598",
        "1.0,   1.570796",
        "0.999, 1.526123"
    })
    void testAsinValidRange(double input, double expected) {
        assertEquals(expected, calculator.asin(input), ERROR);
    }

    @ParameterizedTest()
    @ValueSource(doubles = {-2.0, 2.0})
    void testAsinInvalidRange(double input) {
        assertTrue(Double.isNaN(calculator.asin(input)));
    }
}