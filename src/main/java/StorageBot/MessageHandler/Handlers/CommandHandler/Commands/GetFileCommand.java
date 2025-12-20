package StorageBot.MessageHandler.Handlers.CommandHandler.Commands;

import FileManager.FileManager;
import FileManager.FileManagerException;
import StorageBot.MessageHandler.Handlers.CommandHandler.CommandHandlerException;

public class GetFileCommand implements Command_I
{
    FileManager fileManager;

    public GetFileCommand(FileManager fileManager)
    {
        this.fileManager = fileManager;
    }

    public Object execute(String[] args)
            throws CommandHandlerException
    {
        String fileName = args[0];

        if (!isValidFileName(fileName))
        {
            throw new CommandHandlerException(
                    CommandHandlerException.ErrorCode.INVALID_FILE_NAME);
        }
        try
        {
            return fileManager.getFile(fileName);
        }
        catch (FileManagerException fileManagerException)
        {
            if (fileManagerException.getCode() == 8)
            {
                throw new CommandHandlerException(
                        CommandHandlerException.ErrorCode.STORAGE_IS_NOT_SELECTED);
            }
            throw new CommandHandlerException(
                    CommandHandlerException.ErrorCode.NO_SUCH_FILE_EXIST);
        }
    }

    private boolean isValidFileName(String fileName)
    {
        String regEx = "^[^<>:\"/\\\\|?*]+\\.[^<>:\"/\\\\|?*]+$";
        return fileName.matches(regEx);
    }
}
