package core.ga.ops;

/**
 *
 * @author Rekin
 */
public class EqOp implements Operator {

    public boolean apply(Object a, Object b) {
        return a.equals(b);
    }

    @Override
    public String toString() {
        return "==";
    }
}