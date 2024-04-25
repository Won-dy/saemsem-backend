package com.wealdy.saemsembackend.domain.core.util;

import java.time.LocalDate;

public class DateUtils {

    public static LocalDate convertFirstDayOfMonth(int year, int month) {
        return LocalDate.of(year, month, 1);
    }
}
