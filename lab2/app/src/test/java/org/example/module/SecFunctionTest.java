package org.example.module;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
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
    @CsvSource(value = {
        "3.1415926,  -1.0,  -1.0",   // PI: cos=-1 -> sec=-1
        "-3.1415926, -1.0,  -1.0",   // -PI: cos=-1 -> sec=-1
        "0.0,         1.0,   1.0",   // 0: cos=1 -> sec=1
        "6.283185,    1.0,   1.0",   // 2PI: cos=1 -> sec=1
        "1.04719,     0.5,   2.0",   // PI/3: cos=0.5 -> sec=2
        "7.330382,    0.5,   2.0",   // PI/3 + 2PI -> sec=2
        "2.094395,   -0.5,  -2.0",   // 2PI/3: cos=-0.5 -> sec=-2
        "8.377580,   -0.5,  -2.0",   // 2PI/3 + 2PI -> sec=-2
        "4.188790,   -0.5,  -2.0",   // 4PI/3: cos=-0.5 -> sec=-2
        "10.471975,  -0.5,  -2.0",   // 4PI/3 + 2PI -> sec=-2
        "5.235987,    0.5,   2.0",   // 5PI/3: cos=0.5 -> sec=2
        "11.519173,   0.5,   2.0"    // 5PI/3 + 2PI -> sec=2
    })
    void testValidValues(double argument, double cosRes, double expected) {
        MathFunction sec = new SecFunction(cosMock);
        
        Mockito.doReturn(cosRes).when(cosMock).calculate(eq(argument), eq(EPS / 2));

        assertEquals(expected, sec.calculate(argument, EPS), EPS);
    }
}
