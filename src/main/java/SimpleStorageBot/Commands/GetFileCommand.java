package SimpleStorageBot.Commands;

import java.io.File;
import SimpleStorageBot.FileManager;

public class GetFileCommand implements Command_I
{
    private String fileName;
    private FileManager fileManager;
    public GetFileCommand(String fileName, FileManager fileManager)
    {
        this.fileName = fileName;
        this.fileManager = fileManager;
    }

    @Override
    public File execute()
    {
        return fileManager.getFile(fileName);
    }
}
