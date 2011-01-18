package core.copop;

import core.ga.Evaluator;
import core.ga.Individual;
import core.ga.Mutable;
import core.ga.Mutator;
import core.ga.Rule;
import core.ga.RulePopulation;
import core.ga.ops.ec.ExecutionEnv;
import core.io.dataframe.Row;
import core.io.dataframe.UniformDataFrame;
import core.stat.BinaryConfMtx;
import core.stat.ConfMtx;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * Brakuje mutacji, która
 * 1. usuwa regułę
 * 2. dodaje regułę, ale za to jest
 * 3. zmieniająca regułę.
 *
 * Aczkolwiek, nie do końca optymalna.
 *
 * @author Rekin
 */
public class PittsIndividual implements Mutable {

    Random rand;
    private int ruleNo;
    private int clazzMax;
    private int maxLength;
    Mutator mutator;
    RulePopulation rpop;
    ArrayList<Integer> list = new ArrayList<Integer>();
    HashSet<Integer> indexes = new HashSet<Integer>();
    private int clazz;
    private RuleSet ruleSet;
    private ConfMtx cm;
    private ExecutionEnv ctx;

    PittsIndividual(ExecutionEnv ctx, RulePopulation rpop) {
        this(ctx.rand(), ctx.maxRuleSetLength(), ctx.signature().getClassDomain().size(), rpop);
        this.ctx = ctx;
    }

    private PittsIndividual(Random rand,
            int maxSize, int clazzMax, RulePopulation rpop) {
        this.rand = rand;
        this.rpop = rpop;
        this.clazzMax = clazzMax;
        this.maxLength = maxSize;
        this.mutator = new Mutator(rand);

        this.ruleNo = rpop.getIndividuals().size();
        int ran = rand.nextInt(maxSize);
        while ((ran--) > 0)
            addInt();
    }

    private PittsIndividual() {
    }

    public PittsIndividual copy() {
        PittsIndividual copy = new PittsIndividual();
        copy.rand = rand;
        copy.rpop = rpop;
        copy.clazzMax = clazzMax;
        copy.maxLength = maxLength;
        copy.mutator = mutator;
        copy.ruleNo = ruleNo;
        copy.clazz = clazz;
        copy.ruleSet = ruleSet.copy();
        copy.cm = cm;
        copy.ctx = ctx;
        copy.list = new ArrayList<Integer>(list);
        copy.indexes = new HashSet<Integer>(indexes);
        return copy;
    }

    public ConfMtx getCm() {
        return cm;
    }

    public void evaluate(UniformDataFrame<Integer, Integer> data,
            Evaluator evaluator) {
        cm = new ConfMtx(clazzMax);
        for (Row<Integer, Integer> row : data) {
            evaluator.evaluate(getRS(), row, cm);
        }
        cm.getCMes();
    }

    public RuleSet getRS() {
        if (ruleSet == null) {

            List<Rule> rs = new ArrayList<Rule>();
            final List<Individual> inds = rpop.getIndividuals();
            for (Integer i : list) {
                rs.add(inds.get(i).rule());
            }
            sortRules(rs);
            ruleSet = new RuleSet(rs, clazz);
        }
        return ruleSet;
    }

    private void sortRules(List<Rule> rs) {
        if (!ctx.isRuleSortingEnabled())
            return;

        Collections.sort(rs, ruleComparator);
    }
    final static Comparator<Rule> ruleComparator = new Comparator<Rule>() {

        public int compare(Rule o1, Rule o2) {
            BinaryConfMtx cm1 = o1.individual().cm();
            BinaryConfMtx cm2 = o2.individual().cm();
            return Double.compare(cm1.fn + cm1.fp, cm2.fn + cm2.fp);
        }
    };

    public void addOrRemoveWith(double prob) {
        if (rand.nextDouble() > prob)
            return;
        if (indexes.isEmpty()) {
            addInt();
            return;
        }
        if (indexes.size() == maxLength) {
            removeInt();
            return;
        }
        if (rand.nextBoolean())
            removeInt();
        else
            addInt();
    }

    private void addInt() {
        Integer r = getNewRandomUnique();
        indexes.add(r);
        list.add(r);
    }

    private void removeInt() {
        int id = rand.nextInt(list.size());
        Integer toRemove = list.remove(id);
        indexes.remove(toRemove);
    }

    private Integer getNewRandomUnique() {
        int el = rand.nextInt(ruleNo);
        while (indexes.contains(el))
            el = rand.nextInt(ruleNo);
        return el;
    }

    public void mutateClass(double mprob) {
        if (rand.nextDouble() < mprob)
            clazz = rand.nextInt(clazzMax);
    }

    public void reset() {
        ruleSet = null;
    }

    @Override
    public String toString() {
        return indexes.toString() + " " + list.toString() + " " + clazz;
    }

    void mutate(double mprob) {
        mutator.mutate(this, mprob);
    }

    public void mutateAt(int i) {
        Integer r = getNewRandomUnique();
        Integer toRemove = list.get(i);
        indexes.remove(toRemove);
        indexes.add(r);
        list.set(i, r);
    }

    public int size() {
        return indexes.size();
    }

    public double fitness() {
        return ctx.fitnessEvaluator().eval(getCm().getWeighted());
    }

    public void updateIndexes(boolean elitist) {
        String beforeString = toString();
        if (ctx.getDebugOptions().isUpdatingIndexesOutput())
            System.out.println("[IDXup] Before: " + beforeString);
        for (int i = 0; i < list.size(); i++) {
            Integer candidate = rpop.getNewIndexesFor(list.get(i));
            if (ctx.getDebugOptions().isUpdatingIndexesOutput())
                System.out.println("[IDXup] candidate = " + candidate);
            if (elitist || !indexes.contains(candidate))
                list.set(i, candidate);
        }
        int oldsize = indexes.size();

        indexes.clear();
        indexes.addAll(list);

        String afterString = toString();
        // check invariants
        if (indexes.size() != oldsize) {
            System.out.println("Corrupt individual");
            System.out.println("beforeString = " + beforeString);
            System.out.println("afterString = " + afterString);
            throw new RuntimeException(
                    "Possible PittsIndividual corruption: "
                    + " oldSize=" + oldsize
                    + ", newSize=" + indexes.size()
                    + ", listSize=" + list.size());
        }
        if (ctx.getDebugOptions().isUpdatingIndexesOutput())
            System.out.println("[IDXup] After: " + afterString);

    }
}
