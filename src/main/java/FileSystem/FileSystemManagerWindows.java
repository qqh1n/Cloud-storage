package FileSystem;

import StorageBot.ConfigLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public final class FileSystemManagerWindows
        implements FileSystemManager_I
{
    private final String ROOT_DIRECTORY;
    private String currentDirectory;

    public FileSystemManagerWindows()
    {
        ConfigLoader configLoader = new ConfigLoader();
        ROOT_DIRECTORY = configLoader.getRootDir();
        currentDirectory = ROOT_DIRECTORY;
    }

    private String getPath(String fileName)
    {
        return currentDirectory + "\\" + fileName;
    }

    private boolean isFileExists(String fileName)
    {
        String path = getPath(fileName);
        return new File(path).exists();
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
        if (!isFileExists(fileName))
        {
            try (InputStream inputStream = url.openStream()) {
                Files.copy(inputStream,
                        Paths.get(getPath(fileName)),
                        StandardCopyOption.REPLACE_EXISTING);
                return fileName;
            } catch (IOException ioException) {
                throw new FileSystemManagerException(
                        FileSystemManagerException.ErrorCode.UNABLE_TO_SAVE_FILE);
            }
        }
        else
        {
            throw new FileSystemManagerException(
                    FileSystemManagerException.ErrorCode.UNABLE_TO_SAVE_FILE);
        }
    }

    public void deleteFile(String fileName)
            throws FileSystemManagerException
    {
        if (!isFileExists(fileName))
        {
            throw new FileSystemManagerException(
                    FileSystemManagerException.ErrorCode.NO_SUCH_FILE_EXIST);
        }

        File fileToDelete = new File(getPath(fileName));

        if (!fileToDelete.delete())
        {
            throw new FileSystemManagerException(
                    FileSystemManagerException.ErrorCode.UNABLE_TO_DELETE_FILE);
        }
    }
}