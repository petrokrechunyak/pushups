package com.alphabetas.bot.caller.service.impl;

import com.alphabetas.bot.caller.command.premium.model.PremiumChat;
import com.alphabetas.bot.caller.command.premium.service.PremiumChatService;
import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.ChatConfig;
import com.alphabetas.bot.caller.repo.CallerChatRepo;
import com.alphabetas.bot.caller.service.CallerChatService;
import com.alphabetas.bot.caller.service.ChatConfigService;
import com.alphabetas.bot.caller.utils.CommandUtils;
import com.alphabetas.bot.caller.utils.ConfigUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CallerChatServiceImpl implements CallerChatService {
    @Autowired
    private CallerChatRepo chatRepo;

    @Autowired
    private ChatConfigService chatConfigService;

    @Autowired
    private PremiumChatService premiumChatService;

    @Override
    public CallerChat getById(Long id, Update update) {
        try {
            CallerChat chat = chatRepo.findById(id).get();
            if (chat.getConfig() == null || chat.getPremiumChat() == null) {
                return saveWithMoreTables(chat);
            }
            if(update.getMessage().getChat().getTitle() != null && !chat.getTitle().equals(update.getMessage().getChat().getTitle())) {
                chat.setTitle(update.getMessage().getChat().getTitle());
                save(chat);
            }
            return chat;
        } catch (NoSuchElementException e) {
            String chatTitle = CommandUtils.deleteBadSymbols(update.getMessage().getChat().getTitle());
            CallerChat chat = new CallerChat(id, chatTitle);

            saveWithMoreTables(chat);
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

    @Override
    public CallerChat getByUpdate(Update update) {
        Long chatId = update.getMessage() == null
                ? update.getCallbackQuery().getMessage().getChatId()
                : update.getMessage().getChatId();

        return getById(chatId, update);
    }

    @Override
    public List<CallerChat> findAll() {
        return chatRepo.findAll();
    }

    private CallerChat saveWithMoreTables(CallerChat chat) {
        ChatConfig config = new ChatConfig(chat.getId(), ConfigUtils.DEFAULT_CHAT_LIMIT);
        chatConfigService.save(config);
        chat.setConfig(config);

        PremiumChat premiumChat = new PremiumChat(chat.getId());
        premiumChatService.save(premiumChat);
        chat.setPremiumChat(premiumChat);

        return save(chat);
    }
}
