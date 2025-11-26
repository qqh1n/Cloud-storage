package StorageBot.MessageHandler.Handlers.DocumentHandler;

import FileManager.FileManager;
import FileManager.FileManagerException;
import StorageBot.MessageHandler.Handlers.MessageHandler_I;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class DocumentHandler
        implements MessageHandler_I<DocumentHandlerException>
{
    private final TelegramLongPollingBot bot;
    private final FileManager fileManager;

    public DocumentHandler(TelegramLongPollingBot bot, FileManager fileManager)
    {
        this.bot = bot;
        this.fileManager = fileManager;
    }

    public void handleMessage(Message message)
            throws DocumentHandlerException
    {
        Document document = message.getDocument();
        String fileName = document.getFileName();

        GetFile getFile = new GetFile();
        getFile.setFileId(document.getFileId());
        try
        {
            String filePath = bot.execute(getFile).getFilePath();
            String nameOfSavedFile = fileManager.saveFile(fileName,filePath);
            sendMessage(message.getChatId(),
                        "Файл успешно сохранён под именем '%s'".formatted(nameOfSavedFile));
        }
        catch (TelegramApiException telegramApiException)
        {
            throw new DocumentHandlerException(
                    DocumentHandlerException.ErrorCode.UNABLE_TO_SAVE_FILE);
        }
        catch (FileManagerException fileManagerException)
        {
            throw new DocumentHandlerException(
                    DocumentHandlerException.ErrorCode.UPLOAD_ATTEMPTS_LIMIT_EXCEEDED);
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
