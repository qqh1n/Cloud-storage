package StorageBot.MessageHandler.Handlers.CommandHandler.Commands;

import FileManager.FileManager;
import FileManager.FileManagerException;
import StorageBot.MessageHandler.Handlers.CommandHandler.CommandHandlerException;

public class DeleteFileCommand implements Command_I
{
    FileManager fileManager;

    public DeleteFileCommand(FileManager fileManager)
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
            fileManager.deleteFile(fileName);
            return "Файл '%s' успешно удалён.".formatted(fileName);
        }
        catch (FileManagerException fileManagerException)
        {
            if (fileManagerException.getCode() == 0)
            {
                throw new CommandHandlerException(
                        CommandHandlerException.ErrorCode.NO_SUCH_FILE_EXIST);
            }
            else
            {
                throw new CommandHandlerException(
                        CommandHandlerException.ErrorCode.UNABLE_TO_DELETE_FILE);
            }
        }
    }

    private boolean isValidFileName(String fileName)
    {
        String regEx = "^[^<>:\"/\\\\|?*]+\\.[^<>:\"/\\\\|?*]+$";
        return fileName.matches(regEx);
    }
}
