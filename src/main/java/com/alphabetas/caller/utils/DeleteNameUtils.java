package com.alphabetas.caller.utils;

import com.alphabetas.caller.model.CallerChat;
import com.alphabetas.caller.model.CallerName;
import com.alphabetas.caller.model.CallerUser;

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
                builder.append("Ім'я <b>").append(arg).append("</b> ");
                if (name.getCallerUser().equals(user)) {
                    nameService.delete(name);
                    builder.append("успішно видалено!");
                } else {
                    builder.append("зайнято іншою людиною. Чужі імена видаляти не можна!");
                }
            } else {
                builder.append("У вас немає імені ").append(arg);
            }
            builder.append("\n");
        }
        return builder.toString();
    }

}
