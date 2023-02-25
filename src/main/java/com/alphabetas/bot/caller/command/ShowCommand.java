package com.alphabetas.bot.caller.command;

import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerName;
import com.alphabetas.bot.caller.model.CallerUser;
import com.alphabetas.bot.caller.model.GroupName;
import com.alphabetas.bot.caller.utils.CommandUtils;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@NoArgsConstructor
@Slf4j
public class ShowCommand extends Command {

    public ShowCommand(String msgText, CallerChat chat, CallerUser user, Integer threadId) {
        super(msgText, chat, user, threadId);
    }

    @Override
    public void execute(Update update) {
        // delete message if starts with /
        if (msgText.startsWith("/")) {
            messageService.deleteMessage(chat.getId().toString(), update.getMessage().getMessageId());
        }

        if (update.getMessage().getReplyToMessage() != null) {
            Message message = update.getMessage().getReplyToMessage();

            if (message.getFrom().getIsBot()) {

                if (message.getFrom().getUserName().equals("caller_ua_bot")) {
                    messageService.sendMessage(chat.getId(),
                            "У мене тільки одне ім'я - Кликун ;)",
                            threadId);
                    return;
                }
                messageService.sendMessage(chat.getId(),
                        "У ботів не може бути імен :/",
                        threadId);
                return;
            }
            CommandUtils.setUserToUpdate(update);
        }

        user = userService.getByUserIdAndCallerChat(chat, update);

        if (user.getNames().size() == 0 && user.getGroupNames().size() == 0) {
            messageService.sendMessage(user.getCallerChat().getId(),
                    "У користувача " + CommandUtils.makeLink(
                            user.getUserId(),
                            user.getFirstname()
                    ) + " ще немає імен, але їх завжди можна додати командою /add",
                    threadId);
            return;
        }
        StringBuilder builder = new StringBuilder("<b>Імена ")
                .append(CommandUtils.makeLink(user.getUserId(), user.getFirstname()))
                .append("</b>\n");
        for (CallerName name : user.getNames()) {
            builder.append(name.getName()).append("\n");
        }

        builder.append("\n<b>Групові імена: </b>\n");
        for (GroupName name : user.getGroupNames()) {
            builder.append(name.getName()).append("\n");
        }

        messageService.sendMessage(user.getCallerChat().getId(), CommandUtils.decryptSpace(builder.toString()),
                threadId);
    }

    @Override
    public String[] getSpecialArgs() {
        return new String[]{"/show_names", "/show_name", "/show",
                "імена", "шов", "покажи",
                ".імена", ".шов"};
    }
}
