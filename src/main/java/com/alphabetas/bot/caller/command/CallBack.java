package com.alphabetas.bot.caller.command;

import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerUser;
import com.alphabetas.bot.caller.model.ChatConfig;
import com.alphabetas.bot.caller.model.enums.ConfigStateEnum;
import com.alphabetas.bot.caller.utils.CommandUtils;
import com.alphabetas.bot.caller.utils.ConfigUtils;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberOwner;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class CallBack extends Command {

    public CallBack(String msgText, CallerChat chat, CallerUser user, Integer threadId) {
        super(msgText, chat, user, threadId);
    }

    public static void edit(String text, InlineKeyboardMarkup markup, Update update, CallerChat chat) {
        if (update.getCallbackQuery().getMessage().getText().equals(text))
            return;
        EditMessageText editMessage = new EditMessageText(text);
        editMessage.setChatId(chat.getId());
        editMessage.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        editMessage.setReplyMarkup(markup);
        editMessage.enableHtml(true);
        messageService.sendMessage(editMessage);
    }

    public void execute(Update update) {
        String[] params = msgText.split("\\.");
        chat.setTitle(CommandUtils.deleteBadSymbols(chat.getTitle()));
        if (params[0].equals("CONFIG")) {
            List<ChatMember> admins = messageService.getAdminsByChatId(chat.getId());
            for (ChatMember member : admins) {
                if (user.getUserId().equals(member.getUser().getId())
                        && !(member instanceof ChatMemberOwner)) {
                    return;
                }
            }

            ConfigStateEnum state = ConfigStateEnum.valueOf(params[2]);
            ChatConfig config = configService.findById(chat.getId());
            switch (state) {
                case CLOSE:
                    EditMessageText text = new EditMessageText("Налаштування завершено!");
                    text.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
                    text.setChatId(chat.getId());
                    messageService.sendMessage(text);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    messageService.deleteMessage(chat.getId().toString(),
                            update.getCallbackQuery().getMessage().getMessageId());
                    break;
                case MAIN_MENU:
                case BACK:
                    edit(String.format(ConfigUtils.MAIN_MENU_TEXT, chat.getTitle()),
                            ConfigUtils.mainMenu(chat), update);
                    break;
                case LIMIT_MENU:
                    ConfigUtils.limitMenu(config, update);
                    break;
                case CHANGE_NAME_LIMIT:
                    if (params.length > 3) {
                        int param = Integer.parseInt(params[3]);
                        if (param > 0 && config.getNameLimit().equals(Integer.MAX_VALUE)) {
                            config.setNameLimit(0);
                        } else if (config.getNameLimit().equals(Integer.MAX_VALUE)) {
                            return;
                        }
                        config.setNameLimit(config.getNameLimit() + param);
                        configService.save(config);
                    }
                    String callback = ConfigUtils.CONFIG_DATA + chat.getId() + "."
                            + ConfigStateEnum.CHANGE_NAME_LIMIT + ".";
                    edit("Зміна ліміту\nЛіміт на імена: " +
                                    (config.getNameLimit().equals(Integer.MAX_VALUE)
                                            ? 0 : config.getNameLimit()),
                            ConfigUtils.createMarkupByButton(
                                    ConfigUtils.byTextAndCallBack("-1", callback + "-1"),
                                    ConfigUtils.byTextAndCallBack("+1", callback + "+1"),
                                    ConfigUtils.byTextAndCallBack("-5", callback + "-5"),
                                    ConfigUtils.byTextAndCallBack("+5", callback + "+5"),
                                    ConfigUtils.backButton(chat)
                            ),
                            update);
                    break;
                case DISABLE_NAME_LIMIT:
                    config.setNameLimit(Integer.MAX_VALUE);
                    configService.save(config);
                    ConfigUtils.limitMenu(config, update);
                    break;
                case ENABLE_NAME_LIMIT:
                    config.setNameLimit(ConfigUtils.DEFAULT_CHAT_LIMIT);
                    configService.save(config);
                    ConfigUtils.limitMenu(config, update);
                    break;
                // --------- end name limit ---------
                // --------- start rp config ---------
                case RP_MENU:
                    if (params.length > 3) {
                        boolean param = Boolean.parseBoolean(params[3]);
                        config.setRpEnabled(param);
                        configService.save(config);
                    }
                    booleanChanger(config.isRpEnabled(), ConfigStateEnum.RP_MENU,
                            "Рольові команди: ", update);
                    break;
                case AGGRESSION_MENU:
                    if (params.length > 3) {
                        boolean param = Boolean.parseBoolean(params[3]);
                        config.setAggressionEnabled(param);
                        configService.save(config);
                    }
                    booleanChanger(config.isAggressionEnabled(), ConfigStateEnum.AGGRESSION_MENU,
                            "Агресія при відмітці когось через @: ", update);
                    break;
            }
        }
    }

    private void booleanChanger(boolean current, ConfigStateEnum state, String text, Update update) {
        InlineKeyboardButton button = ConfigUtils.byTextAndCallBack(current ? "Вимкнути ❌" : "Ввімкнути ✅",
                ConfigUtils.CONFIG_DATA + chat.getId() + "." + state + "." +
                        (current ? "false" : "true"));
        edit(text + (current ? "Увімкнено ✅" : "Вимкнено ❌"),
                ConfigUtils.createMarkupByButton(button, ConfigUtils.backButton(chat))
                , update);
    }

    private void edit(String text, InlineKeyboardMarkup markup, Update update) {
        edit(text, markup, update, chat);
    }

    @Override
    public String[] getSpecialArgs() {
        throw new UnsupportedOperationException("CallBack have not any special args");
    }
}
