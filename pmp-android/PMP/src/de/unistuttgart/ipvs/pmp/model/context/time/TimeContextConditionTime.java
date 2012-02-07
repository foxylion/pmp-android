package de.unistuttgart.ipvs.pmp.model.context.time;

import java.util.TimeZone;

/**
 * Class to store a specific time for {@link TimeContextCondition}.
 * 
 * @author Tobias Kuhn
 * 
 */
public class TimeContextConditionTime implements Comparable<TimeContextConditionTime> {
    
    public static final int SECONDS_PER_MINUTE = 60;
    public static final int MINUTES_PER_HOUR = 60;
    public static final int HOURS_PER_DAY = 24;
    public static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;
    public static final int SECONDS_PER_DAY = SECONDS_PER_HOUR * HOURS_PER_DAY;
    
    private int hour, minute, second;
    
    
    public TimeContextConditionTime(int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }
    
    
    public TimeContextConditionTime(TimeContextConditionTime timeContextConditionTime) {
        this.hour = timeContextConditionTime.hour;
        this.minute = timeContextConditionTime.minute;
        this.second = timeContextConditionTime.second;
    }
    
    
    public int getHour() {
        return this.hour;
    }
    
    
    /**
     * Sets a lenient hour.
     * 
     * @param hour
     */
    public void setHour(int hour) {
        this.hour = hour % HOURS_PER_DAY;
    }
    
    
    public int getMinute() {
        return this.minute;
    }
    
    
    /**
     * Sets a lenient minute.
     * 
     * @param minute
     */
    public void setMinute(int minute) {
        int lenientHrs = minute / MINUTES_PER_HOUR;
        if (lenientHrs != 0) {
            setHour(getHour() + lenientHrs);
        }
        this.minute = minute % MINUTES_PER_HOUR;
    }
    
    
    public int getSecond() {
        return this.second;
    }
    
    
    /**
     * Sets a lenient second.
     * 
     * @param second
     */
    public void setSecond(int second) {
        int lenientMins = second / SECONDS_PER_MINUTE;
        if (lenientMins != 0) {
            setMinute(getMinute() + lenientMins);
        }
        this.second = second % SECONDS_PER_MINUTE;
    }
    
    
    /**
     * Converts this time from a specific {@link TimeZone} to another. <b>The result is only valid for <i>right
     * now</i>!</b> * (Or whatever <i>right now</i> is when calculating between TimeZones...) This is because daylight
     * saving times must be respected.
     * 
     * @param timeZone
     */
    public void convertTimeZone(TimeZone from, TimeZone to) {
        long now = System.currentTimeMillis();
        int secDiff = (to.getOffset(now) - from.getOffset(now)) / 1000;
        setSecond(getSecond() + secDiff);
    }
    
    
    /**
     * Calculates the difference in seconds from <code>this</code> to <code>to</code>.
     * 
     * @param to
     * @param assumeNextDayIfWrap
     *            if set to true and <code>to</code> is earlier than <code>this</code>, it is assumed that
     *            <code>to</code> lies on the next day
     * @return
     */
    public int getDifferenceInSeconds(TimeContextConditionTime to, boolean assumeNextDayIfWrap) {
        if (assumeNextDayIfWrap && (this.compareTo(to) > 0)) {
            return SECONDS_PER_DAY - to.getDifferenceInSeconds(this, false);
        } else {
            return (to.second - this.second) + (to.minute - this.minute) * SECONDS_PER_MINUTE + (to.hour - this.hour)
                    * SECONDS_PER_HOUR;
        }
        
    }
    
    
    @Override
    public boolean equals(Object o) {
        if ((o == null) || (!(o instanceof TimeContextConditionTime))) {
            return false;
        }
        TimeContextConditionTime another = (TimeContextConditionTime) o;
        return (this.second == another.second) && (this.minute == another.minute) && (this.hour == another.hour);
    }
    
    
    @Override
    public int hashCode() {
        return this.second + SECONDS_PER_MINUTE * this.minute + SECONDS_PER_HOUR * this.hour;
    }
    
    
    @Override
    public int compareTo(TimeContextConditionTime another) {
        return (this.second - another.second) + (this.minute - another.minute) + (this.hour - another.hour);
    }
}