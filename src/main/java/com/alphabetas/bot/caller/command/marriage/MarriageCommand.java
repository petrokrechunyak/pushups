package com.alphabetas.bot.caller.command.marriage;

import com.alphabetas.bot.caller.command.Command;
import com.alphabetas.bot.caller.command.marriage.model.MarriageModel;
import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerUser;
import com.alphabetas.bot.caller.utils.ConfigUtils;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.regex.qual.Regex;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@NoArgsConstructor
@Slf4j
public class MarriageCommand extends Command {

    private static final String NO_REPLY_MESSAGE = "Для вступу у шлюб напишіть повідомлення у відповідь на повідомлення іншої людини";

    private static final String USER1_ALREADY_MARRIED = "Ви вжи знаходитесь у шлюбі з %s. Два шлюба одночасно - це неприйнятно!";
    private static final String USER2_ALREADY_MARRIED = "Користувач %s вже знаходиться у шлюбі. Пошукайте собі когось іншого.";
    private static final String MARRIAGE_INVITE = "Користувач %s зробив пропозицію руки та серця %s";

    @Override
    public void execute(Update update) {
        log.info("Entered in MarriageCommand with command {}", update.getMessage().getText());

        if(repliedMessage != null) {

            if(repliedMessage.getFrom().getId().equals(user.getUserId())) {
                messageService.sendMessage(chat.getId(), "Вступати в шлюб з собою не можна!", update.getMessage().getMessageId().longValue());
                return;
            }

            if(repliedMessage.getFrom().getIsBot()) {
                if(repliedMessage.getFrom().getUserName().equals("caller_ua_bot")) {
                    messageService.sendMessage(chat.getId(), "Мені не потрібна пара!", update.getMessage().getMessageId().longValue());
                } else {
                    messageService.sendMessage(chat.getId(), "З ботами вступати в шлюб не можна!", update.getMessage().getMessageId().longValue());
                }
                return;
            }

            if(checkIfMarried(user.getUserId(), true) || checkIfMarried(repliedUser.getUserId(), false)) {
                return;
            }
            sendMarriageInviteMessage();

        } else {
            messageService.sendMessage(chat.getId(), NO_REPLY_MESSAGE, threadId);
        }
    }

    private void sendMarriageInviteMessage() {
        SendMessage message = new SendMessage();
        message.setChatId(chat.getId());
        message.setText(String.format(MARRIAGE_INVITE, user.getMentionedUser(), repliedUser.getMentionedUser()));



        InlineKeyboardMarkup markup = ConfigUtils.createMarkupByButton(
                ConfigUtils.buttonByTextAndCallBack("Відхилити \uD83D\uDC94", "MARRIAGE." + repliedUser.getUserId() + ".false." + user.getUserId() + "." + update.getMessage().getMessageId()),
                ConfigUtils.buttonByTextAndCallBack("Прийняти \uD83D\uDC9E", "MARRIAGE." + repliedUser.getUserId() + ".true." + user.getUserId() + "." + update.getMessage().getMessageId())
        );
        message.setReplyMarkup(markup);
        messageService.sendMessage(message);

    }



    private boolean checkIfMarried(Long userId, boolean user1) {
        MarriageModel marriageModel = marriageService.findByUserIdAndChat(userId, chat);
        if(marriageModel != null) {
            String pattern = user1 ?
                    USER1_ALREADY_MARRIED : USER2_ALREADY_MARRIED;
            String userName = user1 ?
                    (userId.equals(marriageModel.getUser1().getUserId()) ?
                    marriageModel.getUser2().getFirstname() :
                    marriageModel.getUser1().getFirstname())
                    : (repliedUser.getFirstname());
            messageService.sendMessage(chat.getId(), String.format(pattern, userName),
                    threadId);
            return true;
        }
        return false;
    }


    @Override
    public String[] getSpecialArgs() {
        return new String[]{"шлюб"};
    }
}
