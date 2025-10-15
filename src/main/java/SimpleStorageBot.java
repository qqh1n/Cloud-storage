import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;

public class SimpleStorageBot extends TelegramLongPollingBot
{
    final private String BOT_NAME = "";
    final private String BOT_TOKEN = "";

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
        try
        {
            if (update.hasMessage() && update.getMessage().hasText())
            {
                Message msg = update.getMessage();
                Long chatID = msg.getChatId();
                String textMSG = msg.getText();
                SendMessage sm = new SendMessage();
                sm.setChatId(chatID);
                if (textMSG.equals("/start"))
                {
                    String responce = "Добро пожаловать! Этот бот реализует облачное хранилище.";

                    sm.setText(responce);
                }
                else
                {
                    sm.setText(stub(chatID));
                }

                sm.setReplyMarkup(createKeyboard());
                execute(sm);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String stub(Long who)
    {
        String msg = "В разработке...";
        return msg;
    }

    public ReplyKeyboardMarkup createKeyboard()
    {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRows.add(keyboardRow);
        keyboardRow.add("/start");
        keyboardRow.add("storage/");
        keyboardRow.add("/files");

        replyKeyboardMarkup.setKeyboard(keyboardRows);

        return replyKeyboardMarkup;
    }
}
