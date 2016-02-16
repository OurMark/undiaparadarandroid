package itba.undiaparadar.utils;

/**
 * This class contains all constants for th app
 *
 * @author Martin Purita - martinpurita@gmail.com
 */
public final class Constants {

    /**
     * This class contains all the constants values
     */
    public static class Value {
        public static final int MIN_RADIUS = 100;
    }

    /**
     * This class is use when you want to put/get an extra to a
     * {@link android.os.Bundle} or {@link android.content.Intent}
     */
    public static class ExtraKeys {
        public static final String TOPICS = "topics";
        public static final String RADIUS = "radius";
        public static final String DONE = "done";
        public static final String PLEDGES = "pledges";
    }

    /**
     * This class contains all the result code for activities and fragments
     */
    public static class ResultCode {
        public static final int FILTER = 1000;
    }
}
