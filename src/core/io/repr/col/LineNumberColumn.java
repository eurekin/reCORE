package core.io.repr.col;

/**
 *
 * @author Rekin
 */
public class LineNumberColumn implements Column {

    public Integer get(int i) {
        return i;
    }

    public void add(Object el) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
