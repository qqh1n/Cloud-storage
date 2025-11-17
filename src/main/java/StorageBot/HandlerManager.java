package StorageBot;

import FileManager.FileManager;
import StorageBot.Handlers.CommandHandler.CommandHandlerException;
import StorageBot.Handlers.DocumentHandler.DocumentHandler;
import StorageBot.Handlers.DocumentHandler.DocumentHandlerException;
import StorageBot.MessageHandler.MessageHandler_I;
import StorageBot.Handlers.CommandHandler.CommandHandler;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;

public class HandlerManager
{
    private TelegramLongPollingBot bot;
    private HashMap<String, MessageHandler_I> handlers;
    public HandlerManager(TelegramLongPollingBot bot)
    {
        this.bot = bot;
        handlers = new HashMap<String, MessageHandler_I>();
        FileManager fileManager = new FileManager;
        handlers.put("CommandHandler", new CommandHandler(bot, fileManager));
        handlers.put("DocumentHandler", new DocumentHandler(bot, fileManager));
    }

    public void HandleMessage(Message message)
    {
        MessageHandler_I handler;
        if (message.hasText())
        {
            handler = handlers.get("CommandHandler");
            try
            {
                handler.handleMessage(message);
            }
            catch (CommandHandlerException e)
            {
                sendErrorMessage(message.getChatId(), e.getMessage());
            }
        }
        else if (message.hasDocument())
        {
            handler = handlers.get("DocumentHandler");
            try
            {
                handler.handleMessage(message);
            }
            catch (DocumentHandlerException e)
            {
                sendErrorMessage(message.getChatId(), e.getMessage());
            }
        }
        else
        {
            sendErrorMessage(message.getChatId());
        }
    }

    private void sendErrorMessage(Long chatId)
    {

    }

    private void sendErrorMessage(Long chatId, String errorMessageText)
    {

    }
}
