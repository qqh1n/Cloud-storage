package FileSystem;

import Pair.Pair;

import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;

public interface FileSystemManager_I
{
    String getCurrentDirectory();
    String saveFile(URL url, String fileName)
            throws FileSystemManagerException;
    ArrayList<Pair<String, Boolean>> printFilesInDir();
    Pair<FileInputStream, String> getFile(String fileName)
            throws FileSystemManagerException;
    void deleteFile(String fileName)
            throws FileSystemManagerException;
    String makeDirectory(String dirName)
            throws FileSystemManagerException;
    void deleteDirectory(String dirName)
            throws FileSystemManagerException;
    void callDirectory(String dirName)
            throws FileSystemManagerException;
}
