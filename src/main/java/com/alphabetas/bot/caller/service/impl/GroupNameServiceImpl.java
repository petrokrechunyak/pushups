package com.alphabetas.bot.caller.service.impl;

import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.GroupName;
import com.alphabetas.bot.caller.repo.GroupNameRepo;
import com.alphabetas.bot.caller.service.GroupNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class GroupNameServiceImpl implements GroupNameService {

    @Autowired
    private GroupNameRepo groupNameRepo;

    @Override
    public GroupName getById(Long id, Update update) {
        return groupNameRepo.findById(id).get();
    }

    @Override
    public GroupName save(GroupName groupName) {
        return groupNameRepo.save(groupName);
    }

    @Override
    public void delete(GroupName groupName) {
        groupNameRepo.delete(groupName);
    }

    @Override
    public GroupName getByNameAndChat(String name, CallerChat chat) {
        return groupNameRepo.findByNameIgnoreCaseAndChat(name, chat);
    }
}
