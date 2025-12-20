package StorageBot.MessageHandler.Handlers.CommandHandler;

import FileManager.FileManager;
import Pair.Pair;
import StorageBot.MessageHandler.Handlers.CommandHandler.Commands.Command_I;
import StorageBot.MessageHandler.Handlers.MessageHandler_I;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;


public class CommandHandler implements MessageHandler_I<CommandHandlerException>
{
    TelegramLongPollingBot bot;
    FileManager fileManager;
    CommandGetter commandGetter;
    Command_I executableCommand;

    public CommandHandler(TelegramLongPollingBot bot, FileManager fileManager)
    {
        this.bot = bot;
        this.fileManager = fileManager;
        this.commandGetter = new CommandGetter(fileManager);
    }

    public void handleMessage(Message message)
            throws CommandHandlerException {
        String text = message.getText();

        if (text.charAt(0) != '/') {
            throw new CommandHandlerException(
                    CommandHandlerException.ErrorCode.INVALID_INPUT);
        }

        int indSpace = text.indexOf(" ");
        String commandName;
        String argument;
        if (indSpace == -1) {
            commandName = text;
            argument = "";
        } else {
            commandName = text.substring(0, indSpace);
            argument = text.substring(indSpace + 1);
        }

        executableCommand = commandGetter.getCommand(commandName);
        if (executableCommand != null) {
            sendMessage(message.getChatId(),
                    executableCommand.execute(new String[]{argument}));
        } else {
            throw new CommandHandlerException(
                    CommandHandlerException.ErrorCode.UNKNOWN_COMMAND);
        }
    }

    private void sendMessage(Long chatId, Object objToSend)
    {
        if (objToSend instanceof String)
        {
            sendMessage(chatId, (String) objToSend);
        }
        else
        {
            sendMessage(chatId, (Pair<FileInputStream, String>) objToSend);
        }
    }

    private void sendMessage(Long chatId, String text)
    {
        ReplyKeyboardMarkup keyboardMarkup = createKeyboard();
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        message.setReplyMarkup(keyboardMarkup);
        try
        {
            bot.execute(message);
        }
        catch (TelegramApiException telegramApiException)
        {
            throw new RuntimeException(telegramApiException);
        }
    }

    private void sendMessage(Long chatId,
                             Pair<FileInputStream, String> pair) {
        try (FileInputStream fileInputStream = pair.getFirst())
        {
            ReplyKeyboardMarkup keyboardMarkup = createKeyboard();
            SendDocument sendDocument = new SendDocument();
            sendDocument.setChatId(chatId);
            InputFile inputFile = new InputFile(fileInputStream,
                    pair.getSecond());
            sendDocument.setDocument(inputFile);
            sendDocument.setReplyMarkup(keyboardMarkup);
            bot.execute(sendDocument);
        }
        catch (TelegramApiException telegramApiException)
        {
            throw new RuntimeException(telegramApiException);
        }
        catch (IOException ioException)
        {
            throw new RuntimeException(ioException);
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
