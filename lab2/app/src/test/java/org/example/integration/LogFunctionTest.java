package org.example.integration;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.example.functions.MathFunction;
import org.example.functions.logarifms.LnFunction;
import org.example.functions.logarifms.LogFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class LogFunctionTest {
    
    private MathFunction lnReal;

    @BeforeEach
    void setUp() {
        lnReal = new LnFunction();
    }

    private final double EPS = 0.001;

    @ParameterizedTest(name = "Testing values for base out of range of permissible values")
    @ValueSource(doubles = { 0, 1, -5.0, Double.NaN, Double.POSITIVE_INFINITY })
    void testInvalidBase(double base) {
        assertThrows(IllegalArgumentException.class, () -> new LogFunction(lnReal, base));
    }

    @ParameterizedTest(name = "Testing values for base inside of range of permissible values")
    @CsvFileSource(resources = "/log_base.csv", numLinesToSkip = 1)
    void testValidBase(double base) {
        assertDoesNotThrow(() -> new LogFunction(lnReal, base));
    }

    @ParameterizedTest(name = "Testing values for argument out of range of permissible values")
    @ValueSource(doubles = { 0, -1.0})
    void testInvalidArgument(double argument) {
        LogFunction testLog = new LogFunction(lnReal, 2.0);
        assertThrows(IllegalArgumentException.class, () -> testLog.calculate(argument, EPS));
    }

    @ParameterizedTest(name = "Testing invalid values for argument")
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY })
    void testArgumentNaN(double x) {
        LogFunction testLog = new LogFunction(lnReal, 2.0);
        double result = testLog.calculate(x, EPS);
        
        assertTrue(Double.isNaN(result));
    }

    @ParameterizedTest(name = "Testing values for argument inside of range of permissible values")
    @CsvFileSource(resources = "/log_arg.csv", numLinesToSkip = 1)
    void testValidArgument(double argument, double base, double expectedLog) {

        LogFunction testLog = new LogFunction(lnReal, base);

        assertEquals(expectedLog, testLog.calculate(argument, EPS), EPS);
    }
}