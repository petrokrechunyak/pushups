package com.alphabetas.caller.command;

import com.alphabetas.caller.CallerBot;
import com.alphabetas.caller.model.CallerChat;
import com.alphabetas.caller.model.CallerName;
import com.alphabetas.caller.model.CallerUser;
import com.alphabetas.caller.service.CallerChatService;
import com.alphabetas.caller.service.CallerNameService;
import com.alphabetas.caller.service.CallerUserService;
import com.alphabetas.caller.service.MessageService;
import com.alphabetas.caller.utils.CommandUtils;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
public class ShowCommand implements Command {

    private CallerUserService userService;
    private CallerChatService chatService;
    private CallerNameService nameService;
    private MessageService messageService;


    public final static String[] specialArgs = new String[]{"/show_names", "/show_name", "/show",
            "імена", "шов", "покажи",
            ".імена", ".шов"};

    public ShowCommand(CallerUserService userService, CallerChatService chatService, CallerNameService nameService, MessageService messageService) {
        this.userService = userService;
        this.chatService = chatService;
        this.nameService = nameService;
        this.messageService = messageService;
    }

    @Override
    public void execute(Update update) {
        String msgText = update.getMessage().getText();
        log.info("Entered in ShowCommand with text {}", msgText);
        CallerChat chat = chatService.getById(update.getMessage().getChatId(), update);
        CallerUser user;

        Long chatId = update.getMessage().getChatId();
        Long userId = update.getMessage().getFrom().getId();

        // delete message if starts with /
        if(msgText.startsWith("/")) {
            messageService.deleteMessage(chatId.toString(), update.getMessage().getMessageId());
        }

        if(update.getMessage().getReplyToMessage() != null) {
            Message message = update.getMessage().getReplyToMessage();

            if(message.getFrom().getIsBot()) {

                if(message.getFrom().getUserName().equals("caller_ua_bot")) {
                    messageService.sendMessage(chat.getId(),
                            "У мене тільки одне ім'я - Кликун ;)");
                    return;
                }
                messageService.sendMessage(chat.getId(),
                        "У ботів не може бути імен :/");
                return;
            }
            CommandUtils.setUserToUpdate(update);
        }

        user = userService.getByUserIdAndCallerChat(chat, update);

        if(user.getNames().size() == 0) {
            messageService.sendMessage(user.getCallerChat().getId(),
                    "У користувача " + CommandUtils.makeLink(
                            user.getUserId(),
                            user.getFirstname()
                    ) + " ще немає імен, але їх завжди можна додати командою /add");
            return;
        }
        StringBuilder builder = new StringBuilder("<b>Імена ")
                .append(CommandUtils.makeLink(user.getUserId(), user.getFirstname()))
                .append("</b>\n");
        for (CallerName name: user.getNames()) {
            builder.append(name.getName()).append("\n");
        }

        messageService.sendMessage(user.getCallerChat().getId(), CommandUtils.decryptSpace(builder.toString()));
    }
}
