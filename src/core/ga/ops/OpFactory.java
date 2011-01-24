package core.ga.ops;

/**
 *
 * @author Rekin
 */
public class OpFactory {

    private static final Operator eq = new EqOp();
    private static final Operator neq = new NotEqOp();
    private static final Operator goet = new GreaterOrEqualTo();
    private static final Operator loet = new LessOrEqualTo();

    public static Operator eq() {
        return eq;
    }

    public static Operator neq() {
        return neq;
    }

    public static Operator goet() {
        return goet;
    }

    public static Operator loet() {
        return loet;
    }

}
