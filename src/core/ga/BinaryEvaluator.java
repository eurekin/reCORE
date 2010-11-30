package core.ga;

import core.io.dataframe.Row;
import core.stat.BinaryConfMtx;

/**
 *
 * @author Rekin
 */
public class BinaryEvaluator implements Evaluator {

    public void evaluate(Rule rule, Row<Integer, Integer> row, BinaryConfMtx cm) {
        boolean predicted = rule.apply(row.getAtts().toArray());
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
}
