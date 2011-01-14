package core.examples;

import core.ExecutionContextFactory;
import core.ga.RulePopulation;
import core.ga.ops.ec.ExecutionEnv;
import core.ga.ops.ec.FitnessEval;
import core.ga.ops.ec.FitnessEvaluatorFactory;

/**
 *
 * @author Rekin
 */
public class RulePopulationExample2 {

    public static void main(String[] args) {
        ExecutionEnv ec;
        FitnessEval fit = FitnessEvaluatorFactory.EVAL_FMEASURE;
        ec = ExecutionContextFactory.MONK(1, true, 1000, fit);
        RulePopulation rp = new RulePopulation(ec);
        rp.decode();
        rp.repair();
        rp.evaluate();

        for (int i = 0; i < 1000; i++) {
            rp.select();
            rp.mutate();

            rp.decode();
            rp.repair();
            rp.evaluate();

            System.out.printf("%4d. %s\n", i + 1, rp.stats());
        }
    }
}
