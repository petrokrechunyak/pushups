package com.alphabetas.caller.utils;

import com.alphabetas.caller.model.CallerChat;
import com.alphabetas.caller.model.CallerName;
import com.alphabetas.caller.model.CallerUser;

import java.util.Arrays;

import static com.alphabetas.caller.utils.CommandUtils.SINGLE_WORD_REGEX;

public class AddNameUtils extends AbstractNameUtils {

    public static String saveNames(String message, CallerUser user, CallerChat chat) {
        String[] args = getNamesFromMessage(message);
        args = Arrays.stream(args).distinct().toArray(String[]::new);

        StringBuilder builder = new StringBuilder();
        for (String arg : args) {
            builder.append("Ім'я <b>").append(arg).append("</b> ");
            if (arg.matches(SINGLE_WORD_REGEX)) {
                arg = CommandUtils.encryptSpace(arg);
                CallerName name = nameService.getByCallerChatAndName(chat, arg);
                if(name != null) {
                    builder.append("вже зайнято! Власник - ").append(CommandUtils.makeLink(
                            name.getCallerUser().getUserId(),
                            name.getCallerUser().getFirstname()
                    ));
                    builder.append("\n");
                    continue;
                }
                if(arg.length() < 3) {
                    builder.append("занадто мале! Мінімальна кількість символів: <u>3</u>\n");
                    continue;
                }
                arg = CommandUtils.encryptSpace(arg);
                name = new CallerName(user.getUserId(), chat, arg);
                nameService.save(name);
                builder.append("успішно додано!");
            } else
                builder.append("не може бути додано через через заборонені символи.");
            builder.append("\n");
        }
        return builder.toString();
    }

}
