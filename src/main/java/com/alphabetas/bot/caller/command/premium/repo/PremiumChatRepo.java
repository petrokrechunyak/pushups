package com.alphabetas.bot.caller.command.premium.repo;

import com.alphabetas.bot.caller.command.premium.model.PremiumChat;
import com.alphabetas.bot.caller.model.CallerChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PremiumChatRepo extends JpaRepository<PremiumChat, Long> {

    PremiumChat getByChat(CallerChat chat);
}
