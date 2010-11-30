package core.io.repr.col;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Rekin
 */
public class Cardinal<T> implements DomainMemoizable<T>, ColumnDecorator<T> {

    private Set<T> memoized = new HashSet<T>();
    Column<T> delegate;

    public Cardinal(Column<T> delegate) {
        this.delegate = delegate;
    }

    public Set<T> getDomain() {
        return memoized;
    }

    public Column getDecorated() {
        return delegate;
    }

    public T get(int i) {
        return delegate.get(i);
    }

    public void add(T el) {
        memoized.add(el);
        delegate.add(el);
    }

    @Override
    public String toString() {
        return memoized.toString();
    }
}
