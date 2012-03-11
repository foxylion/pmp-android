package de.unistuttgart.ipvs.pmp.test.model.context.time;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import android.test.InstrumentationTestCase;
import de.unistuttgart.ipvs.pmp.model.context.time.TimeContextCondition;
import de.unistuttgart.ipvs.pmp.model.context.time.TimeContextIntervalType;
import de.unistuttgart.ipvs.pmp.model.context.time.TimeContextTime;
import de.unistuttgart.ipvs.pmp.model.exception.InvalidConditionException;

public class TimeContextConditionTest extends InstrumentationTestCase {
    
    /**
     * Test the condition creation and resulting string representation.
     */
    public void testConditionCreation() {
        TimeContextTime timeBegin = new TimeContextTime();
        timeBegin.setHour(10);
        timeBegin.setMinute(10);
        timeBegin.setSecond(10);
        
        TimeContextTime timeEnd = new TimeContextTime(timeBegin);
        timeEnd.setSecond(11);
        
        List<Integer> days = new ArrayList<Integer>();
        
        TimeContextCondition condition = new TimeContextCondition(true, timeBegin, timeEnd,
                TimeContextIntervalType.REPEAT_DAILY, days);
        
        /* In the following no trailing comma is expected. */
        
        assertEquals("Given string representation is not like expceted", "utc10:10:10-10:10:11-D", condition.toString());
        
        days.add(1);
        assertEquals("Given string representation is not like expceted", "utc10:10:10-10:10:11-D1",
                condition.toString());
        
        days.add(2);
        assertEquals("Given string representation is not like expceted", "utc10:10:10-10:10:11-D1,2",
                condition.toString());
    }
    
    
    /**
     * Test the condition creation with null parameters.
     */
    public void testConditionCreationWithNullParameters() {
        try {
            new TimeContextCondition(true, null, null, null, null).toString();
        } catch (Throwable e) {
            fail("No exception was expected, but " + e.getClass().getSimpleName() + " was thrown.");
        }
    }
    
    
    public void testConditionCreationFromStringRepresentation() {
        /* Test a valid one */
        String conditionString = "utc10:10:10-10:10:11-D1";
        try {
            TimeContextCondition condition = TimeContextCondition.parse(conditionString);
            assertEquals("Should be the same as input.", conditionString, condition.toString());
        } catch (InvalidConditionException e) {
            fail("Did not expect an exception.");
        }
        
        /* Test a invalid ones */
        conditionString = "utc29:10:10-29:10:11-D1,2,3,4,,,5";
        try {
            TimeContextCondition.parse(conditionString);
            fail("Expected InvalidConditionException exception to be thrown");
        } catch (InvalidConditionException e) {
        }
        
        conditionString = "utc29:10:10-29:10:11-D1,2,3,4,247182750235723853125138,5";
        try {
            TimeContextCondition.parse(conditionString);
            fail("Expected InvalidConditionException exception to be thrown");
        } catch (InvalidConditionException e) {
        }
    }
    
    
    public void testConditionSatisfaction() {
        /*
         * We assume that currently is the 2012-03-12 at 10:10:20.
         * that means it is Monday, second day of the week (Sunday is first)
         */
        
        TimeContextTime timeBegin = new TimeContextTime();
        timeBegin.setHour(10);
        timeBegin.setMinute(10);
        timeBegin.setSecond(10);
        
        TimeContextTime timeEnd = new TimeContextTime(timeBegin);
        timeEnd.setSecond(30);
        
        List<Integer> days = new ArrayList<Integer>();
        days.add(2);
        days.add(12);
        
        TimeContextCondition conditionDaily = new TimeContextCondition(true, timeBegin, timeEnd,
                TimeContextIntervalType.REPEAT_DAILY, days);
        
        TimeContextCondition conditionWeekly = new TimeContextCondition(true, timeBegin, timeEnd,
                TimeContextIntervalType.REPEAT_WEEKLY, days);
        
        TimeContextCondition conditionMonthly = new TimeContextCondition(true, timeBegin, timeEnd,
                TimeContextIntervalType.REPEAT_WEEKLY, days);
        TimeContextCondition conditionYearly = new TimeContextCondition(true, timeBegin, timeEnd,
                TimeContextIntervalType.REPEAT_WEEKLY, days);
        
        GregorianCalendar validDate = new GregorianCalendar(2012, 2, 12, 10, 10, 20);
        GregorianCalendar invalidDate = new GregorianCalendar(2012, 2, 12, 10, 10, 40);
        
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(validDate.getTimeInMillis());
        
        assertTrue(conditionDaily.satisfiedIn(validDate.getTimeInMillis()));
        assertFalse(conditionDaily.satisfiedIn(invalidDate.getTimeInMillis()));
        
        assertTrue(conditionWeekly.satisfiedIn(validDate.getTimeInMillis()));
        assertFalse(conditionWeekly.satisfiedIn(invalidDate.getTimeInMillis()));
        
        assertTrue(conditionMonthly.satisfiedIn(validDate.getTimeInMillis()));
        assertFalse(conditionMonthly.satisfiedIn(invalidDate.getTimeInMillis()));
        
        assertTrue(conditionYearly.satisfiedIn(validDate.getTimeInMillis()));
        assertFalse(conditionYearly.satisfiedIn(invalidDate.getTimeInMillis()));
    }
}
