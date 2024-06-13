package utilities;

public class EventLogging {
    public static boolean loggingEnabled = true;

    public static boolean isLoggingEnabled() {
        return loggingEnabled;
    }

    public static void setLoggingEnabled(boolean loggingEnabled) {
        EventLogging.loggingEnabled = loggingEnabled;
    }

    public static void log(String message) {
        if (loggingEnabled) {
            System.out.println(message);
        }
    }

    public static class Color {
        public static String red(String message) {
            return "\u001B[31m" + message + "\u001B[0m";
        }

        public static String green(String message) {
            return "\u001B[32m" + message + "\u001B[0m";
        }

        public static String yellow(String message) {
            return "\u001B[33m" + message + "\u001B[0m";
        }

        public static String blue(String message) {
            return "\u001B[34m" + message + "\u001B[0m";
        }
    }

}
