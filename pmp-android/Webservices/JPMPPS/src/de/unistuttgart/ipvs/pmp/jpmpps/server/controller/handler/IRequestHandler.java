package de.unistuttgart.ipvs.pmp.jpmpps.server.controller.handler;

import de.unistuttgart.ipvs.pmp.jpmpps.io.request.AbstractRequest;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.AbstractResponse;

/**
 * The {@link IRequestHandler}.
 * 
 * @author Jakob Jarosch
 */
public interface IRequestHandler {
    
    /**
     * The method is called to process a request.
     * 
     * @param request
     *            {@link AbstractRequest} which should be processed.
     * @return Returns the Response of the {@link AbstractRequest}, or null if connection should be closed.
     */
    public AbstractResponse process(AbstractRequest request);
}
