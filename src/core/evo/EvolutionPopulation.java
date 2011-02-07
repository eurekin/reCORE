package core.evo;

import core.ga.ops.ec.ExecutionEnv;
import core.ga.sel.FitnessableSelector;
import core.ga.sel.SelectorFactory;
import core.stat.SimpleStatistics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author gmatoga
 */
public final class EvolutionPopulation {

    ArrayList<EvoIndividual> individuals;
    private ExecutionEnv context;
    FitnessableSelector<EvoIndividual> selector;

    public EvolutionPopulation(ExecutionEnv context) {
        this.individuals = new ArrayList<EvoIndividual>();
        this.context = context;
        for (int i = 0; i < context.getRuleSetCount(); i++) {
            individuals.add(new EvoIndividual(this, context));
        }
        evaluate();
    }

    public void mutate() {
        int toLeaveAlone = context.getEliteSelectionSize();
        for (EvoIndividual evoIndividual : individuals) {
            if (toLeaveAlone > 0) {
                toLeaveAlone--;
                continue;
            }
            evoIndividual.mutate(context.getRsmp());
        }
    }

    private void updateSelector() {
        selector = SelectorFactory.give(context, individuals);
    }

    public void select() {
        ArrayList<EvoIndividual> next = new ArrayList<EvoIndividual>();
        addEliteOnesFirst(next);
        updateSelector();
        while (next.size() < individuals.size())
            next.add(individuals.get(selector.select()).copy());
        individuals = next;
    }

    public void decode() {
        for (EvoIndividual evoIndividual : individuals) {
            evoIndividual.decode();
        }
    }

    public void evaluate() {
        for (EvoIndividual evoIndividual : individuals) {
            evoIndividual.evaluate(context.data(), context.evaluator());
        }
    }

    public SimpleStatistics stats() {
        SimpleStatistics ss = new SimpleStatistics();
        for (EvoIndividual i : individuals)
            ss.addValue(i.fitness());
        return ss;
    }

    public Iterable<EvoIndividual> getIndividuals() {
        return individuals;
    }

    public void evolve() {
        select();
        mutate();
        decode();
        evaluate();
    }
    EvoIndividual best;

    public EvoIndividual getBest() {
        double max = stats().getMax();
        for (EvoIndividual i : getIndividuals()) {
            if (i.fitness() == max)
                return i;
        }
        return null;
    }

    private void addEliteOnesFirst(ArrayList<EvoIndividual> next) {
        int eliteSelectionSize = context.getEliteSelectionSize();
        Collections.sort(individuals, new Comparator<EvoIndividual>() {

            public int compare(EvoIndividual o1, EvoIndividual o2) {
                return Double.compare(o2.fitness(), o1.fitness());
            }
        });
        for (int i = 0; i < eliteSelectionSize; i++) {
            EvoIndividual get = individuals.get(i);
            next.add(get);
        }
    }
}
