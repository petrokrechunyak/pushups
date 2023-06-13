package com.alphabetas.bot.caller.command.marriage.service.impl;

import com.alphabetas.bot.caller.command.marriage.model.MarriageModel;
import com.alphabetas.bot.caller.command.marriage.repo.MarriageRepo;
import com.alphabetas.bot.caller.command.marriage.service.MarriageService;
import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerUser;
import com.alphabetas.bot.caller.service.CallerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Service
public class MarriageServiceImpl implements MarriageService {

    @Autowired
    private MarriageRepo repo;
    @Autowired
    private CallerUserService userService;

    @Override
    public MarriageModel findByUserIdAndChat(Long userId, CallerChat chat) {
        MarriageModel marriage;
        marriage = repo.getByUser1IdAndChat(userId, chat);
        if(marriage == null) {
            return repo.getByUser2IdAndChat(userId, chat);
        }
        return marriage;
    }

    @Override
    public List<MarriageModel> findAll() {
        return repo.findAll();
    }

    @Override
    public MarriageModel getById(Long id, Update update) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MarriageModel save(MarriageModel marriageModel) {
        return repo.save(marriageModel);
    }

    @Override
    public void delete(MarriageModel marriageModel) {
        repo.delete(marriageModel);
    }
}
