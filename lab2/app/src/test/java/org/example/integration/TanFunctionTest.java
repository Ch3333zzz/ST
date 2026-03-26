package org.example.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.example.functions.MathFunction;
import org.example.functions.trigonometry.CosFunction;
import org.example.functions.trigonometry.SinFunction;
import org.example.functions.trigonometry.TanFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class TanFunctionTest {
    private MathFunction sinReal;
    private MathFunction cosReal;

    private final double EPS = 0.001;

    @BeforeEach
    void setUp() {
        sinReal = new SinFunction();
        cosReal = new CosFunction(sinReal);
    }

    @ParameterizedTest(name = "Checking division by zero")
    @ValueSource(doubles = {1.570796, -1.570796, 4.712389, -4.712389})
    void testDivisionByZero(double argument) {
        MathFunction tan = new TanFunction(sinReal, cosReal);
        
        double result = tan.calculate(argument, EPS);
        assertTrue(Double.isNaN(result));
    }

    @ParameterizedTest(name = "checking valid values")
    @CsvSource(value = {
        "0,              0",                   // x=0
        "6.283185,       0",            // x=2PI
        "0.785398,       1",             // x=PI/4
        "-0.785398,     -1",            // x=-PI/4
        "2.356194,      -1",             // x=3PI/4
        "-2.356194,      1",            // x=-3PI/4
        "3.141592,       0",            // x=PI
        "-3.141592,      0"               // x=-PI
    })
    void validArgumentTest(double argument, double expected) {
        MathFunction tan = new TanFunction(sinReal, cosReal);

        assertEquals(expected, tan.calculate(argument, EPS), EPS);
    }

    @ParameterizedTest(name = "Checking invalid input")
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void testInvalidInput(double argument) {
        MathFunction tan = new TanFunction(sinReal, cosReal);
        assertTrue(Double.isNaN(tan.calculate(argument, EPS)));
    }
}