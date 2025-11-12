import java.io.File;
import java.io.IOException;
import java.net.URL;

public class FileManager
{
    private final FileSystemManager_I fileManager = new FileSystemManagerWindows();
    private final String urlBase;

    public FileManager(String urlBase)
    {
        this.urlBase = urlBase;
    }

    public boolean storageFile(String fileName, String filePath)
    {
        URL url = new URL(urlBase + filePath);
        try {
            fileManager.storageFile(url, fileName);
            return true;
        }
        catch (IOException e)
        {
            return false;
        }
    }

    public String getFilesInDir()
    {
        
    }
}
