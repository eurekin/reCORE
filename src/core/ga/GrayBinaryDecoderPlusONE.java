package core.ga;

/**
 *
 * @author Rekin
 */
public class GrayBinaryDecoderPlusONE implements BinaryDecoder {

    public int decode(Addressable code, int start, int n) {
        boolean bit = code.get(start);
        int d = bit ? 1 : 0;
        for (int i = start + 1, end = start + n; i < end; i++) {
            d <<= 1;
            bit = code.get(i) ^ bit;
            if (bit) {
                d++;
            }
        }
        return d  + 1;
    }
}
