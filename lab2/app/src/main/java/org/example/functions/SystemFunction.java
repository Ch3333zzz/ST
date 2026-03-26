package org.example.functions;

public class SystemFunction extends AbstractMathFunction {

    private final MathFunction sin;
    private final MathFunction cos;
    private final MathFunction tan;
    private final MathFunction cot;
    private final MathFunction sec;
    private final MathFunction csc;

    private final MathFunction ln;
    private final MathFunction log2;
    private final MathFunction log3;
    private final MathFunction log5;

    public SystemFunction(MathFunction sin, MathFunction cos, MathFunction tan, 
                          MathFunction cot, MathFunction sec, MathFunction csc, 
                          MathFunction ln, MathFunction log2, MathFunction log3, 
                          MathFunction log5) {
        this.sin = sin;
        this.cos = cos;
        this.tan = tan;
        this.cot = cot;
        this.sec = sec;
        this.csc = csc;
        this.ln = ln;
        this.log2 = log2;
        this.log3 = log3;
        this.log5 = log5;
    }

    @Override
    public double doCalculate(double x, double error) {
        if (x <= 0) {
            return calculateTrigPart(x, error);
        } else {
            return calculateLogPart(x, error);
        }
    }

    private double calculateTrigPart(double x, double error) {
        double sinVal = sin.calculate(x, error);
        double cosVal = cos.calculate(x, error);
        double tanVal = tan.calculate(x, error);
        double cotVal = cot.calculate(x, error);
        double secVal = sec.calculate(x, error);
        double cscVal = csc.calculate(x, error);

        // 1. (((tan + sin) - csc) * (sec * sin))
        double step1 = ((tanVal + sinVal) - cscVal) * (secVal * sinVal);
        // 2. step1 - (sin/sin)
        double step2 = step1 - (sinVal / sinVal);
        // 3. step2 * cot * cos
        double step3 = step2 * cotVal * cosVal;
        // 4. (step3 / csc)^3  
        double step4 = Math.pow(step3 / cscVal, 3);
        // 5. step 4 + sin
        double step5 = step4 + sinVal;
        
        // 6. (csc - (sec + cot) + cos)^2
        double step6 = Math.pow(cscVal - (secVal + cotVal + cosVal), 2);
        double leftPart = (step5 - step6) / cscVal;

        // (sin / ((cot / csc) - cot)) * tan
        double rightInner = sinVal / ((cotVal / cscVal) - cotVal);

        // (rightInner * tan)^3
        double rightPart = Math.pow(rightInner * tanVal, 3);

        return leftPart - rightPart;
    }

    private double calculateLogPart(double x, double error) {
        double lnVal = ln.calculate(x, error);
        double log2Val = log2.calculate(x, error);
        double log3Val = log3.calculate(x, error);
        double log5Val = log5.calculate(x, error);

        // ((ln * log3) / log2)
        double step1 = (lnVal * log3Val) / log2Val;
        // step1 - log2
        double step2 = step1 - log2Val;
        //step2 + log3
        double step3 = step2 + log3Val;

        // (log5^2)^3 = log5^6
        double step4 = Math.pow(Math.pow(log5Val, 2), 3);

        return step3 - step4;
    }
}