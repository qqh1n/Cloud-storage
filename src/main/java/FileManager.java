import java.io.File;

public class FileManager
{
    private File[] fileBuffer;
    private String rootDirectoryPath;

    public FileManager()
    {
        ConfigLoader configLoader = new ConfigLoader();
        rootDirectoryPath = configLoader.getStoragePath();
        File rootDirectory = new File(rootDirectoryPath);
        fileBuffer = rootDirectory.listFiles();
    }

    public File[] getFiles()
    {
        return fileBuffer;
    }

    public boolean storageFiles()
    {
        return true;
    }
}
