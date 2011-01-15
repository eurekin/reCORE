package core.copop;

import core.ga.RulePopulation;
import core.ga.ops.ec.ExecutionEnv;
import core.stat.SimpleStatistics;
import core.vis.RuleASCIIPlotter;

/**
 *
 * @author gmatoga
 */
public class CoPopulations {

    private RulePopulation rp;
    private RuleSetPopulation rsp;
    private boolean debug = false;
    private int i;
    private final ExecutionEnv ec;

    public CoPopulations(int populationSize, ExecutionEnv ec) {
        rp = new RulePopulation(ec);
        rsp = new RuleSetPopulation(populationSize, ec, rp);
        this.ec = ec;
        initPopulations();
    }

    private void initPopulations() {
        rp.decode();
        rp.repair();
        rp.evaluate();

        rsp.evaluate();
    }

    public void evolve() {
        rp.select(); // buduje też indeks do zaktualizowania reguł
        rp.mutate(); // zachowuje elitarność
        rp.decode();  // bezwzględnie aktualizuje (dekoduje) regułę
        // reguła naprawia samą siebie, jeśli naruszono sygnaturę
        rp.evaluate(); // aktualizacja macierzy pomyłek - uaktualnienie oceny

        rsp.evaluate();      // tworzenie wieloklasowej oraz ważonej mac. pom.
        rsp.updateIndexes(); // naprawia wskaźniki do reguł
        rsp.select();        // selekcja elitarnas
        rsp.evolve();  // reset (ruleSet=null;) oraz mutacja (uwzgl. elitarność)
        updateBest();

        // do raports and stuff

        debugOutputIfEnabled();
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
            best = rsp.getBest().copy();
        } else {
            if (best.fitness() < rsp.getBest().fitness())
                best = rsp.getBest().copy();
        }

    }

    double max;

    private void debugOutputIfEnabled() {
        if (!ec.getDebugOptions().isGenerationStatisticsOutput()) {
            return;
        }

        RuleSet ruleSet;
        {
            final SimpleStatistics ruleStats = ruleStats();
            final SimpleStatistics ruleSetStats = ruleSetStats();

            System.out.printf("%4d.  RULE: %s", i + 1, ruleStats);
            System.out.println("   RULESET stats: " + ruleSetStats);

            if (max < ruleSetStats.getMax()) {
                max = ruleSetStats.getMax();
                System.out.println("Congratulations!");
                java.awt.Toolkit.getDefaultToolkit().beep();
                outer:
                for (PittsIndividual ind : ruleSets().getIndividuals()) {
                    final double eval = ind.fitness();
                    if (eval == max) {
                        System.out.println("HERE");
                        System.out.println("Seed = " + ec.rand());
                        System.out.println(ind);
                        ruleSet = ind.getRS();
                        System.out.println(ec.getBundle().getPrinter().print(ruleSet));
                        System.out.println(ind.getCm().getWeighted());

                        RuleASCIIPlotter plotter = ec.getBundle().getPlotter();
                        System.out.println(HORSPC);
                        System.out.println(HEADER);
                        System.out.println(HORSPC);
                        plotter.detailedPlots(ruleSet);
                        System.out.println(HORSPC);

                        break outer;

                    }
                }
            }
            //}
        }
        i++;
    }
    private static String HEADER =
            "|  non-zero class vis    |    class number vis    |     rule number vis    |";
    private static String HORSPC =
            "+------------------------+------------------------+------------------------+";
}
