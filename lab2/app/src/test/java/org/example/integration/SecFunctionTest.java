package org.example.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;


import org.example.functions.MathFunction;
import org.example.functions.trigonometry.CosFunction;
import org.example.functions.trigonometry.SecFunction;
import org.example.functions.trigonometry.SinFunction;

public class SecFunctionTest {

    private MathFunction cosReal;

    private final double EPS = 0.01;

    @BeforeEach
    void setUp() {
        cosReal = new CosFunction(new SinFunction());
    }
    @ParameterizedTest(name = "Checking sin is near zero")
    @ValueSource(doubles = {1.570796, -1.570796, 4.712388}) // PI/2, -PI/2, PI/2 + 2PI
    void testNaNOnZeroSin(double argument) {
        MathFunction sec = new SecFunction(cosReal);
        
        double result = sec.calculate(argument, EPS);
        assertTrue(Double.isNaN(result));
    }

    @ParameterizedTest(name = "Checking invalid input")
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void testInvalidInput(double argument) {
        MathFunction sec = new SecFunction(cosReal);
        assertTrue(Double.isNaN(sec.calculate(argument, EPS)));
    }

    @ParameterizedTest(name = "Checking valid values")
    @CsvFileSource(resources = "/sec.csv", numLinesToSkip = 1)
    void testValidValues(double argument, double expected) {
        MathFunction sec = new SecFunction(cosReal);
        
        assertEquals(expected, sec.calculate(argument, EPS), EPS);
    }
}
