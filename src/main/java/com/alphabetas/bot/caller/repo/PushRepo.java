package com.alphabetas.bot.caller.repo;

import com.alphabetas.bot.caller.model.Push;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PushRepo extends JpaRepository<Push, Long> {

    Push getByUserId(Long userId);
}
