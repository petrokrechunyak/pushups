package com.alphabetas.bot.caller.command.container;

import com.alphabetas.bot.caller.command.*;
import com.alphabetas.bot.caller.service.MessageService;
import com.alphabetas.caller.command.*;
import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerUser;
import com.alphabetas.bot.caller.service.CallerChatService;
import com.alphabetas.bot.caller.service.CallerNameService;
import com.alphabetas.bot.caller.service.CallerUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Command Container Test")
class CommandContainerTest {

    private CommandContainer commandContainer;
    private CallerChatService chatService;
    private CallerUserService userService;
    private CallerNameService nameService;
    private Update update;


    @BeforeEach
    public void init() {
        MessageService sendBotMessageService = Mockito.mock(MessageService.class);
        chatService = Mockito.mock(CallerChatService.class);
        userService = Mockito.mock(CallerUserService.class);
        nameService = Mockito.mock(CallerNameService.class);

        commandContainer = new CommandContainer(sendBotMessageService, chatService, userService,
                nameService);

        update = mock(Update.class);
        when(chatService.getByUpdate(update)).thenReturn(new CallerChat());
        when(userService.getByUpdate(update)).thenReturn(new CallerUser());
        when(update.getMessage()).thenReturn(mock(Message.class));
        when(update.getMessage().getText()).thenReturn("text");
    }

    @Test
    public void shouldReturnUnknownCommand() {
        //given
        String[] args = {"/add_naMEe", "/do_something"};

        for(String unknownCommand: args) {
            //when
            Command command = commandContainer.retrieveCommand(unknownCommand, update);
            //then
            assertEquals(UnknownCommand.class, command.getClass());
        }
    }

    @Test
    public void shouldReturnAddNameCommand() {

        String[] args = new String[]{"/add_naMe", "/add",
                "/add_name@caller_ua_bot", "/add@caller_ua_bot"};
        for(String addCommand: args) {
            //when
            Command command = commandContainer.retrieveCommand(addCommand, update);
            //then
            assertEquals(AddNameCommand.class, command.getClass());
        }
    }

    @Test
    public void shouldReturnAddNameCommandText() {
        String[] args = {"Кликун додай", "кликун додати", ".додай",
                ".додати", "кликун додай ім'я", "кликун додай імена", ".додати ім'я",
                ".додати імена", "!ДОДАТИ ІМЕНА", "!ДОДАЙ ІМЕНА", "!ДОДАЙ", "!ДОдати",
                "!Додай ім'я", "!Додати ім'я",
                // with argument
                "Кликун додай щось", "кликун додати щось", ".додай щось",
                ".додати щось", "кликун додай ім'я щось", "кликун додай імена щось", ".додати ім'я щось",
                ".додати імена щось", "!ДОДАТИ ІМЕНА щось", "!ДОДАЙ ІМЕНА щось", "!ДОДАЙ щось", "!ДОдати щось",
                "!Додай ім'я щось", "!Додати ім'я щось",
                // with two arguments
                "Кликун додай щось, ще", "кликун додати щось, ще", ".додай щось, ще",
                ".додати щось, ще", "кликун додай ім'я щось, ще", "кликун додай імена щось, ще",
                ".додати ім'я щось, ще", ".додати імена щось, ще", "!ДОДАТИ ІМЕНА щось, ще",
                "!ДОДАЙ ІМЕНА щось, ще", "!ДОДАЙ щось, ще", "!ДОдати щось, ще",
                "!Додай ім'я щось, ще", "!Додати ім'я щось, ще"
        };
        for(String addCommand: args) {
            //when
            Command command = commandContainer.retrieveText(addCommand, update);
            System.out.println(addCommand);
            //then
            assertEquals(AddNameCommand.class, command.getClass());
        }
    }

    @Test
    public void shouldReturnDeleteNameCommandText() {
        String[] args = {"Кликун видали", "кликун видалити", ".видали",
                ".видалити", "кликун видали ім'я", "кликун видали імена", ".видалити ім'я",
                ".видалити імена", "!видалити ІМЕНА", "!видали ІМЕНА", "!видали", "!видалити",
                "!видали ім'я", "!видалити ім'я",
                // with argument
                "Кликун видали щось", "кликун видалити щось", ".видали щось",
                ".видалити щось", "кликун видали ім'я щось", "кликун видали імена щось", ".видалити ім'я щось",
                ".видалити імена щось", "!видалити ІМЕНА щось", "!видали ІМЕНА щось", "!видали щось", "!видалити щось",
                "!видали ім'я щось", "!видалити ім'я щось",
                // with two arguments
                "Кликун видали щось, ще", "кликун видалити щось, ще", ".видали щось, ще",
                ".видалити щось, ще", "кликун видали ім'я щось, ще", "кликун видали імена щось, ще",
                ".видалити ім'я щось, ще", ".видалити імена щось, ще", "!видалити ІМЕНА щось, ще",
                "!видали ІМЕНА щось, ще", "!видали щось, ще", "!видалити щось, ще",
                "!видали ім'я щось, ще", "!видалити ім'я щось, ще"
        };
        for(String deleteCommand: args) {
            //when
            Command command = commandContainer.retrieveText(deleteCommand, update);
            System.out.println(deleteCommand);
            //then
            assertEquals(DeleteCommand.class, command.getClass());
        }
    }


    @Test
    public void shouldSayIAmHere() {
        //given
        String iAmHere = "КлИкУн";
        //when
        Command command = commandContainer.retrieveText(iAmHere, update);
        //then
        assertEquals(IAmHereCommand.class, command.getClass());
    }

    @Test
    public void shouldReturnNoCommand() {
        //given
        String iAmHere = "КлИкУн привіт, як справи?";
        //when
        Command command = commandContainer.retrieveText(iAmHere, update);
        //then
        assertEquals(NoCommand.class, command.getClass());
    }

}