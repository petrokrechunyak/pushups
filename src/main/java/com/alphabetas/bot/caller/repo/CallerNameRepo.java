package com.alphabetas.bot.caller.repo;

import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerName;
import com.alphabetas.bot.caller.model.CallerUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface CallerNameRepo extends JpaRepository<CallerName, Long> {
    CallerName getByChatAndNameIgnoreCase(CallerChat chat, String name);

    Set<CallerName> getAllByChat(CallerChat chat);

    boolean deleteAllByCallerUser(CallerUser callerUser);

    Set<CallerName> getAllByCallerUser(CallerUser user);

}
