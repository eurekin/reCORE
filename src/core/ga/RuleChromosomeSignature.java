package core.ga;

import core.io.repr.col.Domain;
import java.util.List;
import java.util.ArrayList;
import core.io.repr.col.DomainMemoizable;
import core.io.repr.col.IntegerDomain;

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
 * 22.01.2011
 *
 * Modification is needed to handle numerical values
 *
 * At first, I'll implement only '>=' and '<=' operators.
 *
 * @author Rekin
 * 
 */
public class RuleChromosomeSignature {

    IntegerDomain classDomain;
    Integer clazzAddress;
    List<Domain> attrDomain;
    Integer[] geneAddresses;
    Integer[] valueCodeSizes;
    final int relCodeSize = 1;
    final int switchCodeSize = 1;
    Class[] types;

    public RuleChromosomeSignature(List<DomainMemoizable> attributes,
            DomainMemoizable classCol) {
        attrDomain = new ArrayList<Domain>();
        valueCodeSizes = new Integer[attributes.size()];
        int i = 0;
        for (DomainMemoizable att : attributes) {
            attrDomain.add(att.getDomain());
            valueCodeSizes[i++] = att.getDomain().bitSize();
        }
        classDomain = (IntegerDomain) classCol.getDomain();
        calculateGeneAddresses();
//        debugValues();
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

    public List<Domain> getAttrDomain() {
        return attrDomain;
    }

    public IntegerDomain getClassDomain() {
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
        for (Domain dom : attrDomain) {
            sum += dom.bitSize() + switchCodeSize + relCodeSize;
        }
        sum += classDomain.bitSize();
        return sum;
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

    private void debugValues() {
        System.out.println("Signature debug:");
        for (Domain domain : attrDomain) {
            System.out.println(domain.getClass().getName() + domain.toString());
        }
    }
}
