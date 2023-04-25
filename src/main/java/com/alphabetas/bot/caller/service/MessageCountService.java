package com.alphabetas.bot.caller.service;

import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerUser;
import com.alphabetas.bot.caller.model.MessageCount;

import java.util.List;

public interface MessageCountService extends AbstractService<MessageCount> {

    List<MessageCount> getAllByCallerUser(CallerUser user);

    MessageCount getByUserIdAndStartTime(Long userId, Long startTime);

    List<MessageCount> getAllByChat(CallerChat chat);

    List<MessageCount> getAll();

    void deleteAll(List<MessageCount> list);

}
