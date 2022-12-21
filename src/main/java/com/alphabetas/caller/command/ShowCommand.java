package com.alphabetas.caller.command;

import com.alphabetas.caller.model.CallerChat;
import com.alphabetas.caller.model.CallerName;
import com.alphabetas.caller.model.CallerUser;
import com.alphabetas.caller.service.CallerChatService;
import com.alphabetas.caller.service.CallerNameService;
import com.alphabetas.caller.service.CallerUserService;
import com.alphabetas.caller.service.MessageService;
import com.alphabetas.caller.utils.CommandUtils;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
public class ShowCommand implements Command {

    private CallerUserService userService;
    private CallerChatService chatService;
    private CallerNameService nameService;
    private MessageService messageService;

    public final static String[] specialArgs = new String[]{"/show_names", "/show_name", "/show",
            "кликун імена", "кликун шов", "кликун покажи",
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
        CallerUser user = userService.getByUserIdAndCallerChat(chat, update);

        if(user.getNames().size() == 0) {
            messageService.sendMessage(user.getCallerChat().getId(),
                    "В тебе ще немає імен, але їх завжди можна добавити командою /add");
            return;
        }
        StringBuilder builder = new StringBuilder(String.format("<b>Імена <a href='tg://user?id=%d'>%s</a></b>\n", user.getUserId(), user.getFirstname()));
        for (CallerName name: user.getNames()) {
            builder.append(name.getName()).append("\n");
        }

        messageService.sendMessage(user.getCallerChat().getId(), CommandUtils.decryptSpace(builder.toString()));
    }
}
