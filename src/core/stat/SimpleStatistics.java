/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.stat;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * http://stackoverflow.com/questions/2633095/simple-java-library-for-storing-statistical-observations-and-calculating-statisti/2633692#2633692
 */
public class SimpleStatistics {

    private double sum;
    private double squares;
    private long count;
    private double max;
    private double min;

    public SimpleStatistics() {
        reset();
    }

    public void addValue(double x) {
        sum += x;
        squares += x * x;
        min = ((x < min) ? x : min);
        max = ((x > max) ? x : max);
        ++count;

        // If the sum of squares exceeds Double.MAX_VALUE, this means the
        // value has overflowed; reset the state back to zero and start again.
        // All previous calculations are lost.  (Better as all doubles?)
        if (squares < 0L) {
            reset();
        }
    }

    public final void reset() {
        sum = 0;
        squares = 0;
        count = 0;
        max = Double.MIN_VALUE;
        min = Double.MAX_VALUE;
    }

    public double getMean() {
        double mean = 0.0;

        if (count > 0L) {
            mean = (double) sum / count;
        }

        return mean;
    }

    public double getStdDev() {
        double stdDev = 0.0;

        if (count > 1L) {
            stdDev = Math.sqrt((squares - sum * sum / (double) count) / ((double) count - 1));
        }

        return stdDev;
    }

    public double getMax() {
        return max;
    }

    public double getMin() {
        return min;
    }

    @Override
    public String toString() {
        return String.format("min=%5.1f%%,  mean=%5.1f%%,  max=%5.1f%%,  dev=%5.1f%%",
                min * 100, getMean() * 100, max * 100, getStdDev() * 100);
    }
}
