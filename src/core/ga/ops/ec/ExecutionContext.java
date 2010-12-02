package core.ga.ops.ec;

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
    RuleChromosomeSignature signature;
    UniformDataFrame<Integer, Integer> data;
    RuleDecoderSubractingOneFromClass decoder;

    public ExecutionContext(int size, Random rand, Evaluator evaluator,
            RuleChromosomeSignature signature,
            UniformDataFrame<Integer, Integer> data,
            RuleDecoderSubractingOneFromClass decoder) {
        this.size = size;
        this.rand = rand;
        this.evaluator = evaluator;
        this.signature = signature;
        this.data = data;
        this.decoder = decoder;
    }

    public UniformDataFrame<Integer, Integer> getData() {
        return data;
    }

    public RuleDecoderSubractingOneFromClass getDecoder() {
        return decoder;
    }

    public Evaluator getEvaluator() {
        return evaluator;
    }

    public Random getRand() {
        return rand;
    }

    public RuleChromosomeSignature getSignature() {
        return signature;
    }

    public int getSize() {
        return size;
    }
}
