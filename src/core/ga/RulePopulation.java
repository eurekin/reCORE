package core.ga;

import core.ExecutionContextFactory;
import core.ga.ops.ec.ExecutionContext;
import core.ga.ops.ec.FitnessEval;
import core.ga.ops.ec.FitnessEvaluatorFactory;
import core.io.dataframe.UniformDataFrame;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

/**
 *
 * @author Rekin
 * 
 */
public class RulePopulation implements Iterable<Individual> {

    List<Individual> individuals;
    final ExecutionContext context;

    public RulePopulation(ExecutionContext ctx) {
        this.individuals = new ArrayList<Individual>(ctx.size());
        Individual ind;
        for (int i = 0; i < ctx.size(); i++) {
            ind = new Individual(ctx.signature(), ctx.rand(), ctx.decoder());
            individuals.add(ind);
        }
        this.context = ctx;
    }

    public void evolve() {
        select();
//        crossover(); TODO: when prove useful
        mutate();
    }

    public void evaluate(UniformDataFrame<Integer, Integer> data,
            Evaluator evaluator) {
    }

    public void randomize() {
        for (Individual ind : individuals) {
            ind.randomize();
        }
    }

    public void repair() {
        for (Individual ind : individuals) {
            ind.repair();
        }
    }

    public void decode() {
        for (Individual ind : individuals) {
            ind.decode(context.decoder());
        }
    }

    public void evaluate() {
        for (Individual ind : individuals) {
            ind.evaluate(context.data(), context.evaluator());
        }
    }

    public Iterator<Individual> iterator() {
        return individuals.iterator();
    }

    public void mutate() {
        final double mt = context.getMt();
        for (Individual i : individuals) {
            i.mutate(mt);
        }

    }
    private List<Individual> next;

    private void select() {
        next = new ArrayList<Individual>(individuals.size());

        double sumfit = 0, fit;
        for (Individual el : individuals) {
            fit = el.fitness();
            sumfit += fit;
        }
        double CDF[] = new double[individuals.size()];
        double acc = 0;
        for (int i = 0; i < CDF.length; i++) {
            acc += individuals.get(i).fitness();
            CDF[i] = acc / sumfit;
        }
        TreeMap<Double, Integer> m = new TreeMap<Double, Integer>();
        for (int i = 0; i < CDF.length; i++) {
            double d = CDF[i];
            m.put(d, i);
        }

        for (int i = 0; i < individuals.size(); i++) {
            int r = m.ceilingEntry(context.rand().nextDouble()).getValue();
            Individual copy = individuals.get(r).copy();
            next.add(copy);
        }
        individuals = next;
    }

    public static void main(String[] args) {
        ExecutionContext ec;
        FitnessEval fit = FitnessEvaluatorFactory.EVAL_FMEASURE;
        ec = ExecutionContextFactory.MONK(3, true, 1000, fit);
        RulePopulation rp = new RulePopulation(ec);
        rp.decode();
        rp.repair();
        rp.evaluate();
        double fitSum = rp.fitSum();
        System.out.print("fitSum = " + fitSum);

        for (int i = 0; i < 100000; i++) {
            rp.evolve();

            rp.decode();
            rp.repair();
            rp.evaluate();

            fitSum = rp.fitSum();
            System.out.print("fitSum = " + fitSum);
        double bestFit = rp.bestFit();
            System.out.println("bestFit = " + bestFit);
        }
    }

    private double fitSum() {
        double sumfit = 0;
        for (Individual i : individuals) {
            sumfit += i.fitness();
        }
        return sumfit;
    }

    private double bestFit() {
        double bf = 0;
        for (Individual i : individuals) {
            if (i.fitness() > bf) bf = i.fitness();
        }
        return bf;
    }
}
