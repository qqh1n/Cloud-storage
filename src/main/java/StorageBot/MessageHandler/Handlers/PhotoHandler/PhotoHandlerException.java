package StorageBot.MessageHandler.Handlers.PhotoHandler;

public class PhotoHandlerException
        extends Exception
{
    public enum ErrorCode
    {
        UPLOAD_ATTEMPTS_LIMIT_EXCEEDED(0,
                "Превышен лимит попыток загрузки фото."),
        UNABLE_TO_SAVE_PHOTO(1,
                "Невозможно сохранить фото."),
        INVALID_FILE_NAME(2,
                "Неверный формат имени файла.");

        private final int e_code;
        private final String message;

        ErrorCode(int code, String message)
        {
            this.e_code = code;
            this.message = message;
        }
    }

    private final PhotoHandlerException.ErrorCode e_code;

    public PhotoHandlerException(ErrorCode errorCode)
    {
        this.e_code = errorCode;
    }

    public int getCode()
    {
        return e_code.e_code;
    }

    public String getMessage()
    {
        return e_code.message;
    }
}
