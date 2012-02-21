package de.unistuttgart.ipvs.pmp.jpmpps.server.controller.handler;

import de.unistuttgart.ipvs.pmp.jpmpps.io.request.AbstractRequest;
import de.unistuttgart.ipvs.pmp.jpmpps.io.request.RequestCommunicationEnd;
import de.unistuttgart.ipvs.pmp.jpmpps.io.response.AbstractResponse;

/**
 * The {@link CommunicationEndHandler} react on a {@link RequestCommunicationEnd} request. It closes the connection.
 * 
 * @author Jakob Jarosch
 * 
 */
public class CommunicationEndHandler implements IRequestHandler {
    
    @Override
    public AbstractResponse process(AbstractRequest request) {
        return null;
    }
    
}
