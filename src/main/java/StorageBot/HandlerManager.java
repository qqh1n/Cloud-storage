package StorageBot;

import FileManager.FileManager;
import StorageBot.MessageHandler.Handlers.DocumentHandler.DocumentHandler;
import StorageBot.MessageHandler.Handlers.MessageHandler_I;
import StorageBot.MessageHandler.Handlers.CommandHandler.CommandHandler;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;

public class HandlerManager
{
    private TelegramLongPollingBot bot;
    private HashMap<String, MessageHandler_I<? extends Exception>> handlers;

    public HandlerManager(TelegramLongPollingBot bot, String urlBase)
    {
        this.bot = bot;
        handlers = new HashMap<String, MessageHandler_I<? extends Exception>>();
        FileManager fileManager = new FileManager(urlBase);
        handlers.put("CommandHandler", new CommandHandler(bot, fileManager));
        handlers.put("DocumentHandler", new DocumentHandler(bot, fileManager));
    }

    public void handleMessage(Message message)
    {
        MessageHandler_I<? extends Exception> handler;
        if (message.hasText())
        {
            handler = handlers.get("CommandHandler");
            try
            {
                handler.handleMessage(message);
            }
            catch (Exception commandHandlerException)
            {
                sendErrorMessage(message.getChatId(),
                        commandHandlerException.getMessage());
            }
        }
        else if (message.hasDocument())
        {
            handler = handlers.get("DocumentHandler");
            try
            {
                handler.handleMessage(message);
            }
            catch (Exception documentHandlerException)
            {
                sendErrorMessage(message.getChatId(),
                        documentHandlerException.getMessage());
            }
        }
        else
        {
            sendErrorMessage(message.getChatId());
        }
    }

    private void sendErrorMessage(Long chatId)
    {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        String messageText = "❌Получено сообщение, " +
                "которое не представляется возможным обработать❌";
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

    private void sendErrorMessage(Long chatId, String errorMessageText)
    {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        String messageText = "❌" + errorMessageText + "❌";
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
}
