package StorageBot.MessageHandler.Handlers.CommandHandler;

import FileManager.FileManager;
import FileManager.FileManagerException;
import StorageBot.MessageHandler.Handlers.MessageHandler_I;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;

public class CommandHandler implements MessageHandler_I<CommandHandlerException>
{
    TelegramLongPollingBot bot;
    FileManager fileManager;

    public CommandHandler(TelegramLongPollingBot bot, FileManager fileManager)
    {
        this.bot = bot;
        this.fileManager = fileManager;
    }

    public void handleMessage(Message message)
            throws CommandHandlerException
    {
        String text = message.getText();

        if (text.charAt(0) != '/')
        {
            throw new CommandHandlerException(
                    CommandHandlerException.ErrorCode.INVALID_INPUT);
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
                startCommand(message);
                break;
            case "/mkdir":
                makeDirectory(message, argument);
                break;
            case "/rmdir":
                deleteDirectory(message, argument);
                break;
            case "/cd":
                callDirectory(message, argument);
                break;
            case "/commands":
                commandsCommand(message);
                break;
            case "/delete":
                deleteFile(message, argument);
                break;
            case "/files":
                getFilesSummary(message);
                break;
            case "/get":
                getFile(message, argument);
                break;
            default:
                throw new CommandHandlerException(
                        CommandHandlerException.ErrorCode.UNKNOWN_COMMAND);
        }
    }

    private boolean isValidFileName(String fileName)
    {
        String regEx = "^[^<>:\"/\\\\|?*]+\\.[^<>:\"/\\\\|?*]+$";
        return fileName.matches(regEx);
    }

    private boolean isValidDirName(String dirName)
    {
        String regEx = "^[^<>:\"/\\\\|?*]+";
        return dirName.matches(regEx);
    }

    private void startCommand(Message message)
    {
        String menu =  " Cloud-storage\n" +
                        "Телеграм-бот на **Java**, реализующий облачное хранилище.\n"+
                        "\n" +
                        "---\n" +
                        "\n" +
                        "## \uD83D\uDE80 Возможности:\n" +
                        "- \uD83D\uDDC2 Создаёт и удаляет директории и переходит по ним.\n" +
                        "- \uD83D\uDDD1\uFE0F Удаляет  указанный файл из текущей директории.\n" +
                        "- \uD83D\uDCBE Принимает файлы от пользователей и сохраняет их.\n" +
                        "- \uD83D\uDCC2 Показывает список сохранённых файлов\n" +
                        "- \uD83D\uDCE5 Отправляет файл обратно по команде\n" +
                        "\n" +
                        "---\n";

        sendMessage(message.getChatId(), menu);
    }

    private void commandsCommand(Message message)
    {
        String menu = "Вот список команд для бота:\n" +
                        "\tПросто отправьте файл и он автоматически сохранится в текущую папку.\n" +
                        "\t'/mkdir dirName' - Создать папку с именем 'dirName' в текущей папке.\n" +
                        "\t'/cd dirName' - Перейти в папку с именем 'dirName', которая находится в текущей папке.\n" +
                        "\t('/cd .' - Перейти корень файловой системы)\n" +
                        "\t'/rmdir dirName' - Удаляет папку c именем 'dirName' (и её содержимое)," +
                        " которая находится в текущей папке.\n " +
                        "\t'/commands' - Показать список возможных команд.\n" +
                        "\t'/delete fileName - Удалить файл с именем 'fileName' из текущей папки.\n"+
                        "\t'/files' - Вывести список файлов, находящихся в текущей папке.\n"+
                        "\t'/get fileName' - Получить указанный файл (из текущей папки) в чат.\n";
        sendMessage(message.getChatId(), menu);
    }

    private void makeDirectory(Message message, String dirName)
            throws CommandHandlerException
    {
        if (!isValidDirName(dirName) || dirName.equals("."))
        {
            throw new CommandHandlerException(
                    CommandHandlerException.ErrorCode.INVALID_DIR_NAME);
        }

        try
        {
            String resultDirName = fileManager.makeDirectory(dirName);
            sendMessage(message.getChatId(),
                    "Папка '%s' успешно создана.".formatted(resultDirName));
        }
        catch (FileManagerException fileManagerException)
        {

            throw new CommandHandlerException(
                    CommandHandlerException.ErrorCode.UPLOAD_ATTEMPTS_LIMIT_EXCEEDED);
        }
    }

    private void deleteDirectory(Message message, String dirName)
            throws CommandHandlerException {
        if (!isValidDirName(dirName)) {
            throw new CommandHandlerException(
                    CommandHandlerException.ErrorCode.INVALID_DIR_NAME);
        }

        try {
            fileManager.deleteDirectory(dirName);
            sendMessage(message.getChatId(),
                    "Папка '%s' успешно удалена.".formatted(dirName));
        }
        catch (FileManagerException fileManagerException)
        {
            if (fileManagerException.getCode() == 4)
            {
                throw new CommandHandlerException(
                        CommandHandlerException.ErrorCode.NO_SUCH_DIR_EXIST);
            } else {
                throw new CommandHandlerException(
                        CommandHandlerException.ErrorCode.UNABLE_TO_DELETE_DIR);
            }
        }
    }

    private void callDirectory(Message message, String dirName)
            throws CommandHandlerException
    {
        if (!isValidDirName(dirName))
        {
            throw new CommandHandlerException(
                    CommandHandlerException.ErrorCode.INVALID_DIR_NAME);
        }

        try
        {
            fileManager.callDirectory(dirName);
            sendMessage(message.getChatId(),
                    "Вы успешно перешли в папку '%s'".formatted(dirName));
        }
        catch (FileManagerException fileManagerException)
        {
            if (fileManagerException.getCode() == 4)
            {
                throw new CommandHandlerException(
                        CommandHandlerException.ErrorCode.NO_SUCH_DIR_EXIST);
            }

            throw new CommandHandlerException(
                    CommandHandlerException.ErrorCode.UNABLE_TO_CALL_DIR);
        }
    }

    private void getFilesSummary(Message message)
    {
        ArrayList<String> filesArrayList = fileManager.printFilesInDir();

        if (filesArrayList == null)
        {
            String dirIsEmptyText = "Эта директоря пуста.";
            sendMessage(message.getChatId(), dirIsEmptyText);
            return;
        }

        String resultString = "";

        for (String line : filesArrayList)
        {
            resultString = resultString.concat(line.concat("\n"));
        }

        sendMessage(message.getChatId(), resultString);
    }

    private void getFile(Message message, String fileName)
            throws CommandHandlerException
    {
        if (!isValidFileName(fileName))
        {
            throw new CommandHandlerException(
                    CommandHandlerException.ErrorCode.INVALID_FILE_NAME);
        }
        try
        {
            sendMessage(message.getChatId(), fileManager.getFile(fileName));
        }
        catch (FileManagerException fileManagerException)
        {
            throw new CommandHandlerException(
                    CommandHandlerException.ErrorCode.NO_SUCH_FILE_EXIST);
        }
    }

    private void deleteFile(Message message, String fileName)
            throws CommandHandlerException
    {
        if (!isValidFileName(fileName))
        {
            throw new CommandHandlerException(
                    CommandHandlerException.ErrorCode.INVALID_FILE_NAME);
        }

        try
        {
            fileManager.deleteFile(fileName);
            sendMessage(message.getChatId(),
                    "Файл '%s' успешно удалён.".formatted(fileName));
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

    private void sendMessage(Long chatId, String messageText)
    {
        ReplyKeyboardMarkup keyboardMarkup = createKeyboard();
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(messageText);
        message.setReplyMarkup(keyboardMarkup);
        try
        {
            bot.execute(message);
        }
        catch (TelegramApiException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void sendMessage(Long chatId, File sendFile)
    {
        ReplyKeyboardMarkup keyboardMarkup = createKeyboard();
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatId);
        InputFile inputFile = new InputFile(sendFile);
        sendDocument.setDocument(inputFile);
        sendDocument.setReplyMarkup(keyboardMarkup);
        try
        {
            bot.execute(sendDocument);
        }
        catch (TelegramApiException e)
        {
            throw new RuntimeException(e);
        }
    }

    public ReplyKeyboardMarkup createKeyboard()
    {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add("/commands");

        keyboardRows.add(row);

        replyKeyboardMarkup.setKeyboard(keyboardRows);
        return replyKeyboardMarkup;
    }
}
