package StorageBot.MessageHandler.Handlers.CommandHandler;

import FileManager.FileManager;
import StorageBot.MessageHandler.Handlers.CommandHandler.Commands.*;

import java.util.HashMap;

public class CommandGetter
{
    HashMap<String, Command_I> commandsHashMap;
    FileManager fileManager;

    public CommandGetter(FileManager fileManager)
    {
        commandsHashMap = new HashMap<>();
        this.fileManager = fileManager;

        Command_I startCommand = new StartCommand();
        Command_I makeDirectoryCommand = new MakeDirectoryCommand(fileManager);
        Command_I deleteDirectoryCommand = new DeleteDirectoryCommand(fileManager);
        Command_I callDirectoryCommand = new CallDirectoryCommand(fileManager);
        Command_I commandsCommand = new CommandsCommand();
        Command_I deleteFileCommand = new DeleteFileCommand(fileManager);
        Command_I getFilesSummaryCommand = new GetFilesSummaryCommand(fileManager);
        Command_I getFileCommand = new GetFileCommand(fileManager);
        Command_I changeStorage = new ChangeStorageCommand(fileManager);
        Command_I makeStorage = new MakeStorageCommand(fileManager);
        Command_I getStorageId = new GetStorageIdCommand(fileManager);

        commandsHashMap.put("/chstg", changeStorage);
        commandsHashMap.put("/mkstg", makeStorage);
        commandsHashMap.put("/getStgId", getStorageId);
        commandsHashMap.put("/start", startCommand);
        commandsHashMap.put("/mkdir", makeDirectoryCommand);
        commandsHashMap.put("/rmdir", deleteDirectoryCommand);
        commandsHashMap.put("/cd", callDirectoryCommand);
        commandsHashMap.put("/commands",commandsCommand);
        commandsHashMap.put("/delete", deleteFileCommand);
        commandsHashMap.put("/files", getFilesSummaryCommand);
        commandsHashMap.put("/get", getFileCommand);
    }

    public Command_I getCommand(String commandName)
    {
        return commandsHashMap.get(commandName);
    }
}
