package com.alphabetas.caller.command;

import com.alphabetas.caller.model.CallerChat;
import com.alphabetas.caller.model.CallerUser;
import com.alphabetas.caller.service.CallerChatService;
import com.alphabetas.caller.service.CallerNameService;
import com.alphabetas.caller.service.CallerUserService;
import com.alphabetas.caller.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AddNameCommandTest extends AbstractCommandTest {

    private AddNameCommand addCommand;


    @BeforeEach
    public void prepare() {
        super.prepare();
        addCommand = new AddNameCommand(userService, chatService, nameService, messageService);
    }

    void sendMessageToAddName() {
        // given
        Update update = prepareUpdate();
        when(update.getMessage().getText()).thenReturn("кликун додай ім'я");

        // when
        addCommand.execute(update);

        //then
        verify(addCommand).sendMessageToAddName(USER_ID, eq(any()));

    }

    @Test
    void getNamesFromMessage() {
    }

    @Test
    void saveNames() {
    }

    @Test
    void shouldReturnAddNameCommand() {

    }
}