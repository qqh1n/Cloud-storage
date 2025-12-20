package StorageBot.MessageHandler.Handlers.CommandHandler.Commands;

import FileManager.FileManager;
import FileManager.FileManagerException;
import StorageBot.MessageHandler.Handlers.CommandHandler.CommandHandlerException;

import java.util.ArrayList;

public class GetFilesSummaryCommand implements Command_I
{
    FileManager fileManager;

    public GetFilesSummaryCommand(FileManager fileManager)
    {
        this.fileManager = fileManager;
    }

    public Object execute(String[] args)
            throws CommandHandlerException
    {
        try {
            ArrayList<String> filesArrayList = fileManager.printFilesInDir();

            if (filesArrayList == null) {
                return "Эта директория пуста.";
            }

            String resultString = "";

            for (String line : filesArrayList) {
                resultString = resultString.concat(line.concat("\n"));
            }

            return resultString;
        }
        catch (FileManagerException fileManagerException)
        {
            if (fileManagerException.getCode() == 8)
            {
                throw new CommandHandlerException(
                        CommandHandlerException.ErrorCode.STORAGE_IS_NOT_SELECTED);
            }
            return null;
        }
    }
}
