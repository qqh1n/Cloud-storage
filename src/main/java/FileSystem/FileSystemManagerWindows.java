package FileSystem;

import Pair.Pair;
import StorageBot.ConfigLoader;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Stack;

public class FileSystemManagerWindows
        implements FileSystemManager_I
{
    private final String ROOT_DIRECTORY;
    private final String separator = File.separator;
    private String currentDirectory;


    public FileSystemManagerWindows()
    {
        ConfigLoader configLoader = new ConfigLoader();
        ROOT_DIRECTORY = configLoader.getRootDir();
        currentDirectory = ROOT_DIRECTORY;
    }


    public String getCurrentDirectory()
    {
        if (currentDirectory.equals(ROOT_DIRECTORY))
        {
            return "." + separator;
        }
        else
        {
            String currentDir = "." + separator;
            int userDirsInd = currentDirectory.indexOf(ROOT_DIRECTORY) +
                    ROOT_DIRECTORY.length() + 1;
            currentDir += currentDirectory.substring(userDirsInd);
            return currentDir;
        }
    }


    private String getPath(String name)
    {
        return currentDirectory + separator + name;
    }


    private boolean isFileExists(String fileName)
    {
        String path = getPath(fileName);
        File file = new File(path);
        return file.exists() && file.isFile();
    }


    private boolean isDirExists(String dirName)
    {
        String path = getPath(dirName);
        File dir = new File(path);
        return dir.exists() && dir.isDirectory();
    }


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
                    FileSystemManagerException.ErrorCode.SUCH_DIR_EXISTS);
        }
    }


    public void deleteDirectory(String dirName)
            throws FileSystemManagerException
    {
        if (dirName.equals(".") || dirName.equals(".."))
        {
            throw new FileSystemManagerException(
                    FileSystemManagerException.ErrorCode.UNABLE_TO_DELETE_DIR);
        }
        if (!isDirExists(dirName))
        {
            throw new FileSystemManagerException(
                    FileSystemManagerException.ErrorCode.NO_SUCH_DIR_EXISTS);
        }

        Stack<File> filesToDeleteStack = new Stack<>();
        File currentDir = new File(getPath(dirName));
        if (currentDir.canWrite())
        {
            filesToDeleteStack.add(currentDir);
        }
        else
        {
            throw new FileSystemManagerException(
                    FileSystemManagerException.ErrorCode.UNABLE_TO_DELETE_DIR);
        }

        if (!makeStack(filesToDeleteStack, currentDir))
        {
            throw new FileSystemManagerException(
                    FileSystemManagerException.ErrorCode.UNABLE_TO_DELETE_DIR);
        }

        while (!filesToDeleteStack.empty())
        {
            File currentFile = filesToDeleteStack.pop();
            if (!currentFile.delete())
            {
                throw new FileSystemManagerException(
                        FileSystemManagerException.ErrorCode.UNABLE_TO_DELETE_DIR);
            }
        }
    }

    private boolean makeStack(Stack<File> filesToDeleteStack,
                           File currentDir)
    {
        File[] filesList = currentDir.listFiles();

        if (filesList != null)
        {
           for (File file : filesList)
           {
               if (file.canWrite())
                   {
                       filesToDeleteStack.add(file);
                       if (file.isDirectory())
                       {
                           makeStack(filesToDeleteStack, file);
                       }
                   }
               else
               {
                   return false;
               }
           }
        }
        return true;
    }


    public void callDirectory(String dirName)
            throws FileSystemManagerException
    {
        if (dirName.equals("."))
        {
            currentDirectory = ROOT_DIRECTORY;
        }
        else if (dirName.equals(".."))
        {
            if (currentDirectory.equals(ROOT_DIRECTORY))
            {
                currentDirectory = ROOT_DIRECTORY;
            }
            else
            {
                int indexRightSlash = currentDirectory.lastIndexOf(separator);
                currentDirectory = currentDirectory.substring(0, indexRightSlash);
            }
        }
        else if (isDirExists(dirName))
        {
            currentDirectory = getPath(dirName);
        }
        else
        {
            throw new FileSystemManagerException(
                    FileSystemManagerException.ErrorCode.NO_SUCH_DIR_EXISTS);
        }
    }


    public Pair<FileInputStream, String> getFile(String fileName)
            throws FileSystemManagerException
    {
        try
        {
            return new Pair<>(
                    new FileInputStream(getPath(fileName)),
                    fileName);
        }
        catch (FileNotFoundException fileNotFoundException)
        {
            throw new FileSystemManagerException(
                    FileSystemManagerException.ErrorCode.NO_SUCH_FILE_EXIST);
        }
    }


    public ArrayList<Pair<String, Boolean>> printFilesInDir()
    {
        File[] filesInDir =
                new File(currentDirectory).listFiles();
        ArrayList<Pair<String, Boolean>> fileNameIsFileArrayList =
                new ArrayList<>();
        if (filesInDir == null)
        {
            return null;
        }

        for (File file : filesInDir)
        {
            Pair<String, Boolean> pair =
                    new Pair<>(file.getName(), file.isFile());
            fileNameIsFileArrayList.add(pair);
        }

        return fileNameIsFileArrayList;
    }


    public String saveFile(URL url, String fileName)
            throws FileSystemManagerException
    {
        if (!isFileExists(fileName))
        {

            int buffer;
            try (InputStream inputURLStream = url.openStream();
                 FileOutputStream fileOutputStream =
                         new FileOutputStream(getPath(fileName)))
            {
                do
                {
                    buffer = inputURLStream.read();
                    if (buffer != -1)
                    {
                        fileOutputStream.write(buffer);
                    }
                }
                while (buffer != -1);

                return fileName;
            }
            catch (IOException ioException)
            {
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