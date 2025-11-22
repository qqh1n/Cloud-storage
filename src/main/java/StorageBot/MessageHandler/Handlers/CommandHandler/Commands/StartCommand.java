package StorageBot.MessageHandler.Handlers.CommandHandler.Commands;

import org.telegram.telegrambots.meta.api.objects.Message;

public class StartCommand implements Command_I
{
    public Object execute(String[] args )
    {
        return  " Cloud-storage\n" +
                "Телеграм-бот на **Java**, реализующий облачное хранилище.\n"+
                "\n" +
                "---\n" +
                "\n" +
                "## \uD83D\uDE80 Возможности:\n" +
                "- \uD83D\uDDC2 Создаёт и удаляет директории и переходит по ним.\n" +
                "- \uD83D\uDDD1\uFE0F Удаляет  указанный файл из текущей директории.\n" +
                "- \uD83D\uDCBE Принимает файлы от пользователей и сохраняет их.\n" +
                "- \uD83D\uDCC2 Показывает список сохранённых файлов\n" +
                "- \uD83D\uDCE5 Отправляет файл обратно по команде\n" +
                "\n" +
                "---\n";
    }
}
