package org.example.integration;

import org.example.functions.MathFunction;
import org.example.functions.trigonometry.CosFunction;
import org.example.functions.trigonometry.SinFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CosFunctionTest {

    private final double EPS = 0.01;

    private MathFunction realSin;

    @BeforeEach
    void setUp() {
        realSin = new SinFunction();
    }

    @ParameterizedTest(name = "Checking invalid argument")
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void testNaNReturn(double argument) {
        MathFunction cos = new CosFunction(realSin);

        assertTrue(Double.isNaN(cos.calculate(argument, EPS)));
    }

    @ParameterizedTest(name = "Integration check correct values for cos")
    @CsvSource(value = {
        "0, 1",                // x=0
        "6.283185, 1",         // x=2PI
        "3.1415926, -1",      // x=PI
        "-3.1415926, -1",     // x=-PI
        "1.570796, 0",         // x=PI/2
        "-1.570796, 0"         // x=-PI/2
    })
    void testCosSinIntegration(double x, double expected) {
        MathFunction cos = new CosFunction(realSin);

        double result = cos.calculate(x, EPS);

        assertEquals(expected, result, 0.01);
    }

    @ParameterizedTest(name = "checking normalizing argument")
    @CsvSource(value = {
        "-15.707963, -1", // 5 * (-PI)
        "-7.853981, 0", // 5 * * (-PI/2)
        "7.853981, 0", // 5 * PI/2
        "15.707963, -1", // 5 * PI
        "4.0, -0.6536",
        "4.0, -0.6536"
    })
    void testArgumentNormalization(double argument, double expected) {
        MathFunction cos = new CosFunction(realSin);
        
        assertEquals(expected, cos.calculate(argument, EPS), EPS);
    }
}