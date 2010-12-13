package core.copop;

import core.ga.Rule;
import java.util.List;

/**
 *
 * @author Rekin
 */
public class RuleSet {

    List<Rule> rules;
    int defaultClass;

    public int apply(Object[] args) {
        for (Rule rule : rules) {
            if (!rule.apply(args)) continue;
            return rule.getClazz();
        }
        return defaultClass;
    }
}
