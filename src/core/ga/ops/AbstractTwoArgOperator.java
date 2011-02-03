package core.ga.ops;

/**
 *
 * @author gmatoga
 */
public abstract class AbstractTwoArgOperator implements TwoArgOperator {

    public boolean apply(Object a, Object b) {
        Float[] args = (Float[]) a;
        return apply(b, args[0], args[1]);
    }
}
