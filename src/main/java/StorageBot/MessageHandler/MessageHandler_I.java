package StorageBot.MessageHandler;

import org.telegram.telegrambots.meta.api.objects.Message;

public interface MessageHandler_I
{
    void handleMessage(Message message);
    void sendMessage(Long chatId, Object objToSend);
}
