package org.example.module;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

import org.example.functions.logarifms.LnFunction;


public class LnFunctionTest {

    private final LnFunction ln = new LnFunction();
    private final double EPS = 0.001;

    @ParameterizedTest(name = "checking standard values (x > 0)")
    @CsvFileSource(resources = "/ln.csv", numLinesToSkip = 1)
    void testStandardPoints(double x, double expected) {
        double actual = ln.calculate(x, EPS);
        
        assertEquals(expected, actual, EPS);
    }

    @ParameterizedTest(name = "Testing invalid x values (x <= 0 or NaN/Infinity)")
    @ValueSource(doubles = {0.0, -1.0, -15.5, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NaN})
    void testInvalidArgument(double x) {
        assertEquals(Double.NaN, ln.calculate(x, EPS));
    }

    @ParameterizedTest(name = "Testing invalid error values")
    @ValueSource(doubles = {-0.001, 0.2, Double.NaN, Double.POSITIVE_INFINITY})
    void testInvalidError(double invalidError) {
        assertThrows(IllegalArgumentException.class, () -> ln.calculate(2.0, invalidError));
    }

    @Test
    @DisplayName("Checking very low Error for MAX_ITER")
    void testMaxIterations() {
        double result = ln.calculate(10.0, Double.MIN_VALUE); 
        assertNotNull(result);
    }
}