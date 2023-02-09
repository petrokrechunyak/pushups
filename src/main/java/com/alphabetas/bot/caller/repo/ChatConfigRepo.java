package com.alphabetas.bot.caller.repo;

import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.ChatConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatConfigRepo extends JpaRepository<ChatConfig, CallerChat> {

    ChatConfig findByChatId(Long chatId);

}
