package de.unistuttgart.ipvs.pmp.model.context.time;

/**
 * The parsed condition for a {@link TimeContext}.
 * 
 * @author Tobias Kuhn
 * 
 */
public class TimeContextCondition {
    
    /**
     * Parses a {@link TimeContextCondition} from a string.
     * 
     * @param condition
     * @return
     */
    public static TimeContextCondition parse(String condition) {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    private TimeContextCondition(int type, long start, long end) {
        
        // TODO Auto-generated constructor stub
    }
    
    
    /**
     * Checks whether the condition is satisfied in the state
     * 
     * @param state
     * @return
     */
    public boolean satisfiedIn(long state) {
        // TODO Auto-generated method stub
        return false;
    }
    
}
