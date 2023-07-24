package com.alphabetas.bot.caller.utils;

import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerName;
import com.alphabetas.bot.caller.model.CallerUser;
import com.alphabetas.bot.caller.model.GroupName;
import com.alphabetas.bot.caller.model.enums.UserStates;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Set;

import static com.alphabetas.bot.caller.utils.CommandUtils.*;

public class AddNameUtils extends AbstractNameUtils {

    public static String saveNames(String message, CallerUser user, CallerChat chat) {
        String[] args = getNamesFromMessage(message);
        args = Arrays.stream(args).distinct().toArray(String[]::new);

        StringBuilder builder = new StringBuilder();

        for (String arg : args) {
            if(StringUtils.equalsIgnoreCase(arg, "блять") && user.getUserId().equals(1312393409L) && chat.getId().equals(-1001344204192L)) {
                builder.append("Ярік блять, да ти заїбав додавати це ім'я, хватить!\n");
            }
            if(groupNameService.getByNameAndChat(CommandUtils.encryptSpace(arg), chat) != null) {
                GroupUtils.joinGroup(arg, user, chat);
                builder.append("Групове ім'я <b><u>").append(arg).append("</u></b> успішно додано!\n");
                continue;
            }
            Set<CallerName> nameSet = nameService.getAllByCallerUser(user);
            if (nameSet.size() >= chat.getConfig().getNameLimit()) {
                builder.append("Досягнуто ліміту на імена: ")
                        .append(chat.getConfig().getNameLimit())
                        .append(". Ім'я <b><u>")
                        .append(arg)
                        .append("</u></b> не додано!\n");
                continue;
            }
            builder.append("Ім'я <b><u>").append(arg).append("</u></b> ");
            String currentRegex = chat.getConfig().isAllowSpace()
                    ? NAME_WITH_SPACES_REGEX
                    : SINGLE_WORD_REGEX;
            if (arg.matches(currentRegex)) {
                arg = CommandUtils.encryptSpace(arg);
                CallerName name = nameService.getByCallerChatAndName(chat, arg);
                if (name != null) {
                    builder.append("вже зайнято! Власник - ").append(CommandUtils.makeLink(
                            name.getCallerUser().getUserId(),
                            name.getCallerUser().getFirstname()
                    ));
                    builder.append("\n");
                    continue;
                }
                if (arg.length() < 3) {
                    builder.append("занадто мале! Мінімальна кількість символів: <u>3</u>\n");
                    continue;
                }
                arg = CommandUtils.encryptSpace(arg);
                name = new CallerName(user.getUserId(), chat, arg);
                nameService.save(name);
                user.setUserState(UserStates.OFF);
                userService.save(user);
                builder.append("успішно додано!");
            } else
                builder.append("не може бути додано через через заборонені символи.");
            builder.append("\n");
        }
        return builder.toString();
    }
}
