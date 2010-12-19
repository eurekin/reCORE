package core.ga;

import core.copop.RuleSet;
import core.io.dataframe.Row;
import core.stat.BinaryConfMtx;
import core.stat.ConfMtx;

/**
 *
 * @author Rekin
 */
public interface Evaluator {

    void evaluate(Rule rule, Row<Integer, Integer> row, BinaryConfMtx cm);

    void evaluate(RuleSet rs, Row<Integer, Integer> row, ConfMtx cm);
}
