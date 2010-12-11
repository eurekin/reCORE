/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package core.binomial;

/**
 * The default implementation of {@link BinomialDistribution}.
 *
 * @version $Revision: 920852 $ $Date: 2010-03-09 07:53:44 -0500 (Tue, 09 Mar 2010) $
 */
public class BinomialDistr {

    /** The number of trials. */
    private int numberOfTrials;
    /** The probability of success. */
    private double probabilityOfSuccess;

    /**
     * Create a binomial distribution with the given number of trials and
     * probability of success.
     *
     * @param trials the number of trials.
     * @param p the probability of success.
     */
    public BinomialDistr(int trials, double p) {
        setNumberOfTrialsInternal(trials);
        setProbabilityOfSuccessInternal(p);
    }

    /**
     * Access the number of trials for this distribution.
     *
     * @return the number of trials.
     */
    public int getNumberOfTrials() {
        return numberOfTrials;
    }

    /**
     * Access the probability of success for this distribution.
     *
     * @return the probability of success.
     */
    public double getProbabilityOfSuccess() {
        return probabilityOfSuccess;
    }

    /**
     * Change the number of trials for this distribution.
     *
     * @param trials the new number of trials.
     * @throws IllegalArgumentException if <code>trials</code> is not a valid
     *             number of trials.
     */
    private void setNumberOfTrialsInternal(int trials) {
        if (trials < 0) {
            throw new IllegalArgumentException(
                    "number of trials must be non-negative " + trials);
        }
        numberOfTrials = trials;
    }

    /**
     * Change the probability of success for this distribution.
     *
     * @param p the new probability of success.
     * @throws IllegalArgumentException if <code>p</code> is not a valid
     *             probability.
     */
    private void setProbabilityOfSuccessInternal(double p) {
        if (p < 0.0 || p > 1.0) {
            throw new RuntimeException(
                    p + " out of [0, 1.0] range");
        }
        probabilityOfSuccess = p;
    }

    /**
     * For this distribution, X, this method returns P(X &le; x).
     *
     * @param x the value at which the PDF is evaluated.
     * @return PDF for this distribution.
     * @throws RuntimeException if the cumulative probability can not be computed
     *             due to convergence or other numerical errors.
     */
    public double cumulativeProbability(int x) throws RuntimeException {
        double ret;
        if (x < 0) {
            ret = 0.0;
        } else if (x >= numberOfTrials) {
            ret = 1.0;
        } else {
            ret = 1.0 - Beta.regularizedBeta(getProbabilityOfSuccess(),
                    x + 1.0, numberOfTrials - x);
        }
        return ret;
    }
}
