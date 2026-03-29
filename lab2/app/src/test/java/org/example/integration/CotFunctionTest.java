package org.example.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.example.functions.MathFunction;
import org.example.functions.trigonometry.CosFunction;
import org.example.functions.trigonometry.CotFuntion;
import org.example.functions.trigonometry.SinFunction;
import org.example.functions.trigonometry.TanFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class CotFunctionTest {
    private MathFunction sinReal;
    private MathFunction cosReal;

    @BeforeEach
    void setUp() {
        sinReal = new SinFunction();
        cosReal = new CosFunction(sinReal);
    }

    private final double EPS = 0.001;

    @ParameterizedTest(name = "Checking division by zero")
    @ValueSource(doubles = {0, 3.1415926, 6.283185, -3.1415926})
    void testDivisionByZero(double argument) {
        MathFunction cot = new CotFuntion(cosReal, sinReal);

        double result = cot.calculate(argument, EPS);
        assertTrue(Double.isNaN(result));
    }

    @ParameterizedTest(name = "checking valid values")
    @CsvFileSource(resources = "/cot.csv", numLinesToSkip = 1)
    void validArgumentTest(double argument, double expected) {
        MathFunction cot = new CotFuntion(cosReal, sinReal);

        assertEquals(expected, cot.calculate(argument, EPS), EPS);
    }

    @ParameterizedTest(name = "Checking invalid input")
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void testInvalidInput(double argument) {
        MathFunction tan = new TanFunction(cosReal, sinReal);

        assertTrue(Double.isNaN(tan.calculate(argument, EPS)));
    }
}