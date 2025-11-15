package FileSystem;

import SimpleStorageBot.ConfigLoader;

public class LocalFileSystemWindows implements LocalFileSystem_I
{
    private final ConfigLoader configLoader;
    private final String ROOT_DIRECTORY;
    private final String currentDir;

    public LocalFileSystemWindows()
    {
        configLoader = new ConfigLoader();
         ROOT_DIRECTORY = configLoader.getRootDir();
         currentDir = ROOT_DIRECTORY;
    }

    public String getPath(String fileName)
    {
        return currentDir + "\\" + fileName;
    }
    public String getCurrentDirectory()
    {
        return currentDir;
    }
    public void addFile(String fileName)
    {

    }

}
