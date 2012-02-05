package de.unistuttgart.ipvs.pmp.service;

import de.unistuttgart.ipvs.pmp.model.Model;
import de.unistuttgart.ipvs.pmp.model.context.IContext;

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
            if (Model.getInstance().getContextAnnotations(context).length > 0) {
                context.update();
                // TODO actually use that return value
                stop = false;
            }
        }
        
        this.service.contextsDone(stop);
    }
}
