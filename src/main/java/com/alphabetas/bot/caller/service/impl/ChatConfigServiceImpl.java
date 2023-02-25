package com.alphabetas.bot.caller.service.impl;

import com.alphabetas.bot.caller.model.ChatConfig;
import com.alphabetas.bot.caller.repo.ChatConfigRepo;
import com.alphabetas.bot.caller.service.ChatConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatConfigServiceImpl implements ChatConfigService {

    @Autowired
    private ChatConfigRepo configRepo;

    public void save(ChatConfig config) {
        configRepo.save(config);
    }

    @Override
    public ChatConfig findById(Long id) {
        return configRepo.findByChatId(id);
    }
}
