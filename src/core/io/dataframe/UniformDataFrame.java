package core.io.dataframe;

import core.io.repr.col.Column;
import java.util.AbstractList;
import java.util.List;

/**
 *
 * @author Rekin
 */
public class UniformDataFrame<V, C> extends AbstractList<Row<V, C>> {

    Column<C> classColumn;
    List<Column<V>> attributes;
    int size;

    public List<Column<V>> getAttributes() {
        return attributes;
    }

    public Column<C> getClassColumn() {
        return classColumn;
    }

    public UniformDataFrame(Column<C> classColumn, List<Column<V>> attributes, int size) {
        this.classColumn = classColumn;
        this.attributes = attributes;
        this.size = size;
    }

    @Override
    public Row<V, C> get(int index) {
        return new RowImpl(index);
    }

    @Override
    public int size() {
        return size;
    }

    private class RowImpl implements Row<V, C> {

        int index;

        private RowImpl(int index) {
            this.index = index;
        }

        public C getClazz() {
            return classColumn.get(index);
        }

        public List<V> getAtts() {
            return new AttrView();
        }

        private class AttrView extends AbstractList<V> {

            @Override
            public V get(int vid) {
                return attributes.get(vid).get(index);
            }

            @Override
            public int size() {
                return attributes.size();
            }
        }
    }
}
