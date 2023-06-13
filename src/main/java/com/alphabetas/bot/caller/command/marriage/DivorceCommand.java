package com.alphabetas.bot.caller.command.marriage;

import com.alphabetas.bot.caller.command.Command;
import com.alphabetas.bot.caller.command.marriage.model.MarriageModel;
import com.alphabetas.bot.caller.utils.ConfigUtils;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Slf4j
@NoArgsConstructor
public class DivorceCommand extends Command {

    private static final String MARRIAGE_INVITE = "Ви впевнені що хочете розлучитися? ";
    @Override
    public void execute(Update update) {
        log.info("Entered in MarriageCommand with command {}", update.getMessage().getText());

        MarriageModel marriageModel = marriageService.findByUserIdAndChat(user.getUserId(), chat);
        if(marriageModel == null) {
            messageService.sendMessage(chat.getId(), "Ви не знаходитеся в шлюбі!", update.getMessage().getMessageId().longValue());
        } else {
            sendMarriageDivorceMessage();
        }

    }

    private void sendMarriageDivorceMessage() {
        SendMessage message = new SendMessage();
        message.setChatId(chat.getId());
        message.setText(String.format(MARRIAGE_INVITE));

        InlineKeyboardMarkup markup = ConfigUtils.createMarkupByButton(
                ConfigUtils.buttonByTextAndCallBack("Ні \uD83D\uDC9E", "DIVORCE.false." + user.getUserId()),
                ConfigUtils.buttonByTextAndCallBack("Так \uD83D\uDC94", "DIVORCE.true." + user.getUserId())
        );
        message.setReplyMarkup(markup);
        message.setReplyToMessageId(update.getMessage().getMessageId());
        messageService.sendMessage(message);
    }
    @Override
    public String[] getSpecialArgs() {
        return new String[]{"розлучення", "розлучитися"};
    }
}
