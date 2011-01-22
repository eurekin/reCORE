package core.ga.ops;

/**
 *
 * @author Rekin
 */
public class NotEqOp implements Operator {

    public boolean apply(Object a, Object b) {
        return !a.equals(b);
    }

    @Override
    public String toString() {
        return "!=";
    }
}
