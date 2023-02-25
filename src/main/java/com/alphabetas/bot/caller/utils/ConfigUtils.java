package com.alphabetas.bot.caller.utils;

import com.alphabetas.bot.caller.command.CallBack;
import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.ChatConfig;
import com.alphabetas.bot.caller.model.enums.ConfigStateEnum;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class ConfigUtils {

    public static final String MAIN_MENU_TEXT = "Налаштування для групи <b>%s</b>\n";
    public static int DEFAULT_CHAT_LIMIT = 8;
    public static String CONFIG_DATA = "CONFIG.";

    public static InlineKeyboardMarkup mainMenu(CallerChat chat) {
        return createMarkupByButton(limitNamesMenu(chat),
                rpCommandsMenu(chat), aggressionMenu(chat), allowSpaceMenu(chat)
        , close(chat));
    }

    public static InlineKeyboardButton close(CallerChat chat) {
        return InlineKeyboardButton.builder()
                .callbackData(CONFIG_DATA + chat.getId() + "." + ConfigStateEnum.CLOSE)
                .text("Завершити налаштування")
                .build();
    }

    public static InlineKeyboardButton limitNamesMenu(CallerChat chat) {
        return InlineKeyboardButton.builder()
                .callbackData(CONFIG_DATA + chat.getId() + "." + ConfigStateEnum.LIMIT_MENU)
                .text("Налаштування ліміту імен")
                .build();
    }

    public static InlineKeyboardButton rpCommandsMenu(CallerChat chat) {
        return InlineKeyboardButton.builder()
                .callbackData(CONFIG_DATA + chat.getId() + "." + ConfigStateEnum.RP_MENU)
                .text("Налаштування рп-команд")
                .build();
    }

    public static InlineKeyboardButton aggressionMenu(CallerChat chat) {
        return InlineKeyboardButton.builder()
                .callbackData(CONFIG_DATA + chat.getId() + "." + ConfigStateEnum.AGGRESSION_MENU)
                .text("Налаштування агресії")
                .build();
    }

    public static InlineKeyboardButton allowSpaceMenu(CallerChat chat) {
        return InlineKeyboardButton.builder()
                .callbackData(CONFIG_DATA + chat.getId() + "." + ConfigStateEnum.ALLOW_SPACES)
                .text("Імена з пробілами")
                .build();
    }

    public static void limitMenu(ChatConfig config, Update update) {
        CallerChat chat = config.getChat();
        String text = "Ліміт на імена: <b>" +
                (config.getNameLimit().equals(Integer.MAX_VALUE)
                        ? "без ліміту"
                        : config.getNameLimit().toString()) + "</b>";
        CallBack.edit(text, ConfigUtils.createMarkupByButton(
                ConfigUtils.changeLimit(chat), // 1
                chat.getConfig().getNameLimit().equals(Integer.MAX_VALUE) // 2
                        ? ConfigUtils.enableLimitName(chat)
                        : ConfigUtils.disableLimitName(chat),
                ConfigUtils.backButton(chat) // 3
        ), update, chat);
    }

    public static InlineKeyboardButton byTextAndCallBack(String text, String callback) {
        return InlineKeyboardButton.builder()
                .callbackData(callback)
                .text(text)
                .build();
    }

    public static InlineKeyboardButton backButton(CallerChat chat) {
        return byTextAndCallBack("Назад",
                CONFIG_DATA + chat.getId() + "." + ConfigStateEnum.BACK);
    }

    public static InlineKeyboardMarkup createMarkupByButton(InlineKeyboardButton... button) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> allRows = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();
        for (int i = 0; i < button.length; i++) {
            if (i % 2 == 0) {
                row = new ArrayList<>();
                allRows.add(row);
                row.add(button[i]);
            } else {
                row.add(button[i]);
            }
        }

        markup.setKeyboard(allRows);
        return markup;
    }

    public static InlineKeyboardButton changeLimit(CallerChat chat) {
        return byTextAndCallBack("Змінити ліміт",
                CONFIG_DATA + chat.getId() + "." + ConfigStateEnum.CHANGE_NAME_LIMIT);
    }

    public static InlineKeyboardButton enableLimitName(CallerChat chat) {
        return InlineKeyboardButton.builder()
                .callbackData(CONFIG_DATA + chat.getId() + "." + ConfigStateEnum.ENABLE_NAME_LIMIT)
                .text("Ввімкнути ліміт")
                .build();
    }

    public static InlineKeyboardButton disableLimitName(CallerChat chat) {
        return InlineKeyboardButton.builder()
                .callbackData(CONFIG_DATA + chat.getId() + "." + ConfigStateEnum.DISABLE_NAME_LIMIT)
                .text("Вимкнути ліміт")
                .build();
    }
}
