package core.ga;

import core.BinaryChromosome;
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
public class Individual {

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

    void mutate(double mt) {
        for (int i = 0; i < signature.getBits(); i++)
            if (rand.nextDouble() <= mt)
                chromosome.set(i, !chromosome.get(i));
    }

    public int nextBinomial(int trials, double probability) {
        if (trials * probability < 5.0
                || trials * (1.0 - probability) < 5.0) {
            // Not enough trials for a normal approximation.

            // Special case of probablity = 1/2.
            // This might take less time than using nextDouble(), I
            // haven't checked.
            if (probability == 0.5) {
                int successes = 0;
                for (int count = 0; count < trials; count++) {
                    successes += rand.nextBoolean() ? 1 : 0;
                }
                return successes;
            } else {
                int successes = 0;
                for (int count = 0; count < trials; count++) {
                    successes += rand.nextDouble() < probability ? 1 : 0;
                }
                return successes;
            }
        } else {
            // We can use a normal approximation
            return (int) Math.floor(
                    Math.sqrt(trials * probability * (1.0 - probability))
                    * rand.nextGaussian() + 0.5 + probability * trials);

        }
    }
}
