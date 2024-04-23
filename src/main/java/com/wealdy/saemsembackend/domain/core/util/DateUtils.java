package com.wealdy.saemsembackend.domain.core.util;

import java.time.LocalDate;

public class DateUtils {

    public static LocalDate convertFirstDayOfMonth(String date) {
        return LocalDate.parse(date + "-01");
    }
}
