package com.viecinema.moviebooking.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DateUtils {

    private static final Map<Integer, String> dayOfWeekMap = new HashMap<>();

    static {
        dayOfWeekMap.put(1, "T2");
        dayOfWeekMap.put(2, "T3");
        dayOfWeekMap.put(3, "T4");
        dayOfWeekMap.put(4, "T5");
        dayOfWeekMap.put(5, "T6");
        dayOfWeekMap.put(6, "T7");
        dayOfWeekMap.put(7, "CN");
    }

    public static String getDayOfWeekInVietnamese(int dayOfWeek) {
        return dayOfWeekMap.get(dayOfWeek);
    }
}

