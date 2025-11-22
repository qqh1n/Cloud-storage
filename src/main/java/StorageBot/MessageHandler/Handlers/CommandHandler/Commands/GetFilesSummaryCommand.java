package StorageBot.MessageHandler.Handlers.CommandHandler.Commands;

import FileManager.FileManager;

import java.util.ArrayList;

public class GetFilesSummaryCommand implements Command_I
{
    FileManager fileManager;

    public GetFilesSummaryCommand(FileManager fileManager)
    {
        this.fileManager = fileManager;
    }

    public Object execute(String[] args)
    {
        ArrayList<String> filesArrayList = fileManager.printFilesInDir();

        if (filesArrayList == null)
        {
            return "Эта директоря пуста.";
        }

        String resultString = "";

        for (String line : filesArrayList)
        {
            resultString = resultString.concat(line.concat("\n"));
        }

        return resultString;
    }
}
