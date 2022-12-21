package com.alphabetas.caller.command;

import com.alphabetas.caller.model.CallerChat;
import com.alphabetas.caller.model.CallerUser;
import com.alphabetas.caller.service.CallerChatService;
import com.alphabetas.caller.service.CallerNameService;
import com.alphabetas.caller.service.CallerUserService;
import com.alphabetas.caller.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.mockito.Mockito.*;

public class AbstractCommandTest {

    protected MessageService messageService;
    protected static final Long CHAT_ID = 12345L;

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
        Update update = new Update();
        Message message = mock(Message.class);
        when(message.getChatId()).thenReturn(CHAT_ID);
        update.setMessage(message);

        return update;
    }

    protected CallerUser prepareUser() {
        return new CallerUser(123L, "firstname",
                "username", prepareChat());
    }

    protected CallerChat prepareChat() {
        return new CallerChat(12345L, "chatTitle");
    }

}
