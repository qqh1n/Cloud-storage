package StorageBot.MessageHandler.Handlers.AudioHandler;

import FileManager.FileManager;
import FileManager.FileManagerException;
import StorageBot.MessageHandler.Handlers.MessageHandler_I;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Audio;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class AudioHandler
        implements MessageHandler_I <AudioHandlerException>
{
    private final TelegramLongPollingBot bot;
    private final FileManager fileManager;

    public AudioHandler(TelegramLongPollingBot bot, FileManager fileManager)
    {
        this.bot = bot;
        this.fileManager = fileManager;
    }

    public void handleMessage(Message message)
            throws AudioHandlerException
    {
        Audio audio = message.getAudio();
        String fileName = audio.getFileName();

        GetFile getFile = new GetFile();
        getFile.setFileId(audio.getFileId());
        try
        {
            String filePath = bot.execute(getFile).getFilePath();
            String nameOfSavedFile = fileManager.saveFile(fileName,filePath);
            sendMessage(message.getChatId(),
                    "Аудио успешно сохранено под именем '%s'".formatted(nameOfSavedFile));
        }
        catch (TelegramApiException telegramApiException)
        {
            throw new AudioHandlerException(
                    AudioHandlerException.ErrorCode.UNABLE_TO_SAVE_AUDIO);
        }
        catch (FileManagerException fileManagerException)
        {
            throw new AudioHandlerException(
                    AudioHandlerException.ErrorCode.UPLOAD_ATTEMPTS_LIMIT_EXCEEDED);
        }

    }

    private void sendMessage(Long chatId, String messageText)
    {
        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
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
