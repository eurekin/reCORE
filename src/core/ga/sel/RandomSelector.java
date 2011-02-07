package core.ga.sel;

import core.ga.Fitnessable;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author gmatoga
 */
public class RandomSelector<T extends Fitnessable>
        implements FitnessableSelector<T> {

    Random rand;
    int size;

    public RandomSelector(Random rand, int size) {
        this.rand = rand;
        this.size = size;
    }

    public int select() {
        return rand.nextInt(size);
    }

    public static void main(String[] args) {
        ArrayList<Testable> testables = new ArrayList<Testable>();
        for (int i = 0; i < 6; i++) {
            testables.add(new Testable());
        }
        FitnessableSelector<Testable> sel =
                new TournamentSelector<Testable>(
                new Random(), testables, 3);
        for (int i = 0; i < 100000; i++) {
            System.out.println(sel.select());
        }

    }

    public static class Testable implements Fitnessable {

        int id = ++idd;
        static int idd = 0;

        public int getId() {
            return id;
        }

        public double fitness() {
            return (double) id;
        }
    }
}
