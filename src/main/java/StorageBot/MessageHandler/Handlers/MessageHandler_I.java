package StorageBot.MessageHandler.Handlers;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface MessageHandler_I<T extends Exception>
{
    void handleMessage (Message message)
            throws T;
}
