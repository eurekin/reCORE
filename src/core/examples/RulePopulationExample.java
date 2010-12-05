package core.examples;

import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import core.ga.Individual;
import core.ga.RulePopulation;
import core.ga.ops.ec.FitnessEval;
import core.ga.ops.ec.ExecutionContext;
import core.ga.ops.ec.FitnessEvaluatorFactory;
import core.DataSetBundle;
import core.DataSetBundleFactory;
import core.ExecutionContextFactory;
import static core.vis.RuleASCIIPlotter.*;
import static java.lang.System.*;

/**
 *
 * @author Rekin
 */
public class RulePopulationExample {

    public static final Scanner scanner = new Scanner(in);

    public static void main(String[] args) {
        int setno = chooseSet();
        boolean train = chooseTrain();
        FitnessEval fite = chooseFitnessEvaluator();

        ExecutionContext ctx = ExecutionContextFactory.MONK(setno, train, 100, fite);
        DataSetBundle ds = DataSetBundleFactory.MONK(setno, train);

        out.println("Trying to find best rule for: " + ds.getCreationArgs());

        double max = 0;
        for (int i = 0; i < 5000; i++) {
            RulePopulation pop = new RulePopulation(ctx);
            pop.decode();
            pop.repair();
            pop.evaluate();
            for (Individual ind : pop) {
                double fitness = ind.fitness();
                if (fitness >= max) {
                    max = fitness;
                    out.printf("\n\n\n\nfitness  = %.3f\n", max);
                    out.println("measures = " + ind.cm());
                    out.print("rule:      ");
                    out.println(ds.getPrinter().prettyPrint(ind.rule()));
                    simplePlot(ds.getPlotter().plotRule(ind.rule()));
                }
                out.flush();
            }
        }
        out.println("Tried to find best rule for: " + ds.getCreationArgs());
        out.println("Fitness measure: " + fite);
        out.println("Press enter to exit");

        scanner.nextByte();
    }

    private static int chooseSet() {
        out.print("Set no: [1,2 or 3] ");
        return scanner.nextInt();
    }

    private static boolean chooseTrain() {
        out.print("Train? [y/n] ");
        return scanner.next().trim().toLowerCase().equals("y");
    }

    private static FitnessEval chooseFitnessEvaluator() {
        Integer id = 0;
        Map<Integer, FitnessEval> fm = new HashMap<Integer, FitnessEval>();
        for (FitnessEval e : FitnessEvaluatorFactory.EVALS)
            fm.put(id++, e);
        String options = fm.toString().replace("{", "[").replace("}", "]");
        out.print("Fitness: " + options + " ");
        return fm.get(scanner.nextInt());
    }
}
