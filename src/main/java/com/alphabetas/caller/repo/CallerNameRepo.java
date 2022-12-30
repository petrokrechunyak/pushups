package com.alphabetas.caller.repo;

import com.alphabetas.caller.model.CallerChat;
import com.alphabetas.caller.model.CallerName;
import com.alphabetas.caller.model.CallerUser;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CallerNameRepo extends JpaRepository<CallerName, Long> {
    CallerName getByChatAndNameIgnoreCase(CallerChat chat, String name);

    Set<CallerName> getAllByChat(CallerChat chat);

    boolean deleteAllByCallerUser(CallerUser callerUser);

    Set<CallerName> getAllByCallerUser(CallerUser user);

}
