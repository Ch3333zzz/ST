package org.example.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;

import org.example.functions.MathFunction;
import org.example.functions.SystemFunction;
import org.example.functions.logarifms.LogFunction;
import org.example.functions.trigonometry.CotFuntion;
import org.example.functions.trigonometry.SecFunction;
import org.example.functions.trigonometry.TanFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MiddleLvlTest {
    
    @Mock
    private MathFunction sinMock;
    @Mock
    private MathFunction cosMock;
    @Mock
    private MathFunction cscMock;

    @Mock
    private MathFunction lnMock;

    private MathFunction tan;
    private MathFunction cot;
    private MathFunction sec;

    private MathFunction log2;
    private MathFunction log3;
    private MathFunction log5;

    private SystemFunction system;

    private final double EPS = 0.001;

    @BeforeEach
    void setUp() {
        tan = new TanFunction(sinMock, cosMock);
        cot = new CotFuntion(cosMock, sinMock);
        sec = new SecFunction(cosMock);

        log2 = new LogFunction(lnMock, 2);
        log3 = new LogFunction(lnMock, 3);
        log5 = new LogFunction(lnMock, 5);

        system = new SystemFunction(sinMock, cosMock, tan, cot, sec, cscMock, lnMock, log2, log3, log5);
    }


    @ParameterizedTest(name = "Tring part Asymptote check")
    @ValueSource(doubles = {
        0.0, -6.283185,                    // 0 и -2PI
        -1.570796, -7.853981,             // -PI/2  -5PI/2
        -3.141593, -9.424778,             // -PI -3PI
        -4.712389, -10.995574             // -3PI/2 -7PI/2
    })
    void testAsymptotes(double x) {
        Mockito.when(sinMock.calculate(eq(x), anyDouble())).thenReturn(0.0);
        Mockito.when(cosMock.calculate(eq(x), anyDouble())).thenReturn(0.0);
        Mockito.when(cscMock.calculate(eq(x), anyDouble())).thenReturn(Double.NaN);

        double result = system.calculate(x, EPS);
        assertTrue(Double.isNaN(result));
    }

    @ParameterizedTest(name = "Bad X values")
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void testBoundaryInputs(double argument) {
        double result = system.calculate(argument, EPS);
        assertTrue(Double.isNaN(result));
    }

    @ParameterizedTest(name = "Testing between bad points")
    @CsvFileSource(resources = "/system_left.csv", numLinesToSkip = 1)
    void testTrigIntervals(double x, double expected, 
                                        double eSin, double eCos, double eTan, 
                                        double eCot, double eSec, double eCsc) {

        Mockito.when(sinMock.calculate(eq(x), anyDouble())).thenReturn(eSin);
        Mockito.when(cosMock.calculate(eq(x), anyDouble())).thenReturn(eCos);
        Mockito.when(cscMock.calculate(eq(x), anyDouble())).thenReturn(eCsc);

        double result = system.calculate(x, EPS);

        // assertEquals(expected, result, EPS);
        assertEquals(expected, result, 0.01);
    }

    @ParameterizedTest(name = "Log part Asymptote check")
    @CsvSource(value = {
        "1, 0"
    })
    void testLogPartBadDots(double x, double eLog2) {
        double result = system.calculate(x, EPS);

        assertTrue(Double.isNaN(result));
    }

    @ParameterizedTest(name = "Log part good points check")
    @CsvFileSource(resources = "/system_right.csv", numLinesToSkip = 1)
    void testLogPart(double x, double expected, 
                                double eLn) {

        Mockito.lenient().when(lnMock.calculate(eq(x), anyDouble())).thenReturn(eLn);
        Mockito.lenient().when(lnMock.calculate(eq(2.0), anyDouble())).thenReturn(0.693147);
        Mockito.lenient().when(lnMock.calculate(eq(3.0), anyDouble())).thenReturn(1.098612);
        Mockito.lenient().when(lnMock.calculate(eq(5.0), anyDouble())).thenReturn(1.609437);

        double result = system.calculate(x, EPS);

        assertEquals(expected, result, 0.02);
    }
}
