package core.io.repr.col;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Rekin
 */
public class Unique<T> implements ColumnDecorator<T>, DomainMemoizable<T> {

    Column<T> delegate;
    Set<T> alreadyAdded;

    public Unique(Column<T> delegate) {
        this.delegate = delegate;
        alreadyAdded = new HashSet<T>();
    }

    public T get(int i) {
        return delegate.get(i);
    }

    public void add(T el) {
        ensureUniquenessOf(el);
        delegate.add(el);
    }

    private void ensureUniquenessOf(T el) {
        if (alreadyAdded.contains(el)) {
            throw new IllegalArgumentException("Already added value: " + el);
        }
        alreadyAdded.add(el);
    }

    public Column getDecorated() {
        return delegate;
    }

    public Set<T> getDomain() {
        return alreadyAdded;
    }
}
