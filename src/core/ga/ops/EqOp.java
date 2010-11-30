package core.ga.ops;

/**
 *
 * @author Rekin
 */
public class EqOp implements Operator {

    public boolean apply(int a, int b) {
        return a == b;
    }

    @Override
    public String toString() {
        return "==";
    }
}