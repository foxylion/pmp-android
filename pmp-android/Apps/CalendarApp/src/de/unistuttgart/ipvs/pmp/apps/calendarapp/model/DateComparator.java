package de.unistuttgart.ipvs.pmp.apps.calendarapp.model;

import java.util.Comparator;
import java.util.Date;

/**
 * A comparator to compare two {@link Date} objects. Objects will be sorted ascending, oldest dates first.
 * 
 * @author Thorsten Berberich
 * 
 */
public class DateComparator implements Comparator<Appointment> {
    
    @Override
    public int compare(Appointment todo1, Appointment todo2) {
        Date date1 =todo1.getDate();
        Date date2 = todo2.getDate();
        return date1.compareTo(date2);
    }
}
