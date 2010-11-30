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
        StringBuilder sb = new StringBuilder();
        sb.append("TP:").append(tp);
        sb.append("  FN:").append(fn);
        sb.append("  TN:").append(tn);
        sb.append("  FP:").append(fp);
        sb.append("  ACC:").append(accuracy());
        sb.append("  F:").append(fMeasure());
        return sb.toString();
    }
}
