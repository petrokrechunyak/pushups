package com.alphabetas.caller.service;

import com.alphabetas.caller.model.CallerChat;
import com.alphabetas.caller.model.CallerName;
import com.alphabetas.caller.model.CallerUser;

import java.util.Set;

public interface CallerNameService extends AbstractService<CallerName> {

    CallerName getByCallerChatAndName(CallerChat chat, String name);

    Set<CallerName> getAllByCallerChat(CallerChat chat);

    void deleteAllByCallerUser(CallerUser user);

    Set<CallerName> getAllByCallerUser(CallerUser user);


}
