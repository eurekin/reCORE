package core.examples;

import core.DataSetBundle;
import core.DataSetBundleFactory;
import core.ga.Rule;
import core.ga.RuleChromosomeSignature;
import core.ga.Selector;
import core.ga.ops.OpFactory;
import core.vis.RuleASCIIPlotter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rekin
 */
public class RuleASCIIPlotterExample {

    public static void main(String[] args) {
        DataSetBundle ds = DataSetBundleFactory.MONK(3, true);
        RuleChromosomeSignature sig = ds.getSignature();
        RuleASCIIPlotter plotter = new RuleASCIIPlotter(sig);
        System.out.println("M3:");
        RuleASCIIPlotter.simplePlot(plotter.m3());

        List<Selector> sels = new ArrayList<Selector>();
        sels.add(new Selector(false, OpFactory.eq(), 3));
        sels.add(new Selector(true, OpFactory.neq(), 3));
        sels.add(new Selector(false, OpFactory.neq(), 2));
        sels.add(new Selector(false, OpFactory.neq(), 3));
        sels.add(new Selector(true, OpFactory.neq(), 4));
        sels.add(new Selector(false, OpFactory.neq(), 2));

        System.out.println("\n\n\nOne rule for M3:");
        Rule r = new Rule(sels, 1);
        RuleASCIIPlotter.simplePlot(plotter.plotRule(r));
    }
}
