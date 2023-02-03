package com.alphabetas.caller.service;

import com.alphabetas.caller.model.CallerChat;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CallerChatService extends AbstractService<CallerChat> {

    CallerChat getByUpdate(Update update);
}
