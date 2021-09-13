package com.medusa.gruul.common.core.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;

/**
 * The type Local date time utils.
 *
 * @description: LocalDateTimeUtils.java
 * @author: alan
 * @date: 2019 /10/20 14:21
 */
public class LocalDateTimeUtils {
    /**
     * Date转换为LocalDateTime
     *
     * @param date the date
     * @return local date time
     */
    public static LocalDateTime convertDateToLDT(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * LocalDateTime转换为Date
     *
     * @param time the time
     * @return java.util.Date date
     * @author alan
     * @date 2019 /10/20 14:21
     */
    public static Date convertLDTToDate(LocalDateTime time) {
        return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
    }


    /**
     * 获取指定日期的毫秒
     *
     * @param time the time
     * @return java.lang.Long milli by time
     * @author alan
     * @date 2019 /10/20 14:21
     */
    public static Long getMilliByTime(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * 获取指定日期的秒
     *
     * @param time the time
     * @return java.lang.Long seconds by time
     * @author alan
     * @date 2019 /10/20 14:21
     */
    public static Long getSecondsByTime(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant().getEpochSecond();
    }

    /**
     * 获取指定时间的指定格式
     *
     * @param time    the time
     * @param pattern the pattern
     * @return java.lang.String string
     * @author alan
     * @date 2019 /10/20 14:21
     */
    public static String formatTime(LocalDateTime time, String pattern) {
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * 获取当前时间的指定格式
     *
     * @param pattern the pattern
     * @return java.lang.String string
     * @author alan
     * @date 2019 /10/20 14:21
     */
    public static String formatNow(String pattern) {
        return formatTime(LocalDateTime.now(), pattern);
    }

    /**
     * 日期加上一个数,根据field不同加不同值,field为ChronoUnit.*
     *
     * @param time   the time
     * @param number the number
     * @param field  the field
     * @return java.time.LocalDateTime local date time
     * @author alan
     * @date 2019 /10/20 14:21
     */
    public static LocalDateTime plus(LocalDateTime time, long number, TemporalUnit field) {
        return time.plus(number, field);
    }

    /**
     * 日期减去一个数,根据field不同减不同值,field参数为ChronoUnit.*
     *
     * @param time   the time
     * @param number the number
     * @param field  the field
     * @return java.time.LocalDateTime local date time
     * @author alan
     * @date 2019 /10/20 14:21
     */
    public static LocalDateTime minu(LocalDateTime time, long number, TemporalUnit field) {
        return time.minus(number, field);
    }

    /**
     * 获取两个日期的差  field参数为ChronoUnit.*
     *
     * @param startTime the start time
     * @param endTime   the end time
     * @param field     单位(年月日时分秒)
     * @return long
     */
    public static long betweenTwoTime(LocalDateTime startTime, LocalDateTime endTime, ChronoUnit field) {
        Period period = Period.between(LocalDate.from(startTime), LocalDate.from(endTime));
        if (field == ChronoUnit.YEARS) {
            return period.getYears();
        }
        if (field == ChronoUnit.MONTHS) {
            return period.getYears() * 12 + period.getMonths();
        }
        return field.between(startTime, endTime);
    }

    /**
     * 获取一天的开始时间，2017,7,22 00:00
     *
     * @param time the time
     * @return java.time.LocalDateTime day start
     * @author alan
     * @date 2019 /10/20 14:21
     */
    public static LocalDateTime getDayStart(LocalDateTime time) {
        return time.withHour(0)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
    }

    /**
     * 获取一天的结束时间，2017,7,22 23:59:59.999999999
     *
     * @param time the time
     * @return java.time.LocalDateTime day end
     * @author alan
     * @date 2019 /10/20 14:21
     */
    public static LocalDateTime getDayEnd(LocalDateTime time) {
        return time.withHour(23)
                .withMinute(59)
                .withSecond(59)
                .withNano(999999999);
    }


    /**
     * 将long类型的timestamp转为LocalDateTime
     *
     * @param timestamp the timestamp
     * @return java.time.LocalDateTime date time of timestamp
     * @author alan
     * @date 2020 /3/7 下午4:29
     */
    public static LocalDateTime getDateTimeOfTimestamp(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }
}
