package core.token;

import core.ga.Individual;
import core.io.dataframe.Row;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author gmatoga
 */
public class TokenCompetition {

    private ArrayList<Set<Individual>> territory;

    public TokenCompetition(int size) {
        territory = new ArrayList<Set<Individual>>(size);
        for (int i = 0; i < size; i++) {
            territory.add(null);
        }
    }

    void onTerritory(Individual individual, Row row) {
        individual.increaseTerritory();
        addToTerritory(row, individual);
    }

    private void addToTerritory(Row row, Individual individual) {
        final int id = row.getId();
        if (territory.get(id) == null)
            territory.set(id, new HashSet<Individual>());

        territory.get(id).add(individual);
    }

    public void giveOutTokens() {
        for (Set<Individual> set : territory) {
            if (set == null)
                continue;
            giveOutToken(set);
        }
    }

    private void giveOutToken(Set<Individual> set) {
        double max = 0, fit;
        Individual best = null;
        for (Individual individual : set) {
            fit = individual.fitness();
            if (fit >= max) {
                max = fit;
                best = individual;
            }
        }
        best.rewardToken();
    }
}
