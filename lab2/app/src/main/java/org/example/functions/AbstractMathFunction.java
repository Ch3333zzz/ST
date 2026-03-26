package org.example.functions;

public abstract class AbstractMathFunction implements MathFunction {

    @Override
    public double calculate(double x, double error) {

        if (error <= 0 || error > 0.1 || Double.isNaN(error)) {
            throw new IllegalArgumentException("Error must be in range (0; 0.1]");
        }

        if (Double.isNaN(x) || Double.isInfinite(x)) {
            return Double.NaN;
        }
        
        return doCalculate(x, error);
    }

    protected abstract double doCalculate(double x, double error);
}