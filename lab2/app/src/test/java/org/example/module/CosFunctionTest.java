package org.example.module;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;

import org.example.functions.MathFunction;
import org.example.functions.trigonometry.CosFunction;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CosFunctionTest {
    
    @Mock
    private MathFunction sinMock;

    private final double EPS = 0.001;

    @ParameterizedTest(name = "Checking invalid argument")
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void testNaNReturn(double argument) {
        MathFunction cos = new CosFunction(sinMock);
        assertTrue(Double.isNaN(cos.calculate(argument, EPS)));
    }
    
    @ParameterizedTest(name = "checking values within the range [-pi; pi]")
    @CsvSource(value = {
        "0, 1, 1",                // x=0, sin(0 + PI/2) = 1, cos=1
        "6.283185, 1, 1",         // x=2PI
        "3.1415926, -1, -1",      // x=PI, sin(PI + PI/2) = -1, cos=-1
        "-3.1415926, -1, -1",     // x=-PI, sin(-PI + PI/2) = -1, cos=-1
        "1.570796, 0, 0",         // x=PI/2, sin(PI/2 + PI/2) = 0, cos=0
        "-1.570796, 0, 0"         // x=-PI/2, sin(-PI/2 + PI/2) = 0, cos=0
    })
    void validArgumentTest(double argument, double sinResult, double expected) {
        MathFunction cos = new CosFunction(sinMock);
        
        Mockito.doReturn(sinResult).when(sinMock).calculate(eq(argument + Math.PI / 2), anyDouble());

        assertEquals(expected, cos.calculate(argument, EPS), EPS);
    }

    @ParameterizedTest(name = "checking normalizing argument")
    @CsvSource(value = {
        "-15.707963, -1, -1", // 5 * (-PI)
        "-7.853981, 0, 0", // 5 * * (-PI/2)
        "7.853981, 0, 0", // 5 * PI/2
        "15.707963, -1, -1", // 5 * PI
        "4.0, -0.6536, -0.6536",
        "4.0, -0.6536, -0.6536"
    })
    void testArgumentNormalization(double argument, double sinResult, double expected) {
        MathFunction cos = new CosFunction(sinMock);
        
        Mockito.doReturn(sinResult).when(sinMock).calculate(eq(argument + Math.PI / 2), eq(EPS));

        assertEquals(expected, cos.calculate(argument, EPS), EPS);
    }
}