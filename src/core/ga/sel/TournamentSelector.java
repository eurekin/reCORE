package core.ga.sel;

import core.ga.Fitnessable;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author gmatoga
 */
public class TournamentSelector<T extends Fitnessable>
        implements FitnessableSelector<T> {

    int tourneeSize = 2;
    RandomSelector<T> rs;
    ArrayList<T> pool;

    public TournamentSelector(Random rand, ArrayList<T> pool,
            int tourneeSize) {
        if (tourneeSize < 2)
            throw new IllegalArgumentException(
                    "Tournament size must be at least 2, got "
                    + tourneeSize);
        this.pool = pool;
        this.tourneeSize = tourneeSize;
        rs = new RandomSelector<T>(rand, pool.size());
    }

    public int select() {
        int best = rs.select();
        for (int i = 0; i < tourneeSize - 1; i++) {
            int selected = rs.select();
            if (pool.get(selected).fitness() > pool.get(best).fitness())
                best = selected;
        }
        return best;
    }
}
