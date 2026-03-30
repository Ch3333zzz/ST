package org.example.module;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;

import org.example.functions.MathFunction;
import org.example.functions.trigonometry.TanFunction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TanFunctionTest {
    @Mock
    private MathFunction sinMock;
    @Mock
    private MathFunction cosMock;

    private final double EPS = 0.001;

    @Test
    @DisplayName("Devision by 0")
    void testDivisionByZero() {
        MathFunction tan = new TanFunction(sinMock, cosMock);
        
        Mockito.doReturn(0.0).when(cosMock).calculate(anyDouble(), anyDouble());
        Mockito.doReturn(1.0).when(sinMock).calculate(anyDouble(), anyDouble());

        double result = tan.calculate(123.0, EPS);
        assertTrue(Double.isNaN(result));
    }

    @ParameterizedTest(name = "checking valid values")
    @CsvFileSource(resources = "/tan.csv", numLinesToSkip = 1)
    void validArgumentTest(double argument, double expected, double sinRes, double cosRes) {
        MathFunction tan = new TanFunction(sinMock, cosMock);
        
        Mockito.doReturn(sinRes).when(sinMock).calculate(eq(argument), anyDouble());
        Mockito.doReturn(cosRes).when(cosMock).calculate(eq(argument), anyDouble());

        assertEquals(expected, tan.calculate(argument, EPS), EPS);
    }

    @ParameterizedTest(name = "Checking invalid input")
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void testInvalidInput(double argument) {
        MathFunction tan = new TanFunction(sinMock, cosMock);
        assertTrue(Double.isNaN(tan.calculate(argument, EPS)));
    }
}