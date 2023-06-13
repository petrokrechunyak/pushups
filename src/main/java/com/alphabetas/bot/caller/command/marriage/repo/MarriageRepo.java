package com.alphabetas.bot.caller.command.marriage.repo;

import com.alphabetas.bot.caller.command.marriage.model.MarriageModel;
import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarriageRepo extends JpaRepository<MarriageModel, Long> {

    MarriageModel getByUser1IdAndChat(Long user1, CallerChat chat);

    MarriageModel getByUser2IdAndChat(Long user2, CallerChat chat);


}
