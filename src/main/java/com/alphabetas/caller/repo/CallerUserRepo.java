package com.alphabetas.caller.repo;

import com.alphabetas.caller.model.CallerChat;
import com.alphabetas.caller.model.CallerUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CallerUserRepo extends JpaRepository<CallerUser, Long> {
    CallerUser getByUserIdAndCallerChat(Long userId, CallerChat callerChat);

    @Transactional
    void removeByCallerChat(CallerChat chat);

    @Transactional
    void removeByUserIdAndCallerChat(Long userId, CallerChat chat);
}
