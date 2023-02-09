package com.alphabetas.bot.caller.utils;

import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

public final class CommandUtils {
    public static final String SPACE_SYMBOL = "ˁ";
    public static final String TEMPLATE_REGEX = "\\wа-яА-ЯіІїЇґҐєЄ\\-'`" + SPACE_SYMBOL;
    public static final String SINGLE_WORD_REGEX = "[" + TEMPLATE_REGEX + "]*";
    public static final String CURRENT_REGEX = SINGLE_WORD_REGEX;
    public static final String NAME_WITH_SPACES_REGEX = "[" + TEMPLATE_REGEX + " ]*";

    private CommandUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * @param message start of message, which we will be edited
     * @param params  params, which will be cleared
     * @return edited string
     * <p>
     * Removes start of message such as "Кликун", ".", etc
     */
    public static String trimMessage(String message, String[] params) {
        message = message.replaceAll("@caller_ua_bot", "");
        message = message.replaceAll("@bunker_ua_bot", "");
        int max = message.length() > 7 ? 8 : message.length();
        if (StringUtils.containsIgnoreCase(message.substring(0, max), "кликун")) {
            message = StringUtils.replaceOnceIgnoreCase(message, "кликун", "");
        }
        message = message.replaceFirst("[.!]", "");
        for (String param : params) {
            message = StringUtils.replaceIgnoreCase(message, param, "");
        }
        return message.trim();
    }

    public static String makeLink(Update update) {
        return String.format("<a href='tg://user?id=%d'>%s</a>", update.getMessage().getFrom().getId(),
                update.getMessage().getFrom().getFirstName());
    }

    public static String makeLink(Long id, String firstName) {
        firstName = firstName.replaceAll("[<>]", "");
        return String.format("<a href='tg://user?id=%d'>%s</a>", id, firstName);
    }

    public static String encryptSpace(String text) {
        return text.replaceAll(" ", "ˁ");
    }

    public static String decryptSpace(String text) {
        return text.replaceAll("ˁ", " ");
    }


    public static void setUserToUpdate(Update update) {
        update.getMessage().getFrom().setId(update.getMessage().getReplyToMessage().getFrom().getId());
        update.getMessage().getFrom().setUserName(update.getMessage().getReplyToMessage().getFrom().getUserName());
        update.getMessage().getFrom().setFirstName(update.getMessage().getReplyToMessage().getFrom().getFirstName());
    }

    public static boolean isCommand(String text) {
        return text.startsWith("!") || text.startsWith(".") || text.toLowerCase().startsWith("кликун");
    }

    public static String deleteBadSymbols(String text) {
        return text.replaceAll("[<>]", "");
    }


}
