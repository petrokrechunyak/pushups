package com.alphabetas.bot.caller;

import com.alphabetas.bot.caller.command.CallBack;
import com.alphabetas.bot.caller.command.Command;
import com.alphabetas.bot.caller.command.container.CommandContainer;
import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerUser;
import com.alphabetas.bot.caller.model.MessageCount;
import com.alphabetas.bot.caller.model.Service;
import com.alphabetas.bot.caller.service.*;
import com.alphabetas.bot.caller.service.impl.MessageServiceImpl;
import com.alphabetas.bot.caller.utils.AbstractNameUtils;
import com.alphabetas.bot.caller.utils.AddNameUtils;
import com.alphabetas.bot.caller.utils.CommandUtils;
import com.alphabetas.bot.caller.utils.SpaceUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

@Component
@Slf4j
public class CallerBot extends TelegramLongPollingBot {
    private final CommandContainer container;
    private final CallerUserService userService;
    private final CallerChatService chatService;
    private final CallerNameService nameService;
    private final MessageService messageService;
    private final MessageCountService messageCountService;
    @Value("${bot.caller.username}")
    private String botUsername;
    @Value("${bot.caller.token}")
    private String botToken;

    public CallerBot(CallerChatService chatService, CallerUserService userService,
                     CallerNameService nameService, ChatConfigService configService,
                     GroupNameService groupNameService, MessageCountService messageCountService) {

        this.userService = userService;
        this.chatService = chatService;
        this.nameService = nameService;
        this.messageCountService = messageCountService;
        this.messageService = new MessageServiceImpl(this);
        this.container = new CommandContainer(messageService, chatService, userService,
                nameService);

        Service.setService(messageService, chatService, userService, nameService, configService,
                groupNameService, messageCountService);

    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            // count messages for every user
            addMessageCount(update);
            SpaceUtils utils = new SpaceUtils(messageService, botToken);
            String trimSpaces = utils.trimSpaces(update);
            try {
                // if message is sent by bot
                if (update.getMessage().getFrom().getIsBot() ||
                        update.getMessage().getForwardDate() != null) {
                    return;
                }
                if (update.getMessage().getCaption() != null) {
                    update.getMessage().setText(update.getMessage().getCaption());
                }
                // if message have Text
                if (update.getMessage().hasText()) {
                    String msgText = update.getMessage().getText();
                    if (msgText.startsWith("/")) {
                        readCommand(update);
                    } else {
                        container.retrieveText(msgText, update).execute(update);
                    }
                }
            } catch (Exception e) {
                messageService.sendErrorMessage(e);
            }
            // if someone entered/left
            someOneEntered(update);
            someOneLeft(update);
        } else if (update.hasCallbackQuery()) {
            CallerUser user = userService.getByUpdate(update);
            CallerChat chat = chatService.getByUpdate(update);
            Integer threadId = update.getCallbackQuery().getMessage().getIsTopicMessage() != null
                    ? update.getCallbackQuery().getMessage().getMessageThreadId()
                    : null;
            CallBack callBackCommand = new CallBack(update.getCallbackQuery().getData(), chat, user, threadId);
            callBackCommand.execute(update);
        }
    }

    private void addMessageCount(Update update) {
        try {
            long i = System.currentTimeMillis();
            i = i - (i % 100000);
            Long userId = update.getMessage().getFrom().getId();
            CallerChat chat = chatService.getById(update.getMessage().getChatId(), update);

            MessageCount mc = messageCountService.getByUserIdAndStartTime(userId, chat.getId(), i, update);
            if(mc == null) {
                mc = new MessageCount(userId, chat, 0, i);
            }

            mc.setCount(mc.getCount()+1);
            messageCountService.save(mc);
        } catch (Exception e) {
            messageService.sendErrorMessage(e);
        }
    }

    @Transactional
    public void someOneLeft(Update update) {

        User left = update.getMessage().getLeftChatMember();

        if (left != null) {
            CallerChat chat = chatService.getById(update.getMessage().getChatId(), update);

            update.getMessage().getFrom().setId(left.getId());
            CallerUser user = userService.getByUserIdAndCallerChat(left.getId(), chat, update);
            log.info("Into someOneLeft before deleting user wth user {}", user.toString());
            userService.delete(user);
            user = userService.getByUserIdAndCallerChat(left.getId(), chat, update);
            log.info("Into someOneLeft AFTER deleting user {}",
                    user);
            userService.delete(user);
            try {
                messageService.sendMessage(chat.getId(), "Бувайте!\nНадіємося ви повернетеся.",
                        update.getMessage().getMessageThreadId());
            } catch (Exception e) {
                if (e.getMessage().contains("bot was kicked")) {
                    chatService.delete(chat);
                }
            }
        }
    }

    private void someOneEntered(Update update) {
        List<User> users = update.getMessage().getNewChatMembers();
        for (User u : users) {
            if (u.getIsBot()) {
                continue;
            }
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("<b>Привіт " + CommandUtils.deleteBadSymbols(u.getFirstName()) + "!</b>\n" +
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
            container.retrieveCommand(text, update).execute(update);
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }


}
