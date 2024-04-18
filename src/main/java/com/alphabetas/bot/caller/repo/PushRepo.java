package com.alphabetas.bot.caller.repo;

import com.alphabetas.bot.caller.model.Push;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PushRepo extends JpaRepository<Push, Long> {

    List<Push> getByUserId(Long userId);

    Push getByUserIdAndMonthDayAndMonth(Long userId, Integer monthDay, Integer month);

    List<Push> getAllByMonthAndUserId(Integer month, Long userId);

    void deleteAllByUserId(Long userId);
}
