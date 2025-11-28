package StorageBot;

import FileManager.FileManager;
import StorageBot.MessageHandler.Handlers.PhotoHandler.PhotoHandler;
import StorageBot.MessageHandler.Handlers.PhotoHandler.PhotoHandlerException;
import StorageBot.MessageHandler.Handlers.AudioHandler.AudioHandler;
import StorageBot.MessageHandler.Handlers.CommandHandler.CommandHandlerException;
import StorageBot.MessageHandler.Handlers.DocumentHandler.DocumentHandler;
import StorageBot.MessageHandler.Handlers.DocumentHandler.DocumentHandlerException;
import StorageBot.MessageHandler.Handlers.MessageHandler_I;
import StorageBot.MessageHandler.Handlers.CommandHandler.CommandHandler;
import StorageBot.MessageHandler.Handlers.VideoHandler.VideoHandler;
import StorageBot.MessageHandler.Handlers.VideoHandler.VideoHandlerException;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;

public class HandlerManager
{
    private final TelegramLongPollingBot bot;
    private final HashMap<String, MessageHandler_I<? extends Exception>> handlers;

    public HandlerManager(TelegramLongPollingBot bot, String urlBase)
    {
        this.bot = bot;
        handlers = new HashMap<>();
        FileManager fileManager = new FileManager(urlBase);
        handlers.put("CommandHandler", new CommandHandler(bot, fileManager));
        handlers.put("DocumentHandler", new DocumentHandler(bot, fileManager));
        handlers.put("AudioHandler", new AudioHandler(bot, fileManager));
        handlers.put("PhotoHandler", new PhotoHandler(bot, fileManager));
        handlers.put("VideoHandler", new VideoHandler(bot, fileManager));
    }

    public void handleMessage(Message message)
    {

        MessageHandler_I<? extends Exception> handler = null;

        try
        {
            handleMsg(message, handler);
        }
        catch (Exception handlerException)
        {
            handleException(message, handlerException);
        }

    }

    void handleMsg
            (Message message,
             MessageHandler_I <? extends  Exception> handler)
            throws Exception
    {
        if (message.hasText())
        {
            handler = handlers.get("CommandHandler");
        }
        else if (message.hasDocument())
        {
            handler = handlers.get("DocumentHandler");
        }
        else if (message.hasAudio())
        {
            handler = handlers.get("AudioHandler");
        }
        else if (message.hasPhoto())
        {
            handler = handlers.get("PhotoHandler");
        }
        else if (message.hasVideo())
        {
            handler = handlers.get("VideoHandler");
        }
        handler.handleMessage(message);
    }

    void handleException
            (Message message, Exception handlerException)
    {
        if (handlerException instanceof CommandHandlerException ||
                handlerException instanceof DocumentHandlerException||
                handlerException instanceof PhotoHandlerException ||
                handlerException instanceof VideoHandlerException)
        {
            sendErrorMessage(message.getChatId(), handlerException.getMessage());
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
