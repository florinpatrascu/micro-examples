package controllers.utils;

/**
 * Simple implementation of a bijective function as discussed here:
 * - http://stackoverflow.com/questions/742013/how-to-code-a-url-shortener/742047#742047
 *
 * @author <a href="mailto:florin.patrascu@gmail.com">Florin T.PATRASCU</a>
 * @since $Revision$ (created: 2013-04-14)
 */
public class Bijective {
    // safe ALPHABET, removed: 0, oO, Ll, Ii
    private static final String ALPHABET = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ123456789";
    private static final int BASE = ALPHABET.length();

    public static String encode(int num) {
        if (num > 0) {
            StringBuilder sb = new StringBuilder();

            while (num > 0) {
                sb.append(ALPHABET.charAt(num % BASE));
                num /= BASE;
            }
            return sb.reverse().toString();
        } else {
            return ALPHABET.substring(0, 1);
        }
    }

    public static int decode(String str) {
        int num = 0;

        for (int i = 0, len = str.length(); i < len; i++) {
            num = num * BASE + ALPHABET.indexOf(str.charAt(i));
        }

        return num;
    }
}
