package core.io.repr.col;

/**
 *
 * @author Rekin
 */
public class LineNumberColumn implements Column<Integer> {

    public Integer get(int i) {
        return i;
    }

    public void add(Integer el) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
