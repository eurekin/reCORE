package core.copop;

import core.ga.RulePopulation;
import core.ga.ops.ec.ExecutionContext;
import core.ga.ops.ec.FitnessEval;
import core.stat.SimpleStatistics;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gmatoga
 */
public class RuleSetPopulation {

    private List<PittsIndividual> individuals;
    private ExecutionContext context;

    public RuleSetPopulation(int popsize, ExecutionContext ctx, RulePopulation rpop) {
        this.context = ctx;
        individuals = new ArrayList<PittsIndividual>();
        for (int i = 0; i < popsize; i++) {
            individuals.add(new PittsIndividual(ctx, rpop));
        }
    }

    public void mutate() {
        final double mprob = context.getRsmp();
        for (PittsIndividual i : getIndividuals()) {
            i.addOrRemoveWith(mprob);
            i.mutateClass();
        }
    }

    public void evolve() {
        reset();
        mutate();
    }

    private void reset() {
        for (PittsIndividual pi : getIndividuals()) {
            pi.reset();
        }
    }

    public void evaluate() {
        for (PittsIndividual i : getIndividuals()) {
            i.evaluate(context.data(), context.evaluator());
        }
    }

    public SimpleStatistics stats() {
        SimpleStatistics ss = new SimpleStatistics();
        FitnessEval fevl = context.fitnessEvaluator();
        for (PittsIndividual i : getIndividuals()) {
            ss.addValue(fevl.eval(i.getCm().getWeighted()));
        }
        return ss;
    }

    public List<PittsIndividual> getIndividuals() {
        return individuals;
    }
}
