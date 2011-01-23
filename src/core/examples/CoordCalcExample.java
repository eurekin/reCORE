
package core.examples;

import core.io.repr.col.Domain;
import core.io.repr.col.IntegerDomain;
import core.vis.CoordCalc;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author Rekin
 */
public class CoordCalcExample {

    public static void main(String[] args) {

        Integer[][] monk = new Integer[][]{
            {1, 2, 3},
            {1, 2, 3},
            {1, 2},
            {1, 2, 3},
            {1, 2, 3, 4},
            {1, 2}
        };
        List<Domain> sets = new ArrayList<Domain>();
        List<List<Integer>> lists = new ArrayList<List<Integer>>();
        List<Integer> test;
        List<Integer> answer;
        for (Integer[] integers : monk) {
            final List<Integer> l = Arrays.asList(integers);
            lists.add(new ArrayList<Integer>(l));
            sets.add(new IntegerDomain(new HashSet(l)));
        }
        CoordCalc tda = new CoordCalc(sets);

        for (Integer a : lists.get(0)) {
            for (Integer b : lists.get(1)) {
                for (Integer c : lists.get(2)) {
                    for (Integer d : lists.get(3)) {
                        for (Integer e : lists.get(4)) {
                            for (Integer f : lists.get(5)) {
                                test = Arrays.asList(a, b, c, d, e, f);
                                answer = tda.rev(tda.getX(test), tda.getY(test));
                                if (!answer.equals(test)) {
                                    throw new RuntimeException("Wrong rev<>x,y mapping " + test + ", " + answer);
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("tda.getWeight() = " + tda.getWidth());
        System.out.println("tda.getHeight() = " + tda.getHeight());

        HashSet<List<Integer>> visited = new HashSet<List<Integer>>();
        for (List<Integer> list : tda.allCombinations()) {
            answer = tda.rev(tda.getX(list), tda.getY(list));
            if (visited.contains(list)) {
                throw new RuntimeException("Duplicate");

            }
            visited.add(list);
            if (!answer.equals(list)) {
                throw new RuntimeException("Wrong rev<>x,y mapping");
            }
        }
        if (visited.size() != 432) {
            throw new RuntimeException("Illegal size");
        }
    }
}
