package de.unistuttgart.ipvs.pmp.test.model.context;

import java.util.ArrayList;

import android.test.InstrumentationTestCase;
import de.unistuttgart.ipvs.pmp.model.context.time.TimeContextCondition;
import de.unistuttgart.ipvs.pmp.model.context.time.TimeContextIntervalType;
import de.unistuttgart.ipvs.pmp.model.context.time.TimeContextTime;

public class TimeConditionTest extends InstrumentationTestCase {
    
    public void testItWorks() throws Exception {
        TimeContextCondition tcc = new TimeContextCondition(false, new TimeContextTime(1, 0, 0), new TimeContextTime(4,
                4, 4), TimeContextIntervalType.REPEAT_DAILY, new ArrayList<Integer>());
        System.out.println(tcc.toString());
        TimeContextCondition tcc2 = TimeContextCondition.parse(tcc.toString());
        assertEquals(tcc, tcc2);
    }
    
}
