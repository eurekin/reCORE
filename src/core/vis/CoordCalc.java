package core.vis;

import core.ga.RuleChromosomeSignature;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

/**
 *
 * @author Rekin
 */
public class CoordCalc {

    private List<Set> hor, ver;
    private List<Integer> horMul, verMul;

    public CoordCalc(RuleChromosomeSignature sig) {
        this(sig.getAttrDomain());
    }

    public CoordCalc(List<Set> attrs) {
        this.hor = new ArrayList<Set>();
        this.ver = new ArrayList<Set>();
        divideAttrsInHalf(attrs);
        this.horMul = calculateMultipliers(hor);
        this.verMul = calculateMultipliers(ver);
    }

    private void divideAttrsInHalf(List<Set> ads) {
        int half = ads.size() / 2;
        for (int i = 0; i < ads.size(); i++) {
            List<Set> where = i < half ? ver : hor;
            where.add(ads.get(i));
        }
    }

    private static List<Integer> calculateMultipliers(List<Set> l) {
        LinkedList<Integer> r = new LinkedList<Integer>();
        int mul = 1;
        ListIterator<Set> it = l.listIterator(l.size());
        while (it.hasPrevious()) {
            r.addFirst(mul);
            mul *= it.previous().size();
        }
        return r;
    }

    public int getX(List<Integer> vals) {
        return calcDim(vals.size() / 2, vals.size(), horMul, vals);
    }

    public int getY(List<Integer> vals) {
        return calcDim(0, vals.size() / 2, verMul, vals);
    }

    public int getWidth() {
        return getMax(hor, horMul);
    }

    public int getHeight() {
        return getMax(ver, verMul);
    }

    private int getMax(List<Set> l, List<Integer> m) {
        ListIterator<Set> ds = l.listIterator();
        ListIterator<Integer> ms = m.listIterator();
        int dim = 1;
        while (ms.hasNext()) {
            dim += (ds.next().size() - 1) * ms.next();
        }
        return dim;
    }

    private int calcDim(int start, int end, List<Integer> mull, List<Integer> vals) {
        ListIterator<Integer> half = vals.listIterator(start);
        ListIterator<Integer> muls = mull.listIterator();
        int dim = 0;
        while (half.nextIndex() < end) {
            dim += muls.next() * (half.next() - 1); // vals start at 1 for MONK
        }
        return dim;
    }

    public List<Integer> revX(int x) {
        return rev(x, horMul);
    }

    public List<Integer> revY(int y) {
        return rev(y, verMul);
    }

    public List<Integer> rev(int x, int y) {
        List<Integer> a = revY(y);
        a.addAll(revX(x));
        return a;
    }

    private List<Integer> rev(int x, List<Integer> muls) {
        List<Integer> res = new ArrayList<Integer>();
        for (Integer d : muls) {
            res.add(x / d + 1);
            x %= d;
        }
        return res;
    }

    public Iterable<List<Integer>> allCombinations() {
        return new Iterable() {

            public Iterator iterator() {
                return new CombinationIt();
            }
        };
    }

    public class CombinationIt implements Iterator<List<Integer>> {

        int x = -1, y = 0;

        public boolean hasNext() {
            return y != getHeight() - 1 || x != getWidth() - 1;
        }

        public List<Integer> next() {
            x++;
            if (x == getWidth()) {
                y++;
                x = 0;
            }
            return rev(x, y);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }


    public String spillGuts() {
        StringBuilder sb = new StringBuilder();
        sb.append("muls ver = ").append(verMul);
        sb.append(", muls hor = ").append(horMul);
        sb.append(", getWidth() = ").append(getWidth());
        sb.append(", getHeight() = ").append(getHeight());
        return sb.toString();
    }
}
