package core.ga.ops;

/**
 *
 * @author gmatoga
 */
public class NotInOp extends AbstractTwoArgOperator {

    public boolean apply(Object a, Float b, Float c) {
        Float v = (Float) a, r1 = (Float) b, r2 = (Float) c;
        if (r2 < r1) {
            Float t = r2;
            r2 = r1;
            r1 = t;
        }
        return v <= r1 || r2 <= v;
    }

    @Override
    public String toString() {
        return "<>";
    }

}
