package core.stat;

/**
 *
 * @author Rekin
 */
public class BinaryConfMtx {

    public int tp, tn, fp, fn;

    public double accuracy() {
        final double den = (double) (tp + fp + fn + tn);
        return den == 0 ? 0 : (double) (tp + tn) / den;
    }

    public double precision() {
        final double den = tp + fp;
        if (den == 0.) return 0;
        return (double) (tp) / (double) (den);
    }

    public double recall() {
        final double den = tp + fn;
        if (den == 0.) return 0;
        return (double) (tp) / (double) (tp + fn);
    }

    public double fMeasure() {
        final double precision = precision();
        final double recall = recall();
        final double den = precision + recall;
        if (den == 0.) return 0;
        return 2 * precision * recall / (den);
    }

    public double COREwithPenalty(int totalTrainingInstances) {
        double penalty = totalTrainingInstances / (double) (totalTrainingInstances + fp);
        return penalty * (tp / (double) (tp + fn)) * (1 + tn / (double) (tn + fp));
    }

    @Override
    public String toString() {
        String f = "TPR: %.1f FPR: %.1f TP: %d FN: %d TN: %d FP: %d "
                + "ACC: %.1f%% F: %.1f%% RCL: %.1f%% PRC: %.1f%%";
        double tpr = (double) tp / (tp + fn) * 100.0;
        double fpr = (double) fp / (fp + tn);
        return String.format(f, tpr, fpr, tp, fn, tn, fp,
                accuracy() * 100, fMeasure() * 100,
                recall() * 100, precision() * 100);
    }
}
