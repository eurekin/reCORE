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

/**
 *
 * @author gmatoga
 */
public class ElitistSelection {

    public static void main(String[] args) {
        FitnessEval fiteval = FitnessEvaluatorFactory.EVAL_FMEASURE;

        ExecutionEnv ec = ExecutionContextFactory.MONK(1, false, 10, fiteval);
        ec.setRulePopSize(10);
        ec.setMaxRuleSetLength(5);
        ec.setRsmp(0.05);
        ec.setMt(0.05);
        ec.setTokenCompetitionEnabled(true);
        ec.setEliteSelectionSize(1);
        ec.setRuleSortingEnabled(false);
        int popSize = 10;
        CoPopulations co = new CoPopulations(popSize, ec);
        RuleASCIIPlotter plotter = ec.getBundle().getPlotter();
        RulePrinter printer = ec.getBundle().getPrinter();

        for (int i = 0; i < 100000; i++) {
//            System.out.println("");
//            System.out.println("Generation: " + i);
//            System.out.println("Rules:");
//            spitOutPops(co, printer);
//
            co.evolve();
            System.out.printf("%d : %s\n", i, co.ruleSetStats());
//            spitOutPops(co, printer);
//
//            System.out.print(co.ruleStats());

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
