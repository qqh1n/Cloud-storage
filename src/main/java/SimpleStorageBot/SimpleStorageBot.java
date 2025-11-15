package SimpleStorageBot;

import SimpleStorageBot.MessageHandler.MessageHandler;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
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
    private final ConfigLoader configLoader;

    public SimpleStorageBot()
    {
        configLoader = new ConfigLoader();
        BOT_NAME = configLoader.getName();
        BOT_TOKEN = configLoader.getToken();
        messageHandler = new MessageHandler(configLoader.getURLBase() + BOT_TOKEN + "/");
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
                Message message = update.getMessage();
                if (message.hasText())
                {
                    Object result = messageHandler.executeCommand(message);

                    if (result instanceof String)
                    {
                        sendMessage(message.getChatId(), ((String) result), createKeyboard());
                    }
                    else
                    {
                        sendMessage(message.getChatId(), ((File) result), createKeyboard());
                    }
                }
                else if (message.hasDocument())
                {
                    Document document = message.getDocument();
                    String fileName = document.getFileName();

                    GetFile getFile = new GetFile();
                    getFile.setFileId(document.getFileId());
                    String filePath = execute(getFile).getFilePath();

                    messageHandler.putToBuffer(filePath, fileName);
                }
                else
                {
                   sendMessage(message.getChatId(),
                           "Простите, у меня лапки =(",
                            createKeyboard());
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
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
        firstRow.add("/storage <fileName>");

        KeyboardRow secondRow = new KeyboardRow();
        secondRow.add("/files");
        secondRow.add("/get <fileName>");

        keyboardRows.add(firstRow);
        keyboardRows.add(secondRow);

        replyKeyboardMarkup.setKeyboard(keyboardRows);
        return replyKeyboardMarkup;
    }

    private void sendMessage(Long chatId, String text, ReplyKeyboardMarkup keyboard)
    {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(text);
        message.setReplyMarkup(keyboard);
        try
        {
            execute(message);
        }
        catch (TelegramApiException e)
        {
            e.printStackTrace();
        }
    }

    private void sendMessage(Long chatId, File file, ReplyKeyboardMarkup keyboard)
    {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatId);
        InputFile inputFile = new InputFile(file);
        sendDocument.setDocument(inputFile);
        sendDocument.setReplyMarkup(keyboard);
        try
        {
            execute(sendDocument);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}