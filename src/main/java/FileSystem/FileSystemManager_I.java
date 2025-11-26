package FileSystem;

import Pair.Pair;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;

public interface FileSystemManager_I
{
    public String getCurrentDirectory();
    public String saveFile(URL url, String fileName)
            throws FileSystemManagerException;
    public ArrayList<Pair<String, Boolean>> printFilesInDir();
    public Pair<FileInputStream, String> getFile(String fileName)
            throws FileSystemManagerException;
    public void deleteFile(String fileName)
            throws FileSystemManagerException;
    public String makeDirectory(String dirName)
            throws FileSystemManagerException;
    public void deleteDirectory(String dirName)
            throws FileSystemManagerException;
    public void callDirectory(String dirName)
            throws FileSystemManagerException;
}
