package core.examples;

import core.DataSetBundle;
import core.ExecutionContextFactory;
import core.copop.PittsIndividual;
import core.copop.RuleSet;
import core.copop.RuleSetPopulation;
import core.ga.RulePopulation;
import core.ga.ops.ec.ExecutionContext;
import core.ga.ops.ec.FitnessEval;
import core.ga.ops.ec.FitnessEvaluatorFactory;
import core.stat.SimpleStatistics;
import core.vis.RuleASCIIPlotter;

/**
 *
 * @author gmatoga
 */
public class RuleSetPopulationExample {

    public static void main(String[] args) {
        ExecutionContext ec;
        FitnessEval fit = FitnessEvaluatorFactory.EVAL_FMEASURE;
        ec = ExecutionContextFactory.MONK(3, true, 1000, fit);

        ec.setMt(0.002);
        ec.setRsmp(0.01);
        ec.setMaxRuleSetLength(5);

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

        RulePopulation rp = new RulePopulation(ec);
        RuleSetPopulation rsp = new RuleSetPopulation(1000, ec, rp);
        final DataSetBundle bundle = ec.getBundle();
        rp.decode();
        rp.repair();
        rp.evaluate();

        rsp.evaluate();

        double best = 0;
        FitnessEval fevl;
        RuleSet ruleSet;
        for (int i = 0; i < 100000; i++) {
            rp.evolve();

            rp.decode();
            rp.repair();
            rp.evaluate();

            rsp.evolve();
            rsp.evaluate();

            final SimpleStatistics ruleStats = rp.stats();
            final SimpleStatistics ruleSetStats = rsp.stats();
            if (measure(ruleSetStats) > best) {
                best = measure(ruleSetStats);
                System.out.printf("%4d.  RULE: %s", i + 1, ruleStats);
                System.out.println("   RULESET stats: " + ruleSetStats);

                if (ruleSetStats.getMax() > ruleStats.getMax()) {
                    System.out.println("Congratulations!");
                    java.awt.Toolkit.getDefaultToolkit().beep();
                    fevl = ec.fitnessEvaluator();
                    for (PittsIndividual ind : rsp.getIndividuals()) {
                        final double eval = fevl.eval(ind.getCm().getWeighted());
                        if (eval > ruleStats.getMax()) {
                            System.out.println("HERE");
                            System.out.println("Seed = " + seed);
                            System.out.println(ind);
                            System.out.println(ind.getCm().getWeighted());
                            ruleSet = ind.getRS();
                            System.out.println(bundle.getPrinter().print(ruleSet));
                            String[][] plot = bundle.getPlotter().plotRuleSet(ruleSet);
                            System.out.println("Visualization: ");
                            RuleASCIIPlotter.simplePlot(plot);
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
