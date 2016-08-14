package com.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by pavel.sopher on 7/12/2016.
 */
public class DateUtils {
    public static final String DateFormatAmPmHMS = "yyyy-MM-dd HH:mm:ss.SSS";

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
