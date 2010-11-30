package core.io;

import java.util.List;

/**
 *
 * @author Rekin
 */
public class SplittingLineProcessor implements LineProcessor {

    private String delimeter;
    private Interpreter interp;
    private ResultConsumer cons;

    public SplittingLineProcessor(String delimeter, Interpreter interp, ResultConsumer cons) {
        this.delimeter = delimeter;
        this.interp = interp;
        this.cons = cons;
    }

    public void process(String line, int no) {
        String[] split = line.trim().split(delimeter);
        List interpreted = interp.interpret(split);
        interpreted.add(no);
        cons.consume(interpreted);
    }
}
