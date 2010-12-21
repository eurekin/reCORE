package core.io.dataframe;

import core.io.repr.col.Column;
import java.util.AbstractList;
import java.util.HashMap;
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
    private final HashMap<Integer, RowImpl> mem = new HashMap<Integer, RowImpl>();

    @Override
    public Row<V, C> get(int index) {
        if (mem.containsKey(index)) {
            return mem.get(index);
        } else {
            RowImpl ri = new RowImpl(index);
            mem.put(index, ri);
            return ri;
        }
    }

    @Override
    public int size() {
        return size;
    }

    private final class RowImpl implements Row<V, C> {

        private final int index;
        private final AttrView attrView = new AttrView();
        private final C clazz;

        private RowImpl(int index) {
            this.index = index;
            this.clazz = classColumn.get(index);
        }

        public C getClazz() {
            return clazz;
        }

        public List<V> getAtts() {
            return attrView;
        }

        private final class AttrView extends AbstractList<V> {

            private final HashMap<Integer, V> mem = new HashMap<Integer, V>();
            final int size1 = attributes.size();

            @Override
            public V get(int vid) {
                if (mem.containsKey(vid)) {
                    return mem.get(vid);
                } else {
                    V value = attributes.get(vid).get(index);
                    mem.put(vid, value);
                    return value;
                }
            }

            @Override
            public int size() {
                return size1;
            }
        }

        @Override
        public String toString() {
            return new AttrView().toString() + " => " + getClazz();
        }
    }
}
