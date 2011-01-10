package core.ga;

import core.ga.ops.ec.ExecutionContext;
import core.ga.ops.ec.FitnessEval;
import core.stat.SimpleStatistics;
import core.token.TokenCompetition;
import core.token.TokenizingEvaluator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    private int toSkip;

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

    private void addIndividualToTemporaryPopulationAndUpdateIndex(int r) {
        int currentIndex = next.size(); // since we're filling it up
        addToIndexMap(r, currentIndex);
        Individual copy = individuals.get(r).copy();
        next.add(copy);
    }

    private void addToIndexMap(int oldId, int newId) {
        Set<Integer> set;
        if (oldIndexesToNew.containsKey(oldId)) {
            set = oldIndexesToNew.get(oldId);
        } else {
            set = new HashSet<Integer>();
            oldIndexesToNew.put(oldId, set);
        }
        set.add(newId);
    }

    private void switchPopulations() {
        individuals = next;
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
        int save = toSkip;
        for (Individual i : individuals) {
            if (save > 0) {
                save--;
                continue;
            }

            i.mutate(mt);
        }

    }

    public List<Individual> getIndividuals() {
        return individuals;
    }
    Map<Integer, Set<Integer>> oldIndexesToNew =
            new HashMap<Integer, Set<Integer>>();

    private void select() {
        next = new ArrayList<Individual>(individuals.size());

        // index updates
        oldIndexesToNew.clear();
        saveTheRulesWhichAreNeededByRulePopulationsElitism();
        // random one
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
        while (next.size() < individuals.size()) {
            int r = m.ceilingEntry(context.rand().nextDouble()).getValue();
            addIndividualToTemporaryPopulationAndUpdateIndex(r);
        }
        switchPopulations();
        //debugOutput();
    }

    public SimpleStatistics stats() {
        FitnessEval fevl = context.fitnessEvaluator();
        SimpleStatistics ss = new SimpleStatistics();
        for (Individual i : individuals) {
            ss.addValue(fevl.eval(i.cm()));
        }
        return ss;
    }

    public List<Integer> getNewIndexesFor(Integer integer) {
        // TODO: construct a map of candidates for each
        Set<Integer> get = oldIndexesToNew.get(integer);
        if (get == null)
            return Collections.singletonList(integer);
        else
            return new ArrayList<Integer>(get);
    }

    public static void main(String[] args) {
        System.out.println(1.0d / 7.0d + 6.0d / 7.0d);
    }

    private void debugOutput() {
        System.out.print("[");
        for (Integer integer : oldIndexesToNew.keySet()) {
            System.out.println(integer + " => " + oldIndexesToNew.get(integer).size() + ", ");
        }
        System.out.println("]");

    }

    public Individual getBest() {
        Individual b = individuals.get(0);
        for (Individual individual : individuals) {
            if (b.fitness() < individual.fitness())
                b = individual;
        }
        return b;
    }

    public void pleaseSaveThisRulesForMe(Set<Integer> indexesToSave) {
        this.indexesToSave = indexesToSave;
    }
    Set<Integer> indexesToSave;

    private void saveTheRulesWhichAreNeededByRulePopulationsElitism() {
        if (indexesToSave == null)
            return;
        toSkip = indexesToSave.size();
        for (Integer i : indexesToSave) {
            addIndividualToTemporaryPopulationAndUpdateIndex(i);
        }
        indexesToSave = null;
    }
}
