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
public class ExecutionEnv {

    int size;
    Random rand;
    Evaluator evaluator;
    DataSetBundle bundle;
    FitnessEval fitnessEvaluator;
    RuleDecoderSubractingOneFromClass decoder;

    public ExecutionEnv(int size, Random rand, Evaluator evaluator,
            DataSetBundle bundle, RuleDecoderSubractingOneFromClass decoder,
            FitnessEval ruleFitEval) {
        this.size = size;
        this.rand = rand;
        this.bundle = bundle;
        this.decoder = decoder;
        this.evaluator = evaluator;
        this.fitnessEvaluator = ruleFitEval;
    }

    public void setRand(Random rand) {
        this.rand = rand;
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

    public void setRulePopSize(int size) {
        this.size = size;
    }
    private double mt = 0.01;

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

    public void setBundle(DataSetBundle bundle) {
        this.bundle = bundle;
    }
    private boolean tokenCompetitionEnabled = true;

    public boolean isTokenCompetitionEnabled() {
        return tokenCompetitionEnabled;
    }

    public void setTokenCompetitionEnabled(boolean tokenCompetitionEnabled) {
        this.tokenCompetitionEnabled = tokenCompetitionEnabled;
    }
    private int eliteSelectionSize;

    public int getEliteSelectionSize() {
        return eliteSelectionSize;
    }

    public void setEliteSelectionSize(int eliteSelectionSize) {
        this.eliteSelectionSize = eliteSelectionSize;
    }

    boolean ruleSortingEnabled = false;

    public boolean isRuleSortingEnabled() {
        return ruleSortingEnabled;
    }

    public void setRuleSortingEnabled(boolean ruleSortingEnabled) {
        this.ruleSortingEnabled = ruleSortingEnabled;
    }

    DebugOptions debugOptions = new DebugOptions();

    public DebugOptions getDebugOptions() {
        return debugOptions;
    }

    public void setDebugOptions(DebugOptions debugOptions) {
        this.debugOptions = debugOptions;
    }

}
