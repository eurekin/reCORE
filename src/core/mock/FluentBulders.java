package core.mock;

import core.copop.RuleSet;
import core.ga.Rule;
import core.ga.Selector;
import core.ga.ops.OpFactory;
import core.io.dataframe.Row;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Rekin
 */
public class FluentBulders {

    public static List<Integer> ints(Integer ... ints) {
        return Arrays.asList(ints);
    }

    public static Row<Integer, Integer> makeRow(final int clazz, final Integer... attrs) {
        return new Row<Integer, Integer>() {

            public Integer getClazz() {
                return clazz;
            }

            public List<Integer> getAtts() {
                return Arrays.asList(attrs);
            }

            @Override
            public String toString() {
                return getAtts() + " => " + getClazz();
            }
        };
    }

    public static RuleSet makeRuleSet(int clazz, Rule... rules) {
        return new RuleSet(new ArrayList<Rule>(Arrays.asList(rules)), clazz);
    }

    public static RuleBuilder rule() {
        return new RuleBuilder();
    }

    public static class RuleBuilder {

        List<Selector> sels = new ArrayList<Selector>();

        public RuleBuilder in(int i) {
            sels.add(new Selector(true, OpFactory.eq(), i));
            return this;
        }

        public RuleBuilder out(int i) {
            sels.add(new Selector(true, OpFactory.neq(), i));
            return this;
        }

        public RuleBuilder X() {
            sels.add(new Selector(false, OpFactory.eq(), 0));
            return this;
        }

        public Rule clazz(int clazz) {
            return new Rule(sels, clazz);
        }
    }
}
