package com.alphabetas.bot.caller.utils;

import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerName;
import com.alphabetas.bot.caller.model.CallerUser;
import com.alphabetas.bot.caller.model.enums.UserStates;

import java.util.Arrays;

public class DeleteNameUtils extends AbstractNameUtils {

    public static String deleteNames(String message, CallerUser user, CallerChat chat) {
        String[] args = getNamesFromMessage(message);
        args = Arrays.stream(args).distinct().toArray(String[]::new);

        StringBuilder builder = new StringBuilder();
        for (String arg : args) {
            arg = CommandUtils.encryptSpace(arg);
            CallerName name = nameService.getByCallerChatAndName(chat, arg);
            arg = CommandUtils.decryptSpace(arg);
            if (name != null) {
                builder.append("Ім'я <b><u>").append(arg).append("</u></b> ");
                if (name.getCallerUser().equals(user)) {
                    nameService.delete(name);
                    user.setUserState(UserStates.OFF);
                    userService.save(user);
                    builder.append("успішно видалено!");
                } else {
                    builder.append("зайнято іншою людиною. Чужі імена видаляти не можна!");
                }
            } else {
                builder.append("У вас немає імені ").append("<b><u>" + arg + "</u></b>");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

}
