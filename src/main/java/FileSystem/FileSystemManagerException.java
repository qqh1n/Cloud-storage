package FileSystem;

public class FileSystemManagerException extends Exception
{
    public final static ErrorCode NO_SUCH_FILE_EXIST = ErrorCode.NO_SUCH_FILE_EXIST;
    public final static ErrorCode SUCH_FILE_EXIST = ErrorCode.SUCH_FILE_EXIST;
    public final static ErrorCode UNABLE_TO_STORAGE_FILE = ErrorCode.UNABLE_TO_STORAGE_FILE;

    public static enum ErrorCode
    {
        NO_SUCH_FILE_EXIST(0, "No such file exist."),
        SUCH_FILE_EXIST(1, "Such file exist."),
        UNABLE_TO_STORAGE_FILE(2, "Unable to storage file.");

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
