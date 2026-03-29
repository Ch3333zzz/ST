package org.example.module;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;

import org.example.functions.MathFunction;
import org.example.functions.trigonometry.CosFunction;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
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
    @CsvFileSource(resources = "/cos.csv", numLinesToSkip = 1)
    void validArgumentTest(double argument, double expected, double sin) {
        MathFunction cos = new CosFunction(sinMock);
        
        Mockito.doReturn(sin).when(sinMock).calculate(eq(argument), anyDouble());

        assertEquals(expected, cos.calculate(argument, EPS), EPS);
    }
}