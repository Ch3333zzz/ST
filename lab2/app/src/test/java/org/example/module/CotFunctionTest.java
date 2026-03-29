package org.example.module;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;

import org.example.functions.MathFunction;
import org.example.functions.trigonometry.CotFuntion;
import org.example.functions.trigonometry.TanFunction;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CotFunctionTest {
    @Mock
    private MathFunction sinMock;
    @Mock
    private MathFunction cosMock;

    private final double EPS = 0.001;

    @ParameterizedTest(name = "Checking division by zero")
    @ValueSource(doubles = {0, 3.1415926, 6.283185, -3.1415926})
    void testDivisionByZero(double argument) {
        MathFunction cot = new CotFuntion(cosMock, sinMock);
        
        Mockito.doReturn(1.0).when(cosMock).calculate(eq(argument), anyDouble());
        Mockito.doReturn(0.0).when(sinMock).calculate(eq(argument), anyDouble());

        double result = cot.calculate(argument, EPS);
        assertTrue(Double.isNaN(result));
    }

    @ParameterizedTest(name = "checking valid values")
    @CsvFileSource(resources = "/cot.csv", numLinesToSkip = 1)
    void validArgumentTest(double argument, double expected, double sinRes, double cosRes) {
        MathFunction cot = new CotFuntion(cosMock, sinMock);
        
        Mockito.doReturn(sinRes).when(sinMock).calculate(eq(argument), anyDouble());
        Mockito.doReturn(cosRes).when(cosMock).calculate(eq(argument), anyDouble());

        assertEquals(expected, cot.calculate(argument, EPS), EPS);
    }

    @ParameterizedTest(name = "Checking invalid input")
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void testInvalidInput(double argument) {
        MathFunction tan = new TanFunction(cosMock, sinMock);
        assertTrue(Double.isNaN(tan.calculate(argument, EPS)));
    }
}