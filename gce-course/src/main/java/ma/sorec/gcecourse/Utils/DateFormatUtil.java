package ma.sorec.gcecourse.Utils;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.Date;

public class DateFormatUtil {

    public static String toFullDateFormat(Date date) {
        DateFormat fullDateFormat = DateFormat.getDateTimeInstance(
                DateFormat.FULL,
                DateFormat.FULL);
        return fullDateFormat.format(date);
    }

    public static String toLongDateFormat(Date date) {
        DateFormat longDateFormat = DateFormat.getDateTimeInstance(
                DateFormat.LONG,
                DateFormat.LONG);
        return longDateFormat.format(date);
    }

    public static String toShortDateFormat(Date date) {
        DateFormat shortDateFormat = DateFormat.getDateTimeInstance(
                DateFormat.SHORT,
                DateFormat.SHORT);
        return shortDateFormat.format(date);
    }

    public static String toMediumDateFormat(Date date) {
        DateFormat mediumDateFormat = DateFormat.getDateTimeInstance(
                DateFormat.MEDIUM,
                DateFormat.MEDIUM);
        return mediumDateFormat.format(date);
    }

    public static String toFrenchFormat(LocalDate date) {
        return "Le " + date.getDayOfMonth() + "/" + date.getMonthValue() + "/" + date.getYear();
    }
}
