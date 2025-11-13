package FileSystem;

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
    private LocalFileSystem_I fileSystem = new LocalFileSystemWindows();

    @Override
    public File getFile(String fileName)
            throws FileNotFoundException
    {
        File returnFile = new File(fileSystem.getPath(fileName));
        if (!returnFile.exists())
        {
            throw new FileNotFoundException();
        }
        return returnFile;
    }

    @Override
    public File[] getFilesInDir()
    {
        return new File(fileSystem.getCurrentDirectory()).listFiles();
    }

    @Override
    public void storageFile(URL url, String fileName)
            throws IOException
    {
        try(InputStream inputStream = url.openStream())
        {
            Files.copy(inputStream,
                        Paths.get(fileSystem.getPath(fileName)),
                        StandardCopyOption.REPLACE_EXISTING);
            fileSystem.addFile(fileName);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}