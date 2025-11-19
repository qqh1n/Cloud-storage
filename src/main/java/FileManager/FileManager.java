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
    private final FileSystemManager_I fileSystemManagerWindows;
    private final String urlBase;

    public FileManager(String urlBase)
    {
        fileSystemManagerWindows = new FileSystemManagerWindows();
        this.urlBase = urlBase;
    }

    public File getFile(String fileName)
            throws FileManagerException
    {
        try
        {
            return fileSystemManagerWindows.getFile(fileName);
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
            throw new RuntimeException(e);
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
                return fileSystemManagerWindows.saveFile(url, saveFileName);
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

    public ArrayList<String> printFilesInDir()
    {
        File[] filesArray = fileSystemManagerWindows.printFilesInDir();

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
