package core.io.repr.col;

import java.util.ArrayList;

/**
 *
 * @author Rekin
 */
public abstract class AbstractColumn<T> implements Column<T> {

    protected ArrayList<T> list = new ArrayList<T>();

    public T get(int i) {
        return list.get(i);
    }

    public void add(T el) {
        list.add(el);
    }
}
