package StorageBot.MessageHandler.Handlers.CommandHandler;

public class CommandHandlerException
    extends Exception
{
    public static enum ErrorCode
    {
        INVALID_INPUT(0,"Invalid input."),
        UNKNOWN_COMMAND(1,"Unknown command."),
        NO_SUCH_FILE_EXIST(2,"No such file exist.");

        private final int e_code;
        private final String message;

        private ErrorCode(int code, String message)
        {
            this.e_code = code;
            this.message = message;
        }
    }

    private ErrorCode e_code;

    public CommandHandlerException(ErrorCode errorCode)
    {
        this.e_code = errorCode;
    }

    public String getMessage()
    {
        return e_code.message;
    }
}
