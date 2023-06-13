package com.alphabetas.bot.caller.command;

import com.alphabetas.bot.caller.CallerBot;
import com.alphabetas.bot.caller.command.marriage.AllMarriagesCommand;
import com.alphabetas.bot.caller.command.marriage.model.MarriageModel;
import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerUser;
import com.alphabetas.bot.caller.model.ChatConfig;
import com.alphabetas.bot.caller.model.enums.ConfigStateEnum;
import com.alphabetas.bot.caller.utils.CommandUtils;
import com.alphabetas.bot.caller.utils.ConfigUtils;
import com.alphabetas.bot.caller.utils.SpaceUtils;
import org.aspectj.weaver.ast.Call;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMemberOwner;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class CallBack extends Command {

    private static final String MARRIAGE_CREATED = "Шановні гості, дорога родино, вітаємо молодят %s та %s з шлюбом \uD83D\uDC8D";

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
        messageService.editMessage(editMessage);
    }

    public void execute(Update update) {
        String[] params = msgText.split("\\.");
        chat.setTitle(CommandUtils.deleteBadSymbols(chat.getTitle()));
        if (params[0].equals("CONFIG")) {
            List<ChatMember> admins = messageService.getAdminsByChatId(chat.getId());
            for (ChatMember member : admins) {
                if (user.getUserId().equals(member.getUser().getId())
                        && (
                        !(member instanceof ChatMemberOwner))
                        && !member.getUser().getId().equals(CallerBot.MY_ID)) {
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
                    messageService.editMessage(text);
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
                                    ConfigUtils.buttonByTextAndCallBack("-1", callback + "-1"),
                                    ConfigUtils.buttonByTextAndCallBack("+1", callback + "+1"),
                                    ConfigUtils.buttonByTextAndCallBack("-5", callback + "-5"),
                                    ConfigUtils.buttonByTextAndCallBack("+5", callback + "+5"),
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
                // --------- end rp config ---------
                // --------- start aggression config ---------
                case AGGRESSION_MENU:
                    if (params.length > 3) {
                        boolean param = Boolean.parseBoolean(params[3]);
                        config.setAggressionEnabled(param);
                        configService.save(config);
                    }
                    booleanChanger(config.isAggressionEnabled(), ConfigStateEnum.AGGRESSION_MENU,
                            "Агресія при відмітці когось через @: ", update);
                    break;
                case ALLOW_SPACES:
                    if (params.length > 3) {
                        boolean param = Boolean.parseBoolean(params[3]);
                        config.setAllowSpace(param);
                        configService.save(config);
                    }
                    booleanChanger(config.isAllowSpace(), ConfigStateEnum.ALLOW_SPACES,
                            "Дозвіл на імена з пробілами: ", update);
                    break;
            }
        }
        else if (params[0].equals("MARRIAGE")) {
            // MARRIAGE.repliedUser.answer.userId.messageId
            boolean answer = Boolean.parseBoolean(params[2]);
            Long repliedUserId = Long.parseLong(params[1]);
            CallerUser repliedUser = userService.getByUserIdAndCallerChat(repliedUserId, chat);
            CallerUser user = userService.getByUserIdAndCallerChat(Long.parseLong(params[3]), chat);

            if(!repliedUser.getUserId().equals(update.getCallbackQuery().getFrom().getId()) && !(!answer && user.getUserId().equals(update.getCallbackQuery().getFrom().getId()))) {
                AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery(update.getCallbackQuery().getId());
                answerCallbackQuery.setText("Це повідомлення було адресоване не вам!");
                answerCallbackQuery.setShowAlert(true);
                messageService.sendAnswerCallback(answerCallbackQuery);
                return;
            }
            int msgId = Integer.parseInt(params[4]);
            if (answer) {
                MarriageModel current = marriageService.findByUserIdAndChat(user.getUserId(), chat);
                if(current != null) {
                    messageService.deleteMessage(chat.getId().toString(), update.getCallbackQuery().getMessage().getMessageId());
                    messageService.sendMessage(chat.getId(), repliedUser.getMentionedUser() + " співчуваємо, ви запізнилися. Користувач " + user.getMentionedUser() + " вже знаходиться в шлюбі", threadId);
                    return;
                }
                MarriageModel marriage = createMarriage(user.getUserId(), repliedUser.getUserId());
                marriageService.save(marriage);
                SendMessage message = new SendMessage();
                message.setChatId(chat.getId());
                message.setText(String.format(MARRIAGE_CREATED, user.getMentionedUser(), repliedUser.getMentionedUser()));
                messageService.sendMessage(chat.getId(), "Вітаємо молодих " + user.getMentionedUser() +
                        " та " + repliedUser.getMentionedUser() + " зі шлюбом. \uD83D\uDC8D", (long) msgId);
                messageService.deleteMessage(chat.getId().toString(), update.getCallbackQuery().getMessage().getMessageId());

            } else {
                if(user.getUserId().equals(update.getCallbackQuery().getFrom().getId())) {
                    messageService.deleteMessage(chat.getId().toString(), update.getCallbackQuery().getMessage().getMessageId());
                } else {
                    messageService.sendMessage(chat.getId(), user.getMentionedUser() + " на жаль, вам відмовили, пощастить наступного разу, але не з цією людиною", (long) msgId);
                    messageService.deleteMessage(chat.getId().toString(), update.getCallbackQuery().getMessage().getMessageId());
                }
            }
        }
        else if (params[0].equals("DIVORCE")) {
            // DIVORCE.answer.userId
            boolean answer = Boolean.parseBoolean(params[1]);
            CallerUser user = userService.getByUserIdAndCallerChat(Long.parseLong(params[2]), chat);
            if(!user.getUserId().equals(update.getCallbackQuery().getFrom().getId())) {
                AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery(update.getCallbackQuery().getId());
                answerCallbackQuery.setText("Це повідомлення було адресоване не вам!");
                answerCallbackQuery.setShowAlert(true);
                messageService.sendAnswerCallback(answerCallbackQuery);
                return;
            }
            MarriageModel marriageModel = marriageService.findByUserIdAndChat(user.getUserId(), chat);
            if(answer) {
                CallerUser user2 = marriageModel.getUser1().equals(user) ? marriageModel.getUser2() : marriageModel.getUser1();
                marriageService.delete(marriageModel);
                messageService.sendMessage(chat.getId(), user2.getMentionedUser() + " співчуваємо, ваш шлюб проіснував "
                        + AllMarriagesCommand.getMarriageDuration(marriageModel.getStartDate()) + ".\uD83D\uDC94 Не сумуйте, життя довге, а людей багато.", threadId);
            }
            messageService.deleteMessage(chat.getId().toString(), update.getCallbackQuery().getMessage().getMessageId());
        }

    }

    private MarriageModel createMarriage(Long user, Long user2Id) {
        MarriageModel marriageModel = new MarriageModel(user, user2Id, chat);
        marriageModel.setStartDate(System.currentTimeMillis());
        userService.getByUserIdAndCallerChat(user2Id, chat);
        return marriageModel;
    }

    private void booleanChanger(boolean current, ConfigStateEnum state, String text, Update update) {
        InlineKeyboardButton button = ConfigUtils.buttonByTextAndCallBack(current ? "Вимкнути ❌" : "Ввімкнути ✅",
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
