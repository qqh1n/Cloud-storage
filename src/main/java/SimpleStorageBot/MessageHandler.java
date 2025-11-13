package SimpleStorageBot;

import SimpleStorageBot.Commands.Command_I;
import SimpleStorageBot.Commands.GetFileCommand;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.HashMap;

public class MessageHandler
{
    private final FileManager fileManager;
    private final HashMap<String, Command_I> commandsHashMap;

    public MessageHandler(String urlBase)
    {
        fileManager = new FileManager(urlBase);
        commandsHashMap = new HashMap<>();
        Command_I getFileCommand = new GetFileCommand();
        commandsHashMap.put()
    }

    public Object executeCommand(Message message)
    {
        if (message.hasText())
        {
            String text = message.getText();

            switch(text)
            {
                case "/start":

                    break;
            }
        }
    }
}
