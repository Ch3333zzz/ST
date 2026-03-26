package org.example.functions.trigonometry;

import org.example.functions.AbstractMathFunction;
import org.example.functions.MathFunction;

public class SecFunction extends AbstractMathFunction {
    
    private final MathFunction cos;

    public SecFunction(MathFunction cos) {
        this.cos = cos;
    }

    @Override
    public double doCalculate(double x, double error) {

        double denominator = cos.calculate(x, error/2);
        
        if (Math.abs(denominator) < error) return Double.NaN;

        return 1/denominator;
    }
}
