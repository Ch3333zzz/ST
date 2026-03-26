package org.example.functions.logarifms;

import org.example.functions.AbstractMathFunction;
import org.example.functions.MathFunction;

public class LogFunction extends AbstractMathFunction {
    private final MathFunction ln;
    private final double base;

    public LogFunction(MathFunction ln, double base) {
        if (Double.isNaN(base) || Double.isInfinite(base))
            throw new IllegalArgumentException("Base must be a finite number");
        if (base == 1) throw new IllegalArgumentException("Base can't be 1");
        if (base <= 0) throw new IllegalArgumentException("Base must be positive");

        this.ln = ln;
        this.base = base;
    }

    @Override
    public double doCalculate(double x, double error) {

        if (x <= 0) throw new IllegalArgumentException("Argument must be positive");

        double numerator = ln.calculate(x, error / 2);
        double denominator = ln.calculate(base, error / 2);

        // if (Math.abs(denominator) < error) return Double.NaN;

        return numerator / denominator;
    }
}
