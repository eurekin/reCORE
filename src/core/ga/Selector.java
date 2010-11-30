package core.ga;

import core.ga.ops.Operator;

/**
 *
 * @author Rekin
 */
public class Selector {

    boolean on;
    Operator op;
    int val;

    public Selector(boolean on, Operator op, int val) {
        this.on = on;
        this.op = op;
        this.val = val;
    }
}
