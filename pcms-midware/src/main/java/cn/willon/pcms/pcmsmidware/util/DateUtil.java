package cn.willon.pcms.pcmsmidware.util;

/**
 * DateUtil
 *
 * @author Willon
 * @since 2019-04-25
 */
public class DateUtil {


    private static final int ONE = 1;
    private static final int TWO = 2;

    public static String get(String src) {

        StringBuilder sb = new StringBuilder("0");
        if (src.length() == TWO) {
            return src;
        }
        if (src.length() == ONE) {
            return sb.append(src).toString();
        }
        return "";

    }
}
