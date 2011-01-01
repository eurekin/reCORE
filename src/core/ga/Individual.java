package core.ga;

import core.BinaryChromosome;
import core.ga.ops.ec.FitnessEval;
import core.ga.ops.ec.FitnessEvaluatorFactory;
import core.io.dataframe.Row;
import core.io.dataframe.UniformDataFrame;
import core.stat.BinaryConfMtx;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Rekin
 */
public class Individual implements Mutable {

    private Rule rule;
    private Random rand;
    private double fitness;
    private Mutator mutator;
    private BinaryConfMtx cm;
    private BinaryChromosome chromosome;
    private FitnessEval fitnessEvaluator = FitnessEvaluatorFactory.EVAL_FMEASURE;
    private RuleChromosomeSignature signature;
    private RuleDecoderSubractingOneFromClass decoder;

    public Individual() {
    }

    public Individual(RuleChromosomeSignature signature, Random rand,
            RuleDecoderSubractingOneFromClass decoder, Mutator mutator) {
        this.signature = signature;
        this.rand = rand;
        this.decoder = decoder;
        this.mutator = mutator;
        chromosome = new BinaryChromosome(signature.getBits());
        randomize();
    }

    public Individual copy() {
        Individual c = new Individual();
        c.chromosome = chromosome.copy();
        c.signature = signature;
        c.rand = rand;
        c.decoder = decoder;
        c.mutator = mutator;
        c.tokens = 0;
        c.territory = 0;
        return c;
    }

    public void evaluate(UniformDataFrame<Integer, Integer> data,
            Evaluator evaluator) {
        cm = new BinaryConfMtx();
        for (Row<Integer, Integer> row : data)
            evaluator.evaluate(rule(), row, cm);
        fitness = fitnessEvaluator.eval(cm);
    }

    public void decode(RuleDecoderSubractingOneFromClass decoder) {
        rule = decoder.decode(chromosome);
        rule.setIndividual(this);
    }

    public final void randomize() {
        randomize(0, signature.getBits());
    }

    private void randomize(int start, int length) {
        for (int i = start, end = start + length; i < end; i++) {
            chromosome().set(i, rand.nextBoolean());
        }
    }

    public void repair() {
        List<Selector> allSelectors = rule.getAllSelectors();
        for (int selId = 0; selId < allSelectors.size(); selId++) {
            Selector sel = allSelectors.get(selId);
            Set domain = signature.getAttrDomain().get(selId);
            while (!domain.contains(sel.val)) {
                Integer startAddr = signature.getGeneAddresses()[selId] + 2;
                Integer length = signature.getValueCodeSizes()[selId];
                randomize(startAddr, length);
                sel = decoder.decodeSelector(chromosome(), selId);
                allSelectors.set(selId, sel);
                rule.reset();
            }
        }
    }

    public Rule rule() {
        return rule;
    }

    public BinaryConfMtx cm() {
        return cm;
    }

    public BinaryChromosome chromosome() {
        return chromosome;
    }

    public void mutateAt(int i) {
        chromosome.set(i, !chromosome.get(i));
    }

    public int size() {
        return signature.getBits();
    }

    public void mutate(double mt) {
        mutator.mutate(this, mt);
    }

    // needed only for benchmark
    public void mutateInter(double mt) {
        mutator.mutateInter(this, mt);
    }
    
    // token competition
    private int territory = 0;
    private int tokens = 0;

    public void increaseTerritory() {
        territory++;
    }

    public void rewardToken() {
        tokens++;
    }

    double fitness() {
        return fitness;
    }
}
