package core.ga.sel;

import core.ga.Fitnessable;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeMap;

/**
 *
 * @author gmatoga
 */
public class RouletteSelector<T extends Fitnessable>
        implements FitnessableSelector<T> {

    Random rand;
    TreeMap<Double, Integer> m;

    public RouletteSelector(Random rand, ArrayList<T> individuals) {
        this.rand = rand;
        m = new TreeMap<Double, Integer>();
        double CDF[] = new double[individuals.size()];
        double acc = 0;
        double sumfit = 0;

        for (Fitnessable el : individuals)
            sumfit += el.fitness();
        for (int i = 0; i < CDF.length; i++) {
            acc += individuals.get(i).fitness();
            CDF[i] = acc / sumfit;
        }
        for (int i = 0; i < CDF.length; i++)
            m.put(CDF[i], i);
    }

    public int select() {
        return m.ceilingEntry(rand.nextDouble()).getValue();
    }
}
