package org.example.functions.trigonometry;

import org.example.functions.AbstractMathFunction;
import org.example.functions.MathFunction;

public class CosFunction extends AbstractMathFunction {

    private final MathFunction sin;

    public CosFunction(MathFunction sin) {
        this.sin = sin;
    }

    @Override
    public double doCalculate(double x, double error) {

        return sin.calculate(x + Math.PI/2, error);
    }
}