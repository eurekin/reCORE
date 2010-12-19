package core.copop;

import org.junit.Test;
import static core.mock.FluentBulders.*;
import static org.junit.Assert.*;

/**
 *
 * @author Rekin
 */
public class RuleSetTest {

    RuleSet rs = makeRuleSet(666,
            rule().in(3).clazz(1),
            rule().in(9).clazz(2),
            rule().in(1).clazz(3));

    @Test
    public void testFallback() {
        assertEquals(666, rs.apply(ints(-1)));
    }

    @Test
    public void testFirst() {
        assertEquals(1, rs.apply(ints(3)));
    }

    @Test
    public void testSecond() {
        assertEquals(2, rs.apply(ints(9)));
    }

    @Test
    public void testThird() {
        assertEquals(3, rs.apply(ints(1)));
    }
}
