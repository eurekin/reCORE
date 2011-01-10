package core.copop;

import core.ga.RulePopulation;
import core.ga.ops.ec.ExecutionContext;
import core.ga.ops.ec.FitnessEval;
import core.stat.SimpleStatistics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

/**
 *
 * @author gmatoga
 */
public class RuleSetPopulation {

    private List<PittsIndividual> individuals;
    private ExecutionContext context;
    private PittsIndividual best;
    private RulePopulation rpop;

    public RuleSetPopulation(int popsize, ExecutionContext ctx, RulePopulation rpop) {
        this.context = ctx;
        individuals = new ArrayList<PittsIndividual>();
        this.rpop = rpop;
        for (int i = 0; i < popsize; i++) {
            individuals.add(new PittsIndividual(ctx, rpop));
        }
    }

    public void mutate() {
        final double mprob = context.getRsmp();
        int toPass = eliteSelectionSize();
        for (PittsIndividual i : individuals) {
            if (toPass > 0) {
                toPass--;
                continue;
            }
            i.addOrRemoveWith(mprob);
            i.mutateClass(mprob);
            i.mutate(mprob);
        }
    }

    public void evolve() {
        reset();
        mutate();
    }

    public int eliteSelectionSize() {
        return 50;
    }

    public void select() {
        ArrayList<PittsIndividual> next = new ArrayList<PittsIndividual>(individuals.size());
        Collections.sort(individuals, new Comparator<PittsIndividual>() {

            public int compare(PittsIndividual o1, PittsIndividual o2) {
                return Double.compare(o2.fitness(), o1.fitness());
            }
        });

        Set<Integer> indexesToSave = new HashSet<Integer>();
        for (int i = 0, end = eliteSelectionSize(); i < end; i++) {
            final PittsIndividual ind = individuals.get(i);
            next.add(ind);
            indexesToSave.addAll(ind.indexes);
        }
        rpop.pleaseSaveThisRulesForMe(indexesToSave);

        TreeMap<Double, Integer> m = new TreeMap<Double, Integer>();
        double CDF[] = new double[individuals.size()];
        double acc = 0;
        double sumfit = 0;

        for (PittsIndividual el : individuals)
            sumfit += el.fitness();
        for (int i = 0; i < CDF.length; i++) {
            acc += individuals.get(i).fitness();
            CDF[i] = acc / sumfit;
        }
        for (int i = 0; i < CDF.length; i++)
            m.put(CDF[i], i);
        while (next.size() < individuals.size()) {
            int r = m.ceilingEntry(context.rand().nextDouble()).getValue();
            PittsIndividual copy = individuals.get(r).copy();
            next.add(copy);
        }
        individuals = next;
    }

    public void reset() {
        for (PittsIndividual pi : getIndividuals()) {
            pi.reset();
        }
    }

    public void evaluate() {
        for (PittsIndividual i : getIndividuals()) {
            i.evaluate(context.data(), context.evaluator());
        }
    }

    public SimpleStatistics stats() {
        SimpleStatistics ss = new SimpleStatistics();
        FitnessEval fevl = context.fitnessEvaluator();
        for (PittsIndividual i : getIndividuals()) {
            ss.addValue(fevl.eval(i.getCm().getWeighted()));
        }
        return ss;
    }

    public PittsIndividual getBest() {
        double max = stats().getMax();
        FitnessEval fevl = context.fitnessEvaluator();
        for (PittsIndividual i : getIndividuals()) {
            if (fevl.eval(i.getCm().getWeighted()) == max)
                return i;
        }
        return null;
    }

    public List<PittsIndividual> getIndividuals() {
        return individuals;
    }
}
