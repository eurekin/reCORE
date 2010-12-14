package core.ga;

import core.stat.BinaryConfMtx;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rekin
 */
class ConfMtx {

    List<BinaryConfMtx> cms = new ArrayList<BinaryConfMtx>();
    int[][] cm;
    BinaryConfMtx weighted;
    int count;

    public ConfMtx(int classes) {
        cm = new int[classes][classes];
        for (int i = 0; i < classes; i++) {
            cms.add(new BinaryConfMtx());
            cm[i] = new int[classes];
        }
    }

    void add(int expected, int predicted) {
        cm[expected][predicted]++;
        count++;
    }

    private void updateCmses() {
        for (int i = 0; i < cm.length; i++) {
            updateCms(i, cms.get(i));
        }
        updateWeighted();
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

    public static void main(String[] args) {
        ConfMtx m = new ConfMtx(3);
        /*

        === Confusion Matrix ===

        a   b  c   <-- classified as
        61  0  1 |  a = c0
        0  20  2 |  b = c1
        2   2 12 |  c = c2

         */

        // diagonal
        for (int i = 0; i < 61; i++) m.add(0, 0);
        for (int i = 0; i < 20; i++) m.add(1, 1);
        for (int i = 0; i < 12; i++) m.add(2, 2);

        m.add(2, 0);
        m.add(2, 0);

        m.add(2, 1);
        m.add(2, 1);

        m.add(1, 2);
        m.add(1, 2);

        m.add(0, 2);

        m.updateCmses();
        for (BinaryConfMtx b : m.cms) {
            System.out.println(b);
        }
        System.out.println("");
        System.out.println(m.weighted);

    }

    private void updateWeighted() {
        weighted = new BinaryConfMtx();
        double testtpr = 0.;
        double testfpr = 0.;
        for (BinaryConfMtx g : cms) {
            int sum = g.tp + g.fn;
            weighted.fn += g.fn * ((double)sum/count*100);
            weighted.tn += g.tn * ((double)sum/count*100);
            weighted.fp += g.fp * ((double)sum/count*100);
            weighted.tp += g.tp * ((double)sum/count*100);
            testtpr += (double)sum * ((double) g.tp / (g.tp+g.fn));
            testfpr += (double)sum * ((double) g.fp / (g.fp+g.tn));
        }
        System.out.println("TESTTPR: " + testtpr);
        System.out.println("TESTFPR: " + testfpr);
    }
}
/**
 * === Run information ===

Scheme:       weka.classifiers.rules.DecisionTable -X 1 -S "weka.attributeSelection.BestFirst -D 1 -N 5"
Relation:     weka.datagenerators.classifiers.classification.RDG1-S_1_-n_100_-a_4_-c_3_-N_4_-I_0_-M_1_-R_10
Instances:    100
Attributes:   5
a0
a1
a2
a3
class
Test mode:    10-fold cross-validation

=== Classifier model (full training set) ===

Decision Table:

Number of training instances: 100
Number of Rules : 4
Non matches covered by Majority class.
Best first.
Start set: no attributes
Search direction: forward
Stale search after 5 node expansions
Total number of subsets evaluated: 10
Merit of best subset found:   95
Evaluation (for feature selection): CV (leave one out)
Feature set: 1,4,5

Time taken to build model: 0.01 seconds

=== Stratified cross-validation ===
=== Summary ===

Correctly Classified Instances          93               93      %
Incorrectly Classified Instances         7                7      %
Kappa statistic                          0.8696
Mean absolute error                      0.1107
Root mean squared error                  0.2132
Relative absolute error                 30.3938 %
Root relative squared error             50.1153 %
Total Number of Instances              100

=== Detailed Accuracy By Class ===

                TP Rate   FP Rate   Precision   Recall  F-Measure   ROC Area  Class
                0.984     0.053      0.968     0.984     0.976      0.987    c0
                0.909     0.026      0.909     0.909     0.909      0.957    c1
                0.75      0.036      0.8       0.75      0.774      0.918    c2
Weighted Avg.   0.93      0.044      0.928     0.93      0.929      0.969

=== Confusion Matrix ===

a  b  c   <-- classified as
61  0  1 |  a = c0
 0 20  2 |  b = c1
 2  2 12 |  c = c2


 */
