package org.example.functions.trigonometry;

import org.example.functions.AbstractMathFunction;
import org.example.functions.MathFunction;

public class CotFuntion extends AbstractMathFunction {

    private MathFunction cos;
    private MathFunction sin;

    public CotFuntion(MathFunction cos, MathFunction sin) {
        this.cos = cos;
        this.sin = sin;
    }

    @Override
    public double doCalculate(double x, double error) {

        double c = cos.calculate(x, error / 2);
        double s = sin.calculate(x, error / 2);

        if (Math.abs(s) < error) return Double.NaN;

        return c / s;
    }
}