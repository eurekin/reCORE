package core.io.dataframe;

import core.io.repr.col.Column;
import java.util.AbstractList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Rekin
 */
public class DataFrame extends AbstractList<Row> {

    Column classColumn;
    List<Column> attributes;
    int size;

    public List<Column> getAttributes() {
        return attributes;
    }

    public Column getClassColumn() {
        return classColumn;
    }

    public DataFrame(Column classColumn, List<Column> attributes, int size) {
        this.classColumn = classColumn;
        this.attributes = attributes;
        this.size = size;
    }
    private final HashMap<Integer, RowImpl> mem = new HashMap<Integer, RowImpl>();

    @Override
    public Row get(int index) {
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

    private final class RowImpl implements Row {

        private final int index;
        private final AttrView attrView = new AttrView();
        private final Integer clazz;

        private RowImpl(int index) {
            this.index = index;
            this.clazz = (Integer) classColumn.get(index);
        }

        public Integer getClazz() {
            return clazz;
        }

        public List getAtts() {
            return attrView;
        }

        public int getId() {
            return index;
        }

        private final class AttrView extends AbstractList {

            private final HashMap<Integer, Object> mem = new HashMap<Integer, Object>();
            final int size1 = attributes.size();

            @Override
            public Object get(int vid) {
                if (mem.containsKey(vid)) {
                    return mem.get(vid);
                } else {
                    Object value = attributes.get(vid).get(index);
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
