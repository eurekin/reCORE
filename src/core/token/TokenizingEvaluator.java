package core.token;

import core.copop.RuleSet;
import core.ga.DefaultEvaluator;
import core.ga.Evaluator;
import core.ga.Rule;
import core.io.dataframe.Row;
import core.stat.BinaryConfMtx;
import core.stat.ConfMtx;

/**
 *
 * @author gmatoga
 */
public class TokenizingEvaluator implements Evaluator {

    private final DefaultEvaluator delegate = new DefaultEvaluator();
    private final TokenCompetition tokenComp;

    public TokenizingEvaluator(TokenCompetition comp) {
        this.tokenComp = comp;
    }

    public void evaluate(Rule rule, Row<Integer, Integer> row, BinaryConfMtx cm) {
        boolean predicted = rule.apply(row.getAtts());
        boolean same = row.getClazz().equals(rule.getClazz());

        if (same) {
            if (predicted) {
                cm.tp++;
                tokenComp.onTerritory(rule.individual(), row);
            } else {
                cm.fn++;
            }
        } else {
            if (predicted) {
                cm.fp++;
            } else {
                tokenComp.onTerritory(rule.individual(), row);
                cm.tn++;
            }
        }
    }

    public void evaluate(RuleSet rs, Row<Integer, Integer> row, ConfMtx cm) {
        delegate.evaluate(rs, row, cm);
    }
}
