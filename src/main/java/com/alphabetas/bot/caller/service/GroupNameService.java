package com.alphabetas.bot.caller.service;

import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.GroupName;

public interface GroupNameService extends AbstractService<GroupName> {

    GroupName getByNameAndChat(String name, CallerChat chat);

}
