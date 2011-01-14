package core.ga;

import core.ga.ops.ec.ExecutionEnv;
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
    final ExecutionEnv context;
    private List<Individual> next;
    private boolean tokenCompetition = true;
    private int toSkip;

    public RulePopulation(ExecutionEnv ctx) {
        this.individuals = new ArrayList<Individual>(ctx.size());
        Individual ind;
        tokenCompetition = ctx.isTokenCompetitionEnabled();
        Mutator mutator = new Mutator(ctx.rand());
        for (int i = 0; i < ctx.size(); i++) {
            ind = new Individual(ctx.signature(), ctx.rand(),
                    ctx.decoder(), mutator, ctx.fitnessEvaluator());
            individuals.add(ind);
        }
        this.context = ctx;
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
//        System.out.println("Add index to map called with " + oldId + " => " + newId);
        if (oldIndexesToNew.containsKey(oldId)) {
            return;
            // brutal, but required
            // if the mapping can result in more than one substitution,
            // than it would lead to an explosion - no rules could be
            // recombined, since we would have to be sure we're not
            // destroying the elite rulesets.
            // Tracking selected rules would require a lot more sophisticated
            // mechanism and very intricate rule <-> ruleset interaction
            // during update phase.
            // So... NO to 1-N mapping.
            // And it will run faster also.

            //set = oldIndexesToNew.get(oldId);
        } else {
            oldIndexesToNew.put(oldId, newId);
        }
//        System.out.println("New Map = " + oldIndexesToNew);
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
        Collection<Integer> values = oldIndexesToNew.values();
        for (Integer i = 0; i < individuals.size(); i++) {
            if (values.contains(i))
                continue;
            individuals.get(i).mutate(mt);

        }
    }

    public List<Individual> getIndividuals() {
        return individuals;
    }
    Map<Integer, Integer> oldIndexesToNew =
            new HashMap<Integer, Integer>();

    public void select() {
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

//        debugOutput();
    }

    public SimpleStatistics stats() {
        FitnessEval fevl = context.fitnessEvaluator();
        SimpleStatistics ss = new SimpleStatistics();
        for (Individual i : individuals) {
            ss.addValue(fevl.eval(i.cm()));
        }
        return ss;
    }

    public Integer getNewIndexesFor(Integer integer) {
        // TODO: construct a map of candidates for each
        if (oldIndexesToNew.containsKey(integer))
            return oldIndexesToNew.get(integer);
        else
            return integer;
    }

    private void debugOutput() {
        System.out.println(oldIndexesToNew);
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
//        System.out.println("Going to save rules no: " + indexesToSave);
        if (indexesToSave == null)
            return;
        toSkip = indexesToSave.size();
        for (Integer i : indexesToSave) {

            addIndividualToTemporaryPopulationAndUpdateIndex(i);
        }
        indexesToSave = null;
    }
}
