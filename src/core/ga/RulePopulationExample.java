package core.ga;

import core.DataSetBundle;
import core.DataSetBundleFactory;
import core.io.dataframe.Row;
import core.stat.BinaryConfMtx;
import core.vis.RuleASCIIPlotter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rekin
 */
public class RulePopulationExample {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Set no: [1,2 or 3] ");
        int setno = scanner.nextInt();
        System.out.print("Train? [y/n] ");
        boolean train = scanner.next().trim().toLowerCase().equals("y");
        System.out.print("Fitness: [0 - accuracy, 1 - F, 2 - Precision, 3 - Recall] ");
        measure = scanner.nextInt();

        DataSetBundle ds = DataSetBundleFactory.MONK(setno, train);
        System.out.println("Trying to find best rule for: " + ds.getCreationArgs());
        double max = 0;
        BinaryConfMtx cmxi = null;
        for (int i = 0; i < 5000; i++) {
            RulePopulation pop = new RulePopulation(10, ds.getSignature(), ds.getData());
            pop.decode();
            pop.repair();
            pop.evaluate();
            Iterator<BinaryConfMtx> ci = pop.cms.iterator();
            for (Rule rule : pop.rules) {
                 cmxi = ci.next();
                double fitness = fitness(cmxi);
                if (fitness >= max) {
                    max = fitness;
                    System.out.print("\n\n\n\nfitness = " + max);
                    System.out.print("measures = " + cmxi);
                    System.out.println(" rule: " + ds.getPrinter().prettyPrint(rule));
                    RuleASCIIPlotter.simplePlot(ds.getPlotter().plotRule(rule));
                }
                System.out.flush();
            }
        }
        System.out.println("Tried to find best rule for: " + ds.getCreationArgs());
        System.out.print("Fitness measure: ");
        switch (measure) {
            case 0:
                System.out.println("accuracy");
                break;
            case 1:
                System.out.println("F score");
                break;
            case 2:
                System.out.println("precision");
                break;
            case 3:
                System.out.println("recall");
                break;
            default:
                System.out.println("accuracy");
                break;
        }
        System.out.println("Reference: ");
        
        System.out.println("Press enter to exit");
        try {
            System.in.read();
        } catch (IOException ex) {
            Logger.getLogger(RulePopulationExample.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private static int measure = 0;

    private static double fitness(BinaryConfMtx cmxi) {
        switch (measure) {
            case 0:
                return cmxi.accuracy();
            case 1:
                return cmxi.fMeasure();
            case 2:
                return cmxi.precision();
            case 3:
                return cmxi.recall();
            default:
                return cmxi.accuracy();

        }
    }

    private static Row<Integer, Integer> mockRow(final int classid,
            final Integer... atts) {
        return new Row<Integer, Integer>() {

            public Integer getClazz() {
                return classid;
            }

            public List<Integer> getAtts() {
                return Arrays.asList(atts);
            }
        };
    }
}
