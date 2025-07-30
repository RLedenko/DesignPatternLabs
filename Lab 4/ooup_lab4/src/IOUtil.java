public class IOUtil {
    public static boolean containsOnlyNumbers(String data) {
        return !data.contains("[a-zA-Z]+");
    }

    public static Point pointFromFileEntry(String x, String y) {
        if(!containsOnlyNumbers(x) || !containsOnlyNumbers(y))
            return null;
        return new Point(Integer.parseInt(x), Integer.parseInt(y));
    }
}
