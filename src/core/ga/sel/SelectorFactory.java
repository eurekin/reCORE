package core.ga.sel;

import core.ga.Fitnessable;
import core.ga.ops.ec.ExecutionEnv;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author gmatoga
 */
public class SelectorFactory {

    public static <T extends Fitnessable> FitnessableSelector<T> give(Random rand,
            int i, ArrayList<T> pop) {
        switch (i) {
            case 0:
                return new RandomSelector<T>(rand, pop.size());
            case 1:
                return new RouletteSelector<T>(rand, pop);
            default:
                return new TournamentSelector<T>(rand, pop, i);
        }
    }

    public static <T extends Fitnessable> FitnessableSelector<T> give(
            ExecutionEnv ctx, ArrayList<T> pop) {
        return give(ctx.rand(), ctx.getSelectionType(), pop);
    }
}
