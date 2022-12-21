package com.alphabetas.caller.command.container;

import com.alphabetas.caller.command.*;
import com.alphabetas.caller.service.CallerChatService;
import com.alphabetas.caller.service.CallerNameService;
import com.alphabetas.caller.service.CallerUserService;
import com.alphabetas.caller.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Command Container Test")
class CommandContainerTest {

    private CommandContainer commandContainer;
    private CallerChatService chatService;
    private CallerUserService userService;
    private CallerNameService nameService;


    @BeforeEach
    public void init() {
        MessageService sendBotMessageService = Mockito.mock(MessageService.class);
        chatService = Mockito.mock(CallerChatService.class);
        userService = Mockito.mock(CallerUserService.class);
        nameService = Mockito.mock(CallerNameService.class);

        commandContainer = new CommandContainer(sendBotMessageService, chatService, userService,
                nameService);

    }


    @Test
    public void shouldReturnUnknownCommand() {
        //given
        String unknownCommand = "/add_naMEe";
        //when
        Command command = commandContainer.retrieveCommand(unknownCommand);
        //then
        assertEquals(UnknownCommand.class, command.getClass());
    }

    @Test
    public void shouldReturnAddNameCommand() {
        //given
        String addCommand = "/aDd@caller_ua_bot";
        //when
        Command command = commandContainer.retrieveCommand(addCommand);
        //then
        assertEquals(AddNameCommand.class, command.getClass());
    }

    @Test
    public void shouldReturnAddNameCommandText() {
        //given
        String addName = "КликУн доДАЙ Петро";
        //when
        Command command = commandContainer.retrieveText(addName);
        //then
        assertEquals(AddNameCommand.class, command.getClass());
    }

    @Test
    public void shouldSayIAmHere() {
        //given
        String iAmHere = "КлИкУн";
        //when
        Command command = commandContainer.retrieveText(iAmHere);
        //then
        assertEquals(IAmHereCommand.class, command.getClass());
    }

    @Test
    public void shouldReturnNoCommand() {
        //given
        String iAmHere = "КлИкУн привіт, як справи?";
        //when
        Command command = commandContainer.retrieveText(iAmHere);
        //then
        assertEquals(NoCommand.class, command.getClass());
    }



}