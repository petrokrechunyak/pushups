package com.alphabetas.bot.caller.command.premium.service;

import com.alphabetas.bot.caller.command.premium.model.PremiumChat;
import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.service.AbstractService;

public interface PremiumChatService extends AbstractService<PremiumChat> {
    PremiumChat getByChat(CallerChat chat);
}
