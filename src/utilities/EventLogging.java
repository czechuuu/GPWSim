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
}
