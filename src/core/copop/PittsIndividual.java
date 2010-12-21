package core.copop;

import core.ga.Evaluator;
import core.ga.Individual;
import core.ga.Mutator;
import core.ga.Rule;
import core.ga.RulePopulation;
import core.ga.ops.ec.ExecutionContext;
import core.io.dataframe.Row;
import core.io.dataframe.UniformDataFrame;
import core.stat.ConfMtx;
import java.util.ArrayList;
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
public class PittsIndividual {

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

    PittsIndividual(ExecutionContext ctx, RulePopulation rpop) {
        this(ctx.rand(), ctx.maxRuleSetLength(), ctx.signature().getClassDomain().size(), rpop);
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
            ruleSet = new RuleSet(rs, clazz);
        }
        return ruleSet;
    }

    public void addOrRemoveWith(double prob) {
        if (rand.nextDouble() < prob)
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

    public void mutateClass() {
        clazz = rand.nextInt(clazzMax);
    }

    public void reset() {
        ruleSet = null;
    }

    @Override
    public String toString() {
        return indexes.toString() + " " + list.toString() + " " + clazz;
    }
}