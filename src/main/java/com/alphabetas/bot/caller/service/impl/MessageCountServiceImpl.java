package com.alphabetas.bot.caller.service.impl;

import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerUser;
import com.alphabetas.bot.caller.model.MessageCount;
import com.alphabetas.bot.caller.repo.MessageCountRepo;
import com.alphabetas.bot.caller.service.CallerChatService;
import com.alphabetas.bot.caller.service.CallerUserService;
import com.alphabetas.bot.caller.service.MessageCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Service
public class MessageCountServiceImpl implements MessageCountService {

    @Autowired
    private MessageCountRepo messageCountRepo;

    @Autowired
    private CallerChatService chatService;

    @Autowired
    private CallerUserService userService;

    @Override
    public List<MessageCount> getAllByCallerUser(CallerUser user) {
        return messageCountRepo.getAllByCallerUser(user);
    }

    @Override
    public MessageCount getByUserIdAndStartTime(Long userId, Long chatId, Long startTime, Update update) {
        CallerChat chat = chatService.getById(chatId, update);
        CallerUser user = userService.getByUserIdAndCallerChat(userId, chat, update);

        return messageCountRepo.getByCallerUserAndStartTime(user, startTime);
    }

    @Override
    public List<MessageCount> getAllByChat(CallerChat chat) {
        return messageCountRepo.getAllByChat(chat);
    }

    @Override
    public List<MessageCount> getAll() {
        return messageCountRepo.findAll();
    }

    @Override
    public void deleteAll(List<MessageCount> list) {
        messageCountRepo.deleteAll(list);
    }

    @Override
    public MessageCount getById(Long id, Update update) {
        throw new UnsupportedOperationException();
    }

    @Override
    public MessageCount save(MessageCount messageCount) {
        return messageCountRepo.save(messageCount);
    }

    @Override
    public void delete(MessageCount messageCount) {
        messageCountRepo.delete(messageCount);
    }
}
