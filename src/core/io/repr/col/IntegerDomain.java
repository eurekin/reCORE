package core.io.repr.col;

import java.util.HashSet;
import static java.lang.Math.ceil;
import static java.lang.Math.log10;
import static java.lang.Math.max;

/**
 *
 * @author gmatoga
 */
public class IntegerDomain implements Domain {

    private final HashSet<Integer> set;

    public IntegerDomain(HashSet<Integer> set) {
        this.set = set;
    }

    public boolean contains(Object o) {
        Integer integer = (Integer) o;
        return set.contains(integer);
    }

    public int bitSize() {
        return max(1, (int) ceil(log10(set.size()) / log10(2)));
    }

    public int size() {
        return set.size();
    }

    @Override
    public String toString() {
        return set.toString();
    }
}
