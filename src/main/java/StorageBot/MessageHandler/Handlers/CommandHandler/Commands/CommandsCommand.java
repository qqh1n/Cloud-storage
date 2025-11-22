package StorageBot.MessageHandler.Handlers.CommandHandler.Commands;


public class CommandsCommand implements Command_I
{
    public Object execute(String[] args)
    {
        return "Вот список команд для бота:\n" +
                "\tПросто отправьте файл и он автоматически сохранится в текущую папку.\n" +
                "\t'/mkdir dirName' - Создать папку с именем 'dirName' в текущей папке.\n" +
                "\t'/cd dirName' - Перейти в папку с именем 'dirName', которая находится в текущей папке.\n" +
                "\t('/cd .' - Перейти корень файловой системы)\n" +
                "\t('/cd ..' - Перейти в родительскую папку)\n" +
                "\t'/rmdir dirName' - Удаляет папку c именем 'dirName' (и её содержимое)," +
                " которая находится в текущей папке.\n " +
                "\t'/commands' - Показать список возможных команд.\n" +
                "\t'/delete fileName - Удалить файл с именем 'fileName' из текущей папки.\n"+
                "\t'/files' - Вывести список файлов, находящихся в текущей папке.\n"+
                "\t'/get fileName' - Получить указанный файл (из текущей папки) в чат.\n";
    }
}
