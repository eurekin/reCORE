package core.ga;

import core.ga.ops.Operator;
import java.io.Serializable;

/**
 *
 * @author Rekin
 */
public class Selector implements Serializable {

    boolean on;
    Operator op;
    Object val;

    public Selector(boolean on, Operator op, Object val) {
        this.on = on;
        this.op = op;
        this.val = val;
    }
}
