package com.solexgames.challenges.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author GrowlyX
 * @since 6/12/2021
 */

@UtilityClass
public class DateValidationUtil {

    public static boolean isSameDay(Date previous, Date newDate) {
        final LocalDate prevLocalDate = previous.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        final LocalDate newLocalDate = newDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        return prevLocalDate.isEqual(newLocalDate);
    }
}
