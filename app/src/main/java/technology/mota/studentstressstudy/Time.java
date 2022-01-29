package technology.mota.studentstressstudy;

import java.util.Calendar;
import java.util.TimeZone;


public class Time {
    public static final long THREE_DAY = 24 * 60 * 60 * 1000 * 30 * 12;
    public static final long THREE_DAY_START_UTC_TIME;
    public  static final long TODAY_START_UTC_TIME;
    static {
        TODAY_START_UTC_TIME = getTodayStartUtcTime();
        THREE_DAY_START_UTC_TIME = TODAY_START_UTC_TIME - THREE_DAY;
    }
    private static long getTodayStartUtcTime() {
        Calendar today = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        return today.getTimeInMillis();
    }
}
