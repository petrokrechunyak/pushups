package com.alphabetas.bot.caller.command;

import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerUser;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Random;

public class IAmHereCommand extends Command {
    private final String[] commonPhrases = new String[]{"Я тут та готовий вас кликати", "Хтось мене кликав?",
            "Так?", "Завжди тут"};

    private final String[] rarePhrases = new String[]{"Я тут, привіт)", "Га?", "Завжди до ваших послуг ;)"};

    private final String[] epicPhrases = new String[]{"Я живий, здоровий і взагалі працюю"};

    private final String[] legendaryPhrases = new String[]{"Я тут та готовий вас кликат... Почекайте це ж легендарна фраза, якою я відповідаю з шансом в 0.1%. Вітаю! В честь цього підпишіться на хатину Кликуна будь ласка: \n\n" +
            "<a href='https://t.me/callerHut'>Моя хатина</a>)))"};

    public IAmHereCommand(String msgText, CallerChat chat, CallerUser user, Integer threadId) {
        super(msgText, chat, user, threadId);
    }

    public static String getPhrase(String[] arr) {
        return arr[new Random().nextInt(arr.length)];
    }

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        Random random = new Random();
        int num = random.nextInt(1001);

        String phrase;
        if (num == 1000) {
            phrase = getPhrase(legendaryPhrases);
        } else if (num >= 990) {
            phrase = getPhrase(epicPhrases);
        } else if (num >= 900) {
            phrase = getPhrase(rarePhrases);
        } else {
            phrase = getPhrase(commonPhrases);
        }
        messageService.sendMessage(chatId, phrase, threadId);
    }
}
