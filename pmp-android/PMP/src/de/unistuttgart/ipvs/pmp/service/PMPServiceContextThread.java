package de.unistuttgart.ipvs.pmp.service;

import java.util.logging.Level;

import de.unistuttgart.ipvs.pmp.model.Model;
import de.unistuttgart.ipvs.pmp.model.context.IContext;
import de.unistuttgart.ipvs.pmp.util.FileLog;

/**
 * A thread on the {@link PMPService} to update all the contexts.
 * 
 * @author Tobias Kuhn
 * 
 */
public class PMPServiceContextThread extends Thread {
    
    private PMPService service;
    
    
    public PMPServiceContextThread(PMPService service) {
        this.service = service;
    }
    
    
    @Override
    public void run() {
        boolean stop = false;
        
        for (IContext context : Model.getInstance().getContexts()) {
            if (Model.getInstance().getContextAnnotations(context).size() > 0) {
                FileLog.get().logWithForward(this, null, FileLog.GRANULARITY_CONTEXT_CHANGES, Level.INFO,
                        "Context update '%s'", context.getName());
                
                context.update(this.service.getBaseContext());
                // TODO actually use that return value
                stop = false;
            }
        }
        
        this.service.contextsDone(stop);
    }
}
