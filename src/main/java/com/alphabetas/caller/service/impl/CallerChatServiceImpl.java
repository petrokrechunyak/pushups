package com.alphabetas.caller.service.impl;

import com.alphabetas.caller.model.CallerChat;
import com.alphabetas.caller.service.CallerChatService;
import com.alphabetas.caller.repo.CallerChatRepo;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class CallerChatServiceImpl implements CallerChatService {
    @Autowired
    private CallerChatRepo chatRepo;

    @Override
    public CallerChat getById(Long id, Update update) {
        try {
            return chatRepo.findById(id).get();
        } catch (NoSuchElementException e) {
            String chatTitle = update.getMessage().getChat().getTitle();
            CallerChat chat = new CallerChat(id, chatTitle);
            save(chat);
            return chat;
        }
    }

    @Override
    public CallerChat save(CallerChat chat) {
        return chatRepo.save(chat);
    }

    @Override
    public void delete(CallerChat chat) {
        chatRepo.delete(chat);
    }
}
