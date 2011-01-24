package core.io.dataframe;

import java.util.List;

/**
 *
 * @author Rekin
 */
public interface Row {

    public Object getClazz();

    public List<Object> getAtts();

    public int getId();
}
