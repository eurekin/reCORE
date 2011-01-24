package core.ga;

import core.ga.ops.OpFactory;
import core.ga.ops.Operator;
import core.io.repr.col.Domain;
import core.io.repr.col.FloatDomain;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rekin
 */
public class RuleDecoder {

    Addressable addressable;
    RuleChromosomeSignature sig;
    BinaryDecoder dec;

    public RuleDecoder(
            RuleChromosomeSignature sig,
            BinaryDecoder dec) {
        this.dec = dec;
        this.sig = sig;
    }

    /**
     * Here is an entry point to implement numerical attribute handling
     * @param toDecode
     * @return
     */
    public Rule decode(Addressable toDecode) {
        this.addressable = toDecode;
        int clazz = decode(addressable, sig.getClazzAddress(), sig.getClazzSize());
        List<Selector> sels = createSelectors();
        return new Rule(sels, clazz);
    }

    public int decodeClass(Addressable toDecode) {
        return decode(addressable, sig.getClazzAddress(), sig.getClazzSize());
    }

    private List<Selector> createSelectors() {
        List<Selector> sels = new ArrayList<Selector>();
        Integer[] adrs = sig.getGeneAddresses();
        for (int i = 0; i < adrs.length; i++) {
            sels.add(decodeSelector(addressable, i));
        }
        return sels;
    }

    public Selector decodeSelector(Addressable toDecode, int selId) {
        int i = sig.getGeneAddresses()[selId];
        boolean on = toDecode.get(i);
        boolean operatorBit = toDecode.get(i + 1);
        Operator op = operatorBit ? OpFactory.eq() : OpFactory.neq();
        Integer codeSize = sig.getValueCodeSizes()[selId];
        Domain domain = sig.getAttrDomain().get(selId);

        Object val = decode(toDecode, i + 2, codeSize);

        // Special numeric operator handling
        if (domain instanceof FloatDomain) {
            FloatDomain flomain = (FloatDomain) domain;
            op = operatorBit ? OpFactory.loet() : OpFactory.goet();
            val = flomain.adjust(val);
        }
        return new Selector(on, op, val);
    }

    private int decode(Addressable toDecode, int start, int size) {
        return dec.decode(toDecode, start, size);
    }
}
