package com.alphabetas.bot.caller.service.impl;

import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerUser;
import com.alphabetas.bot.caller.repo.CallerUserRepo;
import com.alphabetas.bot.caller.service.CallerChatService;
import com.alphabetas.bot.caller.service.CallerUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Service
@Slf4j
public class CallerUserServiceImpl implements CallerUserService {

    @Autowired
    private CallerUserRepo userRepo;
    @Autowired
    private CallerChatService chatService;

    @Override
    public CallerUser getById(Long id, Update update) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Caller user cant be found by only id");
    }

    @Override
    public CallerUser save(CallerUser callerUser) {
        return userRepo.save(callerUser);
    }

    @Override
    public void delete(CallerUser user) {
        userRepo.delete(user);
    }

    @Override
    public CallerUser getByUpdate(Update update) {
        CallerChat chat = chatService.getByUpdate(update);
        return getByUserIdAndCallerChat(chat, update);
    }

    @Override
    public CallerUser getByUserIdAndCallerChat(CallerChat callerChat, Update update) {
        Long userId = update.getMessage() == null
                ? update.getCallbackQuery().getFrom().getId()
                : update.getMessage().getFrom().getId();
        return getByUserIdAndCallerChat(userId, callerChat, update);
    }

    @Override
    public CallerUser getByUserIdAndCallerChat(Long userId, CallerChat callerChat, Update update) {
        CallerUser user = userRepo.getByUserIdAndCallerChat(userId, callerChat);
        User from = update.getMessage() == null
                ? update.getCallbackQuery().getFrom()
                : update.getMessage().getFrom();
        if (user != null) {
            String updateUsername = from.getUserName();
            String updateFirstname = from.getFirstName();

            if (!updateFirstname.equals(user.getFirstname())) {
                user.setFirstname(from.getFirstName());
                save(user);
            }
            if (updateUsername != null && !updateUsername.equals(user.getUsername())) {
                user.setUsername(from.getUserName());
                save(user);
            }
            return user;
        }

        log.debug("Creating new user..");
        Long chatId = update.getMessage().getChatId();
        CallerChat chat = chatService.getById(chatId, update);
        String firstName = from.getFirstName();
        String userName = from.getUserName();
        user = new CallerUser(userId, firstName, userName, chat);
        save(user);
        return user;
    }

    @Override
    public void removeByCallerChat(CallerChat chat) {
        userRepo.removeByCallerChat(chat);
    }

    @Override
    public void removeByUserIdAndCallerChat(Long userId, CallerChat chat) {
        userRepo.removeByUserIdAndCallerChat(userId, chat);
    }
}
