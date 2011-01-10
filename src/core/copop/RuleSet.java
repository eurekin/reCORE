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

    public RuleSet(List<Rule> rules, int defaultClass) {
        this.rules = rules;
        this.defaultClass = defaultClass;
    }

    public int apply(List args) {
        for (Rule rule : rules) {
            if (!rule.apply(args))
                continue;
            return rule.getClazz();
        }
        return defaultClass;
    }
    public int determineRuleNo(List args) {
        int i = 0;
        for (Rule rule : rules) {
            if (rule.apply(args))
                return i;
            i++;
        }
        return -1;
    }

    public int getDefaultClass() {
        return defaultClass;
    }

    public List<Rule> getRules() {
        return rules;
    }
}
