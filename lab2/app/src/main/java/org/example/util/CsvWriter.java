package org.example.util;

import org.example.functions.MathFunction;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Locale;

public class CsvWriter {

    private char delimiter = ',';

    public void setDelimiter(char delimiter) {
        this.delimiter = delimiter;
    }

    public void write(String filename, MathFunction function, double from, double to, double step, double eps) {
        File file = new File(filename);
        try (PrintWriter pw = new PrintWriter(file)) {
            for (double x = from; x <= to; x += step) {
                double res = function.calculate(x, eps);
                pw.printf(Locale.US, "%f%c%f\n", x, delimiter, res);
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}