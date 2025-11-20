package StorageBot.MessageHandler.Handlers.DocumentHandler;

public class DocumentHandlerException
    extends Exception
{
    public static enum ErrorCode
    {
        UPLOAD_ATTEMPTS_LIMIT_EXCEEDED(0,
                "Превышен лимит попыток загрузки файла."),
        UNABLE_TO_SAVE_FILE(1,
                            "Невозможно отправить файл.");

        private final int e_code;
        private final String message;

        private ErrorCode(int code, String message)
        {
            this.e_code = code;
            this.message = message;
        }
    }

    private ErrorCode e_code;

    public DocumentHandlerException(ErrorCode errorCode)
    {
        this.e_code = errorCode;
    }

    public String getMessage()
    {
        return e_code.message;
    }
}
