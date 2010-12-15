package core.copop;

import core.ga.Individual;
import core.ga.Mutable;
import core.ga.Mutator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 *
 * Brakuje mutacji, która
 * 1. usuwa regułę
 * 2. dodaje regułę, ale za to jest
 * 3. zmieniająca regułę.
 *
 * Aczkolwiek, nie do końca optymalna.
 *
 * @author Rekin
 */
public class PittsIndividual implements Mutable {

    Random rand;
    Mutator mutator;
    List<Individual> rules;
    HashSet<Integer> indexes;
    LinkedList<Integer> list;
    private int size;

    public RuleSet getRS() {
        return new RuleSet(null, size);
    }

    public void mutate(double mt) {
        mutator.mutate(this, mt);
    }

    public void mutateAt(int i) {
        Integer el = list.get(i);
        indexes.remove(el);
        while (!indexes.contains(el))
            el = rand.nextInt(size);
        list.set(i, el);
        indexes.add(el);
    }

    public int size() {
        return indexes.size();
    }
}
