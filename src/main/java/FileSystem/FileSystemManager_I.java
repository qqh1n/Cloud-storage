package FileSystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

public interface FileSystemManager_I
{
    public void storageFile(URL url, String fileName)
            throws IOException;
    public File[] printFilesInDir();
    public File getFile(String fileName)
            throws FileNotFoundException;

}
