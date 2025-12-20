package StorageBot.MessageHandler.Handlers.CommandHandler.Commands;


import FileManager.FileManager;
import FileManager.FileManagerException;
import StorageBot.MessageHandler.Handlers.CommandHandler.CommandHandlerException;

public class ChangeStorageCommand implements Command_I
{
    private FileManager fileManager;
    public ChangeStorageCommand(FileManager fileManager)
    {
        this.fileManager = fileManager;
    }
    public Object execute(String[] args)
            throws CommandHandlerException
    {
        String storageId = args[0];
        if (isValidId(storageId))
        {
            try
            {
               fileManager.changeStorage(storageId);
               return "Вы успешно вошли в хранилище '%s'".formatted(storageId);
            }
            catch (FileManagerException fileManagerException)
            {
                throw new CommandHandlerException(CommandHandlerException
                        .ErrorCode.NO_SUCH_STORAGE);
            }
        }
        else
        {
            throw new CommandHandlerException(CommandHandlerException
                    .ErrorCode.INVALID_STORAGE_ID);
        }
    }

    private boolean isValidId(String storageId)
    {
        try
        {
            Long.parseLong(storageId);
            return true;
        }
        catch (NumberFormatException NumberFormatException)
        {
            return false;
        }
    }
}
