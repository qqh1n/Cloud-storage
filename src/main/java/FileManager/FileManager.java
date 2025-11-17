package FileManager;

import FileManager.FileSystem.FileSystemManagerWindows;
import FileManager.FileSystem.FileSystemManager_I;
import java.io.File;
import java.io.IOException;
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
        catch(FileSystemManagerException e)
        {
            if (e.code == 1)
            {
                throw new FileManagerException(ErrorCode.NO_SUCH_FILE);
            }
        }
    }

    public String storageFile(String fileName, String filePath)
//            throws SuchFileExistExeption, IncorrectFileName
    {
        URL url;
        try {
            url = new URL(urlBase + filePath);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i <= 1000; i++)
        {
            if (i != 0)
            {
                fileName = fileName + "(" + i + ")";
            }

//            try
//            {
            String savedFileName = null;
            try {
                savedFileName = fileSystemManagerWindows.storageFile(url, fileName);
                return savedFileName;
            } catch (IOException e) {
                return savedFileName;
            }
//            }
//            catch (SuchFileExistExeption e)
//            {
//                if (i == 1000)
//                {
//                    throw new SuchFileExistExeption();
//                }
//            }
//            catch (IncorrectFileNameExeption e)
//            {
//                throw new IncorrectFileName();
//            }
        }
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
