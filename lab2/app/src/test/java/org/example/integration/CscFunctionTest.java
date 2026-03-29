package org.example.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import org.example.functions.MathFunction;
import org.example.functions.trigonometry.CscFunction;
import org.example.functions.trigonometry.SinFunction;

public class CscFunctionTest {
    
    private MathFunction sinReal;

    private final double EPS = 0.01;

    @BeforeEach
    void setUp() {
        sinReal = new SinFunction();
    }

    @ParameterizedTest(name = "Checking sin is near zero")
    @ValueSource(doubles = {-3.1415926, 0.0, 3.1415926, 6.283185}) // -PI, 0, PI, 2PI
    void testNaNOnZeroSin(double argument) {
        MathFunction csc = new CscFunction(sinReal);
        
        double result = csc.calculate(argument, EPS);
        assertTrue(Double.isNaN(result));
    }

    @ParameterizedTest(name = "Checking invalid input")
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void testInvalidInput(double argument) {
        MathFunction csc = new CscFunction(sinReal);
        assertTrue(Double.isNaN(csc.calculate(argument, EPS)));
    }

    @ParameterizedTest(name = "Checking valid values")
    @CsvFileSource(resources = "/csc.csv", numLinesToSkip = 1)
    void testValidValues(double argument, double expected) {
        MathFunction csc = new CscFunction(sinReal);
        
        assertEquals(expected, csc.calculate(argument, EPS), EPS);
    }
}
