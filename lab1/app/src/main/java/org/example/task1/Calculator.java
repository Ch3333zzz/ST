package org.example.task1;

public class Calculator {

    private final double ERROR = 1e-10;
    private final int MAX_ITER = 100000;

    public Double asin(double x) {
        if (x > 1 || x < -1)
            return Double.NaN;

        if (x == 1)
            return Math.PI/2;
        if (x == -1)
            return -Math.PI/2;

        double sum = x;
        double term = x; // current term

        for (int i = 1; i < MAX_ITER; i++) {
            double multiplier = (x * x * (2 * i - 1) * (2 * i - 1)) / 
                                (2.0 * i * (2 * i + 1));
            
            term *= multiplier;
            sum += term;

            if (Math.abs(term) < ERROR)
                break;
        }
        return sum;
    }
}
