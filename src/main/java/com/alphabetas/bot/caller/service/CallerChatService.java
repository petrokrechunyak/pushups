package com.alphabetas.bot.caller.service;

import com.alphabetas.bot.caller.model.CallerChat;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CallerChatService extends AbstractService<CallerChat> {

    CallerChat getByUpdate(Update update);
}
