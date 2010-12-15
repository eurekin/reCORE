/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.binomial;

import java.util.Random;
import java.util.TreeMap;

/**
 *
 * @author gmatoga
 */
public class InvCDFRandGenerator {

    private final int n;
    private final double p;
    private final Random r;
    TreeMap<Double, Integer> dbinom = new TreeMap<Double, Integer>();

    public InvCDFRandGenerator(int n, double p, Random r) {
        this.n = n;
        this.p = p;
        this.r = r;
        BinomialDistr bd = new BinomialDistr(n, p);
        for (int i = 0; i <= n; i++) {
            dbinom.put(bd.cumulativeProbability(i), i);
        }
    }

    public double getP() {
        return p;
    }

    public int nextBinomial() {
        if (p == 0) return 0;
        final double nextDouble = r.nextDouble();
        try {
            return dbinom.ceilingEntry(nextDouble).getValue();
        } catch (NullPointerException exc) {
            System.out.println("Double: " + nextDouble);
            System.out.println("n: " + n);
            System.out.println("p: " + p);
            System.out.println("r: " + r);
            System.out.println("dbinom: " + dbinom);
            throw exc;
        }
    }

}
