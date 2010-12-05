package core.ga.ops.ec;

import core.stat.BinaryConfMtx;

/**
 *
 * @author Rekin
 */
public interface FitnessEval {

    public double eval(BinaryConfMtx cm);
}
