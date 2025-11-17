package StorageBot.MessageHandler;

import FileManager.FileManager;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class MessageHandler
{
    private final FileManager fileManager;

    public MessageHandler(String urlBase)
    {
        fileManager = new FileManager(urlBase);
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
            case "/delete":
                return deleteFile(argument);
            case "/files":
                return getFilesSummary();
            case "/get":
                return getFile(argument);
        }

        return null;
    }

    private String deleteFile(String fileName)
    {
        return null;
    }

    private String startCommand()
    {
        return "Телеграм-бот на **Java**, реализующий облачное хранилище:  \n" +
                "принимает файлы от пользователей, сохраняет их локально и позволяет получить обратно по команде.\n" +
                "\n" +
                "---\n" +
                "\n" +
                "## \uD83D\uDE80 Возможности:\n" +
                "\n" +
                "- \uD83D\uDCE4 Принимает документы (файлы) от пользователей и сохраняет их \n" +
                "- \uD83D\uDCBE Удаляет файл из локальной папки `/delete <имя_файла>`  \n" +
                "- \uD83D\uDCC2 Показывает список файлов в локальной папке`/files`  \n" +
                "- \uD83D\uDCE5 Отправляет файл обратно по команде `/get <имя_файла>`  \n" +
                "- \uD83D\uDC4B Приветственное сообщение через `/start`";
    }

    public String storageFile(String fileName, String filePath)
{
//        try
//        {
            String savedFileName = fileManager.storageFile(fileName, filePath);
            return "Файл успешно сохранён под именем '%s'.".formatted(savedFileName);
//        }
//        catch (SuchFileExistExeption e)
//        {
//            return "Не удалось сохранить файл '%s'. Слишком много файлов с одинаковым именем.".formatted(fileName);
//        }
//        catch (IncorrectFileNameExeption e)
//        {
//            return "Не удалось сохранить файл '%s'. Его имя некорректно.".formatted(fileName);
//        }

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

    private Object getFile(String fileName)
    {
        try
        {
            return fileManager.getFile(fileName);
        }
        catch (FileNotFoundException e)
        {
            return "Файл не с именем '%s' не найден.".formatted(fileName);
        }

    }
}
