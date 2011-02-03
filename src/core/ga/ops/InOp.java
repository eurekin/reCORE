package core.ga.ops;

/**
 *
 * @author gmatoga
 */
public class InOp extends AbstractTwoArgOperator {

    public boolean apply(Object a, Float b, Float c) {
        Float v = (Float) a, r1 = (Float) b, r2 = (Float) c;
        if (r1 > r2) {
            Float t = r2;
            r2 = r1;
            r1 = t;
        }
        return r1 <= v && v <= r2;
    }

    @Override
    public String toString() {
        return "><";
    }
}
