package core.ga;

import core.BinaryChromosome;
import core.ga.ops.ec.ExecutionEnv;
import core.ga.ops.ec.FitnessEval;
import core.io.dataframe.Row;
import core.io.dataframe.DataFrame;
import core.io.repr.col.Domain;
import core.io.repr.col.FloatDomain;
import core.io.repr.col.IntegerDomain;
import core.stat.BinaryConfMtx;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Rekin
 */
public class Individual implements Mutable, Fitnessable {

    private Rule rule;
    private Random rand;
    private double fitness;
    private Mutator mutator;
    private BinaryConfMtx cm;
    private BinaryChromosome chromosome;
    private FitnessEval fitnessEvaluator;
    private RuleChromosomeSignature signature;
    private RuleDecoder decoder;
    private ExecutionEnv ctx;

    public Individual() {
    }

    public Individual(RuleChromosomeSignature signature, Random rand,
            RuleDecoder decoder, Mutator mutator,
            FitnessEval fitnessEvaluator, ExecutionEnv ctx) {
        this.signature = signature;
        this.rand = rand;
        this.decoder = decoder;
        this.mutator = mutator;
        this.fitnessEvaluator = fitnessEvaluator;
        this.ctx = ctx;
        chromosome = new BinaryChromosome(signature.getBits());
        randomize();
    }

    public Individual copy() {
        Individual c = new Individual();
        c.ctx = ctx;
        c.tokens = 0;
        c.rand = rand;
        c.territory = 0;
        c.decoder = decoder;
        c.mutator = mutator;
        c.rule = rule.copy();
        c.signature = signature;
        c.chromosome = chromosome.copy();
        c.fitnessEvaluator = fitnessEvaluator;
        return c;
    }

    public void evaluate(DataFrame data,
            Evaluator evaluator) {
        cm = new BinaryConfMtx();
        for (Row row : data) {
            evaluator.evaluate(rule(), row, cm);
        }
        fitness = fitnessEvaluator.eval(cm);
    }

    public void decode(RuleDecoder decoder) {
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
            Domain domain = signature.getAttrDomain().get(selId);
            if (domain instanceof FloatDomain)
                continue;
            while (!domain.contains(sel.val)) {
                Integer startAddr = signature.getGeneAddresses()[selId] + 2;
                Integer length = signature.getValueCodeSizes()[selId];
                randomize(startAddr, length);
                sel = decoder.decodeSelector(chromosome(), selId);
                allSelectors.set(selId, sel);
                rule.reset();
            }
        }
        repairClass();
    }

    private void repairClass() {
        IntegerDomain classDomain = signature.getClassDomain();
        while (!classDomain.contains(rule.clazz)) {
            randomize(signature.getClazzAddress(), signature.getClazzSize());
            rule.clazz = decoder.decodeClass(chromosome());
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

    public void penalizeToken() {
        //        System.out.printf("Penalizing fit=%.3f tok=%d ter=%d", fitness, tokens, territory);
        final double penalty = (double) (tokens) / (double) territory;
        final double w = ctx.getTokenCompetitionWeight();
        fitness = fitness * (1 - w) + penalty * (w);
        //        System.out.printf(" newfit=%.3f\n", fitness);
    }

    public double fitness() {
        return fitness;
    }
}
