package org.example.functions.logarifms;

import org.example.functions.AbstractMathFunction;

public class LnFunction extends AbstractMathFunction {

    private final int MAX_ITER = 20;

    @Override
    public double doCalculate(double x, double error) {

        if (x <= 0.0) return Double.NaN;
        if (x == 1.0) return 0.0;

        double result = 0.0;
        double constant = (x - 1) / (x + 1);
        double currentTerm = constant;
        int n = 0;

        while (Math.abs(2 * currentTerm / (2 * n + 1)) > error / 10) {
            result += 2 * currentTerm / (2 * n + 1);
            
            currentTerm *= (constant * constant);
            n++;
            
            if (n > MAX_ITER) break;
        }

        return result;
    }
}