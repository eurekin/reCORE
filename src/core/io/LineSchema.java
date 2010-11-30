package core.io;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Rekin
 */
public class LineSchema implements Interpreter {

    int length;
    List<ElementInterpreter> interpreters;

    public List<ElementInterpreter> getInterpreters() {
        return Collections.unmodifiableList(interpreters);
    }

    public LineSchema(int length) {
        this.length = length;
        interpreters = new ArrayList<ElementInterpreter>(length);
        for (int i = 0; i < length; i++) {
            interpreters.add(null);
        }
    }

    public void setAt(int i, ElementInterpreter interp) {
        interpreters.set(i, interp);
    }

    public List interpret(String[] args) {
        List results = new ArrayList();
        check(args);
        for (int i = 0; i < args.length; i++) {
            ElementInterpreter ier = interpreters.get(i);
            results.add(ier.interpret(args[i]));
        }
        return results;
    }

    private void check(Object[] args) {
        if (args.length != length) {
            throw new IllegalArgumentException("Args.length to interpret must equal "
                    + length + ", got " + args.length);
        }
    }
}
