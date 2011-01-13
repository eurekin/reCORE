package core.ga;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import static java.lang.Math.*;
import core.io.repr.col.DomainMemoizable;

/**
 * 
 * Syntax
 *
 * Rule Genotype -> Gene1 [Gene2]* Class
 * Gene          -> Switch AtrId Rel Val
 * Switch        -> on | off
 * AtrId         -> Integer
 * Rel           -> == | != 
 * Value         -> Integer
 * Class         -> Integer
 * 
 * Semantics
 *
 * AtrId  - index of column in range [0, no of columns-1]. Derived from column
 *         size
 * Rel    - normal interpretation
 * Value  - pointer to one of column's domain value
 * Switch - turns Gene on or off
 * 
 * Coding size
 * 
 * AtrId    - ceil(log2(nc)) where cn is the no of columns
 * Rel      - 2
 * Value    - ceil(log2(nv)) where nv is the number of values in domain
 * Switch   - 2
 * 
 * Rule
 * 
 * IF Attr1 Rel_1_1 Value_1_1 AND Attr2 Rel_1_2 Value_1_2 then Class_1
 * 
 * Genes have different lengths.
 * 
 * Switch, Relation, AtrId have constant length of 1, 1 and .
 * 
 * @author Rekin
 * 
 */
public class RuleChromosomeSignature {

    Set classDomain;
    Integer clazzAddress;
    List<Set> attrDomain;
    Integer[] geneAddresses;
    Integer[] valueCodeSizes;
    final int relCodeSize = 1;
    final int switchCodeSize = 1;

    public RuleChromosomeSignature(List<DomainMemoizable> attributes,
            DomainMemoizable classCol) {
        attrDomain = new ArrayList<Set>();
        valueCodeSizes = new Integer[attributes.size()];
        int i = 0;
        for (DomainMemoizable att : attributes) {
            attrDomain.add(att.getDomain());
            valueCodeSizes[i++] = noOfBitsToEncode(att.getDomain().size());
        }
        classDomain = classCol.getDomain();
        calculateGeneAddresses();
    }

    public Integer getClazzAddress() {
        return clazzAddress;
    }

    public Integer getClazzSize() {
        return getBits() - clazzAddress;
    }

    public Integer[] getGeneAddresses() {
        return geneAddresses;
    }

    public List<Set> getAttrDomain() {
        return attrDomain;
    }

    public Set getClassDomain() {
        return classDomain;
    }

    public int getRelCodeSize() {
        return relCodeSize;
    }

    public int getSwitchCodeSize() {
        return switchCodeSize;
    }

    public Integer[] getValueCodeSizes() {
        return valueCodeSizes;
    }

    public int getBits() {
        int sum = 0;
        for (Set set : attrDomain) {
            sum += noOfBitsToEncode(set.size()) + switchCodeSize + relCodeSize;
        }
        sum += noOfBitsToEncode(classDomain.size());
        return sum;
    }

    private int noOfBitsToEncode(int n) {
        return max(1, (int) ceil(log10(n) / log10(2)));
    }

    private void calculateGeneAddresses() {
        final int n = valueCodeSizes.length;
        geneAddresses = new Integer[n];
        geneAddresses[0] = 0;
        for (int i = 1; i < n; i++) {
            geneAddresses[i] = geneAddresses[i - 1] + getSizeOfGeneNo(i - 1);
        }
        clazzAddress = geneAddresses[n - 1] + getSizeOfGeneNo(n - 1);
    }

    private int getSizeOfGeneNo(int no) {
        return switchCodeSize + relCodeSize + valueCodeSizes[no];
    }
}
