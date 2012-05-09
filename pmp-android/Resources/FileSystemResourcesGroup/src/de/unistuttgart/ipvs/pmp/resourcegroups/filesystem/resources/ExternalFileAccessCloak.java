package de.unistuttgart.ipvs.pmp.resourcegroups.filesystem.resources;

import java.util.List;

import android.os.RemoteException;

public class ExternalFileAccessCloak extends IFileAccess.Stub {
    
    @Override
    public String read(String path) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public boolean write(String path, String data, boolean append) throws RemoteException {
        // TODO Auto-generated method stub
        return false;
    }
    
    
    @Override
    public boolean delete(String path) throws RemoteException {
        // TODO Auto-generated method stub
        return false;
    }
    
    
    @Override
    public List<FileDetails> list(String directory) throws RemoteException {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    @Override
    public boolean makeDirs(String path) throws RemoteException {
        // TODO Auto-generated method stub
        return false;
    }
    
}
