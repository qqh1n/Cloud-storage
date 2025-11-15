package SimpleStorageBot.MessageHandler;

import org.telegram.telegrambots.meta.api.objects.Message;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class MessageHandler
{
    private final FileManager fileManager;
    private HashMap<String, String> pathsAndFileNamesBufferHashMap;

    public MessageHandler(String urlBase)
    {
        fileManager = new FileManager(urlBase);
        pathsAndFileNamesBufferHashMap = new HashMap<>();
    }

    public Object executeCommand(Message message)
    {
        String text = message.getText();

        if (text.charAt(0) != '/')
        {
            return "Введена неверная команда.";
        }

        int indSpace = text.indexOf(" ");
        String commandName;
        String argument;
        if (indSpace == -1)
        {
            commandName = text;
            argument = "";
        }
        else
        {
            commandName = text.substring(0 , indSpace);
            argument = text.substring(indSpace + 1);
        }

        switch (commandName)
        {
            case "/start":
                return startCommand();
            case "/storage":
                return storageFile(argument);
            case "/files":
                return getFilesSummary();
            case "/get":
                return getFile(argument);
        }

        return null;
    }

    private String startCommand()
    {
        return "Телеграм-бот на **Java**, реализующий облачное хранилище:  \n" +
                "принимает файлы от пользователей, сохраняет их локально и позволяет получить обратно по команде.\n" +
                "\n" +
                "---\n" +
                "\n" +
                "## \uD83D\uDE80 Возможности\n" +
                "\n" +
                "- \uD83D\uDCE4 Принимает документы (файлы) от пользователей  \n" +
                "- \uD83D\uDCBE Сохраняет файлы в локальную папку `/storage <имя_файла>`  \n" +
                "- \uD83D\uDCC2 Показывает список сохранённых файлов `/files`  \n" +
                "- \uD83D\uDCE5 Отправляет файл обратно по команде `/get <имя_файла>`  \n" +
                "- \uD83D\uDC4B Приветственное сообщение через `/start`";
    }

    private String storageFile(String fileName)
    {
        for (String filePath : pathsAndFileNamesBufferHashMap.keySet())
        {
            if (pathsAndFileNamesBufferHashMap.get(filePath).equals(fileName))
            {
                if (fileManager.storageFile(fileName, filePath))
                {
                    return "Файл '%s' успешно сохранён.".formatted(fileName);
                }
            }
        }

        return "Произошла ошибка при сохранении файла '%s'".formatted(fileName);
    }

    private String getFilesSummary()
    {
        ArrayList<String> filesArrayList = fileManager.printFilesInDir();

        if (filesArrayList == null)
        {
            return "Директория пуста.";
        }

        String resultString = "";

        for (String line : filesArrayList)
        {
            resultString = resultString.concat(line.concat("\n"));
        }

        return resultString;
    }

    private File getFile(String fileName)
    {
        return fileManager.getFile(fileName);
    }

    public void putToBuffer(String filePath, String fileName)
    {
        pathsAndFileNamesBufferHashMap.put(filePath, fileName);
    }
}
