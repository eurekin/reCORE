package core.io.repr.col;

import java.util.HashSet;

/**
 *
 * @author Rekin
 */
public class Unique implements ColumnDecorator, DomainMemoizable {

    Column delegate;
    HashSet alreadyAdded;

    public Unique(Column delegate) {
        this.delegate = delegate;
        alreadyAdded = new HashSet();
    }

    public Object get(int i) {
        return delegate.get(i);
    }

    public void add(Object el) {
        ensureUniquenessOf(el);
        delegate.add(el);
    }

    private void ensureUniquenessOf(Object el) {
        if (alreadyAdded.contains(el)) {
            throw new IllegalArgumentException("Already added value: " + el);
        }
        alreadyAdded.add(el);
    }

    public Column getDecorated() {
        return delegate;
    }

    public Domain getDomain() {
        return new IntegerDomain(alreadyAdded);
    }
}
