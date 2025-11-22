package FileSystem;

public class FileSystemManagerException extends Exception
{
    public static enum ErrorCode
    {
        NO_SUCH_FILE_EXIST(0, "No such file exist."),
        UNABLE_TO_SAVE_FILE(1, "Unable to storage file."),
        UNABLE_TO_DELETE_FILE(2, "Unable to delete file."),
        NO_SUCH_DIR_EXISTS(3, "No such directory exist."),
        UNABLE_TO_CALL_DIR(4, "Unable to call directory."),
        UNABLE_TO_MAKE_DIR(5, "Unable to make directory."),
        SUCH_DIR_EXISTS(6, "Such directory exist."),
        UNABLE_TO_DELETE_DIR(7, "Unable to delete directory.");

        private final int e_code;
        private final String message;

        private ErrorCode(int e_code, String message)
        {
            this.e_code = e_code;
            this.message = message;
        }
    }

    private ErrorCode e_code;

    public FileSystemManagerException(ErrorCode code)
    {
        this.e_code = code;
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
