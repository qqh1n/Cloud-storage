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

public class CommandHandler implements MessageHandler_I
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
            case "/delete":
                deleteFile(message, argument);
            case "/files":
                getFilesSummary(message);
            case "/get":
                getFile(message, argument);
            default:
                throw new CommandHandlerException(
                        CommandHandlerException.ErrorCode.UNKNOWN_COMMAND);
        }
    }

    private void deleteFile(Message message, String fileName)
    {
        sendMessage(message.getChatId(),
                    "Эта функция пока в разработке.");
    }

    private void startCommand(Message message)
    {
        String menu =  "Телеграм-бот на **Java**, реализующий облачное хранилище:  \n" +
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

        sendMessage(message.getChatId(), menu);
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

    private void sendMessage(Long chatId, String messageText)
    {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(messageText);
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
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatId);
        InputFile inputFile = new InputFile(sendFile);
        sendDocument.setDocument(inputFile);
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
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        replyKeyboardMarkup.setSelective(true);

        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow firstRow = new KeyboardRow();
        firstRow.add("/start");
        firstRow.add("/delete <fileName>");

        KeyboardRow secondRow = new KeyboardRow();
        secondRow.add("/files");
        secondRow.add("/get <fileName>");

        keyboardRows.add(firstRow);
        keyboardRows.add(secondRow);

        replyKeyboardMarkup.setKeyboard(keyboardRows);
        return replyKeyboardMarkup;
    }
}
