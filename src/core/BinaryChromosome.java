package core;

import core.ga.Addressable;
import java.util.BitSet;

/**
 * @author Rekin
 */
public class BinaryChromosome implements Addressable {

    BitSet data;
    
    private BinaryChromosome(BitSet data) {
        this.data = data;
    }
    public BinaryChromosome(int length) {
        data = new BitSet(length);
    }

    public boolean get(int address) {
        return data.get(address);
    }

    public void set(int address, boolean b) {
        data.set(address, b);
    }

    @Override
    public String toString() {
        return data.toString();
    }

    public BinaryChromosome copy() {
        return new BinaryChromosome((BitSet)data.clone());
    }
}
