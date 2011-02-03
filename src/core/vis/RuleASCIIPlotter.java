package core.vis;

import core.copop.RuleSet;
import core.ga.Rule;
import core.ga.RuleChromosomeSignature;
import java.util.ArrayList;
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
            boolean ok = rule.apply(comb);
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
            if (comb.get(4) == 2 && comb.get(3) == 0) {
                s = "#";
            }
            // jacket color is not blue and body shape is not octagon
            if (comb.get(4) != 3 && comb.get(1) != 2) {
                s = "#";
            }
            datavis[c.getY(comb)][c.getX(comb)] = s;
        }
        return datavis;
    }

    public static String[][] initEmptyDataVis(CoordCalc c) {
        String[][] datavis = new String[c.getHeight()][];
        for (int i = 0; i < datavis.length; i++) {
            datavis[i] = new String[c.getWidth()];
            for (int j = 0; j < datavis[i].length; j++) {
                datavis[i][j] = " ";
            }
        }
        return datavis;
    }

    public static void simpleBinaryPlot(String[][] datavis) {
        for (int i = 0; i < datavis.length; i++) {
            String[] strings = datavis[i];
            for (int j = 0; j < strings.length; j++) {
                String string = strings[j].equals("#") ? "[]" : "  ";
                System.out.print(string);
            }
            System.out.println();
        }
    }

    public String[][] plotBinaryRuleSet(RuleSet rs) {
        String[][] datavis = initEmptyDataVis(c);
        for (List<Integer> comb : c.allCombinations()) {
            int ok = rs.apply(comb);
             String s = ok == 0 ? " " : "#";
            datavis[c.getY(comb)][c.getX(comb)] = s;
        }
        return datavis;
    }

    public void plotPlots(String[][]... plots) {
        String PLOT_DELIMETER = "|";
        for (int i = 0; i < plots[0].length; i++) {
            System.out.print(PLOT_DELIMETER);
            for (String[][] stringses : plots) {
                String line[] = stringses[i];
                for (String string : line) {
                    System.out.print(string);
                }
                System.out.print(PLOT_DELIMETER);
            }
            System.out.println("");
        }
    }

    public String[][] plotRules(RuleSet rs) {
        String[][] vis = initEmptyDataVis(c);
        for (List<Integer> comb : c.allCombinations()) {
            int ok = rs.determineRuleNo(comb);
            String toString = new Integer(ok).toString();
            toString = toString.equals("-1") ? " " : toString;
            vis[c.getY(comb)][c.getX(comb)] = toString;
        }
        return vis;
    }

    public String[][] plotClasses(RuleSet rs) {
        String[][] vis = initEmptyDataVis(c);
        for (List<Integer> comb : c.allCombinations()) {
            List<Float> converted = new ArrayList<Float>();
            for (Integer toconv : comb) {
                converted.add(new Float(toconv.intValue()));
            }
            int ok = rs.apply(converted);
            String toString = new Integer(ok).toString();
             toString = toString.equals("0") ? " " : toString;
            vis[c.getY(comb)][c.getX(comb)] = toString;
        }
        return vis;
    }

    public void printOutClassPlot(RuleSet rs) {
        String[][] vis = initEmptyDataVis(c);
        for (List<Integer> comb : c.allCombinations()) {
            int ok = rs.apply(comb);
            vis[c.getY(comb)][c.getX(comb)] = new Integer(ok).toString();
        }
        for (String[] strings : vis) {
            for (String string : strings)
                System.out.print(string);
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

    public void detailedPlots(RuleSet ruleSet) {
        plotPlots(
                plotBinaryRuleSet(ruleSet),
                plotClasses(ruleSet),
                plotRules(ruleSet));
    }
}
