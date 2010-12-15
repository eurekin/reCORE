
package core.examples;

import core.binomial.BinomialDistr;
import core.binomial.InvCDFRandGenerator;
import java.util.Arrays;
import java.util.Random;
import java.util.TreeMap;

/**
 *
 * @author Rekin
 */
public class InvCDFRandGeneratorExample {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        TreeMap<Double, Integer> dbinomN10P01 = new TreeMap<Double, Integer>();
        // generated in R with: dbinom(seq(0,9), 10, 0.1)
        double[] vals = {0.3486784401, 0.3874204890, 0.1937102445, 0.0573956280, 0.0111602610,
            0.0014880348, 0.0001377810, 0.0000087480, 0.0000003645, 0.0000000090};
        BinomialDistr bd = new BinomialDistr(10, 0.1);
        // now cumulative test
        double d = 0;
        for (int i = 0; i < vals.length; i++) {
            d += vals[i];
            System.out.printf("mycumm %.3f ", d);
            System.out.printf("comcumm %.3f ", bd.cumulativeProbability(i));
            System.out.printf("error " + (bd.cumulativeProbability(i) - d));
            System.out.println("");
        }

        double accum = 0;
        for (int i = 0; i < vals.length; i++) {
            accum += vals[i];
            dbinomN10P01.put(accum, i);
        }
        Random r = new Random(131313);
        double nextDouble = 0;
        for (int i = 0; i < 10; i++) {
            nextDouble = r.nextDouble();
            System.out.printf("%d", dbinomN10P01.ceilingEntry(nextDouble).getValue());
            System.out.printf(" %.3f", nextDouble);
            System.out.println();
        }
        Integer hist[] = new Integer[10];
        for (int i = 0; i < hist.length; i++) {
            hist[i] = 0;
        }
        final int max = 10000000;
        InvCDFRandGenerator gen = new InvCDFRandGenerator(10, 0.1, r);
        for (int i = 0; i < max; i++) {
            // nextDouble = r.nextDouble();
            // hist[dbinomN10P01.ceilingEntry(nextDouble).getValue()]++;
            hist[gen.nextBinomial()]++;
        }
        double normHist[] = new double[10];
        for (int i = 0; i < 10; i++) {
            normHist[i] = ((double) hist[i]) / max;
        }
        System.out.println("Histogram:  " + Arrays.toString(hist));
        System.out.println("Normalized: " + Arrays.toString(normHist));

        System.out.println("ALT TEST");
        altTest();

    }

    private static void altTest() {
        TreeMap<Double, Integer> m = new TreeMap<Double, Integer>();
        m.put(0.1, 1);
        m.put(0.3, 3);
        m.put(0.7, 9);
        m.put(1d, 10);
        System.out.println("Ceiling");
        for (double i = 0; i <= 1.0; i += 0.05d) {
            System.out.println("i:=" + String.format("%.2f", i) + " " + m.ceilingEntry(i).getValue());
        }
        System.out.println("Floor");
        for (double i = 0; i <= 1.0; i += 0.05d) {
            System.out.println("i:=" + String.format("%.2f", i) + " " + m.floorEntry(i));
        }
    }
}
