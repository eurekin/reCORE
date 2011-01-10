package core.ga;

import core.binomial.InvCDFRandGenerator;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 *
 * @author Rekin
 */
public class Mutator {

    Random rand;
    private static InvCDFRandGenerator bin;

    public Mutator(Random rand) {
        this.rand = rand;
    }

    // sophisticated mutation
    public void mutate(Mutable mutable, double mt) {
        if (mutable.size() == 0)
            return;
//        if (mutable.size() > 5)
//            binomialMutateRepeat(mutable, mt);
//        else
            mutateInter(mutable, mt);
    }

    public void mutateInter(Mutable mutable, double mt) {
        for (int i = 0; i < mutable.size(); i++)
            if (rand.nextDouble() < mt)
                mutable.mutateAt(i);
    }

    void binomialMutateRepeat(Mutable mutable, double mt) {
        updateBin(mutable, mt);

        Set<Integer> previous = new HashSet<Integer>();
        int bits = bin.nextBinomial();
        Integer rm;
        final int len = mutable.size();
        while (bits > 0) {
            rm = rand.nextInt(len);
            if (previous.contains(rm))
                continue;

            mutable.mutateAt(rm);
            previous.add(rm);
            bits--;
        }
    }

    private void updateBin(Mutable mutable, double mt) {
        if (bin == null)
            bin = new InvCDFRandGenerator(mutable.size(), mt, rand);
        if (bin.getP() != mt)
            bin = new InvCDFRandGenerator(mutable.size(), mt, rand);
    }
}
