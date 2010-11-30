package core.io;

import java.util.List;

public class PrintingConsumer implements ResultConsumer {

    public void consume(List interpreted) {
        System.out.println(interpreted);
    }
}
