package core.io.repr.col;

import core.ga.DomainAdjuster;

/**
 *
 * @author gmatoga
 */
public final class FloatDomain implements Domain, DomainAdjuster {

    private Float min, max;
    int last;
    private final float range;

    public FloatDomain(float min, float max) {
        if (min > max)
            throw new IllegalArgumentException("min > max");
        this.min = min;
        this.max = max;
        this.range = max - min;
        this.last = 1 << (bitSize() - 1) / 2; // hack hackedy hack hack
    }

    public FloatDomain(double lowerNumericBound, double upperNumericBound) {
        this((float) lowerNumericBound, (float) upperNumericBound);
    }

    public boolean contains(Object o) {
        Float f = (Float) o;
        return min <= f && f <= max;
    }

    public int bitSize() {
        return 40 + 1; // TODO FIXME XXX hahaha, problem z głowy
    }

    public Object adjust(Object o) {
        // should come Integer here
        final Integer i = (Integer) o;

        // let's work with it
        final float f = new Float(i);

        // f is in range 0..2^N, where
        // N is no of bits
        confirmRange(f);

        // 0..1
        final float normalized = f / (float) last;

        // min..max
        final Float res = min + normalized * range;


        return res;
    }

    private void confirmRange(Float f) {
        if (f < 0 || f > last)
            throw new IllegalStateException(
                    "Float f =" + f + " not in range 0..." + last);
    }

    @Override
    public String toString() {
        return "<" + min + ", " + max + "> ";
    }
}
