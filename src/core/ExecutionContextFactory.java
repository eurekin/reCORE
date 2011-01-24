package core;

import core.ga.DefaultEvaluator;
import core.ga.GrayBinaryDecoderPlusONE;
import core.ga.RuleDecoder;
import core.ga.ops.ec.ExecutionEnv;
import core.ga.ops.ec.FitnessEval;
import java.util.Random;

/**
 *
 * @author Rekin
 */
public class ExecutionContextFactory {

    private ExecutionContextFactory() {
    }

    public static ExecutionEnv MONK(int version, boolean train,
            int popsize, FitnessEval ruleFitEval) {
        final Random random = new Random();
        final DataSetBundle mb = DataSetBundleFactory.MONK(version, train);
        final DefaultEvaluator eval = new DefaultEvaluator();
        final GrayBinaryDecoderPlusONE bdec = new GrayBinaryDecoderPlusONE();
        final RuleDecoder dec =
                new RuleDecoder(mb.getSignature(), bdec);
        return new ExecutionEnv(popsize, random, eval, mb, dec, ruleFitEval);
    }
}
