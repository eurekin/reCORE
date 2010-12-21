package core.copop;

import core.DataSetBundle;
import core.ExecutionContextFactory;
import core.ga.RulePopulation;
import core.ga.ops.ec.ExecutionContext;
import core.ga.ops.ec.FitnessEval;
import core.ga.ops.ec.FitnessEvaluatorFactory;
import core.stat.SimpleStatistics;
import core.vis.RuleASCIIPlotter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gmatoga
 */
public class RuleSetPopulation {

    private List<PittsIndividual> individuals;
    private ExecutionContext context;

    public RuleSetPopulation(int popsize, ExecutionContext ctx, RulePopulation rpop) {
        this.context = ctx;
        individuals = new ArrayList<PittsIndividual>();
        for (int i = 0; i < popsize; i++) {
            individuals.add(new PittsIndividual(ctx, rpop));
        }
    }

    public void mutate() {
        final double mprob = context.getRsmp();
        for (PittsIndividual i : individuals) {
            i.addOrRemoveWith(mprob);
            i.mutateClass();
        }
    }

    public void evolve() {
        reset();
        mutate();
    }

    private void reset() {
        for (PittsIndividual pi : individuals) {
            pi.reset();
        }
    }

    public void evaluate() {
        for (PittsIndividual i : individuals) {
            i.evaluate(context.data(), context.evaluator());
        }
    }

    public SimpleStatistics stats() {
        SimpleStatistics ss = new SimpleStatistics();
        FitnessEval fevl = context.fitnessEvaluator();
        for (PittsIndividual i : individuals) {
            ss.addValue(fevl.eval(i.getCm().getWeighted()));
        }
        return ss;
    }

    public static void main(String[] args) {
        ExecutionContext ec;
        FitnessEval fit = FitnessEvaluatorFactory.EVAL_FMEASURE;
        ec = ExecutionContextFactory.MONK(3, true, 1000, fit);

        ec.setMt(0.002);
        ec.setRsmp(0.01);
        ec.setMaxRuleSetLength(5);

        long seed =  System.currentTimeMillis();
        ec.rand().setSeed(seed);

        // example successful seeds
        // 1292886991233
        // 1292962795459
        // 1292962881548

        RulePopulation rp = new RulePopulation(ec);
        RuleSetPopulation rsp = new RuleSetPopulation(1000, ec, rp);
        final DataSetBundle bundle = ec.getBundle();
        rp.decode();
        rp.repair();
        rp.evaluate();

        rsp.evaluate();

        double best = 0;
        FitnessEval fevl;
        String pr;
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
                if (best > 0.9344262295081968) {
                    System.out.println("Congratulations!");
                    java.awt.Toolkit.getDefaultToolkit().beep();
                    fevl = ec.fitnessEvaluator();
                    for (PittsIndividual ind : rsp.individuals) {
                        final double eval = fevl.eval(ind.getCm().getWeighted());
                        if (eval > 0.9344262295081968) {
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
