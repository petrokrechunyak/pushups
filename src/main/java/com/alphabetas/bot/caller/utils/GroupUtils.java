package com.alphabetas.bot.caller.utils;

import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerUser;
import com.alphabetas.bot.caller.model.GroupName;

import java.util.Arrays;

public class GroupUtils extends AbstractNameUtils {

    public static String createGroup(String message, CallerUser user, CallerChat chat) {

        String[] args = getNamesFromMessage(message);
        args = Arrays.stream(args).distinct().toArray(String[]::new);

        StringBuilder builder = new StringBuilder();


        for(String arg: args) {
            String decrypted = arg;
            arg = CommandUtils.encryptSpace(arg);

            builder.append("Групове ім'я <b>").append(decrypted).append("</b> ");

            GroupName groupName = groupNameService.getByNameAndChat(arg, chat);
            if(groupName == null) {
                groupName = new GroupName(chat, arg);
                groupNameService.save(groupName);
                builder.append("успішно створено!\nПриєднатися до нього можна командою <b>Кликун приєднай ")
                        .append(decrypted).append("</b>");
            } else {
                builder.append("вже існує!\nПриєднатися до нього можна командою <b>Кликун приєднай ")
                        .append(decrypted).append("</b>");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public static String leaveGroup(String msgText, CallerUser user, CallerChat chat) {
        String[] args = getNamesFromMessage(msgText);
        args = Arrays.stream(args).distinct().toArray(String[]::new);

        StringBuilder builder = new StringBuilder();

        for(String arg: args) {
            String decrypted = arg;
            arg = CommandUtils.encryptSpace(arg);

            GroupName groupName = groupNameService.getByNameAndChat(arg, chat);
            if(groupName != null) {
                if(groupName.getUsers().contains(user)) {
                    builder.append("Групове ім'я <b>").append(decrypted).append(" </b>успішно покинуто");
                } else {
                    builder.append("Ви не входите до цього групового імені!");
                }
                groupName.getUsers().remove(user);
                groupNameService.save(groupName);
            } else {
                builder.append("Такого групового імені не існує!");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public static String joinGroup(String msgText, CallerUser user, CallerChat chat) {
        String[] args = getNamesFromMessage(msgText);
        args = Arrays.stream(args).distinct().toArray(String[]::new);

        StringBuilder builder = new StringBuilder();

        for(String arg: args) {
            String decrypted = arg;
            String boldArg = "<b>" + arg + "</b>";
            arg = CommandUtils.encryptSpace(arg);

            GroupName groupName = groupNameService.getByNameAndChat(arg, chat);
            if(groupName != null) {
                if(groupName.getUsers().contains(user)) {
                    builder.append("Ви вже входите до групового імені ").append(boldArg);
                } else {
                    builder.append("Групове ім'я ").append(boldArg).append(" успішно додано");
                }
                groupName.getUsers().add(user);
                groupNameService.save(groupName);
            } else {
                builder.append("Групового імені ").append(boldArg).append(" не існує! ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

    public static String removeGroup(String msgText, CallerUser user, CallerChat chat) {
        String[] args = getNamesFromMessage(msgText);
        args = Arrays.stream(args).distinct().toArray(String[]::new);

        StringBuilder builder = new StringBuilder();

        for(String arg: args) {
            String decrypted = arg;
            arg = CommandUtils.encryptSpace(arg);

            builder.append("Групове ім'я <b>").append(decrypted).append("</b> ");

            GroupName groupName = groupNameService.getByNameAndChat(arg, chat);
            if(groupName != null) {
                groupNameService.delete(groupName);
                builder.append("успішно видалено!");
            } else {
                builder.append("не існує!");
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}
