package com.hola.hola.util;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

//@SuppressLint("SimpleDateFormat")
public class DateFormatUtil {
    private static SimpleDateFormat serverFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    private static SimpleDateFormat lastSeenTodayFormat = new SimpleDateFormat("'Last seen' KK:mm a", Locale.US);
    private static SimpleDateFormat lastSeenFormat = new SimpleDateFormat("'Last seen' MMMM dd 'at' KK:mm a", Locale.US);

    private static SimpleDateFormat lastMessageDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.US);
    private static SimpleDateFormat lastMessageDateTodayFormat = new SimpleDateFormat("KK:mm a", Locale.US);

    public static String formatLastSeen(String dateString){
        try {
            Date date = serverFormat.parse(dateString);
            if(isToday(date)){
                return lastSeenTodayFormat.format(date);
            } else {
                return lastSeenFormat.format(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString;
        }
    }

    public static String formatLastMessageDate(String dateString){
        try{
            Date date = serverFormat.parse(dateString);
            if(isToday(date)){
                return lastMessageDateTodayFormat.format(date);
            } else {
                return lastMessageDateFormat.format(date);
            }
        } catch (ParseException e){
            e.printStackTrace();
            return dateString;
        }
    }

    private static boolean isToday(Date date){
        Calendar now = Calendar.getInstance();
        Calendar then = Calendar.getInstance(); then.setTimeInMillis(date.getTime());
        return now.get(Calendar.DAY_OF_YEAR) == then.get(Calendar.DAY_OF_YEAR)
                && now.get(Calendar.YEAR) == then.get(Calendar.YEAR);
    }
}
