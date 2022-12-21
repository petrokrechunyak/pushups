package com.alphabetas.caller.service.impl;

import com.alphabetas.caller.service.CallerUserService;
import com.alphabetas.caller.model.CallerChat;
import com.alphabetas.caller.model.CallerUser;
import com.alphabetas.caller.repo.CallerUserRepo;
import com.alphabetas.caller.service.CallerChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
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
    public CallerUser getByUserIdAndCallerChat(CallerChat callerChat, Update update) {
        Long userId = update.getMessage().getFrom().getId();
        return getByUserIdAndCallerChat(userId, callerChat, update);
    }

    @Override
    public CallerUser getByUserIdAndCallerChat(Long userId, CallerChat callerChat, Update update) {
        CallerUser user = userRepo.getByUserIdAndCallerChat(userId, callerChat);
        if (user != null) {
            String updateUsername = update.getMessage().getFrom().getUserName();
            String updateFirstname = update.getMessage().getFrom().getFirstName();

            if (updateUsername != null) {
                if(!updateUsername.equals(user.getUsername())
                        || !updateFirstname.equals(user.getFirstname())) {
                    user.setUsername(update.getMessage().getFrom().getUserName());
                    user.setFirstname(update.getMessage().getFrom().getFirstName());
                    save(user);
                }
            }
            return user;
        }

        Long chatId = update.getMessage().getChatId();
        CallerChat chat = chatService.getById(chatId, update);
        String firstName = update.getMessage().getFrom().getFirstName();
        String userName = update.getMessage().getFrom().getUserName();
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
