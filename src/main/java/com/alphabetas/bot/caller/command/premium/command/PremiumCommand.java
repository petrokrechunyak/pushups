package com.alphabetas.bot.caller.command.premium.command;

import com.alphabetas.bot.caller.command.Command;
import org.telegram.telegrambots.meta.api.objects.Update;

public class PremiumCommand extends Command {
    @Override
    public void execute(Update update) {
        chat.getPremiumChat().setPremium(true);
        chatService.save(chat);
    }

    @Override
    public String[] getSpecialArgs() {
        return new String[]{"/premium"};
    }
}
