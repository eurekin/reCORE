package core.ga;

import core.ga.ops.ec.ExecutionContext;
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
    private List<Individual> next;

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

    private void select() {
        next = new ArrayList<Individual>(individuals.size());
        TreeMap<Double, Integer> m = new TreeMap<Double, Integer>();
        double CDF[] = new double[individuals.size()];
        double acc = 0;
        double sumfit = 0, fit;

        for (Individual el : individuals) {
            fit = el.fitness();
            sumfit += fit;
        }
        for (int i = 0; i < CDF.length; i++) {
            acc += individuals.get(i).fitness();
            CDF[i] = acc / sumfit;
        }
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
}
