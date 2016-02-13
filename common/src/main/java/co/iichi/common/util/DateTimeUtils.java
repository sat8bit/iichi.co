package co.iichi.common.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Date;

/**
 * Created by sat8bit on 2016/02/11.
 */
public class DateTimeUtils {
    public static ZonedDateTime now() {
        return ZonedDateTime.now();
    }

    public static String toString(ZonedDateTime zonedDateTime) {
        return new DateTimeFormatterBuilder()
                .appendValue(ChronoField.YEAR)
                .appendLiteral("/")
                .appendValue(ChronoField.MONTH_OF_YEAR)
                .appendLiteral("/")
                .appendValue(ChronoField.DAY_OF_MONTH)
                .appendLiteral(" ")
                .appendValue(ChronoField.HOUR_OF_DAY)
                .appendLiteral(":")
                .appendValue(ChronoField.MINUTE_OF_HOUR)
                .appendLiteral(":")
                .appendValue(ChronoField.SECOND_OF_MINUTE)
                .toFormatter().format(zonedDateTime);
    }

    public static ZonedDateTime toZonedDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.of("Asia/Tokyo"));
    }

    public static Date toDate(ZonedDateTime zonedDateTime) {
        return Date.from(zonedDateTime.toInstant());
    }
}
