package utilities;

public class EventLogging {
    public static boolean loggingEnabled = true;

    /**
     * Returns whether logging is enabled.
     *
     * @return true if logging is enabled, false otherwise
     */
    public static boolean isLoggingEnabled() {
        return loggingEnabled;
    }

    /**
     * Sets whether logging is enabled.
     *
     * @param loggingEnabled whether logging is enabled
     */
    public static void setLoggingEnabled(boolean loggingEnabled) {
        EventLogging.loggingEnabled = loggingEnabled;
    }

    /**
     * Logs the given message, if logging is enabled.
     *
     * @param message the message to log
     */
    public static void log(String message) {
        if (loggingEnabled) {
            System.out.println(message);
        }
    }

    /**
     * Inner class for printing colored messages.
     */
    public static class Color {
        /**
         * Returns the given message in red color.
         *
         * @param message the message
         * @return the given message in red color
         */
        public static String red(String message) {
            return "\u001B[31m" + message + "\u001B[0m";
        }

        /**
         * Returns the given message in green color.
         *
         * @param message the message
         * @return the given message in green color
         */
        public static String green(String message) {
            return "\u001B[32m" + message + "\u001B[0m";
        }

        /**
         * Returns the given message in yellow color.
         *
         * @param message the message
         * @return the given message in yellow color
         */
        public static String yellow(String message) {
            return "\u001B[33m" + message + "\u001B[0m";
        }

        /**
         * Returns the given message in blue color.
         *
         * @param message the message
         * @return the given message in blue color
         */
        public static String blue(String message) {
            return "\u001B[34m" + message + "\u001B[0m";
        }

        /**
         * Returns the given message in purple color.
         *
         * @param message the message
         * @return the given message in purple color
         */
        public static String purple(String message) {
            return "\u001B[35m" + message + "\u001B[0m";
        }
    }

}
