package rage.sqltrainer.databasecomparison;

public class StringUtils {

    public static String getNameFromEnd(String name) {
        return name.substring(name.lastIndexOf(".") + 1);
    }

    public static String sSuffixFromCount(int count) {
        return count == 1 ? "" : "s";
    }
}
