package core.ga.ops.ec;

import core.DataSetBundle;
import core.ga.Evaluator;
import core.ga.RuleChromosomeSignature;
import core.ga.RuleDecoderSubractingOneFromClass;
import core.io.dataframe.DataFrame;
import java.util.Random;

/**
 *
 * @author Rekin
 */
public class ExecutionEnv {

    Random rand;
    Evaluator evaluator;
    DataSetBundle bundle;
    FitnessEval fitnessEvaluator;
    RuleDecoderSubractingOneFromClass decoder;
    private double tokenCompetitionWeight = 1.0;
    private boolean ruleSortingEnabled = false;
    private boolean tokenCompetitionEnabled = true;
    private int maxRuleSetLength = 9;
    private int eliteSelectionSize = 1;
    private double mt = 0.01;
    int ruleCount = 200;
    int ruleSetCount = 20;
    private double rsmp = 0.01;
    DebugOptions debugOptions = new DebugOptions();

    public int getRuleCount() {
        return ruleCount;
    }

    public void setRuleCount(int ruleCount) {
        this.ruleCount = ruleCount;
    }

    public int getRuleSetCount() {
        return ruleSetCount;
    }

    public void setRuleSetCount(int ruleSetCount) {
        this.ruleSetCount = ruleSetCount;
    }

    public ExecutionEnv(int size, Random rand, Evaluator evaluator,
            DataSetBundle bundle, RuleDecoderSubractingOneFromClass decoder,
            FitnessEval ruleFitEval) {
        this.ruleCount = size;
        this.rand = rand;
        this.bundle = bundle;
        this.decoder = decoder;
        this.evaluator = evaluator;
        this.fitnessEvaluator = ruleFitEval;
    }

    public void setSize(int size) {
        this.ruleCount = size;
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

    public DataFrame data() {
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
        return ruleCount;
    }

    public void setRulePopSize(int size) {
        this.ruleCount = size;
    }

    public double getMt() {
        return mt;
    }

    public void setMt(double mt) {
        this.mt = mt;
    }

    public double getRsmp() {
        return rsmp;
    }

    public void setRsmp(double rsmp) {
        this.rsmp = rsmp;
    }

    public int maxRuleSetLength() {
        return maxRuleSetLength;
    }

    public void setMaxRuleSetLength(int length) {
        this.maxRuleSetLength = length;
    }

    public void setBundle(DataSetBundle bundle) {
        this.bundle = bundle;
    }

    public boolean isTokenCompetitionEnabled() {
        return tokenCompetitionEnabled;
    }

    public void setTokenCompetitionEnabled(boolean tokenCompetitionEnabled) {
        this.tokenCompetitionEnabled = tokenCompetitionEnabled;
    }

    public int getEliteSelectionSize() {
        return eliteSelectionSize;
    }

    public void setEliteSelectionSize(int eliteSelectionSize) {
        this.eliteSelectionSize = eliteSelectionSize;
    }

    public boolean isRuleSortingEnabled() {
        return ruleSortingEnabled;
    }

    public void setRuleSortingEnabled(boolean ruleSortingEnabled) {
        this.ruleSortingEnabled = ruleSortingEnabled;
    }

    public DebugOptions getDebugOptions() {
        return debugOptions;
    }

    public void setDebugOptions(DebugOptions debugOptions) {
        this.debugOptions = debugOptions;
    }

    public double getTokenCompetitionWeight() {
        return tokenCompetitionWeight;
    }

    public void setTokenCompetitionWeight(double tokenCompetitionWeight) {
        this.tokenCompetitionWeight = tokenCompetitionWeight;
    }
}
