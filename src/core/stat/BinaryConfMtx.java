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
        return (double) (tp) / (double) (tp + fp);
    }

    public double recall() {
        return (double) (tp) / (double) (tp + fn);
    }

    public double fMeasure() {
        final double precision = precision();
        final double recall = recall();
        return 2 * precision * recall / (precision + recall);
    }

    public double COREwithPenalty(int totalTrainingInstances) {
        double penalty = totalTrainingInstances / (double) (totalTrainingInstances + fp);
        return penalty * (tp / (double) (tp + fn)) * (1 + tn / (double) (tn + fp));
    }

    @Override
    public String toString() {
        String f = "TP: %d FN: %d TN: %d FP: %d "
                + "ACC: %.1f%% F: %.1f%% RCL: %.1f%% PRC: %.1f%%";
        return String.format(f, tp, fn, tn, fp,
                accuracy() * 100, fMeasure() * 100,
                recall() * 100, precision() * 100);
    }
}
