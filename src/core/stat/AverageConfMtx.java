package core.stat;

/**
 * Adapter class. Helps reducing multi-class confusion matrix to
 * n binary confusion matrices.
 * 
 * @author Rekin
 */
public class AverageConfMtx extends BinaryConfMtx {

    int count;
    double acc, f, p, r, w, tpr, fpr;
    Iterable<BinaryConfMtx> cms;

    public AverageConfMtx(Iterable<BinaryConfMtx> cms, int count) {
        this.cms = cms;
        this.count = count;
        calculateStats();
    }

    private void calculateStats() {
        for (BinaryConfMtx g : cms) {
            w = (g.tp + g.fn) / (double) count;
            tp += g.tp;
            fp += g.fp;
            fn += g.fn;
            fp += g.fp;
            tpr += g.tpr() * w;
            fpr += g.fpr() * w;
            r += g.recall() * w;
            f += g.fMeasure() * w;
            p += g.precision() * w;
            acc += g.accuracy() * w;
        }
    }

    @Override
    public double COREwithPenalty(int n) {
        double val = 0;
        for (BinaryConfMtx g : cms) val += g.COREwithPenalty(n) * (g.tp + g.fn);
        return val / count;
    }

    @Override
    public double fpr() {
        return fpr;
    }

    @Override
    public double tpr() {
        return tpr;
    }

    @Override
    public double accuracy() {
        return acc;
    }

    @Override
    public double fMeasure() {
        return f;
    }

    @Override
    public double precision() {
        return p;
    }

    @Override
    public double recall() {
        return r;
    }
}
