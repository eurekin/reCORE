package core.ga.sel;

import core.ga.Fitnessable;

/**
 *
 * @author gmatoga
 */
public interface FitnessableSelector<T extends Fitnessable> {

    public int select();
}
