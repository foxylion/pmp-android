package de.unistuttgart.ipvs.pmp.api.ipc.command;

import java.util.List;

import de.unistuttgart.ipvs.pmp.api.handler.PMPRequestSFHandler;

public class IPCRequestSFCommand extends IPCCommand {
    
    private List<String> sfs;
    public PMPRequestSFHandler handler;
}
