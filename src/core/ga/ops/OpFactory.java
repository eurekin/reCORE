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
    private static final Operator in = new InOp();
    private static final Operator nin = new NotInOp();

    public static Operator forCodedInt(int choice) {
        if (choice < 0 || choice > 3)
            throw new IllegalArgumentException("Expected choice in range 0..4, got " + choice);
        switch (choice) {

            case 0:
                return goet();
            case 1:
                return loet();
            case 2:
                return in;
            default:
                return nin;
        }
    }

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
