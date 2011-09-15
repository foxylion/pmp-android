package de.unistuttgart.ipvs.pmp.resourcegroups.filesystem;

import java.util.List;
import de.unistuttgart.ipvs.pmp.resourcegroups.filesystem.FileDetails;

interface IFileSystemAccess {
    
    String read(String file);
    boolean write(String file, String data, boolean append);
    boolean delete(String file);
    List<FileDetails> list(String directory);
}
