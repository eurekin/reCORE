package core.io;

/**
 *
 * @author Rekin
 */
public class IntegerInterpreter implements ElementInterpreter {

    Integer min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;

    public Object interpret(String s) {
        Integer val = Integer.valueOf(s);
        return val;
    }
}
