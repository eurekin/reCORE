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
    public static final FitnessEval EVAL_MCC = new Mathews();
    public static final Set<FitnessEval> EVALS =
            Collections.unmodifiableSet(new HashSet(
            Arrays.asList(new FitnessEval[]{
                EVAL_ACCURACY,
                EVAL_FMEASURE,
                EVAL_RECALL,
                EVAL_PRECISION,
                EVAL_MCC
            })));

    private FitnessEvaluatorFactory() {
    }

    private abstract static class PPFEval implements FitnessEval {

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

    private static class Mathews extends PPFEval {

        public double eval(BinaryConfMtx cm) {
            int d1 = cm.tp + cm.fp;
            int d2 = cm.tp + cm.fn;
            int d3 = cm.tn + cm.fp;
            int d4 = cm.tn + cm.fn;
            int d = d1 * d2 * d3 * d4;
            double den = d == 0 ? 1 : Math.sqrt(d);
            int nom = cm.tp * cm.tn - cm.fp * cm.fn;
            return ((double) nom) / ((double) den);
        }
    };
}
