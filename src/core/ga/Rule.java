package core.ga;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Rekin
 */
public class Rule implements Serializable {

    List<Selector> sels;
    List<Selector> rsels;
    int clazz;
    private transient Individual individual;

    public Rule(List<Selector> sels, int clazzId) {
        this.sels = sels;
        this.clazz = clazzId;
    }

    public boolean apply(List args) {
        if (sels.size() != args.size())
            throw new RuntimeException("Rule args != no of sels");
        
        for (int i = 0; i < args.size(); i++) {
            Selector sel = sels.get(i);
            if (!sel.on)
                continue;
            boolean result = sel.op.apply(sel.val, args.get(i));
            if (!result)
                return false;
        }
        return true;
    }

    public int getClazz() {
        return clazz;
    }

    public void reset() {
        rsels = null;
    }

    public List<Selector> getRelevantSelectors() {
        if (rsels == null) {
            initRsels();
        }
        return rsels;
    }

    private void initRsels() {
        rsels = new ArrayList<Selector>();
        for (Selector selector : sels) {
            if (selector.on) {
                rsels.add(selector);
            }
        }
    }

    public List<Selector> getAllSelectors() {
        return sels;
    }

    public void setIndividual(Individual aThis) {
        this.individual = aThis;
    }

    public Individual individual() {
        return individual;
    }

    Rule copy() {
        return new Rule(new ArrayList<Selector>(sels), clazz);
    }
}
