package core.io.repr.col;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Rekin
 */
public class ColumnSchema {

    int length;
    List<Column> columns;

    public List<Column> getColumns() {
        return Collections.unmodifiableList(columns);
    }

    public List<DomainMemoizable> getColsWithDomain() {
        List<DomainMemoizable> l = new ArrayList<DomainMemoizable>();
        for (Column column : columns) {
            if(column instanceof DomainMemoizable) {
                l.add((DomainMemoizable) column);
            }
        }
        return l;
    }

    public ColumnSchema(int length) {
        this.length = length;
        columns = new ArrayList<Column>(length);
        for (int i = 0; i < length; i++) {
            columns.add(null);
        }
    }

    public void setAt(int i, Column interp) {
        columns.set(i, interp);
    }

    private void check(List args) {
        if (args.size() != length) {
            throw new IllegalArgumentException("Data size must equal "
                    + length + ", got " + args.size());
        }
    }

    public void addData(List interpreted) {
        check(interpreted);
        for (int i = 0; i < columns.size(); i++) {
            columns.get(i).add(interpreted.get(i));
        }
    }
}
