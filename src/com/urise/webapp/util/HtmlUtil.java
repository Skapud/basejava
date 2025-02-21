package com.urise.webapp.util;

import com.urise.webapp.model.Periods;

public class HtmlUtil {
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static String formatDates(Periods periods) {
        return DateUtil.format(periods.getStartDate()) + " - " + DateUtil.format(periods.getEndDate());
    }

}
