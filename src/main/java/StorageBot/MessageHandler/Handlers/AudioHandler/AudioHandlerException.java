package StorageBot.MessageHandler.Handlers.AudioHandler;

public class AudioHandlerException
        extends Exception
{
    public enum ErrorCode
    {
        UPLOAD_ATTEMPTS_LIMIT_EXCEEDED(0,
                "Превышен лимит попыток загрузки аудио."),
        UNABLE_TO_SAVE_AUDIO(1,
                "Невозможно сохранить аудио.");

        private final int e_code;
        private final String message;

        ErrorCode(int code, String message)
        {
            this.e_code = code;
            this.message = message;
        }
    }

    private final ErrorCode e_code;

    public AudioHandlerException(ErrorCode errorCode)
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
