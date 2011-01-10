package core.examples;

import core.copop.RuleSet;
import core.DataSetBundle;
import core.DataSetBundleFactory;
import core.ga.Rule;
import core.ga.RuleChromosomeSignature;
import core.ga.Selector;
import core.ga.ops.OpFactory;
import core.vis.RuleASCIIPlotter;
import java.util.ArrayList;
import java.util.List;
import static core.mock.FluentBulders.*;

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
        RuleASCIIPlotter.simpleBinaryPlot(plotter.m3());

        List<Selector> sels = new ArrayList<Selector>();
        sels.add(new Selector(false, OpFactory.eq(), 2));
        sels.add(new Selector(true, OpFactory.neq(), 2));
        sels.add(new Selector(false, OpFactory.neq(), 1));
        sels.add(new Selector(false, OpFactory.neq(), 2));
        sels.add(new Selector(true, OpFactory.neq(), 3));
        sels.add(new Selector(false, OpFactory.neq(), 1));

        System.out.println("\n\n\nOne rule for M3:");
        Rule r = new Rule(sels, 1);
        RuleASCIIPlotter.simpleBinaryPlot(plotter.plotRule(r));

        System.out.println("Trying to reconstruct ruleSet for M1");
        RuleSet m1 = makeRuleSet(0,
                rule().in(0).in(0).X().X().X().X().clazz(1),
                rule().in(1).in(1).X().X().X().X().clazz(1),
                rule().in(2).in(2).X().X().X().X().clazz(1),
                rule().X().X().X().X().in(0).X().clazz(1)//
                );
        RuleASCIIPlotter.simpleBinaryPlot(plotter.plotBinaryRuleSet(m1));
    }
}
