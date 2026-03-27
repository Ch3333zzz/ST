package org.example;

import org.example.functions.MathFunction;
import org.example.functions.SystemFunction;
import org.example.functions.logarifms.LnFunction;
import org.example.functions.logarifms.LogFunction;
import org.example.functions.trigonometry.CosFunction;
import org.example.functions.trigonometry.CotFuntion;
import org.example.functions.trigonometry.CscFunction;
import org.example.functions.trigonometry.SecFunction;
import org.example.functions.trigonometry.SinFunction;
import org.example.functions.trigonometry.TanFunction;
import org.example.util.CsvWriter;

public class main {
    public static void main(String[] args) {
        CsvWriter writer = new CsvWriter();
        double step = 0.01;
        double eps = 0.001;

        MathFunction sin = new SinFunction();
        MathFunction cos = new CosFunction(sin);
        MathFunction tan = new TanFunction(sin, cos);
        MathFunction cot = new CotFuntion(cos, sin);
        MathFunction sec = new SecFunction(cos);
        MathFunction csc = new CscFunction(sin);

        MathFunction ln = new LnFunction();
        MathFunction log2 = new LogFunction(ln, 2);
        MathFunction log3 = new LogFunction(ln, 3);
        MathFunction log5 = new LogFunction(ln, 5);

        MathFunction system = new SystemFunction(sin, cos, tan, cot, sec, csc, ln, log2, log3, log5);
        
        writer.write("sin.csv", sin, -10, 10, step, eps);
        writer.write("cos.csv", cos, -10, 10, step, eps);
        writer.write("tan.csv", tan, -10, 10, step, eps);
        writer.write("cot.csv", cot, -10, 10, step, eps);
        writer.write("sec.csv", sec, -10, 10, step, eps);
        writer.write("csc.csv", csc, -10, 10, step, eps);

        writer.write("ln.csv", ln, 0.1, 10, step, eps);
        writer.write("log2.csv", log2, 0.1, 10, step, eps);
        writer.write("log3.csv", log3, 0.1, 10, step, eps);
        writer.write("log5.csv", log5, 0.1, 10, step, eps);

        writer.write("system.csv", system, -10, 10, step, eps);
        
        System.out.println("Success!");
    }
}
