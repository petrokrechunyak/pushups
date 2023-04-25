package com.alphabetas.bot.caller.repo;

import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerUser;
import com.alphabetas.bot.caller.model.MessageCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageCountRepo extends JpaRepository<MessageCount, Long> {

    List<MessageCount> getAllByCallerUser(CallerUser user);

    MessageCount getByUserIdAndStartTime(Long userId, Long startTime);

    List<MessageCount> getAllByChat(CallerChat chat);

}
