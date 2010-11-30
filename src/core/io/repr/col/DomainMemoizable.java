package core.io.repr.col;

import java.util.Set;

/**
 *
 * @author Rekin
 */
public interface DomainMemoizable<T> {

    public Set<T> getDomain();
}
