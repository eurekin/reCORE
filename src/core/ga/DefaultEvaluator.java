package core.ga;

import core.copop.RuleSet;
import core.io.dataframe.Row;
import core.stat.BinaryConfMtx;

/**
 *
 * @author Rekin
 */
public class DefaultEvaluator implements Evaluator {

    public void evaluate(Rule rule, Row<Integer, Integer> row, BinaryConfMtx cm) {
        boolean predicted = rule.apply(row.getAtts());
        boolean same = row.getClazz().equals(rule.getClazz());

        if (same) {
            if (predicted) {
                cm.tp++;
            } else {
                cm.fn++;
            }
        } else {
            if (predicted) {
                cm.fp++;
            } else {
                cm.tn++;
            }
        }
    }
    public void evaluate(RuleSet rs, Row<Integer, Integer> row, ConfMtx cm) {
        int expected = row.getClazz();
        int predicted = rs.apply(row.getAtts());
        cm.add(expected, predicted);

    }
}
