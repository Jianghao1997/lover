package loversmission.hoodee.common;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @version 1.0
 * @author: jianghao
 * @createTime: 2022年08月13日 16:09
 */
public class TimeUtils {

    public static boolean judgeTimeIsToday(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = instant.atZone(zoneId).toLocalDateTime();
        LocalDateTime startTime = LocalDate.now().atTime(0, 0, 0);
        LocalDateTime endTime = LocalDate.now().atTime(23, 59, 59);
        //如果大于今天的开始日期，小于今天的结束日期
        if (localDateTime.isAfter(startTime) && localDateTime.isBefore(endTime)) {
            return true;
        }
        return false;
    }
}
