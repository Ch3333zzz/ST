package org.example.module;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


import org.example.functions.MathFunction;
import org.example.functions.trigonometry.SecFunction;

@ExtendWith(MockitoExtension.class)
public class SecFunctionTest {
    @Mock
    private MathFunction cosMock;

    private final double EPS = 0.001;

    @ParameterizedTest(name = "Checking sin is near zero")
    @ValueSource(doubles = {1.570796, -1.570796, 4.712388}) // PI/2, -PI/2, PI/2 + 2PI
    void testNaNOnZeroSin(double argument) {
        MathFunction sec = new SecFunction(cosMock);
        
        Mockito.doReturn(0.0).when(cosMock).calculate(eq(argument), eq(EPS / 2));

        double result = sec.calculate(argument, EPS);
        assertTrue(Double.isNaN(result));
    }

    @ParameterizedTest(name = "Checking invalid input")
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void testInvalidInput(double argument) {
        MathFunction sec = new SecFunction(cosMock);
        assertTrue(Double.isNaN(sec.calculate(argument, EPS)));
    }

    @ParameterizedTest(name = "Checking valid values")
    @CsvFileSource(resources = "/sec.csv", numLinesToSkip = 1)
    void testValidValues(double argument, double expected, double cosRes) {
        MathFunction sec = new SecFunction(cosMock);
        
        Mockito.doReturn(cosRes).when(cosMock).calculate(eq(argument), eq(EPS / 2));

        assertEquals(expected, sec.calculate(argument, EPS), EPS);
    }
}
