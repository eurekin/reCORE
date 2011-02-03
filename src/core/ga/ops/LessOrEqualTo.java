package core.ga.ops;

/**
 *
 * @author gmatoga
 */
public class LessOrEqualTo extends AbstractTwoArgOperator {

    @Override
    public String toString() {
        return "<=";
    }

    public boolean apply(Object a, Float b, Float c) {
        return ((Float) a).compareTo((Float) b) <= 0;
    }
}
