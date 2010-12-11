package core.ga;

import core.ga.ops.ec.ExecutionContext;
import core.io.dataframe.UniformDataFrame;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Rekin
 * 
 */
public class RulePopulation implements Iterable<Individual> {

    final List<Individual> individuals;
    final ExecutionContext context;

    public RulePopulation(ExecutionContext ctx) {
        this.individuals = new ArrayList<Individual>(ctx.size());
        Individual ind;
        for (int i = 0; i < ctx.size(); i++) {
            ind = new Individual(ctx.signature(), ctx.rand(), ctx.decoder());
            individuals.add(ind);
        }
        this.context = ctx;
    }

    public void evolve() {
    }

    public void evaluate(UniformDataFrame<Integer, Integer> data,
            Evaluator evaluator) {
    }

    public void randomize() {
        for (Individual ind : individuals) {
            ind.randomize();
        }
    }

    public void repair() {
        for (Individual ind : individuals) {
            ind.repair();
        }
    }

    public void decode() {
        for (Individual ind : individuals) {
            ind.decode(context.decoder());
        }
    }

    public void evaluate() {
        for (Individual ind : individuals) {
            ind.evaluate(context.data(), context.evaluator());
        }
    }

    public Iterator<Individual> iterator() {
        return individuals.iterator();
    }

    public void mutate() {
        final double mt = context.getMt();
        for (Individual i : individuals) {
            i.mutate(mt);
        }

    }

    public void binomialCDF(double p, int n) {
        double q = 1 - p;
        int xmin = 0;
        int xmax = n;
        double mean = n * p;
        double stdev = Math.sqrt(n * p * q);
        q = Math.max(q, 0.000001);
        double pdf = Math.pow(q, n);
        double[] CDF = new double[n];
        CDF[0] = pdf;
        int mode = 0;
        int median = 0;
        for (int i = 1; i <= n; i++) {
            double fac = ((double) (n - i + 1) / (double) i) * (p / q);
            if (fac > 1) {
                mode = i;
            }
            pdf *= fac;
            CDF[i] = CDF[i - 1] + pdf;
            if (CDF[i] <= 0.5001) {
                median = i;
            }
        }
        if (CDF[median] < 1) {
            median += (Math.abs(CDF[median] - 0.5) < .0001) ? 0.5 : 1;
        }
//  X = BI;
    }
}
