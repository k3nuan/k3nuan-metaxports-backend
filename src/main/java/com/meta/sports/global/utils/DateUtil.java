package com.meta.sports.global.utils;

import java.time.LocalDate;
import java.time.Period;

public class DateUtil {

    public static Integer getAge(LocalDate birth) {
        return Period.between(birth, LocalDate.now()).getYears();
    }
}
