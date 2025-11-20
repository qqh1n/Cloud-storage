package FileManager;

import FileSystem.FileSystemManagerException;
import FileSystem.FileSystemManagerWindows;
import FileSystem.FileSystemManager_I;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class FileManager
{
    private final FileSystemManager_I fileSystemManager;
    private final String urlBase;

    public FileManager(String urlBase)
    {
        fileSystemManager = new FileSystemManagerWindows();
        this.urlBase = urlBase;
    }

    public File getFile(String fileName)
            throws FileManagerException
    {
        try
        {
            return fileSystemManager.getFile(fileName);
        }
        catch(FileSystemManagerException fileSystemManagerException)
        {
            throw new FileManagerException(
                    FileManagerException.ErrorCode.NO_SUCH_FILE);
        }
    }

    public String saveFile(String fileName, String filePath)
            throws FileManagerException
    {
        String saveFileName = fileName;
        URL url;
        try
        {
            url = new URL(urlBase + filePath);
        } catch (MalformedURLException e) {
            throw new FileManagerException(
                    FileManagerException.ErrorCode.UNABLE_TO_SAVE_FILE);
        }
        for (int i = 0; i <= 1000; i++)
        {
            if (i != 0)
            {
                int extensionInd = saveFileName.indexOf(".");
                saveFileName = saveFileName.substring(0, extensionInd) +
                                "(" + i + ")" +
                                saveFileName.substring(extensionInd);
            }
            try
            {
                return fileSystemManager.saveFile(url, saveFileName);
            }
            catch (FileSystemManagerException fileSystemManagerException)
            {
                if (i == 1000)
                {
                    throw new FileManagerException(
                            FileManagerException.ErrorCode.UPLOAD_ATTEMPTS_LIMIT_EXCEEDED);
                }
            }
        }
        return fileName;
    }

    public void deleteFile(String fileName)
            throws FileManagerException
    {
        try
        {
            fileSystemManager.deleteFile(fileName);
        }
        catch (FileSystemManagerException fileSystemManagerException)
        {
            if (fileSystemManagerException.getCode() == 0)
            {
                throw new FileManagerException(
                        FileManagerException.ErrorCode.NO_SUCH_FILE);
            }
            else
            {
                throw new FileManagerException(
                        FileManagerException.ErrorCode.UNABLE_TO_DELETE_FILE);
            }
        }
    }

    public ArrayList<String> printFilesInDir()
    {
        File[] filesArray = fileSystemManager.printFilesInDir();

        if (filesArray == null)
        {
            return null;
        }

        ArrayList<String> filesFormatedStringsArrayList = new ArrayList<>();

        for (File file : filesArray)
        {
            if (file.isDirectory())
            {
                String fileFormatedString = "\uD83D\uDCC2" + " " + file.getName();
                filesFormatedStringsArrayList.add(fileFormatedString);
            }
            else if (file.isFile())
            {
                String fileFormatedString = "\uD83D\uDCC4" + " " + file.getName();
                filesFormatedStringsArrayList.add(fileFormatedString);
            }
        }

        return filesFormatedStringsArrayList;
    }
}
