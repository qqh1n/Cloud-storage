package StorageBot.MessageHandler.Handlers.DocumentHandler;

public class DocumentHandlerException
    extends Exception
{
    private static final ErrorCode UPLOAD_ATTEMPTS_LIMIT_EXCEEDED =
            ErrorCode.UPLOAD_ATTEMPTS_LIMIT_EXCEEDED;

    public static enum ErrorCode
    {
        UPLOAD_ATTEMPTS_LIMIT_EXCEEDED(0, "Превышен лимит попыток загрузки файла.");

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
