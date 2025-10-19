import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;

public class FileManager
{
    private File[] fileBuffer;
    private HashMap<String, String> telegramFileBuffer;
    private final String rootDirectoryPath;
    private final File rootDirectory;

    public FileManager()
    {
        ConfigLoader configLoader = new ConfigLoader();
        rootDirectoryPath = configLoader.getStoragePath();
        rootDirectory = new File(rootDirectoryPath);
        telegramFileBuffer = new HashMap<>();
    }

    private boolean fileBufferIsEmpty()
    {
        if (fileBuffer == null || fileBuffer.length == 0)
        {
            return true;
        }
        return false;
    }

    public File[] getFiles()
    {
        fileBuffer = rootDirectory.listFiles();
        if (fileBufferIsEmpty())
        {
            return null;
        }
        return fileBuffer;
    }

    public int storageFiles() {
        int numOfStoragedFiles = 0;
        if (telegramFileBuffer.isEmpty()) {
            return numOfStoragedFiles;
        }
        for (String filePath : telegramFileBuffer.keySet()) {
            String fileName = telegramFileBuffer.get(filePath);
            downloadFile(filePath, fileName);
            numOfStoragedFiles++;
        }

        telegramFileBuffer.clear();

        return numOfStoragedFiles;
    }

    private void downloadFile(String filePath, String fileName)
    {
        String fullURL = getDownloadPath(filePath);
        URL url = null;
        try {
            url = new URL(fullURL);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        InputStream inputStream = null;
        try {
            inputStream = url.openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            Files.copy(inputStream, Paths.get(rootDirectoryPath + "/" + fileName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getDownloadPath(String filePath)
    {
        return "https://api.telegram.org/file/bot" + filePath;
    }

    public void addToFileBuffer(String filePath, String fileName)
    {
        telegramFileBuffer.put(filePath, fileName);
    }
}
