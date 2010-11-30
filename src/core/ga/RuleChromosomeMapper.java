package core.ga;

/**
 *
 * @author Rekin
 */
public class RuleChromosomeMapper {

    final int typeNo = CodedType.values().length;

    public int getIndexFor(int gene, CodedType ctype) {
        return typeNo * gene + ctype.ordinal();
    }
}
