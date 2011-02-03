package core.ga.ops;

/**
 *
 * @author Rekin
 */
public class EqOp implements Operator {

    public boolean apply(Object a, Object b) {
        Integer v1 = (Integer) a;
        Float v2;
        if (b instanceof Float) {
            v2 = (Float) b;
        } else {
            v2 = new Float((Integer) b);
        }
        return v1.equals(v2.intValue());
    }

    @Override
    public String toString() {
        return "==";
    }
}
