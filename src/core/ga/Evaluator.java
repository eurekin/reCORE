
package core.ga;

import core.io.dataframe.Row;
import core.stat.BinaryConfMtx;

/**
 *
 * @author Rekin
 */
public interface Evaluator  {

    void evaluate(Rule rule, Row<Integer, Integer> row, BinaryConfMtx cm);

}
