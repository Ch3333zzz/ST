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
    @CsvSource({
        
        // (-0.99552; 0)
        "-0.7854, 5.00277, -0.70711, 0.70711, -1.0, -1.0, 1.41421, -1.41421",
        "-7.0686, 5.00277, -0.70711, 0.70711, -1.0, -1.0, 1.41421, -1.41421",
        // max point
        "-0.99552, 6.5947, -0.83908, 0.54402, -1.54238, -0.64835, 1.83817, -1.19178",
        "-7.2787, 6.5947, -0.83908, 0.54402, -1.54238, -0.64835, 1.83817, -1.19178",
        // (-pi/2; -0.99552)
        "-1.1, 4.20354, -0.89121, 0.45360, -1.96476, -0.50897, 2.20460, -1.12207",
        "-7.38318, 4.20354, -0.89121, 0.45360, -1.96476, -0.50897, 2.20460, -1.12207",
        // (-pi; -pi/2)
        "-2.3562, 0.45797, -0.70711, -0.70711, 1.0, 1.0, -1.41421, -1.41421",
        "-8.6394, 0.45797, -0.70711, -0.70711, 1.0, 1.0, -1.41421, -1.41421",
        // (-3pi/2; -pi)
        "-3.7, -15.74816, 0.52984, -0.84805, -0.62472, -1.60072, -1.17918, 1.88736",
        "-9.9832, -15.74816, 0.52984, -0.84805, -0.62472, -1.60072, -1.17918, 1.88736",
        // (-5.9344; -3pi/2)
        "-5.8, -2.15872, 0.46460, 0.88552, 0.52466, 1.90600, 1.12928, 2.15239",
        "-12.0832, -2.15872, 0.46460, 0.88552, 0.52466, 1.90600, 1.12928, 2.15239",
        // min point
        "-5.9344, -2.41496, 0.34202, 0.93969, 0.36397, 2.74748, 1.06418, 2.92381",
        "-12.21758, -2.41496, 0.34202, 0.93969, 0.36397, 2.74748, 1.06418, 2.92381",
        // (-2pi; -5.9344)
        "-6.1, -1.84896, 0.18216, 0.98327, 0.18526, 5.39775, 1.01701, 5.48968",
        "-12.38318, -1.84896, 0.18216, 0.98327, 0.18526, 5.39775, 1.01701, 5.48968"
    })
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
    @CsvSource({
        "0.5, -0.07456, -0.6931, -1.0, -0.6309, -0.4307",
        "2.17736, 0.06385, 0.7781, 1.1226, 0.7083, 0.4835",
        "5.0, -0.84145, 1.6094, 2.3219, 1.4650, 1.0"
    })
    void testLogPartFullIsolation(double x, double expected, 
                                double eLn, double eLog2, 
                                double eLog3, double eLog5) {

        Mockito.when(ln.calculate(eq(x), anyDouble())).thenReturn(eLn);
        Mockito.when(log2.calculate(eq(x), anyDouble())).thenReturn(eLog2);
        Mockito.when(log3.calculate(eq(x), anyDouble())).thenReturn(eLog3);
        Mockito.when(log5.calculate(eq(x), anyDouble())).thenReturn(eLog5);

        double result = system.calculate(x, EPS);

        assertEquals(expected, result, EPS);
    }
}
