package com.alphabetas.bot.caller.command.premium.service.impl;

import com.alphabetas.bot.caller.command.premium.model.PremiumChat;
import com.alphabetas.bot.caller.command.premium.repo.PremiumChatRepo;
import com.alphabetas.bot.caller.command.premium.service.PremiumChatService;
import com.alphabetas.bot.caller.model.CallerChat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class PremiumChatServiceImpl implements PremiumChatService {

    @Autowired
    private PremiumChatRepo repo;

    @Override
    public PremiumChat getByChat(CallerChat chat) {
        return repo.getByChat(chat);
    }

    @Override
    public PremiumChat getById(Long id, Update update) {
        return repo.getById(id);
    }

    @Override
    public PremiumChat save(PremiumChat premiumChat) {
        return repo.save(premiumChat);
    }

    @Override
    public void delete(PremiumChat premiumChat) {
        throw new UnsupportedOperationException("Premium chat cant be deleted");
    }
}
