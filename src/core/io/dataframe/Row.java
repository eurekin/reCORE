package core.io.dataframe;

import java.util.List;

/**
 *
 * @author Rekin
 */
public interface Row<V, C> {

    public C getClazz();

    public List<V> getAtts();

    public int getId();
}
