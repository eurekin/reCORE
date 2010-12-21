package core.ga.ops.ec;

import core.DataSetBundle;
import core.ga.Evaluator;
import core.ga.RuleChromosomeSignature;
import core.ga.RuleDecoderSubractingOneFromClass;
import core.io.dataframe.UniformDataFrame;
import java.util.Random;

/**
 *
 * @author Rekin
 */
public class ExecutionContext {

    int size;
    Random rand;
    Evaluator evaluator;
    DataSetBundle bundle;
    FitnessEval fitnessEvaluator;
    RuleDecoderSubractingOneFromClass decoder;

    public ExecutionContext(int size, Random rand, Evaluator evaluator,
            DataSetBundle bundle, RuleDecoderSubractingOneFromClass decoder,
            FitnessEval ruleFitEval) {
        this.size = size;
        this.rand = rand;
        this.bundle = bundle;
        this.decoder = decoder;
        this.evaluator = evaluator;
        this.fitnessEvaluator = ruleFitEval;
    }

    public FitnessEval fitnessEvaluator() {
        return fitnessEvaluator;
    }

    public DataSetBundle getBundle() {
        return bundle;
    }

    public UniformDataFrame<Integer, Integer> data() {
        return bundle.getData();
    }

    public RuleDecoderSubractingOneFromClass decoder() {
        return decoder;
    }

    public Evaluator evaluator() {
        return evaluator;
    }

    public Random rand() {
        return rand;
    }

    public RuleChromosomeSignature signature() {
        return bundle.getSignature();
    }

    public int size() {
        return size;
    }
    private double mt = 0.01;  // TODO: make an attribute out of it

    public double getMt() {
        return mt;
    }

    public void setMt(double mt) {
        this.mt = mt;
    }

    private double rsmp = 0.1;

    public double getRsmp() {
        return rsmp;
    }

    public void setRsmp(double rsmp) {
        this.rsmp = rsmp;
    }
    
    private int maxRuleSetLength = 10;

    public int maxRuleSetLength() {
        return maxRuleSetLength;
    }

    public void setMaxRuleSetLength(int length) {
        this.maxRuleSetLength = length;
    }
}
