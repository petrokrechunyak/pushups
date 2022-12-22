package com.alphabetas.caller.utils;

import org.telegram.telegrambots.meta.api.objects.Update;

public final class CommandUtils {
    public static final String SPACE_SYMBOL = "ˁ";
    public static final String TEMPLATE_REGEX = "\\wа-яА-ЯіІїЇґҐєЄ\\-'`" + SPACE_SYMBOL;
    public static final String SINGLE_WORD_REGEX = "[" + TEMPLATE_REGEX + "]*";

    public static final String NAME_WITH_SPACES_REGEX = "[" + TEMPLATE_REGEX + " ]*";
    private CommandUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * @param message start message, it we will be edited
     * @param params params, which will be cleared
     * @return edited string
     *
     * Removes start of message such as "Кликун", ".", etc
     */
    public static String trimMessage(String message, String[] params) {
        message = message.toLowerCase();
        for (String param: params) {
            message = message.replaceAll("@caller_ua_bot", "");
            message = message.replaceAll("@bunker_ua_bot", "");
            message = message.replaceAll(param + " *", "");
        }
        return message.trim();
    }

    public static String makeLink(Update update) {
        return String.format("<a href='tg://user?id=%d'>%s</a>", update.getMessage().getFrom().getId(),
                update.getMessage().getFrom().getFirstName());
    }

    public static String makeLink(Long id, String firstName) {
        return String.format("<a href='tg://user?id=%d'>%s</a>", id, firstName);
    }

    public static String encryptSpace(String text) {
        return text.replaceAll(" ", "ˁ");
    }

    public static String decryptSpace(String text) {
        return text.replaceAll("ˁ", " ");
    }



}
