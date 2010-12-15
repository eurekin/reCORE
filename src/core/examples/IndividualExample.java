package core.examples;

import core.ExecutionContextFactory;
import core.ga.Individual;
import core.ga.ops.ec.ExecutionContext;
import core.ga.ops.ec.FitnessEvaluatorFactory;

/**
 *
 * @author Rekin
 */
public class IndividualExample {

    public static void main(String[] args) {
        testCorrectness();
        testSpeed();
        testSpeed();
    }

    public static void testCorrectness() {
        int times = 100;
        ExecutionContext c = ExecutionContextFactory.MONK(3, true, 100, FitnessEvaluatorFactory.EVAL_FMEASURE);
        Individual r = new Individual(c.signature(), c.rand(), c.decoder());
        for (int i = 0; i < times; i++) {
            System.out.println(r.chromosome());
            //r.binomialMutateRepeat(0.01);
            r.mutate(0.9);
        }
    }

    public static void testSpeed() {
        System.out.println("Testing bit by bit mutation");
        int tests = 100000;
        int reps = 5;

        ExecutionContext c = ExecutionContextFactory.MONK(3, true, 100, FitnessEvaluatorFactory.EVAL_FMEASURE);
        for (int j = 0; j < reps; j++) {
            Individual r = new Individual(c.signature(), c.rand(), c.decoder());
            long start = System.currentTimeMillis();
            for (int i = 0; i < tests; i++) {
                r.mutateInter(0.5d);
            }

            long stop = System.currentTimeMillis();
            long durationMs = stop - start;
            System.out.println("Duration of " + tests + " mutations: (ms) " + durationMs);
        }

        System.out.println("Binomial randomized without replacement with repeat");
        int nureps = tests;
        int steps = 10;
        for (int j = 0; j <= steps; j++) {
            double scaled = (double) j / (double) steps;
            Individual r = new Individual(c.signature(), c.rand(), c.decoder());
            long start = System.currentTimeMillis();
            for (int i = 0; i < nureps; i++) {
                r.mutate(scaled);
            }

            long stop = System.currentTimeMillis();

            long durationMs = stop - start;
            System.out.println("Duration of " + tests + "(for MP=" + scaled + ") mutations: (ms) " + durationMs);


        }

    }
}
