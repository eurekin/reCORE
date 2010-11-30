package core.io.repr;

import core.io.ResultConsumer;
import core.io.repr.col.ColumnSchema;
import java.util.List;

/**
 * @author Rekin
 */
public class DataSetBuildingConsumer implements ResultConsumer {

    ColumnSchema schema;
    int count;

    public DataSetBuildingConsumer(ColumnSchema schema) {
        this.schema = schema;
        count = 0;
    }

    public void consume(List interpreted) {
        schema.addData(interpreted);
        count++;
    }

    public int getCount() {
        return count;
    }
    
    public ColumnSchema getSchema() {
        return schema;
    }
    
}
