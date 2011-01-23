package core;

import core.ga.RuleChromosomeSignature;
import core.ga.RulePrinter;
import core.io.dataframe.DataFrame;
import core.vis.RuleASCIIPlotter;

/**
 *
 * @author Rekin
 */
public class DataSetBundle {

    DataFrame  data;
    RuleASCIIPlotter plotter;
    RuleChromosomeSignature signature;
    RulePrinter printer;
    String creationArgs;

    public DataSetBundle(DataFrame data,
            RuleASCIIPlotter plotter, RuleChromosomeSignature signature,
            RulePrinter printer, String creationArgs) {
        this.data = data;
        this.plotter = plotter;
        this.printer = printer;
        this.signature = signature;
        this.creationArgs = creationArgs;
    }

    public DataFrame getData() {
        return data;
    }

    public RuleASCIIPlotter getPlotter() {
        return plotter;
    }

    public RulePrinter getPrinter() {
        return printer;
    }

    public RuleChromosomeSignature getSignature() {
        return signature;
    }

    public String getCreationArgs() {
        return creationArgs;
    }
}
