package FileSystem;

public interface LocalFileSystem_I
{
    public String getPath(String fileName);
    public String getCurrentDirectory();
    public void addFile(String fileName);
}
