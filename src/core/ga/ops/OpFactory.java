package core.ga.ops;

/**
 *
 * @author Rekin
 */
public class OpFactory {

    private static final Operator eq = new EqOp();
    private static final Operator neq = new NotEqOp();

    public static Operator eq() {
        return eq;
    }

    public static Operator neq() {
        return neq;
    }
}
