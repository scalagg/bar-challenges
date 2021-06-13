package com.solexgames.challenges.util;

import lombok.experimental.UtilityClass;

import java.time.ZoneId;
import java.util.Date;

/**
 * @author GrowlyX
 * @since 6/12/2021
 */

@UtilityClass
public class DateValidationUtil {

    public static boolean isSameDay(Date previous, Date newDate) {
        final int prevYearDay = previous.toInstant()
                .atZone(ZoneId.of("US/Eastern"))
                .toLocalDate().getDayOfYear();
        final int newYearDay = newDate.toInstant()
                .atZone(ZoneId.of("US/Eastern"))
                .toLocalDate().getDayOfYear();

        return prevYearDay == newYearDay;
    }
}
