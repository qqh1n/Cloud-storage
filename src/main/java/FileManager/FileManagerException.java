package FileManager;

public class FileManagerException
    extends Exception
{
    public static enum ErrorCode
    {
        NO_SUCH_FILE(0, "No such file."),
        UPLOAD_ATTEMPTS_LIMIT_EXCEEDED(1, "Upload attempts limit exceeded."),
        UNABLE_TO_SAVE_FILE(2,"Unable to save file."),
        UNABLE_TO_DELETE_FILE(3, "Unable to delete file."),
        NO_SUCH_DIR_EXISTS(4, "No such directory."),
        UNABLE_TO_CALL_DIRECTORY(5, "Unable to call directory."),
        UNABLE_TO_DELETE_DIRECTORY(6, "Unable to delete directory."),
        NO_SUCH_STORAGE(7, "No such storage."),
        STORAGE_IS_NOT_SELECTED(8, "Storage is not selected."),
        UNABLE_TO_CREATE_STORAGE(9, "Unable to create storage."),
        STORAGE_EXISTS(10, "Storage exists.");

        private final int e_code;
        private final String message;

        private ErrorCode(int code, String message)
        {
            this.e_code = code;
            this.message = message;
        }
    }

    private final ErrorCode e_code;

    public FileManagerException(ErrorCode errorCode)
    {
        this.e_code = errorCode;
    }

    public String getMessage()
    {
        return e_code.message;
    }

    public int getCode()
    {
        return e_code.e_code;
    }
}
