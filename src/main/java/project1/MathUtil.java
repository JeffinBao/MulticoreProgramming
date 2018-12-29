package project1;

/**
 * Author: baojianfeng
 * Date: 2018-09-21
 */
public class MathUtil {

    /**
     * calculate the log base 2 value and get its ceiling final value
     * @param n input
     * @return log value
     */
    public static int log2Ceil(int n) {
        if (n == 0)
            return 0;

        double val = Math.log10(n) / Math.log10(2);
        return (int) Math.ceil(val);
    }

    /**
     * calculate the log base 2 value and get its floor final value
     * @param n n
     * @return log value
     */
    public static int log2Floor(int n) {
        if (n == 0)
            return 0;

        double val = Math.log10(n) / Math.log10(2);
        return (int) Math.floor(val);
    }
}
