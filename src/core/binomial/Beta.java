/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package core.binomial;

/**
 * This is a utility class that provides computation methods related to the
 * Beta family of functions.
 *
 * @version $Revision: 811685 $ $Date: 2009-09-05 13:36:48 -0400 (Sat, 05 Sep 2009) $
 */
public class Beta {

    /** Maximum allowed numerical error. */
    private static final double DEFAULT_EPSILON = 10e-15;

    /**
     * Default constructor.  Prohibit instantiation.
     */
    private Beta() {
        super();
    }

    /**
     * Returns the
     * <a href="http://mathworld.wolfram.com/RegularizedBetaFunction.html">
     * regularized beta function</a> I(x, a, b).
     *
     * @param x the value.
     * @param a the a parameter.
     * @param b the b parameter.
     * @return the regularized beta function I(x, a, b)
     * @throws RuntimeException if the algorithm fails to converge.
     */
    public static double regularizedBeta(double x, double a, double b)
            throws RuntimeException {
        return regularizedBeta(x, a, b, DEFAULT_EPSILON, Integer.MAX_VALUE);
    }

    /**
     * Returns the
     * <a href="http://mathworld.wolfram.com/RegularizedBetaFunction.html">
     * regularized beta function</a> I(x, a, b).
     *
     * @param x the value.
     * @param a the a parameter.
     * @param b the b parameter.
     * @param epsilon When the absolute value of the nth item in the
     *                series is less than epsilon the approximation ceases
     *                to calculate further elements in the series.
     * @return the regularized beta function I(x, a, b)
     * @throws RuntimeException if the algorithm fails to converge.
     */
    public static double regularizedBeta(double x, double a, double b,
            double epsilon) throws RuntimeException {
        return regularizedBeta(x, a, b, epsilon, Integer.MAX_VALUE);
    }

    /**
     * Returns the regularized beta function I(x, a, b).
     *
     * @param x the value.
     * @param a the a parameter.
     * @param b the b parameter.
     * @param maxIterations Maximum number of "iterations" to complete.
     * @return the regularized beta function I(x, a, b)
     * @throws RuntimeException if the algorithm fails to converge.
     */
    public static double regularizedBeta(double x, double a, double b,
            int maxIterations) throws RuntimeException {
        return regularizedBeta(x, a, b, DEFAULT_EPSILON, maxIterations);
    }

    /**
     * Returns the regularized beta function I(x, a, b).
     *
     * The implementation of this method is based on:
     * <ul>
     * <li>
     * <a href="http://mathworld.wolfram.com/RegularizedBetaFunction.html">
     * Regularized Beta Function</a>.</li>
     * <li>
     * <a href="http://functions.wolfram.com/06.21.10.0001.01">
     * Regularized Beta Function</a>.</li>
     * </ul>
     *
     * @param x the value.
     * @param a the a parameter.
     * @param b the b parameter.
     * @param epsilon When the absolute value of the nth item in the
     *                series is less than epsilon the approximation ceases
     *                to calculate further elements in the series.
     * @param maxIterations Maximum number of "iterations" to complete.
     * @return the regularized beta function I(x, a, b)
     * @throws RuntimeException if the algorithm fails to converge.
     */
    public static double regularizedBeta(double x, final double a,
            final double b, double epsilon, int maxIterations) throws RuntimeException {
        double ret;

        if (Double.isNaN(x) || Double.isNaN(a) || Double.isNaN(b) || (x < 0)
                || (x > 1) || (a <= 0.0) || (b <= 0.0)) {
            ret = Double.NaN;
        } else if (x > (a + 1.0) / (a + b + 2.0)) {
            ret = 1.0 - regularizedBeta(1.0 - x, b, a, epsilon, maxIterations);
        } else {
            ContinuedFraction fraction = new ContinuedFraction() {

                @Override
                protected double getB(int n, double x) {
                    double ret;
                    double m;
                    if (n % 2 == 0) { // even
                        m = n / 2.0;
                        ret = (m * (b - m) * x)
                                / ((a + (2 * m) - 1) * (a + (2 * m)));
                    } else {
                        m = (n - 1.0) / 2.0;
                        ret = -((a + m) * (a + b + m) * x)
                                / ((a + (2 * m)) * (a + (2 * m) + 1.0));
                    }
                    return ret;
                }

                @Override
                protected double getA(int n, double x) {
                    return 1.0;
                }
            };
            ret = Math.exp((a * Math.log(x)) + (b * Math.log(1.0 - x))
                    - Math.log(a) - logBeta(a, b, epsilon, maxIterations))
                    * 1.0 / fraction.evaluate(x, epsilon, maxIterations);
        }

        return ret;
    }

    /**
     * Returns the natural logarithm of the beta function B(a, b).
     *
     * @param a the a parameter.
     * @param b the b parameter.
     * @return log(B(a, b))
     */
    public static double logBeta(double a, double b) {
        return logBeta(a, b, DEFAULT_EPSILON, Integer.MAX_VALUE);
    }

    /**
     * Returns the natural logarithm of the beta function B(a, b).
     *
     * The implementation of this method is based on:
     * <ul>
     * <li><a href="http://mathworld.wolfram.com/BetaFunction.html">
     * Beta Function</a>, equation (1).</li>
     * </ul>
     *
     * @param a the a parameter.
     * @param b the b parameter.
     * @param epsilon When the absolute value of the nth item in the
     *                series is less than epsilon the approximation ceases
     *                to calculate further elements in the series.
     * @param maxIterations Maximum number of "iterations" to complete.
     * @return log(B(a, b))
     */
    public static double logBeta(double a, double b, double epsilon,
            int maxIterations) {

        double ret;

        if (Double.isNaN(a) || Double.isNaN(b) || (a <= 0.0) || (b <= 0.0)) {
            ret = Double.NaN;
        } else {
            ret = logGamma(a) + logGamma(b)
                    - logGamma(a + b);
        }

        return ret;
    }

    /**
     * Returns the natural logarithm of the gamma function &#915;(x).
     *
     * The implementation of this method is based on:
     * <ul>
     * <li><a href="http://mathworld.wolfram.com/GammaFunction.html">
     * Gamma Function</a>, equation (28).</li>
     * <li><a href="http://mathworld.wolfram.com/LanczosApproximation.html">
     * Lanczos Approximation</a>, equations (1) through (5).</li>
     * <li><a href="http://my.fit.edu/~gabdo/gamma.txt">Paul Godfrey, A note on
     * the computation of the convergent Lanczos complex Gamma approximation
     * </a></li>
     * </ul>
     *
     * @param x the value.
     * @return log(&#915;(x))
     */
    public static double logGamma(double x) {
        double ret;

        if (Double.isNaN(x) || (x <= 0.0)) {
            ret = Double.NaN;
        } else {
            double g = 607.0 / 128.0;

            double sum = 0.0;
            for (int i = LANCZOS.length - 1; i > 0; --i) {
                sum = sum + (LANCZOS[i] / (x + i));
            }
            sum = sum + LANCZOS[0];

            double tmp = x + g + .5;
            ret = ((x + .5) * Math.log(tmp)) - tmp
                    + HALF_LOG_2_PI + Math.log(sum / x);
        }

        return ret;
    }
    private static final double[] LANCZOS = {
        0.99999999999999709182,
        57.156235665862923517,
        -59.597960355475491248,
        14.136097974741747174,
        -0.49191381609762019978,
        .33994649984811888699e-4,
        .46523628927048575665e-4,
        -.98374475304879564677e-4,
        .15808870322491248884e-3,
        -.21026444172410488319e-3,
        .21743961811521264320e-3,
        -.16431810653676389022e-3,
        .84418223983852743293e-4,
        -.26190838401581408670e-4,
        .36899182659531622704e-5,};
    /** Avoid repeated computation of log of 2 PI in logGamma */
    private static final double HALF_LOG_2_PI = 0.5 * Math.log(2.0 * Math.PI);
}
