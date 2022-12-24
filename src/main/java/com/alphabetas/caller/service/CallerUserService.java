package com.alphabetas.caller.service;

import com.alphabetas.caller.model.CallerChat;
import com.alphabetas.caller.model.CallerUser;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CallerUserService extends AbstractService<CallerUser> {

    CallerUser getByUserIdAndCallerChat(CallerChat callerChat, Update update);

    CallerUser getByUserIdAndCallerChat(Long userId, CallerChat callerChat, Update update);

    void removeByCallerChat(CallerChat chat);

    void removeByUserIdAndCallerChat(Long userId, CallerChat chat);

}
