package com.alphabetas.bot.caller.command;

import com.alphabetas.bot.caller.service.MessageService;
import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerUser;
import com.alphabetas.bot.caller.service.CallerChatService;
import com.alphabetas.bot.caller.service.CallerNameService;
import com.alphabetas.bot.caller.service.CallerUserService;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import static org.mockito.Mockito.*;

public class AbstractCommandTest {

    protected MessageService messageService;
    protected static final Long CHAT_ID = 12345L;
    protected static final Long USER_ID = 123456L;

    protected CallerChatService chatService;
    protected CallerUserService userService;
    protected CallerNameService nameService;

    public void prepare() {
        messageService = mock(MessageService.class);
        chatService = mock(CallerChatService.class);
        userService = mock(CallerUserService.class);
        nameService = mock(CallerNameService.class);

        when(chatService.getById(any(), any())).thenReturn(prepareChat());
        when(userService.getByUserIdAndCallerChat(any(), any())).thenReturn(prepareUser());

    }

    protected Update prepareUpdate() {
        Chat chat = new Chat();
        chat.setId(CHAT_ID);

        User user = new User(USER_ID, "Some firstname", false);

        Message message = mock(Message.class);
        when(message.getChatId()).thenReturn(CHAT_ID);
        when(message.getChat()).thenReturn(chat);
        when(message.getFrom()).thenReturn(user);

        Update update = new Update();
        update.setMessage(message);


        return update;
    }

    protected CallerUser prepareUser() {
return new CallerUser();
    }

    protected CallerChat prepareChat() {
        return new CallerChat(12345L, "chatTitle");
    }

}
