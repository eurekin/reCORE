package core.ga;

import core.BinaryChromosome;
import core.io.dataframe.Row;
import core.io.dataframe.UniformDataFrame;
import core.stat.BinaryConfMtx;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Rekin
 */
public class RulePopulation {

    int size;
    Random rand;
    List<Rule> rules;
    List<BinaryConfMtx> cms;
    Evaluator evaluator;
    List<BinaryChromosome> chromosomes;
    RuleChromosomeSignature signature;
    UniformDataFrame<Integer, Integer> data;
    RuleDecoderSubractingOneFromClass decoder;

    public RulePopulation(int size, RuleChromosomeSignature signature,
            UniformDataFrame<Integer, Integer> data) {
        this.data = data;
        this.size = size;
        this.signature = signature;
        rand = new Random(System.currentTimeMillis());
        rules = new ArrayList<Rule>();
        evaluator = new BinaryEvaluator();
        cms = new ArrayList<BinaryConfMtx>();
        chromosomes = new ArrayList<BinaryChromosome>();
        decoder = new RuleDecoderSubractingOneFromClass(signature, new GrayBinaryDecoderPlusONE());
        init();
    }

    public void repair() {
        for (int ruleId = 0; ruleId < rules.size(); ruleId++) {
            Rule rule = rules.get(ruleId);
            List<Selector> allSelectors = rule.getAllSelectors();
            for (int selId = 0; selId < rule.getAllSelectors().size(); selId++) {
                Selector sel = allSelectors.get(selId);
                Set domain = signature.getAttrDomain().get(selId);
                while (!domain.contains(sel.val)) {
                    Integer startAddr = signature.getGeneAddresses()[selId] + 2;
                    Integer length = signature.getValueCodeSizes()[selId];
                    BinaryChromosome chromosome = chromosomes.get(ruleId);
                    randomize(chromosome, startAddr, length);
                    sel = decoder.decodeSelector(chromosome, selId);
                    allSelectors.set(selId, sel);
                    rule.reset();
                }
            }
        }

    }

    public void evaluate() {
        cms.clear();
        for (Rule rule : rules) {
            BinaryConfMtx cm = new BinaryConfMtx();
            for (Row<Integer, Integer> row : data) {
                evaluator.evaluate(rule, row, cm);
            }
            cms.add(cm);
        }
    }

    private void init() {
        for (int i = 0; i < size; i++) {
            BinaryChromosome chromosome = new BinaryChromosome(signature.getBits());
            randomize(chromosome);
            chromosomes.add(chromosome);
        }
    }

    public void decode() {
        rules.clear();
        for (BinaryChromosome ch : chromosomes) {
            rules.add(decoder.decode(ch));
        }
    }

    public void randomize(BinaryChromosome chromosome) {
        randomize(chromosome, 0, signature.getBits());
    }

    public void randomize(BinaryChromosome chromosome, int start, int length) {
        for (int i = start, end = start + length; i < end; i++) {
            chromosome.set(i, rand.nextBoolean());
        }
    }
}
