package org.example.functions.trigonometry;

import org.example.functions.AbstractMathFunction;

public class SinFunction extends AbstractMathFunction {

    private static final int MAX_ITER = 10;

    @Override
    public double doCalculate(double x, double error) {
        
        double nX = x % (2 * Math.PI);
        if (nX > Math.PI) nX -= 2 * Math.PI;
        if (nX < -Math.PI) nX += 2 * Math.PI;

        double result = 0.0;
        double term = nX;
        int n = 1;

        while (Math.abs(term) > error) {
            result += term;
            term = term * (-1.0 * nX * nX / (2 * n * (2 * n + 1)));
            n++;
            
            if (n > MAX_ITER) break; 
        }

        return result;
    }
}