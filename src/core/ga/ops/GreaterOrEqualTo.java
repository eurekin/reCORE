package core.ga.ops;

/**
 *
 * @author gmatoga
 */
public class GreaterOrEqualTo implements NumericalOperator {

    public boolean apply(Object a, Object b) {
        return ((Float) a).compareTo((Float) b) >= 0;
    }

    @Override
    public String toString() {
        return ">=";
    }
}
