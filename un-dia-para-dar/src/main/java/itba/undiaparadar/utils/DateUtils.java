package itba.undiaparadar.utils;

import java.util.Date;

public class DateUtils {

    public static long daysBetween(final Date endDate, final Date startDate) {
        long diff = endDate.getTime() - startDate.getTime();
        return diff/(1000*60*60*24);
    }
}
