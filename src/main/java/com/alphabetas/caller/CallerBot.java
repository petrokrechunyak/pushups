package com.alphabetas.caller;

import com.alphabetas.caller.command.container.CommandContainer;
import com.alphabetas.caller.model.CallerChat;
import com.alphabetas.caller.model.CallerUser;
import com.alphabetas.caller.service.CallerChatService;
import com.alphabetas.caller.service.CallerNameService;
import com.alphabetas.caller.service.CallerUserService;
import com.alphabetas.caller.service.MessageService;
import com.alphabetas.caller.service.impl.MessageServiceImpl;

import java.util.List;

import com.alphabetas.caller.utils.AddNameUtils;
import com.alphabetas.caller.utils.DeleteNameUtils;
import com.alphabetas.caller.utils.SpaceUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Component
public class CallerBot extends TelegramLongPollingBot {
    private CommandContainer container;
    private CallerUserService userService;
    private CallerChatService chatService;
    private CallerNameService nameService;
    private MessageService messageService;

    public CallerBot(CallerChatService chatService, CallerUserService userService,
                     CallerNameService nameService) {
        this.userService = userService;
        this.chatService = chatService;
        this.nameService = nameService;
        this.messageService = new MessageServiceImpl(this);
        this.container = new CommandContainer(messageService, chatService, userService,
                nameService);
        setAllUtils(userService, chatService, nameService);


    }


    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage()) {

            SpaceUtils utils = new SpaceUtils(messageService, botToken);
            String trimSpaces = utils.trimSpaces(update);

            // if message is sent by bot
            if(update.getMessage().getFrom().getIsBot()) {
                return;
            }
            // if message have have
            if(update.getMessage().hasText()) {
                String msgText = update.getMessage().getText();
                if (msgText.startsWith("/")) {
                    readCommand(update);
                } else {
                    container.retrieveText(msgText.toLowerCase()).execute(update);
                }
            }
            // if someone entered/left
            someOneEntered(update);
            someOneLeft(update);
        }

    }

    @Transactional
    public void someOneLeft(Update update) {

        User left = update.getMessage().getLeftChatMember();
        if (left != null) {
            CallerChat chat = chatService.getById(update.getMessage().getChatId(), update);
            update.getMessage().getFrom().setId(left.getId());
            CallerUser user = userService.getByUserIdAndCallerChat(left.getId(), chat, update);
            userService.delete(user);
            messageService.sendMessage(chat.getId(), "Бувай!\nНадіємося ви повернетеся.");
        }
    }

    private void someOneEntered(Update update) {
        List<User> users = update.getMessage().getNewChatMembers();
        for (User u : users) {
            if(u.getIsBot()) {
                continue;
            }
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("<b>Привіт " + u.getFirstName() + "!</b>\n" +
                    "Я Кликун - бот який буде кликати вас щоразу, коли хтось буде писати ваше ім'я.\n" +
                    "Щоб добавити ім'я напишіть /add\n" +
                    "Щоб видалити імя - /delete\n" +
                    "Подивитися імена - /show.\n\n" +
                    "<b>Приємного спілкування!</b>");
            sendMessage.setParseMode("html");
            sendMessage.setReplyToMessageId(update.getMessage().getMessageId());
            sendMessage.setChatId(update.getMessage().getChatId().toString());
            messageService.sendMessage(sendMessage);
            return;
        }
    }

    public void readCommand(Update update) {
        String text = update.getMessage().getText().toLowerCase();
        String[] parts = text.split("@");
        if (parts.length == 1 || parts[1].equals(getBotUsername())) {
            container.retrieveCommand(text).execute(update);
        }
    }

    private void setAllUtils(CallerUserService userService, CallerChatService chatService, CallerNameService nameService) {
        AddNameUtils.setServices(userService, chatService, nameService, messageService);
        DeleteNameUtils.setServices(userService, chatService, nameService, messageService);
    }

    @Value("${bot.username}")
    private String botUsername;
    @Value("${bot.token}")
    private String botToken;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }




}
