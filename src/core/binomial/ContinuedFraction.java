package core.binomial;


/**
 * Provides a generic means to evaluate continued fractions.  Subclasses simply
 * provided the a and b coefficients to evaluate the continued fraction.
 *
 * <p>
 * References:
 * <ul>
 * <li><a href="http://mathworld.wolfram.com/ContinuedFraction.html">
 * Continued Fraction</a></li>
 * </ul>
 * </p>
 *
 * @version $Revision: 920558 $ $Date: 2010-03-08 17:57:32 -0500 (Mon, 08 Mar 2010) $
 */
public abstract class ContinuedFraction {

    /** Maximum allowed numerical error. */
    private static final double DEFAULT_EPSILON = 10e-9;

    /**
     * Default constructor.
     */
    protected ContinuedFraction() {
        super();
    }

    /**
     * Access the n-th a coefficient of the continued fraction.  Since a can be
     * a function of the evaluation point, x, that is passed in as well.
     * @param n the coefficient index to retrieve.
     * @param x the evaluation point.
     * @return the n-th a coefficient.
     */
    protected abstract double getA(int n, double x);

    /**
     * Access the n-th b coefficient of the continued fraction.  Since b can be
     * a function of the evaluation point, x, that is passed in as well.
     * @param n the coefficient index to retrieve.
     * @param x the evaluation point.
     * @return the n-th b coefficient.
     */
    protected abstract double getB(int n, double x);

    /**
     * Evaluates the continued fraction at the value x.
     * @param x the evaluation point.
     * @return the value of the continued fraction evaluated at x.
     * @throws RuntimeException if the algorithm fails to converge.
     */
    public double evaluate(double x) throws RuntimeException {
        return evaluate(x, DEFAULT_EPSILON, Integer.MAX_VALUE);
    }

    /**
     * Evaluates the continued fraction at the value x.
     * @param x the evaluation point.
     * @param epsilon maximum error allowed.
     * @return the value of the continued fraction evaluated at x.
     * @throws RuntimeException if the algorithm fails to converge.
     */
    public double evaluate(double x, double epsilon) throws RuntimeException {
        return evaluate(x, epsilon, Integer.MAX_VALUE);
    }

    /**
     * Evaluates the continued fraction at the value x.
     * @param x the evaluation point.
     * @param maxIterations maximum number of convergents
     * @return the value of the continued fraction evaluated at x.
     * @throws RuntimeException if the algorithm fails to converge.
     */
    public double evaluate(double x, int maxIterations) throws RuntimeException {
        return evaluate(x, DEFAULT_EPSILON, maxIterations);
    }

    /**
     * <p>
     * Evaluates the continued fraction at the value x.
     * </p>
     *
     * <p>
     * The implementation of this method is based on equations 14-17 of:
     * <ul>
     * <li>
     *   Eric W. Weisstein. "Continued Fraction." From MathWorld--A Wolfram Web
     *   Resource. <a target="_blank"
     *   href="http://mathworld.wolfram.com/ContinuedFraction.html">
     *   http://mathworld.wolfram.com/ContinuedFraction.html</a>
     * </li>
     * </ul>
     * The recurrence relationship defined in those equations can result in
     * very large intermediate results which can result in numerical overflow.
     * As a means to combat these overflow conditions, the intermediate results
     * are scaled whenever they threaten to become numerically unstable.</p>
     *
     * @param x the evaluation point.
     * @param epsilon maximum error allowed.
     * @param maxIterations maximum number of convergents
     * @return the value of the continued fraction evaluated at x.
     * @throws RuntimeException if the algorithm fails to converge.
     */
    public double evaluate(double x, double epsilon, int maxIterations)
            throws RuntimeException {
        double p0 = 1.0;
        double p1 = getA(0, x);
        double q0 = 0.0;
        double q1 = 1.0;
        double c = p1 / q1;
        int n = 0;
        double relativeError = Double.MAX_VALUE;
        while (n < maxIterations && relativeError > epsilon) {
            ++n;
            double a = getA(n, x);
            double b = getB(n, x);
            double p2 = a * p1 + b * p0;
            double q2 = a * q1 + b * q0;
            boolean infinite = false;
            if (Double.isInfinite(p2) || Double.isInfinite(q2)) {
                /*
                 * Need to scale. Try successive powers of the larger of a or b
                 * up to 5th power. Throw ConvergenceException if one or both
                 * of p2, q2 still overflow.
                 */
                double scaleFactor = 1d;
                double lastScaleFactor = 1d;
                final int maxPower = 5;
                final double scale = Math.max(a, b);
                if (scale <= 0) {  // Can't scale
                    throw new RuntimeException(
                            "Continued fraction convergents diverged to +/- infinity for value " + x);
                }
                infinite = true;
                for (int i = 0; i < maxPower; i++) {
                    lastScaleFactor = scaleFactor;
                    scaleFactor *= scale;
                    if (a != 0.0 && a > b) {
                        p2 = p1 / lastScaleFactor + (b / scaleFactor * p0);
                        q2 = q1 / lastScaleFactor + (b / scaleFactor * q0);
                    } else if (b != 0) {
                        p2 = (a / scaleFactor * p1) + p0 / lastScaleFactor;
                        q2 = (a / scaleFactor * q1) + q0 / lastScaleFactor;
                    }
                    infinite = Double.isInfinite(p2) || Double.isInfinite(q2);
                    if (!infinite) {
                        break;
                    }
                }
            }

            if (infinite) {
                // Scaling failed
                throw new RuntimeException(
                        "Continued fraction convergents diverged to +/- infinity for value "
                        + x);
            }

            double r = p2 / q2;

            if (Double.isNaN(r)) {
                throw new RuntimeException(
                        "Continued fraction diverged to NaN for value "
                        + x);
            }
            relativeError = Math.abs(r / c - 1.0);

            // prepare for next iteration
            c = p2 / q2;
            p0 = p1;
            p1 = p2;
            q0 = q1;
            q1 = q2;
        }

        if (n >= maxIterations) {
            throw new RuntimeException(
                    "Continued fraction convergents failed to converge for value "
                    + x + ". Reached max iterations: " + maxIterations);
        }

        return c;
    }
}
