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
    FitnessEval ruleFitEval;
    RuleDecoderSubractingOneFromClass decoder;

    public ExecutionContext(int size, Random rand, Evaluator evaluator,
            DataSetBundle bundle, RuleDecoderSubractingOneFromClass decoder,
            FitnessEval ruleFitEval) {
        this.size = size;
        this.rand = rand;
        this.bundle = bundle;
        this.decoder = decoder;
        this.evaluator = evaluator;
        this.ruleFitEval = ruleFitEval;
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
    private double mt = 0.00001;

    public double getMt() {
        return mt;
    }

    public void setMt(double mt) {
        this.mt = mt;
    }
}
