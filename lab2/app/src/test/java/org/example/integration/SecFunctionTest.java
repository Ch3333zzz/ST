package org.example.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;


import org.example.functions.MathFunction;
import org.example.functions.trigonometry.CosFunction;
import org.example.functions.trigonometry.SecFunction;
import org.example.functions.trigonometry.SinFunction;

public class SecFunctionTest {

    private MathFunction cosReal;

    private final double EPS = 0.01;

    @BeforeEach
    void setUp() {
        cosReal = new CosFunction(new SinFunction());
    }
    @ParameterizedTest(name = "Checking sin is near zero")
    @ValueSource(doubles = {1.570796, -1.570796, 4.712388}) // PI/2, -PI/2, PI/2 + 2PI
    void testNaNOnZeroSin(double argument) {
        MathFunction sec = new SecFunction(cosReal);
        
        double result = sec.calculate(argument, EPS);
        assertTrue(Double.isNaN(result));
    }

    @ParameterizedTest(name = "Checking invalid input")
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void testInvalidInput(double argument) {
        MathFunction sec = new SecFunction(cosReal);
        assertTrue(Double.isNaN(sec.calculate(argument, EPS)));
    }

    @ParameterizedTest(name = "Checking valid values")
    @CsvSource(value = {
        "3.1415926,  -1.0",   // PI: cos=-1 -> sec=-1
        "-3.1415926, -1.0",   // -PI: cos=-1 -> sec=-1
        "0.0,         1.0",   // 0: cos=1 -> sec=1
        "6.283185,    1.0",   // 2PI: cos=1 -> sec=1
        "1.04719,     2.0",   // PI/3: cos=0.5 -> sec=2
        "7.330382,    2.0",   // PI/3 + 2PI -> sec=2
        "2.094395,   -2.0",   // 2PI/3: cos=-0.5 -> sec=-2
        "8.377580,   -2.0",   // 2PI/3 + 2PI -> sec=-2
        "4.188790,   -2.0",   // 4PI/3: cos=-0.5 -> sec=-2
        "10.471975,  -2.0",   // 4PI/3 + 2PI -> sec=-2
        "5.235987,    2.0",   // 5PI/3: cos=0.5 -> sec=2
        "11.519173,   2.0"    // 5PI/3 + 2PI -> sec=2
    })
    void testValidValues(double argument, double expected) {
        MathFunction sec = new SecFunction(cosReal);
        
        assertEquals(expected, sec.calculate(argument, EPS), EPS);
    }
}
