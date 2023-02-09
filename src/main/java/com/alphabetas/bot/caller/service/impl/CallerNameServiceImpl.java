package com.alphabetas.bot.caller.service.impl;

import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerName;
import com.alphabetas.bot.caller.model.CallerUser;
import com.alphabetas.bot.caller.repo.CallerNameRepo;
import com.alphabetas.bot.caller.service.CallerNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Set;

@Service
public class CallerNameServiceImpl implements CallerNameService {
    @Autowired
    private CallerNameRepo nameRepo;

    @Override
    public CallerName getById(Long id, Update update) {
        return nameRepo.findById(id).get();
    }

    @Override
    public CallerName save(CallerName callerName) {
        return nameRepo.saveAndFlush(callerName);
    }

    @Override
    public void delete(CallerName name) {
        nameRepo.delete(name);
    }

    @Override
    public CallerName getByCallerChatAndName(CallerChat chat, String name) {
        return nameRepo.getByChatAndNameIgnoreCase(chat, name);
    }

    @Override
    public Set<CallerName> getAllByCallerChat(CallerChat chat) {
        return nameRepo.getAllByChat(chat);
    }

    @Override
    public void deleteAllByCallerUser(CallerUser user) {
        nameRepo.deleteAllByCallerUser(user);
    }

    @Override
    public Set<CallerName> getAllByCallerUser(CallerUser user) {
        return nameRepo.getAllByCallerUser(user);
    }
}
