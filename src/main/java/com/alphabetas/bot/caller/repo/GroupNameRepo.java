package com.alphabetas.bot.caller.repo;

import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.GroupName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupNameRepo extends JpaRepository<GroupName, Long> {

    GroupName findByNameIgnoreCaseAndChat(String name, CallerChat chat);

}
