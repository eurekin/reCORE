package core.io.repr.col;

import java.util.ArrayList;

/**
 *
 * @author Rekin
 */
public abstract class AbstractColumn implements Column {

    protected ArrayList list = new ArrayList();

    public Object get(int i) {
        return list.get(i);
    }

    public void add(Object el) {
        list.add(el);
    }
}
