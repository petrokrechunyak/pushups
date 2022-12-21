package com.alphabetas.caller.command;

import com.alphabetas.caller.comparator.StringLengthComparator;
import com.alphabetas.caller.model.CallerChat;
import com.alphabetas.caller.model.CallerName;
import com.alphabetas.caller.model.CallerUser;
import com.alphabetas.caller.model.enums.UserStates;
import com.alphabetas.caller.service.CallerChatService;
import com.alphabetas.caller.service.CallerNameService;
import com.alphabetas.caller.service.CallerUserService;
import com.alphabetas.caller.service.MessageService;
import com.alphabetas.caller.utils.AddNameUtils;
import com.alphabetas.caller.utils.CommandUtils;
import com.alphabetas.caller.utils.DeleteNameUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
public class NoCommand implements Command {
    private CallerUserService userService;
    private CallerChatService chatService;
    private CallerNameService nameService;
    private MessageService messageService;

    public final static String[] specialArgs = new String[]{"/add",
            "кликун додай ім'я", "кликун додай",
            ".додати ім'я", ".додати"};

    public NoCommand(CallerUserService userService, CallerChatService chatService, CallerNameService nameService, MessageService messageService) {
        this.userService = userService;
        this.chatService = chatService;
        this.nameService = nameService;
        this.messageService = messageService;
    }

    @Override
    public void execute(Update update) {
        String msgText = update.getMessage().getText();
        log.info("Entered into NoCommand with text {}", msgText);
        Long chatId = update.getMessage().getChatId();

        CallerChat chat = chatService.getById(chatId, update);
        CallerUser user = userService.getByUserIdAndCallerChat(chat, update);

        // do action for state of user
        if (user.getUserState() != UserStates.OFF) {
            switch (user.getUserState()) {
                case ADD:
                    addState(chat, user, update);
                    return;
                case DELETE:
                    deleteState(chat, user, update);
                    break;
            }
            return;
        }

        // call user, if his name is in the chat
        callUser(chat, update);
    }

    public void addState(CallerChat chat, CallerUser user, Update update) {
        String msgText = update.getMessage().getText();
        String returnMessage = AddNameUtils.saveNames(msgText, user, chat);

        user.setUserState(UserStates.OFF);
        userService.save(user);

        messageService.sendMessage(chat.getId(), returnMessage);
    }

    public void deleteState(CallerChat chat, CallerUser user, Update update) {
        String msgText = update.getMessage().getText();
        String returnMessage = DeleteNameUtils.deleteNames(msgText, user, chat);

        user.setUserState(UserStates.OFF);
        userService.save(user);

        messageService.sendMessage(chat.getId(), returnMessage);
    }

    public void callUser(CallerChat chat, Update update) {
        String msgText = " " + update.getMessage().getText() + " ";
        boolean send = false;
        chat.setCallerNames(chat.getCallerNames()
                .stream()
                .sorted(new StringLengthComparator())
                .collect(Collectors.toCollection(LinkedHashSet::new)));

        for (CallerName name : chat.getCallerNames()) {
//            if (name.getCallerUser().getUserId().equals(user.getUserId())) {
//                continue;
//            }
            if (update.getMessage().getReplyToMessage() != null) {
                if (name.getCallerUser().getUserId().equals(update.getMessage().getReplyToMessage().getFrom().getId())) {
                    continue;
                }
            }
            String decrypted = CommandUtils.decryptSpace(name.getName());
            if (contains(msgText, decrypted)) {
                msgText = replace(msgText, decrypted, name);
                send = true;
            }
        }

        if (send) {
            msgText = CommandUtils.decryptSpace(msgText);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(update.getMessage().getChatId().toString());
            sendMessage.setText(msgText);
            sendMessage.setParseMode("html");
            sendMessage.setReplyToMessageId(update.getMessage().getMessageId());
            messageService.sendMessage(sendMessage);
        }
    }

    private static boolean contains(String text, String toSearch) {
        text = text.toLowerCase();
        toSearch = toSearch.toLowerCase();
        String exceptWords = "[^" + CommandUtils.TEMPLATE_REGEX + "]";
        String pattern = exceptWords + toSearch + exceptWords;
        return Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(text).find();
    }

    private static String replace(String text, String toSearch, CallerName name) {
        toSearch = toSearch.toLowerCase();
        String exceptWords = "[^" + CommandUtils.TEMPLATE_REGEX + "]";
        Pattern pattern = Pattern.compile("(" + exceptWords + toSearch + exceptWords + ")", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text.toLowerCase());
        while (matcher.find()) {
            String old = matcher.group(1).toLowerCase();
            text = StringUtils.replaceIgnoreCase(text, old,
                    old.charAt(0) +
                            CommandUtils.makeLink(
                            name.getCallerUser().getUserId(),
                            name.getName()) +
                            old.charAt(old.length()-1
                    ));
        }
        return text;
    }
}
