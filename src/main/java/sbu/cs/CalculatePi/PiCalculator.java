package sbu.cs.CalculatePi;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PiCalculator {

    /**
     * Calculate pi and represent it as a BigDecimal object with the given floating point number (digits after . )
     * There are several algorithms designed for calculating pi, it's up to you to decide which one to implement.
     * Experiment with different algorithms to find accurate results.

     * You must design a multithreaded program to calculate pi. Creating a thread pool is recommended.
     * Create as many classes and threads as you need.
     * Your code must pass all of the test cases provided in the test folder.

     * @param floatingPoint the exact number of digits after the floating point
     * @return pi in string format (the string representation of the BigDecimal object)
     */

    public static BigDecimal pi = new BigDecimal(0);

    public static class TermCalculator implements Runnable {
        int floatingPoint;
        MathContext mc = new MathContext(floatingPoint + 1100 );

        int n; // number of the term

        public TermCalculator(int n, int floatingPoint) {
            this.n = n;
            this.floatingPoint = floatingPoint;
        }

        public void run() {
            BigDecimal a1 = new BigDecimal(1).divide(new BigDecimal(16).pow(n), mc);
            BigDecimal a2 = new BigDecimal(4).divide(new BigDecimal(8 * n + 1), mc);
            BigDecimal a3 = new BigDecimal(2).divide(new BigDecimal(8 * n + 4), mc);
            BigDecimal a4 = new BigDecimal(1).divide(new BigDecimal(8 * n + 5), mc);
            BigDecimal a5 = new BigDecimal(1).divide(new BigDecimal(8 * n + 6), mc);
            BigDecimal term = a1.multiply(a2.subtract(a3).subtract(a4).subtract(a5), mc);
            addTerm(term, mc);
        }
    }

    public static synchronized void addTerm(BigDecimal term, MathContext mc) {
        pi = pi.add(term, mc);
    }

    public static String calculate(int floatingPoint) {
        pi = new BigDecimal(0);
        ExecutorService threadPool = Executors.newFixedThreadPool(8);

        final int iterations = 10000;
        for (int i = 0; i <= iterations; i++) {
            TermCalculator tc = new TermCalculator(i, floatingPoint);
            threadPool.execute(tc);
        }

        threadPool.shutdown();

        try {
            threadPool.awaitTermination(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        pi = pi.setScale(floatingPoint+1, RoundingMode.HALF_UP);
        String strpi=pi.toPlainString();
        String pi=strpi.substring(0,strpi.length()-1);
        return pi;

    }

    public static void main(String[] args) {
        // Test the PI number calculation with 100 floating point digits
        String piString = calculate(100);
        System.out.print("PI:");
        System.out.println(piString);
    }
}


