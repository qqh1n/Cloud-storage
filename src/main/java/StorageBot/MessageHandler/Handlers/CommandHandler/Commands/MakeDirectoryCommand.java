package StorageBot.MessageHandler.Handlers.CommandHandler.Commands;

import FileManager.FileManager;
import FileManager.FileManagerException;
import StorageBot.MessageHandler.Handlers.CommandHandler.CommandHandlerException;
import org.telegram.telegrambots.meta.api.objects.Message;

public class MakeDirectoryCommand implements Command_I
{
    FileManager fileManager;
    public MakeDirectoryCommand(FileManager fileManager)
    {
        this.fileManager = fileManager;
    }

    public Object execute(String[] args)
            throws CommandHandlerException
    {
        String dirName = args[0];

        if (!isValidDirName(dirName) || dirName.equals("."))
        {
            throw new CommandHandlerException(
                    CommandHandlerException.ErrorCode.INVALID_DIR_NAME);
        }

        try
        {
            String resultDirName = fileManager.makeDirectory(dirName);
            return "Папка успешно создана под именем '%s'.".formatted(resultDirName);
        }
        catch (FileManagerException fileManagerException)
        {
            if (fileManagerException.getCode() == 8)
            {
                throw new CommandHandlerException(
                        CommandHandlerException.ErrorCode.STORAGE_IS_NOT_SELECTED);
            }
            throw new CommandHandlerException(
                    CommandHandlerException.ErrorCode.UPLOAD_ATTEMPTS_LIMIT_EXCEEDED);
        }
    }

    private boolean isValidDirName(String dirName)
    {
        String regEx = "^[^<>:\"/\\\\|?*]+";
        return dirName.matches(regEx);
    }
}
