package core;

import core.ga.DefaultEvaluator;
import core.ga.GrayBinaryDecoderPlusONE;
import core.ga.RuleDecoderSubractingOneFromClass;
import core.ga.ops.ec.ExecutionContext;
import core.ga.ops.ec.FitnessEval;
import java.util.Random;

/**
 *
 * @author Rekin
 */
public class ExecutionContextFactory {

    private ExecutionContextFactory() {
    }

    public static ExecutionContext MONK(int version, boolean test,
            int popsize, FitnessEval ruleFitEval) {
        final Random random = new Random();
        final DataSetBundle mb = DataSetBundleFactory.MONK(version, test);
        final DefaultEvaluator eval = new DefaultEvaluator();
        final GrayBinaryDecoderPlusONE bdec = new GrayBinaryDecoderPlusONE();
        final RuleDecoderSubractingOneFromClass dec =
                new RuleDecoderSubractingOneFromClass(mb.getSignature(), bdec);
        return new ExecutionContext(popsize, random, eval, mb, dec, ruleFitEval);
    }
}
