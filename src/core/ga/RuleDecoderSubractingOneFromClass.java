package core.ga;

import core.ga.ops.OpFactory;
import core.ga.ops.Operator;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rekin
 */
public class RuleDecoderSubractingOneFromClass {

    Addressable addressable;
    RuleChromosomeSignature sig;
    BinaryDecoder dec;

    public RuleDecoderSubractingOneFromClass(
            RuleChromosomeSignature sig,
            BinaryDecoder dec) {
        this.dec = dec;
        this.sig = sig;
    }

    public Rule decode(Addressable toDecode) {
        this.addressable = toDecode;
        int clazz = decode(addressable, sig.getClazzAddress(), sig.getClazzSize()) - 1;
        List<Selector> sels = createSelectors();
        return new Rule(sels, clazz);
    }

    private List<Selector> createSelectors() {
        List<Selector> sels = new ArrayList<Selector>();
        Integer[] adrs = sig.getGeneAddresses();
        Integer[] sizes = sig.getValueCodeSizes();
        for (int i = 0; i < adrs.length; i++) {
            sels.add(decodeSelector(addressable, i));
        }
        return sels;
    }

    public Selector decodeSelector(Addressable toDecode, int selId) {
        int i = sig.getGeneAddresses()[selId];
        boolean on = toDecode.get(i);
        Operator op = toDecode.get(i + 1) ? OpFactory.eq() : OpFactory.neq();
        int val = decode(toDecode,i + 2, sig.getValueCodeSizes()[selId]);
        return new Selector(on, op, val);
    }

    private int decode(Addressable toDecode, int start, int size) {
        return dec.decode(toDecode, start, size);
    }
}
