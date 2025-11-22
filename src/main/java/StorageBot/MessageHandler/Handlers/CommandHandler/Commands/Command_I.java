package StorageBot.MessageHandler.Handlers.CommandHandler.Commands;

import StorageBot.MessageHandler.Handlers.CommandHandler.CommandHandlerException;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface Command_I
{
    Object execute(String[] args)
        throws CommandHandlerException;
}
