package com.alphabetas.bot.caller.service;

import com.alphabetas.bot.caller.model.ChatConfig;

public interface ChatConfigService {

    void save(ChatConfig config);

    ChatConfig findById(Long id);
}
