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

public class SimpleStorageBot extends TelegramLongPollingBot {
    private String BOT_NAME;
    private String BOT_TOKEN;
    private FileManager fileManager;

    public SimpleStorageBot() {
        ConfigLoader cl = new ConfigLoader();
        BOT_NAME = cl.getName();
        BOT_TOKEN = cl.getToken();
        fileManager = new FileManager();
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                String text = update.getMessage().getText();
                Long chatId = update.getMessage().getChatId();

                switch(text) {
                    case "/start":
                        sendMessage(chatId, "Добро пожаловать! Этот бот реализует облачное хранилище.", createKeyboard());
                        break;
                    case "/files":
                        sendMessage(chatId, "Вот список сохранённых файлов:", createKeyboard());
                        File[] files = fileManager.getFiles();
                        sendFiles(files, chatId);
                        break;
                    case "/storage":
                        String response;
                        if (fileManager.storageFiles()) {
                            response = "Файлы успешно сохранены.";
                        } else {
                            response = "Возникли проблемы с сохранением. Файлы не были сохранены.";
                        }
                        sendMessage(chatId, response, createKeyboard());
                        break;
                    case "/get":
                        sendMessage(chatId, "Введите имя файла для получения:", createKeyboard());
                        break;
                    default:
                        sendMessage(chatId, "Неизвестная команда: " + text, createKeyboard());
                        break;
                }
            }
        } catch (Exception e) {
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

    private void sendFiles(File[] files, long chatId) {
        if (files == null || files.length == 0) {
            sendMessage(chatId, "Файлы не найдены.", createKeyboard());
            return;
        }

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
}