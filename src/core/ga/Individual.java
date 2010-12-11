package core.ga;

import core.BinaryChromosome;
import core.ExecutionContextFactory;
import core.binomial.InvCDFRandGenerator;
import core.ga.ops.ec.ExecutionContext;
import core.ga.ops.ec.FitnessEvaluatorFactory;
import core.io.dataframe.Row;
import core.io.dataframe.UniformDataFrame;
import core.stat.BinaryConfMtx;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Rekin
 */
public class Individual {

    public static void testCorrectness() {
        int times = 100;
        ExecutionContext c = ExecutionContextFactory.MONK(3, true, 100, FitnessEvaluatorFactory.EVAL_FMEASURE);
        Individual r = new Individual(c.signature(), c.rand(), c.decoder());
        for (int i = 0; i < times; i++) {
            System.out.println(r.chromosome);
            //r.binomialMutateRepeat(0.01);
            r.mutate(0.9);
        }
    }
    Random rand;
    private Rule rule;
    private BinaryConfMtx cm;
    private BinaryChromosome chromosome;
    RuleChromosomeSignature signature;
    RuleDecoderSubractingOneFromClass decoder;

    public Individual(RuleChromosomeSignature signature, Random rand,
            RuleDecoderSubractingOneFromClass decoder) {
        this.signature = signature;
        this.rand = rand;
        this.decoder = decoder;
        chromosome = new BinaryChromosome(signature.getBits());
        randomize();
    }


    private Individual() {
        
    }
    public Individual copy() {
        Individual c = new Individual();

        c.chromosome = chromosome.copy();
        c.signature = signature;
        c.rand = rand;
        c.decoder = decoder;
        return c;
    }

    public void evaluate(UniformDataFrame<Integer, Integer> data,
            Evaluator evaluator) {
        cm = new BinaryConfMtx();
        for (Row<Integer, Integer> row : data) {
            evaluator.evaluate(rule(), row, cm);
        }
    }

    public void decode(RuleDecoderSubractingOneFromClass decoder) {
        rule = decoder.decode(chromosome);
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

    /**
     * @return the rule
     */
    public Rule rule() {
        return rule;
    }

    /**
     * @return the cm
     */
    public BinaryConfMtx cm() {
        return cm;
    }

    public double fitness() {
        return cm.fMeasure();
    }

    /**
     * @return the chromosome
     */
    public BinaryChromosome chromosome() {
        return chromosome;
    }

    // sophisticated mutation
    public void mutate(double mt) {
        binomialMutateRepeat(mt);
    }

    private void mutateInter(double mt) {
        for (int i = 0; i < signature.getBits(); i++)
            if (rand.nextDouble() <= mt)
                chromosome.set(i, !chromosome.get(i));
    }
    private static InvCDFRandGenerator bin;

    void binomialMutateRepeat(double mt) {
        updateBin(mt);

        Set<Integer> previous = new HashSet<Integer>();
        int bits = bin.nextBinomial();
        Integer rm;
        final int len = signature.getBits();
        while (bits > 0) {
            rm = rand.nextInt(len);
            if (previous.contains(rm)) continue;

            chromosome.set(rm, !chromosome.get(rm));
            previous.add(rm);
            bits--;
        }
    }

    private void updateBin(double mt) {
        if (bin == null)
            bin = new InvCDFRandGenerator(signature.getBits(), mt, rand);
        if (bin.getP() != mt)
            bin = new InvCDFRandGenerator(signature.getBits(), mt, rand);
    }

    public static void main(String[] args) {
//        testCorrectness();
//        testSpeed();
//        testSpeed();
    }

    public static void testSpeed() {
        System.out.println("Testing bit by bit mutation");
        int tests = 100000;
        int reps = 5;

        ExecutionContext c = ExecutionContextFactory.MONK(3, true, 100, FitnessEvaluatorFactory.EVAL_FMEASURE);
        for (int j = 0; j < reps; j++) {
            Individual r = new Individual(c.signature(), c.rand(), c.decoder());
            long start = System.currentTimeMillis();
            for (int i = 0; i < tests; i++) {
                r.mutate(0.5d);
            }

            long stop = System.currentTimeMillis();
            long durationMs = stop - start;
            System.out.println("Duration of " + tests + " mutations: (ms) " + durationMs);
        }

        System.out.println("Binomial randomized without replacement with repeat");
        int nureps = tests;
        int steps = 10;
        for (int j = 0; j <= steps; j++) {
            double scaled = (double) j / (double) steps;
            Individual r = new Individual(c.signature(), c.rand(), c.decoder());
            long start = System.currentTimeMillis();
            for (int i = 0; i < nureps; i++) {
                r.binomialMutateRepeat(scaled);
            }

            long stop = System.currentTimeMillis();

            long durationMs = stop - start;
            System.out.println("Duration of " + tests + "(for MP=" + scaled + ") mutations: (ms) " + durationMs);


        }

    }
}
