/*
 * Copyright 2012 pmp-android development team
 * Project: PMP
 * Project-Site: http://code.google.com/p/pmp-android/
 * 
 * ---------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
