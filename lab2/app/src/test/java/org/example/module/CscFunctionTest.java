package org.example.module;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import org.example.functions.MathFunction;
import org.example.functions.trigonometry.CscFunction;

@ExtendWith(MockitoExtension.class)
public class CscFunctionTest {
    @Mock
    private MathFunction sinMock;

    private final double EPS = 0.001;

    @ParameterizedTest(name = "Checking sin is near zero")
    @ValueSource(doubles = {-3.1415926, 0.0, 3.1415926, 6.283185}) // -PI, 0, PI, 2PI
    void testNaNOnZeroSin(double argument) {
        MathFunction csc = new CscFunction(sinMock);
        
        Mockito.doReturn(0.0).when(sinMock).calculate(eq(argument), eq(EPS / 2));

        double result = csc.calculate(argument, EPS);
        assertTrue(Double.isNaN(result));
    }

    @ParameterizedTest(name = "Checking invalid input")
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void testInvalidInput(double argument) {
        MathFunction csc = new CscFunction(sinMock);
        assertTrue(Double.isNaN(csc.calculate(argument, EPS)));
    }

    @ParameterizedTest(name = "Checking valid values")
    @CsvFileSource(resources = "/csc.csv", numLinesToSkip = 1)
    void testValidValues(double argument, double expected, double sinRes) {
        MathFunction csc = new CscFunction(sinMock);
        
        Mockito.doReturn(sinRes).when(sinMock).calculate(eq(argument), eq(EPS / 2));

        assertEquals(expected, csc.calculate(argument, EPS), EPS);
    }
}
