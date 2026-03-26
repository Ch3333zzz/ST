package org.example.module;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;

import org.example.functions.MathFunction;
import org.example.functions.trigonometry.TanFunction;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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

    @ParameterizedTest(name = "Checking division by zero")
    @ValueSource(doubles = {1.570796, -1.570796, 4.712389, -4.712389})
    void testDivisionByZero(double argument) {
        MathFunction tan = new TanFunction(sinMock, cosMock);
        
        Mockito.doReturn(0.0).when(cosMock).calculate(eq(argument), anyDouble());
        Mockito.doReturn(1.0).when(sinMock).calculate(eq(argument), anyDouble());

        double result = tan.calculate(argument, EPS);
        assertTrue(Double.isNaN(result));
    }

    @ParameterizedTest(name = "checking valid values")
    @CsvSource(value = {
        "0, 0, 1, 0",                   // x=0
        "6.283185, 0, 1, 0",            // x=2PI
        "0.785398, 0.7071, 0.7071, 1",   // x=PI/4
        "-0.785398, -0.7071, 0.7071, -1",// x=-PI/4
        "2.356194, 0.7071, -0.7071, -1", // x=3PI/4
        "-2.356194, -0.7071, -0.7071, 1",// x=-3PI/4
        "3.141592, 0, -1, 0",           // x=PI
        "-3.141592, 0, -1, 0"           // x=-PI
    })
    void validArgumentTest(double argument, double sinRes, double cosRes, double expected) {
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