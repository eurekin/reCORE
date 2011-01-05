package core.copop;

import core.ga.RulePopulation;
import core.ga.ops.ec.ExecutionContext;
import core.stat.SimpleStatistics;

/**
 *
 * @author gmatoga
 */
public class CoPopulations {

    private RulePopulation rp;
    private RuleSetPopulation rsp;

    public CoPopulations(int populationSize, ExecutionContext ec) {
        rp = new RulePopulation(ec);
        rsp = new RuleSetPopulation(populationSize, ec, rp);
        initPopulations();
    }

    private void initPopulations() {
        rp.decode();
        rp.repair();
        rp.evaluate();

        rsp.evaluate();
    }

    public void evolve() {
        rp.evolve();

        rp.decode();
        rp.repair();
        rp.evaluate();

        rsp.evolve();
        rsp.evaluate();
        updateBest();
    }

    public SimpleStatistics ruleStats() {
        return rp.stats();
    }

    public SimpleStatistics ruleSetStats() {
        return rsp.stats();
    }

    public RulePopulation rules() {
        return rp;
    }

    public RuleSetPopulation ruleSets() {
        return rsp;
    }
    PittsIndividual best;

    public PittsIndividual getBest() {
        return best;
    }

    private void updateBest() {
        if (best == null) {
            best = rsp.getBest();
        } else {
            if (best.fitness() < rsp.getBest().fitness())
                best = rsp.getBest();
        }

    }
}
