package core.stat;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rekin
 */
public class ConfMtx {

    int count;
    int[][] cm;
    BinaryConfMtx weighted;
    List<BinaryConfMtx> cms;

    public ConfMtx(int classes) {
        cms = new ArrayList<BinaryConfMtx>();
        cm = new int[classes][classes];
        for (int i = 0; i < classes; i++) {
            cms.add(new BinaryConfMtx());
            cm[i] = new int[classes];
        }
    }

    public void add(int expected, int predicted) {
        cm[expected][predicted]++;
        count++;
    }

    public List<BinaryConfMtx> getCMes() {
        for (int i = 0; i < cm.length; i++) {
            updateCms(i, cms.get(i));
        }
        return cms;
    }

    private void updateCms(int id, BinaryConfMtx bcm) {
        final int n = cm.length;
        bcm.tp = cm[id][id];

        int acc = 0;
        for (int i = 0; i < n; i++) acc += cm[id][i];
        bcm.fn = acc - bcm.tp;

        acc = 0;
        for (int i = 0; i < n; i++) acc += cm[i][id];
        bcm.fp = acc - bcm.tp;

        bcm.tn = count - bcm.fn - bcm.fp - bcm.tp;
    }


    public BinaryConfMtx getWeighted() {
        return new AverageConfMtx(cms, count);
    }
}
