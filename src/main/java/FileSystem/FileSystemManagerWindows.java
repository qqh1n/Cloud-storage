package FileSystem;

import StorageBot.ConfigLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public final class FileSystemManagerWindows implements FileSystemManager_I
{
    private ConfigLoader configLoader;
    private final String ROOT_DIRECTORY;
    private String currentDirectory;

    public FileSystemManagerWindows()
    {
        configLoader = new ConfigLoader();
        ROOT_DIRECTORY = configLoader.getRootDir();
        currentDirectory = ROOT_DIRECTORY;
    }

    private String getPath(String fileName)
    {
        return currentDirectory + "\\" + fileName;
    }

    @Override
    public File getFile(String fileName)
            throws FileSystemManagerException
    {
        File returnFile = new File(getPath(fileName));
        if (!returnFile.exists())
        {
            throw new FileSystemManagerException(
                        FileSystemManagerException.ErrorCode.NO_SUCH_FILE_EXIST);
        }
        return returnFile;
    }

    @Override
    public File[] printFilesInDir()
    {
        return new File(currentDirectory).listFiles();
    }

    @Override
    public String saveFile(URL url, String fileName)
            throws FileSystemManagerException
    {
        try
        {
            getFile(fileName);
            throw new FileSystemManagerException(
                        FileSystemManagerException.ErrorCode.SUCH_FILE_EXIST);
        }
        catch (FileSystemManagerException fileSystemManagerException)
        {
            try(InputStream inputStream = url.openStream())
            {
                Files.copy(inputStream,
                        Paths.get(getPath(fileName)),
                        StandardCopyOption.REPLACE_EXISTING);
                return fileName;
            }
            catch (IOException ioException)
            {
                throw new FileSystemManagerException(
                            FileSystemManagerException.ErrorCode.UNABLE_TO_STORAGE_FILE);
            }
        }

    }
}