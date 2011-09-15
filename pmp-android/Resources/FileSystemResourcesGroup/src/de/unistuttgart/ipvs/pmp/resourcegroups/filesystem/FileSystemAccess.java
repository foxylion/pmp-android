package de.unistuttgart.ipvs.pmp.resourcegroups.filesystem;

import java.util.List;

import android.os.RemoteException;

public class FileSystemAccess extends IFileSystemAccess.Stub {

	@Override
	public String read(String file) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean write(String file, String data, boolean append)
			throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(String file) throws RemoteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<FileDetails> list(String directory) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}
