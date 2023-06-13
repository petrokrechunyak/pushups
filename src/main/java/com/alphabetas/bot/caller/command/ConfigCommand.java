package com.alphabetas.bot.caller.command;

import com.alphabetas.bot.caller.CallerBot;
import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerUser;
import com.alphabetas.bot.caller.utils.CommandUtils;
import com.alphabetas.bot.caller.utils.ConfigUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberOwner;

import java.util.List;

import static com.alphabetas.bot.caller.utils.ConfigUtils.mainMenu;

public class ConfigCommand extends Command {

    public ConfigCommand(String msgText, CallerChat chat, CallerUser user, Integer threadId) {
        super(msgText, chat, user, threadId);
    }

    public ConfigCommand() {
    }

    public static SendMessage mainMenuMessage(CallerChat chat) {
        String msg = String.format(ConfigUtils.MAIN_MENU_TEXT,
                CommandUtils.deleteBadSymbols(chat.getTitle()));
        SendMessage sendMessage = new SendMessage(chat.getId().toString(), msg);
        sendMessage.enableHtml(true);

        sendMessage.setReplyMarkup(mainMenu(chat));

        return sendMessage;
    }

    @Override
    public void execute(Update update) {

        if(user.getUserId().equals(CallerBot.MY_ID)) {
            messageService.sendMessage(mainMenuMessage(chat));
        }
        List<ChatMember> admins = messageService.getAdminsByChatId(chat.getId());
        for (ChatMember member : admins) {
            if ((member instanceof ChatMemberOwner
                    && user.getUserId().equals(member.getUser().getId()))) {
                messageService.sendMessage(mainMenuMessage(chat));
            }
        }
    }

    @Override
    public String[] getSpecialArgs() {
        return new String[]{"/config", "конфіг"};
    }

}
