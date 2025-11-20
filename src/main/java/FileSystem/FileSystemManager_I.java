package FileSystem;

import java.io.File;
import java.net.URL;

public interface FileSystemManager_I
{
    public String getCurrentDirectory();
    public String saveFile(URL url, String fileName)
            throws FileSystemManagerException;
    public File[] printFilesInDir();
    public File getFile(String fileName)
            throws FileSystemManagerException;
    public void deleteFile(String fileName)
            throws FileSystemManagerException;
    public String makeDirectory(String dirName)
            throws FileSystemManagerException;
    public void deleteDirectory(String dirName)
            throws FileSystemManagerException;
    public void callDirectory(String dirName)
            throws FileSystemManagerException;
}
