
package core.examples;

import core.ga.Rule;
import core.ga.RulePrinter;
import core.ga.Selector;
import core.ga.ops.OpFactory;
import core.io.dataframe.Mapper;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rekin
 */
public class RulePrinterExample {

    public static void main(String[] args) {
        InputStream in = Mapper.class.getResourceAsStream("/monks/monks.map");
        Mapper m = new Mapper(in);
        List<Selector> sels = new ArrayList<Selector>();
        sels.add(new Selector(true, OpFactory.eq(), 3));
        sels.add(new Selector(true, OpFactory.eq(), 3));
        sels.add(new Selector(true, OpFactory.neq(), 2));
        sels.add(new Selector(true, OpFactory.neq(), 3));
        sels.add(new Selector(true, OpFactory.neq(), 4));
        sels.add(new Selector(true, OpFactory.neq(), 2));
        Rule r = new Rule(sels, 1);
        RulePrinter rp = new RulePrinter(m);
        String p = rp.print(r);
        System.out.println(p);
        String pp = rp.prettyPrint(r);
        System.out.println(pp);
    }
}
