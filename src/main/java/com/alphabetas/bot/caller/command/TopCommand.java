package com.alphabetas.bot.caller.command;

import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerUser;
import com.alphabetas.bot.caller.model.MessageCount;
import com.alphabetas.bot.caller.utils.CommandUtils;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
public class TopCommand extends Command {

    public TopCommand(String msgText, CallerChat chat, CallerUser user, Integer threadId) {
        super(msgText, chat, user, threadId);
    }

    @Override
    public void execute(Update update) {
        log.info("Entered into TopCommand");

        List<MessageCount> allInChat = messageCountService.getAllByChat(chat);
        Map<CallerUser, Integer> counter = new LinkedHashMap<>();
        for(MessageCount one: allInChat) {
            counter.put(one.getCallerUser(), counter.getOrDefault(one.getCallerUser(), 0) + one.getCount());
        }

        counter = counter.entrySet().stream().sorted(Map.Entry.comparingByValue((o1, o2) -> o2 - o1)).collect(Collectors.toMap(
                Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));


        StringBuilder builder = new StringBuilder("Статистика по написаним повідомленням за останні 24 години:\n\n");
        int general = 0;
        int i = 1;

        Message message = messageService.sendMessage(chat.getId(), builder.toString(), threadId);

        for(Map.Entry<CallerUser, Integer> entries: counter.entrySet()) {
            builder.append(i)
                    .append(". ")
                    .append(CommandUtils.makeLink(entries.getKey().getUserId(), entries.getKey().getFirstname())).append(" — ")
                    .append(entries.getValue())
                    .append("\n");
            i++;
            general += entries.getValue();
        }

        builder.append("\nЗагальна кількість повідомлень: ").append(general);
        messageService.editMessage(chat.getId(), message.getMessageId().longValue(), builder.toString());
    }

    @Override
    public String[] getSpecialArgs() {
        return new String[]{"/top", "топ"};
    }
}
