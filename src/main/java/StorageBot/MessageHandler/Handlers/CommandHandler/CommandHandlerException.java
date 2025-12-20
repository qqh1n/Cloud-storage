package StorageBot.MessageHandler.Handlers.CommandHandler;

public class CommandHandlerException
    extends Exception
{
    public static enum ErrorCode
    {
        INVALID_INPUT(0,"Неверный ввод."),
        INVALID_FILE_NAME(1, "Неверный формат имени файла."),
        UNKNOWN_COMMAND(2,"Неизвестная команда."),
        NO_SUCH_FILE_EXIST(3,"Файла с таким именем не существует."),
        UNABLE_TO_DELETE_FILE(4, "Невозможно удалить файл."),
        UNABLE_TO_GET_FILE(5, "Не удаётся получить файл."),
        INVALID_DIR_NAME(6, "Неверный формат названия папки."),
        UPLOAD_ATTEMPTS_LIMIT_EXCEEDED(7,
                "Превышен лимит попыток создания директории."),
        NO_SUCH_DIR_EXISTS(8,"Папки с таким именем не существует."),
        UNABLE_TO_CALL_DIR(9, "Невозможно перейти в папку."),
        UNABLE_TO_DELETE_DIR(10, "Невозможно удалить папку.");




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
