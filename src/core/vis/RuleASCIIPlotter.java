package core.vis;

import core.ga.Rule;
import core.ga.RuleChromosomeSignature;
import java.util.List;

/**
 *
 * @author Rekin
 */
public class RuleASCIIPlotter {

    RuleChromosomeSignature sig;
    CoordCalc c;

    public RuleASCIIPlotter(RuleChromosomeSignature sig) {
        this.sig = sig;
        c = new CoordCalc(sig);
    }

    public String[][] plotRule(Rule rule) {
        String[][] datavis = initEmptyDataVis(c);
        for (List<Integer> comb : c.allCombinations()) {
            boolean ok = rule.apply(comb.toArray());
            boolean mark = ok && rule.getClazz() == 1 || !ok && rule.getClazz() != 1;
            String s = mark ? "#" : " ";
            datavis[c.getY(comb)][c.getX(comb)] = s;
        }
        return datavis;
    }

    public String[][] m3() {
        String[][] datavis = initEmptyDataVis(c);
        for (List<Integer> comb : c.allCombinations()) {
            String s = " ";
            // jacket_color is green and holding a sword
            if (comb.get(4) == 3 && comb.get(3) == 1) {
                s = "#";
            }
            // jacket color is not blue and body shape is octagon
            if (comb.get(4) != 4 && comb.get(1) != 3) {
                s = "#";
            }
            datavis[c.getY(comb)][c.getX(comb)] = s;
        }
        return datavis;
    }

    private static String[][] initEmptyDataVis(CoordCalc c) {
        String[][] datavis = new String[c.getHeight()][];
        for (int i = 0; i < datavis.length; i++) {
            datavis[i] = new String[c.getWidth()];
            for (int j = 0; j < datavis[i].length; j++) {
                datavis[i][j] = " ";
            }
        }
        return datavis;
    }

    public static void simplePlot(String[][] datavis) {
        for (int i = 0; i < datavis.length; i++) {
            String[] strings = datavis[i];
            for (int j = 0; j < strings.length; j++) {
                String string = strings[j].equals("#") ? "[]" : "  ";
                System.out.print(string);
            }
            System.out.println();
        }
    }

    public static String[][] getPlot(RuleChromosomeSignature sig, Plotable callable) {
        CoordCalc c = new CoordCalc(sig);
        String[][] datavis = new String[c.getHeight()][];
        for (int i = 0; i < datavis.length; i++) {
            datavis[i] = new String[c.getWidth()];
            for (int j = 0; j < datavis[i].length; j++)
                datavis[i][j] = " ";
        }
        for (List<Integer> comb : c.allCombinations())
            datavis[c.getY(comb)][c.getX(comb)] = callable.call(comb);
        return datavis;
    }
}
