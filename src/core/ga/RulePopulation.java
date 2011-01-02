package core.ga;

import core.ga.ops.ec.ExecutionContext;
import core.ga.ops.ec.FitnessEval;
import core.stat.SimpleStatistics;
import core.token.TokenCompetition;
import core.token.TokenizingEvaluator;
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
    private boolean tokenCompetition = true;

    public RulePopulation(ExecutionContext ctx) {
        this.individuals = new ArrayList<Individual>(ctx.size());
        Individual ind;
        Mutator mutator = new Mutator(ctx.rand());
        for (int i = 0; i < ctx.size(); i++) {
            ind = new Individual(ctx.signature(), ctx.rand(),
                    ctx.decoder(), mutator, ctx.fitnessEvaluator());
            individuals.add(ind);
        }
        this.context = ctx;
    }

    public void evolve() {
        select();
        mutate();
    }

    public void randomize() {
        for (Individual ind : individuals)
            ind.randomize();
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
        if (tokenCompetition)
            tokenCompetitionUpdate();
        else
            normalEvaluate();
    }

    private void tokenCompetitionUpdate() {
        TokenCompetition comp = new TokenCompetition(context.data().size());
        TokenizingEvaluator evaluator = new TokenizingEvaluator(comp);
        for (Individual ind : individuals)
            ind.evaluate(context.data(), evaluator);
        comp.giveOutTokens();
        for (Individual individual : individuals)
            individual.penalizeToken();
    }

    private void normalEvaluate() {
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

    public List<Individual> getIndividuals() {
        return individuals;
    }

    private void select() {
        next = new ArrayList<Individual>(individuals.size());
        TreeMap<Double, Integer> m = new TreeMap<Double, Integer>();
        double CDF[] = new double[individuals.size()];
        double acc = 0;
        double sumfit = 0;

        for (Individual el : individuals)
            sumfit += el.fitness();
        for (int i = 0; i < CDF.length; i++) {
            acc += individuals.get(i).fitness();
            CDF[i] = acc / sumfit;
        }
        for (int i = 0; i < CDF.length; i++)
            m.put(CDF[i], i);
        for (int i = 0; i < individuals.size(); i++) {
            int r = m.ceilingEntry(context.rand().nextDouble()).getValue();
            Individual copy = individuals.get(r).copy();
            next.add(copy);
        }
        individuals = next;
    }

    public SimpleStatistics stats() {
        FitnessEval fevl = context.fitnessEvaluator();
        SimpleStatistics ss = new SimpleStatistics();
        for (Individual i : individuals) {
            ss.addValue(fevl.eval(i.cm()));
        }
        return ss;
    }
}
