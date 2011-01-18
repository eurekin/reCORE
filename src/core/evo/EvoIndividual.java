package core.evo;

import core.ga.Mutable;

/**
 *
 * @author gmatoga
 */
class EvoIndividual implements Mutable {
    private EvolutionPopulation population;

    public EvoIndividual(EvolutionPopulation population) {
        this.population = population;
    }

    public EvoIndividual copy() {
        return new EvoIndividual(population);
    }

    void mutate() {
    }

    public void mutateAt(int i) {
    }

    public int size() {
    }
}
