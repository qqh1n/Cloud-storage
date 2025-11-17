package StorageBot.Handlers.DocumentHandler;

import FileManager.FileManager;
import StorageBot.MessageHandler.MessageHandler_I;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

public class DocumentHandler
    implements MessageHandler_I
{
    private TelegramLongPollingBot bot;
    private FileManager fileManager

    public DocumentHandler(TelegramLongPollingBot bot, FileManager fileManager)
    {
        this.bot = bot;
        this.fileManager = fileManager;
    }


}
