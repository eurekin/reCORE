package core.io.repr.col;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Rekin
 */
public class Cardinal implements DomainMemoizable, ColumnDecorator {

    private HashSet memoized = new HashSet();
    Column delegate;

    public Cardinal(Column delegate) {
        this.delegate = delegate;
    }

    public Domain getDomain() {
        return new IntegerDomain(memoized);
    }

    public Column getDecorated() {
        return delegate;
    }

    public Object get(int i) {
        return delegate.get(i);
    }

    public void add(Object el) {
        memoized.add(el);
        delegate.add(el);
    }

    @Override
    public String toString() {
        return memoized.toString();
    }
}
