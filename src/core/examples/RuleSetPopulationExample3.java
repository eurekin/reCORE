package core.examples;

import core.DataSetBundle;
import core.ExecutionContextFactory;
import core.copop.CoPopulations;
import core.copop.PittsIndividual;
import core.copop.RuleSet;
import core.evo.EvoIndividual;
import core.evo.EvolutionPopulation;
import core.ga.ops.ec.ExecutionEnv;
import core.ga.ops.ec.FitnessEval;
import core.ga.ops.ec.FitnessEvaluatorFactory;
import core.stat.SimpleStatistics;
import core.vis.RuleASCIIPlotter;

/**
 *
 * @author gmatoga
 */
public class RuleSetPopulationExample3 {

    public static void main(String[] args) {
        ExecutionEnv ec;
        FitnessEval fit = FitnessEvaluatorFactory.EVAL_FMEASURE;
        ec = ExecutionContextFactory.MONK(1, true, 1000, fit);

        ec.setMt(0.002);
        ec.setRsmp(0.02);
        ec.setMaxRuleSetLength(15);

        long seed = System.currentTimeMillis();
        ec.rand().setSeed(seed);

        // example successful seeds for M3
        // 1292886991233
        // 1292962795459
        // 1292962881548
        // 1293286883264 3929
        // 1293287138857 568
        // 1293287213164 691
        // 1293288176976 13
        // 1293288497408 18
        // 1293288603812 536
        // 1293289274746 24885

        EvolutionPopulation co = new EvolutionPopulation(ec);

        final DataSetBundle bundle = ec.getBundle();

        double best = 0;
        RuleSet ruleSet;
        for (int i = 0; i < 100000; i++) {

            co.evolve();

            final SimpleStatistics ruleSetStats = co.stats();
            if (measure(ruleSetStats) >= best) {
                best = measure(ruleSetStats);
                System.out.println("   RULESET stats: " + ruleSetStats);
                final double max = ruleSetStats.getMax();

                if (max > co.stats().getMax()) {
                    System.out.println("Congratulations!");
                    java.awt.Toolkit.getDefaultToolkit().beep();
                    for (EvoIndividual ind : co.getIndividuals()) {
                        final double eval = ind.fitness();
                        if (eval == max) {
                            System.out.println("HERE");
                            System.out.println("Seed = " + seed);
                            System.out.println(ind);
                            ruleSet = ind.getRS();
                            System.out.println(bundle.getPrinter().print(ruleSet));
                            System.out.println(ind.getCm().getWeighted());
                            String[][] plot = bundle.getPlotter().plotBinaryRuleSet(ruleSet);
                            System.out.println("Visualization: ");
                            RuleASCIIPlotter.simpleBinaryPlot(plot);
                        }
                    }
                }
            }
        }
    }

    private static double measure(final SimpleStatistics ruleSetStats) {
        return ruleSetStats.getMax();
    }
}
