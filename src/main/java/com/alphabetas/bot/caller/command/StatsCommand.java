package com.alphabetas.bot.caller.command;

import com.alphabetas.bot.caller.model.Push;
import com.alphabetas.bot.caller.repo.PushRepo;
import com.alphabetas.bot.caller.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class StatsCommand implements Command {

    private MessageService service;

    private PushRepo pushRepo;
    public StatsCommand(MessageService service, PushRepo pushRepo) {
        this.service = service;
        this.pushRepo = pushRepo;
    }


    @Override
    public void execute(Update update) {
        Long userId = update.getMessage().getFrom().getId();
        List<Push> pushes = pushRepo.getAllByMonthAndUserId(
                Calendar.getInstance().get(Calendar.MONTH),
                userId);
        AtomicInteger result = new AtomicInteger();
        pushes.forEach(x -> result.addAndGet(x.getAllPushUps()));
        StringBuilder builder = new StringBuilder("Віджимань за цей місяць: ").append(result.get()).append("\n");

        result.set(0);
        pushes = pushRepo.getByUserId(userId);
        pushes.forEach(x -> result.addAndGet(x.getAllPushUps()));
        builder.append("Віджимань в загальному: ").append(result.get());

        service.sendMessage(userId, builder.toString());
    }


}
