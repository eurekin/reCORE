package core.evo;

import core.copop.RuleSet;
import core.ga.Evaluator;
import core.ga.Fitnessable;
import core.ga.Individual;
import core.ga.Mutable;
import core.ga.Mutator;
import core.ga.Rule;
import core.ga.RuleDecoder;
import core.ga.ops.ec.ExecutionEnv;
import core.io.dataframe.Row;
import core.io.dataframe.DataFrame;
import core.stat.ConfMtx;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * Mutacja.
 *
 * Mam dwie możliwości w sprawie mutacji tej populacji:
 * 1. Odzwierciedlić możliwie najwierniej to co się dzieje w koewolucji,
 * 2. Wykorzystać podejście bardziej adekwatne dla ewolucji.
 *
 * Wady i zalety:
 *
 * (1)
 *
 *  + Pozwoliłoby użyć sporej części aktualnie istniejącego kodu
 *  - Nie do końca standardowe podejście.
 *
 * (2)
 *
 *  + Bardziej adekwatne dla ewolucji,
 *  - Koncepcyjnie bardziej złożone. Należałoby dokładnie przemyśleć implementację.
 *
 * Wersja (2):
 *
 * Długość osobnika w przybliżeniu jest równa sumarycznej długości osobników
 * kodujących reguły składowe. Dochodzi tylko jedno pole dodatkowe - kodujące
 * klasę domyślną.
 *
 * Mechanizm mutacji traktuje tego osobnika jednolicie - tak jakby był
 * jednorodnym ciągiem genów. Dzieki temu można użyć istniejącej implementacji
 * mutacji, bez potrzeby dopasowywania.
 *
 * Każdy gen jednak delegowany byłby do odpowiadającej reguły i tam mutowany.
 * Sytuację może uprościć fakt, że reguły są kodowane zawsze ze stałą długością.
 *
 * To załatwia sprawę mutacji na poziomie reguł. Do rozpatrzenia zostaje
 * mutacja na poziomie wektora reguł. Losowa zmiana liczby reguł jest konieczna,
 * aby zachodziło przeszukiwanie na poziomie wektora reguł.
 *
 * @author gmatoga
 */
public final class EvoIndividual implements Mutable, Fitnessable {

    private ConfMtx cm;
    private EvolutionPopulation population;
    private ExecutionEnv ctx;
    private Mutator mutator;
    List<Individual> rules;
    private int defaultClazz;
    private Random rand;
    private int clazzMax;
    private int maxLength;

    public EvoIndividual(EvolutionPopulation population, ExecutionEnv ctx) {
        this.population = population;
        this.ctx = ctx;
        this.mutator = new Mutator(ctx.rand());
        this.rand = ctx.rand();
        this.clazzMax = ctx.signature().getClassDomain().size();
        this.maxLength = ctx.maxRuleSetLength();
        this.rules = new ArrayList<Individual>();
        initiateRandomRules();
        decode();
    }

    public EvoIndividual copy() {
        // must belong to the SAME population - not copy
        EvoIndividual copy = new EvoIndividual(population, ctx);
        // casual fields, could encapsulate them, but who cares
        copy.mutator = mutator;
        copy.defaultClazz = defaultClazz;
        copy.cm = cm;
        copy.maxLength = maxLength;
        // special care with rule individuals
        ArrayList<Individual> copyRules = new ArrayList<Individual>(rules.size());
        for (Individual individual : rules) {
            copyRules.add(individual.copy());
        }
        copy.rules = copyRules;
        return copy;

    }

    public void decode() {
        RuleDecoder decoder = ctx.decoder();
        for (Individual individual : rules) {
            individual.decode(decoder);
            individual.repair();
        }
    }

    void mutate(double mt) {
        mutator.mutate(this, mt);
    }

    public void mutateAt(int i) {
        int genesPerRule = ctx.signature().getBits();
        int indexOfRuleToMutate = i / genesPerRule;
        int indexOfGeneToMutate = i % genesPerRule;
        if (indexOfRuleToMutate < rules.size()) {
            rules.get(indexOfRuleToMutate).mutateAt(indexOfGeneToMutate);
        } else
            throw new RuntimeException(
                    "Illegal mutation state: i=" + i
                    + ", indexOfGeneToMutate=" + indexOfGeneToMutate
                    + ", ruleToMutate=" + indexOfRuleToMutate);
    }

    public int size() {
        return rules.size() * ctx.signature().getBits();
    }

    public ConfMtx getCm() {
        return cm;
    }

    public void evaluate(DataFrame data,
            Evaluator evaluator) {
        cm = new ConfMtx(clazzMax);
        RuleSet rS = getRS();
        for (Row  row : data) {
            evaluator.evaluate(rS, row, cm);
        }
        cm.getCMes();
    }

    /*
     * WARNING: watch out for side effects
     * It returns each object every time
     */
    public RuleSet getRS() {
        ArrayList<Rule> ru = new ArrayList<Rule>();
        for (Individual rule : rules) {
            ru.add(rule.rule());
        }
        return new RuleSet(ru, defaultClazz);
    }
    
    public void addOrRemoveWith(double prob) {
        if (rand.nextDouble() > prob)
            return;
        if (rules.isEmpty()) {
            addRule();
            return;
        }
        if (rules.size() >= maxLength) {
            removeRule();
            return;
        }
        if (rand.nextBoolean())
            removeRule();
        else
            addRule();
    }

    private void addRule() {
        rules.add(
                new Individual(ctx.signature(), rand, ctx.decoder(),
                mutator, ctx.fitnessEvaluator(), ctx));
    }

    private void removeRule() {
        int id = rand.nextInt(rules.size());
        rules.remove(id);
    }

    public void mutateClass(double mprob) {
        if (rand.nextDouble() < mprob)
            defaultClazz = rand.nextInt(clazzMax);
    }

    public double fitness() {
        return ctx.fitnessEvaluator().eval(getCm().getWeighted());
    }

    private void initiateRandomRules() {
        int howMany = rand.nextInt(maxLength);
        for (int i = 0; i < howMany; i++) {
            addRule();
        }
    }
}
