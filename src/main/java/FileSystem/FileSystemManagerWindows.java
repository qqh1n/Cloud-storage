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

    @Override
    public String getCurrentDirectory()
    {
        if (currentDirectory.equals(ROOT_DIRECTORY))
        {
            return ".\\";
        }
        else
        {
            String currentDir = ".\\";
            int userDirsInd = currentDirectory.indexOf(ROOT_DIRECTORY) + ROOT_DIRECTORY.length() + 1;
            currentDir += currentDirectory.substring(userDirsInd);
            return currentDir;
        }
    }

    private String getPath(String name)
    {
        return currentDirectory + "\\" + name;
    }

    private boolean isFileExists(String fileName)
    {
        String path = getPath(fileName);
        return new File(path).exists();
    }

    private boolean isDirExists(String dirName)
    {
        String path = getPath(dirName);
        File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory())
        {
            return false;
        }
        return true;
    }
    @Override
    public String makeDirectory(String dirName)
            throws FileSystemManagerException
    {
        if (!isDirExists(dirName))
        {
            File newDir = new File(getPath(dirName));
            if (!newDir.mkdir())
            {
                throw new FileSystemManagerException(
                        FileSystemManagerException.ErrorCode.UNABLE_TO_MAKE_DIR);
            }
            return dirName;
        }
        else
        {
            throw new FileSystemManagerException(
                    FileSystemManagerException.ErrorCode.NO_SUCH_DIR_EXIST);
        }
    }

    @Override
    public void deleteDirectory(String dirName)
            throws FileSystemManagerException
    {
        if (!isFileExists(dirName) || !new File(getPath(dirName)).isDirectory())
        {
            throw new FileSystemManagerException(
                    FileSystemManagerException.ErrorCode.NO_SUCH_DIR_EXIST);
        }

        File dirToDelete = new File(getPath(dirName));
        String[] filesInDir = dirToDelete.list();
        if (filesInDir.length != 0)
        {
            for (int i = 0; i < filesInDir.length; i++)
            {
                String fileName = filesInDir[i];
                deleteFile(dirName + "\\" + fileName);
            }
        }

        if (!dirToDelete.delete())
        {
            throw new FileSystemManagerException(
                    FileSystemManagerException.ErrorCode.UNABLE_TO_DELETE_FILE);
        }
    }

    @Override
    public void callDirectory(String dirName)
            throws FileSystemManagerException
    {
        if (dirName.equals("."))
        {
            currentDirectory = ROOT_DIRECTORY;
        }
        else if (isDirExists(dirName))
        {
            currentDirectory = getPath(dirName);
        }
        else
        {
            throw new FileSystemManagerException(
                    FileSystemManagerException.ErrorCode.UNABLE_TO_CALL_DIR);
        }
    }

    @Override
    public File getFile(String fileName)
            throws FileSystemManagerException
    {
        File returnFile = new File(getPath(fileName));
        if (!returnFile.exists() || returnFile.isDirectory())
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

    @Override
    public void deleteFile(String fileName)
            throws FileSystemManagerException
    {
        if (!isFileExists(fileName) || new File(getPath(fileName)).isDirectory())
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