package com.medusa.gruul.common.core.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

/**
 * @author whh
 */
public class DateUtils {

    public static LocalDateTime timestampCoverLocalDateTime(long timestamp) {
        DateTime expiresIn = DateUtil.date(timestamp);
        return expiresIn.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static long localDateTimeCoverTimestamp(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.of("+8")).toEpochMilli();
    }
}
