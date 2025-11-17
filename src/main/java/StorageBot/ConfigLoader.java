package StorageBot;

import java.io.FileInputStream;
import java.util.Properties;
import java.io.IOException;


public class ConfigLoader
{
    private static final String PATH = "C:\\Users\\user\\IdeaProjects\\untitled\\config.properties";
    private static Properties properties;

    public ConfigLoader()
    {
        properties = new Properties();
        try (FileInputStream config = new FileInputStream(PATH))
        {
            properties.load(config);
        }
        catch (IOException e)
        {
            throw new RuntimeException("Не удалось загрузить config.properties", e);
        }
    }

    public String getName()
    {
        return properties.getProperty("NAME");
    }

    public String getToken()
    {
        return properties.getProperty("TOKEN");
    }

    public String getURLBase()
    {
        return properties.getProperty("URL_BASE");
    }

    public String getRootDir()
    {
        return properties.getProperty("ROOT_DIRECTORY");
    }
}
