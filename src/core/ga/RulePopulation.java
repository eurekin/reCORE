package core.ga;

import core.ga.ops.ec.ExecutionEnv;
import core.ga.ops.ec.FitnessEval;
import core.stat.SimpleStatistics;
import core.token.TokenCompetition;
import core.token.TokenizingEvaluator;
import java.util.ArrayList;
import java.util.HashMap;
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

    public RulePopulation(ExecutionEnv ctx) {
        this.individuals = new ArrayList<Individual>(ctx.size());
        Individual ind;
        tokenCompetition = ctx.isTokenCompetitionEnabled();
        Mutator mutator = new Mutator(ctx.rand());
        for (int i = 0; i < ctx.size(); i++) {
            ind = new Individual(ctx.signature(), ctx.rand(),
                    ctx.decoder(), mutator, ctx.fitnessEvaluator(), ctx);
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
        if (context.getDebugOptions().isEvolutionPhaseOutput())
            System.out.println("[DEC-r]");

        for (Individual ind : individuals) {
            ind.decode(context.decoder());
            ind.repair();
        }
    }

    public void evaluate() {
        if (context.getDebugOptions().isEvolutionPhaseOutput())
            System.out.println("[EVAL-r]");

        if (tokenCompetition)
            tokenCompetitionUpdate();
        else
            normalEvaluate();
    }

    private void addIndividualToTemporaryPopulationAndUpdateIndex(int r) {
        if (context.getDebugOptions().isSelectionResultOutput())
            System.out.println("[SEL-r] selecting..." + r);
        int currentIndex = next.size(); // since we're filling it up
        addToIndexMap(r, currentIndex);
        Individual copy = individuals.get(r).copy();
        next.add(copy);
    }

    private void addToIndexMap(int oldId, int newId) {
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

        if (!oldIndexesToNew.containsKey(oldId))
            oldIndexesToNew.put(oldId, newId);

        if (context.getDebugOptions().isAddingToIndexMapOutput())
            System.out.println("[IDXup] New Map = " + oldIndexesToNew);
    }

    private void outputAllRules() {
        RulePrinter printer = context.getBundle().getPrinter();
         for (Individual ind : individuals) {
            System.out.printf("Fitness=%5.1f%%\n", ind.fitness() * 100);
            System.out.printf("%s  \n", ind.cm());
            String s = printer.prettyPrint(ind.rule());
            System.out.println(s);
            System.out.println("");
        }
    }

    private void switchPopulations() {
        if (context.getDebugOptions().isEvolutionPhaseOutput())
            System.out.println("[SEL-r] switching temporary pop -> current");
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
        ArrayList<Integer> values = new ArrayList<Integer>();
        if (indexesToSave != null)
            for (Integer integer : indexesToSave) {
                Integer get = oldIndexesToNew.get(integer);
                if (context.getDebugOptions().isRuleSavingOutput()) {
                    System.out.println("[MUT-r-pre] asked to save rule "
                                       + integer + " received new index=" + get);
                }
                values.add(get);
            }

        for (Integer i = 0; i < individuals.size(); i++) {
            // we need to save some rules, represented by id's
            // before mapping
            if (values.contains(i)) {
                if (context.getDebugOptions().isMutationRuleSavingOutput()) {
                    System.out.println("[MUT-r] Rule saving no: " + i);
                }
                continue;
            }
            if (context.getDebugOptions().isMutationRuleSavingOutput())
                System.out.println("[MUT-r] Possibly mutating rule " + i);
            individuals.get(i).mutate(mt);
        }
    }

    public List<Individual> getIndividuals() {
        return individuals;
    }
    Map<Integer, Integer> oldIndexesToNew =
            new HashMap<Integer, Integer>();

    public void select() {
        if (context.getDebugOptions().isEvolutionPhaseOutput())
            System.out.println("[SEL-r]");

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

        debugOutput();
    }

    public SimpleStatistics stats() {
        if (context.getDebugOptions().isEvolutionPhaseOutput())
            System.out.println("[R-STATS] Gathering statistics");
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
        if (context.getDebugOptions().isIndexMappingOutput())
            System.out.println("[SEL] index mapping: " + oldIndexesToNew);
        if (context.getDebugOptions().isRulePopulationAfterSelectionOutput()) {
            outputAllRules();
        }
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
        if (context.getDebugOptions().isRuleSavingOutput())
            System.out.println("[SAVE-R] Rule pop is being asked to save: " + indexesToSave);
        this.indexesToSave = indexesToSave;
    }
    Set<Integer> indexesToSave;

    private void saveTheRulesWhichAreNeededByRulePopulationsElitism() {
        if (context.getDebugOptions().isRuleSavingOutput())
            System.out.println("[SAVE] Going to save rules no: " + indexesToSave);
        if (indexesToSave == null)
            return;
        for (Integer i : indexesToSave) {
            addIndividualToTemporaryPopulationAndUpdateIndex(i);
        }
    }
}
