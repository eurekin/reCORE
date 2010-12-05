package core.ga.ops.ec;

import core.stat.BinaryConfMtx;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Rekin
 */
public class FitnessEvaluatorFactory {

    public static final FitnessEval EVAL_ACCURACY = new Accuracy();
    public static final FitnessEval EVAL_FMEASURE = new Fmeasure();
    public static final FitnessEval EVAL_RECALL = new Recall();
    public static final FitnessEval EVAL_PRECISION = new Precision();
    public static final Set<FitnessEval> EVALS =
            Collections.unmodifiableSet(new HashSet(
            Arrays.asList(new FitnessEval[]{
                EVAL_ACCURACY,
                EVAL_FMEASURE,
                EVAL_RECALL,
                EVAL_PRECISION
            })));

    private FitnessEvaluatorFactory() {
    }
    
    private abstract static class PPFEval  implements FitnessEval{

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
        
    }

    private static class Accuracy extends PPFEval {

        public double eval(BinaryConfMtx cm) {
            return cm.accuracy();
        }
    };

    private static class Fmeasure extends PPFEval {

        public double eval(BinaryConfMtx cm) {
            return cm.fMeasure();
        }
    };

    private static class Recall extends PPFEval {

        public double eval(BinaryConfMtx cm) {
            return cm.recall();
        }
    };

    private static class Precision extends PPFEval {

        public double eval(BinaryConfMtx cm) {
            return cm.precision();
        }
    };
}
