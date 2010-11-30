package core;

import core.ga.RuleChromosomeSignature;
import core.ga.RulePrinter;
import core.io.dataframe.Mapper;
import core.io.dataframe.UniformDataFrame;
import core.io.repr.col.Column;
import core.io.repr.col.DomainMemoizable;
import core.vis.RuleASCIIPlotter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rekin
 */
public class DataSetBundleFactory {

    public static DataSetBundle MONK(int no, boolean train) {
        UniformDataFrame<Integer, Integer> data = DataSetFactory.MONK(no, train);
        RuleChromosomeSignature signature = signatureFor(data);
        RulePrinter printer = getMonkPrinter();
        RuleASCIIPlotter plotter = new RuleASCIIPlotter(signature);
        String s = "MONK M" + no + " - "+(train ? "train" : "test");
        return new DataSetBundle(data, plotter, signature, printer, s);
    }

    public static RuleChromosomeSignature signatureFor(UniformDataFrame<Integer, Integer> df) {
        List<Column<Integer>> cols = df.getAttributes();
        ArrayList<DomainMemoizable> cwd = new ArrayList(getColsWithDomain(cols));
        return new RuleChromosomeSignature(cwd, (DomainMemoizable) df.getClassColumn());
    }

    private static List<DomainMemoizable> getColsWithDomain(List<Column<Integer>> columns) {
        List<DomainMemoizable> l = new ArrayList<DomainMemoizable>();
        for (Column column : columns) {
            if (column instanceof DomainMemoizable) {
                l.add((DomainMemoizable) column);
            }
        }
        return l;
    }

    private static RulePrinter getMonkPrinter() {
        InputStream in2 = DataSetBundleFactory.class.getResourceAsStream("/monks/monks.map");
        return new RulePrinter(new Mapper(in2));
    }
}
