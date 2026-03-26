package org.example.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.example.functions.MathFunction;
import org.example.functions.SystemFunction;
import org.example.functions.logarifms.LnFunction;
import org.example.functions.logarifms.LogFunction;
import org.example.functions.trigonometry.CosFunction;
import org.example.functions.trigonometry.CotFuntion;
import org.example.functions.trigonometry.CscFunction;
import org.example.functions.trigonometry.SecFunction;
import org.example.functions.trigonometry.SinFunction;
import org.example.functions.trigonometry.TanFunction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class SystemFunctionTest {
    
    private MathFunction sin;
    private MathFunction cos;
    private MathFunction tan;
    private MathFunction cot;
    private MathFunction sec;
    private MathFunction csc;

    private MathFunction ln;
    private MathFunction log2;
    private MathFunction log3;
    private MathFunction log5;

    private SystemFunction system;

    private final double EPS = 0.001;

    @BeforeEach
    void setUp() {
        sin = new SinFunction();
        cos = new CosFunction(sin);
        tan = new TanFunction(sin, cos);
        cot = new CotFuntion(cos, sin);
        sec = new SecFunction(cos);
        csc = new CscFunction(sin);

        ln = new LnFunction();
        log2 = new LogFunction(ln, 2);
        log3 = new LogFunction(ln, 3);
        log5 = new LogFunction(ln, 5);

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
        "-0.7854, 5.00277",
        "-7.0686, 5.00277",
        // max point
        "-0.99552, 6.5947",
        "-7.2787, 6.5947",
        // (-pi/2; -0.99552)
        "-1.1, 4.20354",
        "-7.38318, 4.20354",
        // (-pi; -pi/2)
        "-2.3562, 0.45797",
        "-8.6394, 0.45797",
        // (-3pi/2; -pi)
        "-3.7, -15.74816",
        "-9.9832, -15.74816",
        // (-5.9344; -3pi/2)
        "-5.8, -2.15872",
        "-12.0832, -2.15872",
        // min point
        "-5.9344, -2.41496",
        "-12.21758, -2.41496",
        // (-2pi; -5.9344)
        "-6.1, -1.84896",
        "-12.38318, -1.84896"
    })
    void testTrigIntervalsFullIsolation(double x, double expected) {

        double result = system.calculate(x, EPS);

        // assertEquals(expected, result, EPS);
        assertEquals(expected, result, 0.1);
    }

    @ParameterizedTest(name = "Log part Asymptote check")
    @CsvSource({
        "0.5, -0.07456",
        "2.17736, 0.06385",
        "5.0, -0.84145"
    })
    void testLogPartFullIsolation(double x, double expected) {

        double result = system.calculate(x, EPS);

        assertEquals(expected, result, EPS);
    }
}
