package core.ga;

import core.ga.ops.ec.ExecutionContext;
import core.io.dataframe.UniformDataFrame;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Rekin
 * 
 */
public class RulePopulation implements Iterable<Individual> {

    final List<Individual> individuals;
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
}
