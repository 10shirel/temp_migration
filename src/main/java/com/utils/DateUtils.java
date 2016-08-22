package com.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Shirel Azulay on 7/12/2016.
 */
public class DateUtils {
    public static final String DATE_FORMAT_AM_PM_HMS = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * Convert String To Date
     * @param dateFormat
     * @param originalDate
     * @return
     */
    public static Date convertStringToDate(String dateFormat, String originalDate) {
        if (originalDate == null || originalDate.isEmpty())
            return null;
        Date date = null;
        try {
            date = new SimpleDateFormat(dateFormat).parse(originalDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }


}
