package core.ga.ops;

import java.io.Serializable;

/**
 *
 * @author Rekin
 */
public interface Operator extends Serializable {

    public boolean apply(int a, int b);
}
