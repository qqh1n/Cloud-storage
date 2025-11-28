package StorageBot.MessageHandler.Handlers.VideoHandler;

import FileManager.FileManager;
import FileManager.FileManagerException;
import StorageBot.MessageHandler.Handlers.MessageHandler_I;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Video;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class VideoHandler
    implements MessageHandler_I<VideoHandlerException>
{
    private final TelegramLongPollingBot bot;
    private final FileManager fileManager;

    public VideoHandler
            (TelegramLongPollingBot bot, FileManager fileManager)
    {
        this.bot = bot;
        this.fileManager = fileManager;
    }

    public void handleMessage(Message message)
            throws VideoHandlerException
    {
        Video video = message.getVideo();
        String fileName = message.getCaption();

        if (fileName == null)
        {
            fileName = "video.mp4";
        }
        else if (!isValidFileName(fileName))
        {
            throw new VideoHandlerException(
                    VideoHandlerException.ErrorCode.INVALID_FILE_NAME);
        }

        GetFile getFile = new GetFile();
        getFile.setFileId(video.getFileId());
        try
        {
            String filePath = bot.execute(getFile).getFilePath();
            String nameOfSavedFile = fileManager.saveFile(fileName,filePath);
            sendMessage(message.getChatId(),
                    "Видео успешно сохранено под именем '%s'".formatted(nameOfSavedFile));
        }
        catch (TelegramApiException telegramApiException)
        {
            throw new VideoHandlerException(
                    VideoHandlerException.ErrorCode.UNABLE_TO_SAVE_VIDEO);
        }
        catch (FileManagerException fileManagerException)
        {
            throw new VideoHandlerException(
                     VideoHandlerException.ErrorCode.UPLOAD_ATTEMPTS_LIMIT_EXCEEDED);
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

    private boolean isValidFileName(String fileName)
    {
        String regEx = "^[^<>:\"/\\\\|?*]+\\.(mp4|avi|mkv|mov|wmv|flv|webm|mpeg|mpg|m4v|3gp|ts|m2ts|vob|ogv|rm|rmvb)$";
        return fileName.matches(regEx);
    }
}
