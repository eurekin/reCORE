package core.ga;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rekin
 */
public class Rule {

    List<Selector> sels;
    List<Selector> rsels;
    int clazz;

    public Rule(List<Selector> sels, int clazzId) {
        this.sels = sels;
        this.clazz = clazzId;
    }

    public boolean apply(List args) {
        for (int i = 0; i < args.size(); i++) {
            Selector sel = sels.get(i);
            if (!sel.on) continue;
            boolean result = sel.op.apply(sel.val, (Integer) (args.get(i)));
            if (!result) return false;
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
}
