package core.examples;

import core.ExecutionContextFactory;
import core.ga.Individual;
import core.ga.RulePopulation;
import core.ga.ops.ec.ExecutionContext;
import core.ga.ops.ec.FitnessEval;
import core.ga.ops.ec.FitnessEvaluatorFactory;

/**
 *
 * @author Rekin
 */
public class RulePopulationExample2 {

    public static void main(String[] args) {
        ExecutionContext ec;
        FitnessEval fit = FitnessEvaluatorFactory.EVAL_FMEASURE;
        ec = ExecutionContextFactory.MONK(3, true, 1000, fit);
        RulePopulation rp = new RulePopulation(ec);
        rp.decode();
        rp.repair();
        rp.evaluate();
        double fitSum = fitSum(rp);
        System.out.print("fitSum = " + fitSum);

        for (int i = 0; i < 100000; i++) {
            rp.evolve();

            rp.decode();
            rp.repair();
            rp.evaluate();

            fitSum = fitSum(rp);
            System.out.print("fitSum = " + fitSum);
            double bestFit = bestFit(rp);
            System.out.println("bestFit = " + bestFit);
        }
    }

    private static double fitSum(RulePopulation rp) {
        double sumfit = 0;
        for (Individual i : rp) {
            sumfit += i.fitness();
        }
        return sumfit;
    }

    private static double bestFit(RulePopulation rp) {
        double bf = 0;
        for (Individual i : rp) {
            if (i.fitness() > bf) bf = i.fitness();
        }
        return bf;
    }
}
