package org.example.module;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

import org.example.functions.trigonometry.SinFunction;

public class SinFunctionTest {

    private final SinFunction sin = new SinFunction();
    private final double EPS = 0.001;

    @ParameterizedTest(name = "checking values in ODZ")
    @CsvFileSource(resources = "/sin.csv", numLinesToSkip = 1)
    void testStandardPoints(double x, double expected) {
        double actual = sin.calculate(x, EPS);
        
        assertEquals(expected, actual, EPS);
    }

    @ParameterizedTest(name = "Testing invalid x values")
    @ValueSource(doubles = {Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NaN})
    void testInvalidArgument(double x) {
        assertEquals(Double.NaN, sin.calculate(x, EPS));
    }

    @ParameterizedTest(name = "Testing invalid error values")
    @ValueSource(doubles = {-0.001, 0.2, Double.NaN, Double.POSITIVE_INFINITY})
    void testInvalidError(double invalidError) {
        assertThrows(IllegalArgumentException.class, () -> sin.calculate(1.0, invalidError));
    }

    @Test
    @DisplayName("Checking very low Error for MAX_ITER")
    void testMaxIterations() {
        double result = sin.calculate(1.0, Double.MIN_VALUE); 
        assertNotNull(result);
    }
}