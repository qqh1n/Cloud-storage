package StorageBot.MessageHandler.Handlers.CommandHandler.Commands;

import FileManager.FileManager;
import StorageBot.MessageHandler.Handlers.CommandHandler.CommandHandlerException;

public class GetStorageIdCommand implements Command_I
{
    FileManager fileManager;
    public GetStorageIdCommand(FileManager fileManager)
    {
        this.fileManager = fileManager;
    }

    public Object execute(String[] args)
            throws CommandHandlerException
    {
        return fileManager.getStorageId();
    }
}
