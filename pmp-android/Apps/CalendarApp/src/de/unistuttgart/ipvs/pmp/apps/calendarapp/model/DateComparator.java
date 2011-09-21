package de.unistuttgart.ipvs.pmp.apps.calendarapp.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;

/**
 * A comparator to compare two {@link Date} objects. Objects will be sorted ascending, oldest dates first.
 * 
 * @author Thorsten Berberich
 * 
 */
public class DateComparator implements Comparator<Date> {
    
    @Override
    public int compare(Date date1, Date date2) {
        java.util.Date parsedDate1 = new java.util.Date();
        java.util.Date parsedDate2 = new java.util.Date();
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            parsedDate1 = sdf.parse(date1.getDate());
            parsedDate2 = sdf.parse(date2.getDate());
        } catch (ParseException e) {
        }
        return parsedDate1.compareTo(parsedDate2);
    }
}
