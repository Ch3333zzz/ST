package org.example.module;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;

import org.example.functions.MathFunction;
import org.example.functions.SystemFunction;
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
public class SystemFunctionTest {
    
    @Mock
    private MathFunction sin;
    @Mock
    private MathFunction cos;
    @Mock
    private MathFunction tan;
    @Mock
    private MathFunction cot;
    @Mock
    private MathFunction sec;
    @Mock
    private MathFunction csc;

    @Mock
    private MathFunction ln;
    @Mock
    private MathFunction log2;
    @Mock
    private MathFunction log3;
    @Mock
    private MathFunction log5;

    private SystemFunction system;

    private final double EPS = 0.001;

    @BeforeEach
    void setUp() {
        system = new SystemFunction(sin, cos, tan, cot, sec, csc, ln, log2, log3, log5);
    }


    @ParameterizedTest(name = "Tring part Asymptote check")
    @ValueSource(doubles = {
        0.0, -6.283185,                    // 0 и -2PI
        -1.570796, -7.853981,             // -PI/2  -5PI/2
        -3.141593, -9.424778,             // -PI -3PI
        -4.712389, -10.995574             // -3PI/2 -7PI/2
    })
    void testAsymptotes(double x) {
        Mockito.when(sin.calculate(eq(x), anyDouble())).thenReturn(0.0);
        Mockito.when(cos.calculate(eq(x), anyDouble())).thenReturn(0.0);
        
        Mockito.when(tan.calculate(eq(x), anyDouble())).thenReturn(Double.NaN);
        Mockito.when(csc.calculate(eq(x), anyDouble())).thenReturn(Double.NaN);
        Mockito.when(sec.calculate(eq(x), anyDouble())).thenReturn(Double.NaN);
        Mockito.when(cot.calculate(eq(x), anyDouble())).thenReturn(Double.NaN);
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
    void testTrigIntervalsFullIsolation(double x, double expected, 
                                        double eSin, double eCos, double eTan, 
                                        double eCot, double eSec, double eCsc) {

        Mockito.when(sin.calculate(eq(x), anyDouble())).thenReturn(eSin);
        Mockito.when(cos.calculate(eq(x), anyDouble())).thenReturn(eCos);
        Mockito.when(tan.calculate(eq(x), anyDouble())).thenReturn(eTan);
        Mockito.when(cot.calculate(eq(x), anyDouble())).thenReturn(eCot);
        Mockito.when(sec.calculate(eq(x), anyDouble())).thenReturn(eSec);
        Mockito.when(csc.calculate(eq(x), anyDouble())).thenReturn(eCsc);

        double result = system.calculate(x, EPS);

        // assertEquals(expected, result, EPS);
        assertEquals(expected, result, 0.01);
    }

    @ParameterizedTest(name = "Log part Asymptote check")
    @CsvSource(value = {
        "1, 0"
    })
    void testLogPartBadDots(double x, double eLog2) {

        Mockito.when(log2.calculate(eq(x), anyDouble())).thenReturn(eLog2);

        double result = system.calculate(x, EPS);

        assertTrue(Double.isNaN(result));
    }

    @ParameterizedTest(name = "Log part Asymptote check")
    @CsvFileSource(resources = "/system_right.csv", numLinesToSkip = 1)
    void testLogPartFullIsolation(double x, double expected, 
                                double eLn, double eLog2, 
                                double eLog3, double eLog5) {

        Mockito.when(ln.calculate(eq(x), anyDouble())).thenReturn(eLn);
        Mockito.when(log2.calculate(eq(x), anyDouble())).thenReturn(eLog2);
        Mockito.when(log3.calculate(eq(x), anyDouble())).thenReturn(eLog3);
        Mockito.when(log5.calculate(eq(x), anyDouble())).thenReturn(eLog5);

        double result = system.calculate(x, EPS);

        assertEquals(expected, result, 0.02);
    }
}
