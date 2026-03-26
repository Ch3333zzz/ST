package org.example.functions.trigonometry;

import org.example.functions.AbstractMathFunction;
import org.example.functions.MathFunction;

public class TanFunction extends AbstractMathFunction {

    private final MathFunction sin;
    private final MathFunction cos;

    public TanFunction(MathFunction sin, MathFunction cos) {
        this.sin = sin;
        this.cos = cos;
    }

    @Override
    public double doCalculate(double x, double error) {

        double s = sin.calculate(x, error / 2);
        double c = cos.calculate(x, error / 2);

        if (Math.abs(c) < error) return Double.NaN; 

        return s / c;
    }
}