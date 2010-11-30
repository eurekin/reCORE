package core;

import core.io.dataframe.Row;
import core.io.dataframe.Mapper;
import core.io.dataframe.UniformDataFrame;
import core.io.repr.col.DomainMemoizable;
import core.io.repr.col.Column;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;

/**
 *
 * @author Rekin
 */
public class CORE_2 {

    public static void main(String[] args) {
        System.out.println("\n\n\nData set:");
        UniformDataFrame<Integer, Integer> df = DataSetFactory.MONK(1, false);
        for (Row<Integer, Integer> row : df) {
            System.out.print("<< ");
            for (Integer integer : row.getAtts()) {
                System.out.print(integer + " ");
            }
            System.out.println(">> " + row.getClazz());
        }

        System.out.println("\n\n\nData values mapped:");
        InputStream in2 = Mapper.class.getResourceAsStream("/monks/monks.map");
        Mapper m = new Mapper(in2);
        for (Row<Integer, Integer> row : df) {
            System.out.print("<< ");
            List<Integer> atrs = row.getAtts();
            for (int i = 0; i < atrs.size(); i++) {
                System.out.print(m.valueMap(i).get(atrs.get(i)) + " ");
            }
            System.out.println(">> " + m.getClassmap().get(row.getClazz()));
        }
    }

    public static List<DomainMemoizable> getColsWithDomain(List<Column<Integer>> columns) {
        List<DomainMemoizable> l = new ArrayList<DomainMemoizable>();
        for (Column column : columns) {
            if (column instanceof DomainMemoizable) {
                l.add((DomainMemoizable) column);
            }
        }
        return l;
    }
}
