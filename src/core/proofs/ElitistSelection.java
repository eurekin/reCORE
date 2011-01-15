package core.proofs;

import core.ExecutionContextFactory;
import core.copop.CoPopulations;
import core.copop.PittsIndividual;
import core.ga.Individual;
import core.ga.RulePopulation;
import core.ga.RulePrinter;
import core.ga.ops.ec.ExecutionEnv;
import core.ga.ops.ec.FitnessEval;
import core.ga.ops.ec.FitnessEvaluatorFactory;
import core.vis.RuleASCIIPlotter;
import java.util.Random;

/**
 *
 * @author gmatoga
 */
public class ElitistSelection {

    public static void main(String[] args) {
        FitnessEval fiteval = FitnessEvaluatorFactory.EVAL_FMEASURE;

        ExecutionEnv ec = ExecutionContextFactory.MONK(1, true, 10, fiteval);
        ec.setRand(new Random(101004L));
        ec.setRulePopSize(3);
        ec.setMaxRuleSetLength(2);
        ec.setRsmp(0.02);
        ec.setMt(0.02);
        ec.setTokenCompetitionEnabled(false);
        ec.setEliteSelectionSize(1);
        ec.setRuleSortingEnabled(false);
        ec.getDebugOptions().setAllTrue();
        ec.getDebugOptions().setGenerationStatisticsGathered(false);

        int popSize = 3;
        CoPopulations co = new CoPopulations(popSize, ec);
        RuleASCIIPlotter plotter = ec.getBundle().getPlotter();
        RulePrinter printer = ec.getBundle().getPrinter();

        for (int i = 0; i < 3; i++) {
            if (ec.getDebugOptions().isElitistSelectionSpecificOutput()) {
                System.out.println("");
                System.out.println("Generation: " + i);
                System.out.println("Rules:");
                spitOutPops(co, printer);
            }
            co.evolve();
            if (ec.getDebugOptions().isElitistSelectionSpecificOutput()) {
                spitOutPops(co, printer);
                System.out.printf("%d : %s\n", i, co.ruleSetStats());
            } else {
                System.out.print(co.ruleStats());
            }
        }
    }

    private static void spitOutPops(CoPopulations co, RulePrinter printer) {
        RulePopulation rules = co.rules();
        for (Individual object : rules) {
            System.out.printf("%.3f  ", object.cm().tpr());
            String s = printer.prettyPrint(object.rule());
            System.out.println(s);
        }
        System.out.println("RuleSets");
        for (PittsIndividual rs : co.ruleSets().getIndividuals()) {
            System.out.print("    " + rs.getCm().getWeighted() + "   ");
            System.out.println(rs);
        }
    }
}
