package SimpleStorageBot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.util.ArrayList;

public class SimpleStorageBot extends TelegramLongPollingBot
{
    private final String BOT_NAME;
    private final String BOT_TOKEN;
    private final MessageHandler messageHandler;

    public SimpleStorageBot()
    {
        ConfigLoader cl = new ConfigLoader();
        BOT_NAME = cl.getName();
        BOT_TOKEN = cl.getToken();
        messageHandler = new MessageHandler(cl.getURLBase() + BOT_TOKEN + "/");
    }

    @Override
    public String getBotUsername()
    {
        return BOT_NAME;
    }

    @Override
    public String getBotToken()
    {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update)
    {
        try {
            if (update.hasMessage())
            {
                sendMessage(update.getMessage().getChatId(),
                            messageHandler.executeCommand(update.getMessage()),
                            createKeyboard());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ReplyKeyboardMarkup createKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        replyKeyboardMarkup.setSelective(true);

        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow firstRow = new KeyboardRow();
        firstRow.add("/start");
        firstRow.add("/storage");

        KeyboardRow secondRow = new KeyboardRow();
        secondRow.add("/files");
        secondRow.add("/get");

        keyboardRows.add(firstRow);
        keyboardRows.add(secondRow);

        replyKeyboardMarkup.setKeyboard(keyboardRows);
        return replyKeyboardMarkup;
    }

    private void sendMessage(Long chatId, String text, ReplyKeyboardMarkup keyboard) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        message.setReplyMarkup(keyboard);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendFiles(long chatId) {
        File[] files = fileManager.getFiles();
        if (files == null) {
            sendMessage(chatId, "Файлы не найдены.", createKeyboard());
            return;
        }

        sendMessage(chatId, "Вот список сохранённых файлов:", createKeyboard());
        for (File file : files) {
                SendDocument sendDocument = new SendDocument();
                sendDocument.setChatId(chatId);
                InputFile inputFile = new InputFile(file);
                sendDocument.setDocument(inputFile);
                try {
                    execute(sendDocument);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
        }
    }

    private void storageFiles(long chatId)
    {
        int numOfAddedFiles = fileManager.storageFiles();
        if (numOfAddedFiles == 0)
        {
            sendMessage(chatId, "Нет файлов для сохранения.", createKeyboard());
        }
        else
        {
            sendMessage(chatId, "Сохранённых файлов: " + numOfAddedFiles, createKeyboard());
        }
    }

}