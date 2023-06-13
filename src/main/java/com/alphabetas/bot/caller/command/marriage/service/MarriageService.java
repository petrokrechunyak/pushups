package com.alphabetas.bot.caller.command.marriage.service;

import com.alphabetas.bot.caller.command.marriage.model.MarriageModel;
import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.service.AbstractService;

import java.util.List;

public interface MarriageService extends AbstractService<MarriageModel> {

    MarriageModel findByUserIdAndChat(Long userId, CallerChat chat);

    List<MarriageModel> findAll();
}
