package SimpleStorageBot;

import FileSystem.FileSystemManagerWindows;
import FileSystem.FileSystemManager_I;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class FileManager
{
    private final FileSystemManager_I fileManager = new FileSystemManagerWindows();
    private final String urlBase;

    public FileManager(String urlBase)
    {
        this.urlBase = urlBase;
    }

    public File getFile(String fileName)
    {
        try
        {
            return fileManager.getFile(fileName);
        }
        catch(FileNotFoundException e)
        {
            return null;
        }
    }

    public boolean storageFile(String fileName, String filePath)
    {
        URL url = null;
        try {
            url = new URL(urlBase + filePath);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        try {
            fileManager.storageFile(url, fileName);
            return true;
        }
        catch (IOException e)
        {
            return false;
        }
    }

    public ArrayList<String> getFilesInDir()
    {
        File[] filesArray = fileManager.getFilesInDir();
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
