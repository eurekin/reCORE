package core.examples;

import core.vis.Plotable;
import core.DataSetBundleFactory;
import core.ga.RuleChromosomeSignature;
import java.util.List;
import static core.vis.RuleASCIIPlotter.*;

/**
 *
 * @author Rekin
 */
public class ThrunEtAlSetsExample {

    public static RuleChromosomeSignature sig = DataSetBundleFactory.MONK(3, true).getSignature();

    public static void main(String[] args) {
        System.out.println("M1");
        p1();
        System.out.println("\n\n\nM2");
        p2();
        System.out.println("\n\n\nM3");
        p3();
    }

    private static void p3() {
        simplePlot(getPlot(sig, new Plotable() {

            public String call(List<Integer> comb) {
                return m3(comb);
            }
        }));
    }

    private static void p2() {
        simplePlot(getPlot(sig, new Plotable() {

            public String call(List<Integer> comb) {
                return m2(comb);
            }
        }));
    }

    private static void p1() {
        simplePlot(getPlot(sig, new Plotable() {

            public String call(List<Integer> comb) {
                return m1(comb);
            }
        }));
    }

    private static String m1(List<Integer> comb) {
        // head_shape = body_shape
        if (comb.get(0).equals(comb.get(1))) return "#";
        // jacket_color is red
        if (comb.get(4) == 1) return "#";
        return " ";
    }

    private static String m2(List<Integer> comb) {
        int attrs = 0;
        // exactly 2 of the attributes have their first value
        for (Integer attr : comb) attrs += attr == 1 ? 1 : 0;
        return attrs == 2 ? "#" : " ";
    }

    private static String m3(List<Integer> comb) {
        // jacket_color is green and holding a sword
        if (comb.get(4) == 3 && comb.get(3) == 1) return "#";
        // jacket color is not blue and body shape is octagon
        if (comb.get(4) != 4 && comb.get(1) != 3) return "#";
        return " ";
    }
}
