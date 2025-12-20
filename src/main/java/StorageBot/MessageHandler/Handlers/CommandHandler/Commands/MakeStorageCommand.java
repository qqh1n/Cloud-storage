package StorageBot.MessageHandler.Handlers.CommandHandler.Commands;

import FileManager.FileManager;
import FileManager.FileManagerException;
import StorageBot.MessageHandler.Handlers.CommandHandler.CommandHandlerException;

public class MakeStorageCommand implements Command_I
{
    private final FileManager fileManager;
    public MakeStorageCommand(FileManager fileManager)
    {
        this.fileManager = fileManager;
    }

    public Object execute(String[] args)
            throws CommandHandlerException
    {
        String storageId = args[1];
        try
        {
            fileManager.makeStorage(storageId);
            return "Хранилище успешно создано.";
        }
        catch (FileManagerException fileManagerException)
        {
            if (fileManagerException.getCode() == 9)
            {
                throw  new CommandHandlerException(
                        CommandHandlerException.ErrorCode.UNABLE_TO_CREATE_STORAGE);
            }
            throw new CommandHandlerException(
                    CommandHandlerException.ErrorCode.STORGE_EXISTS);
        }

    }
}
