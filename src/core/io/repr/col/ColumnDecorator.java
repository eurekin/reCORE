package core.io.repr.col;

/**
 *
 * @author Rekin
 */
public interface ColumnDecorator<T> extends Column<T> {

    public Column getDecorated();
}
