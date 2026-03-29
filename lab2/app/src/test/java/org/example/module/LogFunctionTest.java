package org.example.module;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;

import org.example.functions.MathFunction;
import org.example.functions.logarifms.LogFunction;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class LogFunctionTest {
    @Mock
    private MathFunction lnMock;

    private final double EPS = 0.001;

    @ParameterizedTest(name = "Testing values for base out of range of permissible values")
    @ValueSource(doubles = { 0, 1, -5.0, Double.NaN, Double.POSITIVE_INFINITY })
    void testInvalidBase(double base) {
        assertThrows(IllegalArgumentException.class, () -> new LogFunction(lnMock, base));
    }

    @ParameterizedTest(name = "Testing values for base inside of range of permissible values")
    @CsvFileSource(resources = "/log_base.csv", numLinesToSkip = 1)
    void testValidBase(double base) {
        assertDoesNotThrow(() -> new LogFunction(lnMock, base));
    }

    @ParameterizedTest(name = "Testing values for argument out of range of permissible values")
    @ValueSource(doubles = { 0, -1.0})
    void testInvalidArgument(double argument) {
        LogFunction testLog = new LogFunction(lnMock, 2.0);
        assertThrows(IllegalArgumentException.class, () -> testLog.calculate(argument, EPS));
    }

    @ParameterizedTest(name = "Testing invalid values for argument")
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY })
    void testArgumentNaN(double x) {
        LogFunction testLog = new LogFunction(lnMock, 2.0);
        double result = testLog.calculate(x, EPS);
        
        assertTrue(Double.isNaN(result));
    }

    @ParameterizedTest(name = "Testing values for argument inside of range of permissible values")
    @CsvFileSource(resources = "/log_arg.csv", numLinesToSkip = 1)
    void testValidArgument(double argument, double base, double expectedLog, double lnArgument, double lnBase) {
        Mockito.doReturn(lnArgument).when(lnMock).calculate(eq(argument), anyDouble());
        
        Mockito.doReturn(lnBase).when(lnMock).calculate(eq(base), anyDouble());

        LogFunction testLog = new LogFunction(lnMock, base);

        assertEquals(expectedLog, testLog.calculate(argument, EPS), EPS);
    }
}