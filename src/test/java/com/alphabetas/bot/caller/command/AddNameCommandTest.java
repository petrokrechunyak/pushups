package com.alphabetas.bot.caller.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.mockito.Mockito.*;

class AddNameCommandTest extends AbstractCommandTest {

    private AddNameCommand addCommand;


    @BeforeEach
    public void prepare() {
        super.prepare();
    }

    void sendMessageToAddName() {
        // given
        Update update = prepareUpdate();
        when(update.getMessage().getText()).thenReturn("кликун додай ім'я");

        // when
        addCommand.execute(update);

        //then
        verify(addCommand).sendMessageToAddName();

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