package core.ga;

import core.ga.ops.ec.ExecutionContext;
import java.util.List;

/**
 *
 * @author Rekin
 */
public class RulePopulation2 {

    List<Individual> individuals;
    ExecutionContext context;

    public RulePopulation2(List<Individual> individuals, ExecutionContext context) {
        this.individuals = individuals;
        this.context = context;
    }


}
