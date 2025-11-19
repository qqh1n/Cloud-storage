package StorageBot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class StorageBot extends TelegramLongPollingBot
{
    private final String BOT_NAME;
    private final String BOT_TOKEN;
    private final HandlerManager handlerManager;
    private final ConfigLoader configLoader;

    public StorageBot()
    {
        configLoader = new ConfigLoader();
        BOT_NAME = configLoader.getName();
        BOT_TOKEN = configLoader.getToken();
        handlerManager = new HandlerManager(this);
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
                handlerManager.handleMessage(update.getMessage());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}