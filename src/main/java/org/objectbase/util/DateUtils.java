package org.objectbase.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DateUtils {
    
    public static String formatDate(Date date) {
            
        if (date == null) return null;
        
        DateFormat tmp = (DateFormat)dateFormat.clone();
        return tmp.format(date);
    }
    
    public static Date parseDate(String dateString) {
            
        if (dateString == null) return null;

        try {
            DateFormat tmp = (DateFormat)dateFormat.clone();
            tmp.setLenient(false);
            return tmp.parse(dateString);
        }
        catch (ParseException e) {
            return null;
        }
    }
    
    private static final DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
}
