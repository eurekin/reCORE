package core;

import core.io.InputStreamProcessor;
import core.io.IntegerInterpreter;
import core.io.LineSchema;
import core.io.ResultConsumer;
import core.io.SplittingLineProcessor;
import core.io.StringInterpreter;
import core.io.dataframe.DataFrame;
import core.io.repr.DataSetBuildingConsumer;
import core.io.repr.col.AttributeColumn;
import core.io.repr.col.Cardinal;
import core.io.repr.col.Column;
import core.io.repr.col.ColumnSchema;
import core.io.repr.col.Unique;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rekin
 */
public class DataSetFactory {

    private DataSetFactory() {
    }

    public static DataFrame MONK(int no, boolean train) {
        InputStream in = getInputStream(no, train);
        DataSetBuildingConsumer cons = initConsumer();
        SplittingLineProcessor processor = initProcessor(cons);
        InputStreamProcessor isp = new InputStreamProcessor(in, processor);
        isp.process();

        ColumnSchema filled = cons.getSchema();
        Column classCol =  filled.getColumns().get(0);
        List<Column> attrs = new ArrayList<Column>();
        for (int i = 1; i < 7; i++) { // column 0 - class
            attrs.add(filled.getColumns().get(i));
        }
        return new DataFrame(classCol, attrs, cons.getCount());
    }

    private static SplittingLineProcessor initProcessor(ResultConsumer cons) {
        LineSchema schema = constructMonkSchema();
        String delimeter = " ";
        return new SplittingLineProcessor(delimeter, schema, cons);
    }

    private static LineSchema constructMonkSchema() {
        LineSchema schema = new LineSchema(8);
        for (int i = 0; i < 7; i++) {
            schema.setAt(i, new IntegerInterpreter());
        }
        schema.setAt(7, new StringInterpreter());
        return schema;
    }

    private static InputStream getInputStream(int no, boolean train) {
        String fname = "/monks/monks-" + no + "." + (train ? "train" : "test");
        return DataSetFactory.class.getResourceAsStream(fname);
    }

    private static DataSetBuildingConsumer initConsumer() {
        ColumnSchema schema = new ColumnSchema(9);
        for (int i = 0; i < 7; i++) {
            schema.setAt(i, new Cardinal(new AttributeColumn()));
        }
        schema.setAt(7, new Unique(new AttributeColumn()));
        schema.setAt(8, new AttributeColumn());
        return new DataSetBuildingConsumer(schema);
    }
}
