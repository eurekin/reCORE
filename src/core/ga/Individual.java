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

    Rule rule;
    Random rand;
    BinaryConfMtx cm;
    BinaryChromosome chromosome;
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
            evaluator.evaluate(rule, row, cm);
        }
    }

    public final void randomize() {
        randomize(0, signature.getBits());
    }

    public void decode(RuleDecoderSubractingOneFromClass decoder) {
        rule = decoder.decode(chromosome);
    }

    public void randomize(int start, int length) {
        for (int i = start, end = start + length; i < end; i++) {
            chromosome.set(i, rand.nextBoolean());
        }
    }

    public void repair() {
        List<Selector> allSelectors = rule.getAllSelectors();
        for (int selId = 0; selId < rule.getAllSelectors().size(); selId++) {
            Selector sel = allSelectors.get(selId);
            Set domain = signature.getAttrDomain().get(selId);
            while (!domain.contains(sel.val)) {
                Integer startAddr = signature.getGeneAddresses()[selId] + 2;
                Integer length = signature.getValueCodeSizes()[selId];
                randomize(startAddr, length);
                sel = decoder.decodeSelector(chromosome, selId);
                allSelectors.set(selId, sel);
                rule.reset();
            }
        }
    }
}
