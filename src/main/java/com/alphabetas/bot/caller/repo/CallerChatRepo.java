package com.alphabetas.bot.caller.repo;

import com.alphabetas.bot.caller.model.CallerChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CallerChatRepo extends JpaRepository<CallerChat, Long> {

}
