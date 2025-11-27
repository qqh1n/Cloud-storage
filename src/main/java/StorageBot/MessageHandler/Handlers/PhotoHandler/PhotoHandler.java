package StorageBot.MessageHandler.Handlers.PhotoHandler;

import FileManager.FileManager;
import FileManager.FileManagerException;
import StorageBot.MessageHandler.Handlers.MessageHandler_I;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class PhotoHandler
    implements MessageHandler_I<PhotoHandlerException>
{
    private final TelegramLongPollingBot bot;
    private final FileManager fileManager;

    public PhotoHandler
            (TelegramLongPollingBot bot, FileManager fileManager)
    {
        this.bot = bot;
        this.fileManager = fileManager;
    }

    public void handleMessage(Message message)
            throws PhotoHandlerException
    {
        List<PhotoSize> photos = message.getPhoto();
        String fileName = message.getCaption();


        if (fileName == null)
        {
            fileName = "photo.png";
        }
        else if (!isValidFileName(fileName))
        {
            throw new PhotoHandlerException(
                    PhotoHandlerException.ErrorCode.INVALID_FILE_NAME);
        }

        GetFile getFile = new GetFile();
        getFile.setFileId(Objects.requireNonNull(photos.stream()
                            .max(Comparator.comparing(PhotoSize::getFileSize))
                            .orElse(null)).getFileId());
        try
        {
            String filePath = bot.execute(getFile).getFilePath();
            String nameOfSavedFile = fileManager.saveFile(fileName,filePath);
            sendMessage(message.getChatId(),
                    "Фото успешно сохранено под именем '%s'".formatted(nameOfSavedFile));
        }
        catch (TelegramApiException telegramApiException)
        {
            throw new PhotoHandlerException(
                    PhotoHandlerException.ErrorCode.UNABLE_TO_SAVE_PHOTO);
        }
        catch (FileManagerException fileManagerException)
        {
            throw new PhotoHandlerException(
                    PhotoHandlerException.ErrorCode.UPLOAD_ATTEMPTS_LIMIT_EXCEEDED);
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
        String regEx = "^[^<>:\"/\\\\|?*]+\\.(jpg|jpeg|png|gif|webp|bmp|tiff|tif|psd|ico|cur|apng|avif|heic|heif|raw|cr2|nef|arw|dng)$";
        return fileName.matches(regEx);
    }
}
