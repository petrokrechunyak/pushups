package com.alphabetas.bot.caller.service.impl;

import com.alphabetas.bot.caller.CallerBot;
import com.alphabetas.bot.caller.command.marriage.model.MarriageModel;
import com.alphabetas.bot.caller.command.marriage.service.MarriageService;
import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerUser;
import com.alphabetas.bot.caller.model.GroupName;
import com.alphabetas.bot.caller.repo.CallerUserRepo;
import com.alphabetas.bot.caller.service.CallerChatService;
import com.alphabetas.bot.caller.service.CallerUserService;
import com.alphabetas.bot.caller.service.GroupNameService;
import com.alphabetas.bot.caller.service.MessageService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.ChatMemberUpdated;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;

@Service
@Slf4j
@Setter
public class CallerUserServiceImpl implements CallerUserService {

    @Autowired
    private CallerUserRepo userRepo;
    @Autowired
    private CallerChatService chatService;
    @Autowired
    private GroupNameService groupNameService;
    private MessageService messageService;

    @Autowired
    private MarriageService marriageService;


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
        MarriageModel marriageModel = marriageService.findByUserIdAndChat(user.getUserId(), user.getCallerChat());
        if(marriageModel != null) {
            marriageService.delete(marriageModel);
        }

        user.getGroupNames().forEach(groupName -> groupName.getUsers().remove(user));
        for(GroupName g: user.getGroupNames()) {
            groupNameService.save(g);
        }
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
        return getByUserIdAndCallerChat(userId, callerChat);
    }

    @Override
    public CallerUser getByUserIdAndCallerChat(Long userId, CallerChat callerChat) {

        CallerUser user = userRepo.getByUserIdAndCallerChat(userId, callerChat);
        User fromUser = messageService.getChatMember(callerChat.getId(), userId).getUser();
        if (user == null) {
            user = new CallerUser(userId, fromUser.getFirstName(), fromUser.getUserName(), callerChat);
            log.info("Creating new user..");
            save(user);
        } else {
            if(!user.getFirstname().equals(fromUser.getFirstName())) {
                user.setFirstname(fromUser.getFirstName());
                save(user);
            }
            if(fromUser.getUserName()!= null && !fromUser.getUserName().equals(user.getUsername())) {
                user.setUsername(fromUser.getUserName());
                save(user);
            }
        }

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

    @Override
    public CallerUserService setBot(CallerBot bot) {
        this.messageService = new MessageServiceImpl(bot);
        return null;
    }
}
