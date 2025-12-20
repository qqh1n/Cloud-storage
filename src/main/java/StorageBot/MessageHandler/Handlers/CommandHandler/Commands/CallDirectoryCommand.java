package StorageBot.MessageHandler.Handlers.CommandHandler.Commands;

import FileManager.FileManager;
import FileManager.FileManagerException;
import StorageBot.MessageHandler.Handlers.CommandHandler.CommandHandlerException;

public class CallDirectoryCommand implements Command_I
{
    FileManager fileManager;

    public CallDirectoryCommand(FileManager fileManager)
    {
        this.fileManager = fileManager;
    }

    public Object execute(String[] args)
            throws CommandHandlerException
    {
        String dirName = args[0];

        if (!isValidDirName(dirName))
        {
            throw new CommandHandlerException(
                    CommandHandlerException.ErrorCode.INVALID_DIR_NAME);
        }

        try
        {
            fileManager.callDirectory(dirName);
            return "Вы успешно перешли в папку '%s'".formatted(dirName);
        }
        catch (FileManagerException fileManagerException)
        {
            if (fileManagerException.getCode() == 8)
            {
                throw new CommandHandlerException(
                        CommandHandlerException.ErrorCode.STORAGE_IS_NOT_SELECTED);
            }
            else if (fileManagerException.getCode() == 4)
            {
                throw new CommandHandlerException(
                        CommandHandlerException.ErrorCode.NO_SUCH_DIR_EXISTS);
            }
            else
            {
                throw new CommandHandlerException(
                        CommandHandlerException.ErrorCode.UNABLE_TO_CALL_DIR);
            }
        }
    }
    private boolean isValidDirName(String dirName)
    {
        String regEx = "^[^<>:\"/\\\\|?*]+";
        return dirName.matches(regEx);
    }
}
