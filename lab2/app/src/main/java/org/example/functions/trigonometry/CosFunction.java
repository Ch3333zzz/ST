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

        double absResult = Math.pow(1 - Math.pow(sin.calculate(x, error), 2), 0.5);

        return (Math.abs(x) % (2 * Math.PI)) > Math.PI / 2 && 
        (Math.abs(x) % (2 * Math.PI)) < Math.PI * 3 / 2 ? -absResult : absResult;
        
        //return sin.calculate(x + Math.PI/2, error);
    }
}