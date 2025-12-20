import StorageBot.StorageBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {
    public static void main(String[] args) {
        try
        {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            StorageBot bot = new StorageBot();
            botsApi.registerBot(bot);
        }
        catch (TelegramApiException e)
        {
            e.getMessage();
        }
    }
}

