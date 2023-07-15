package com.alphabetas.bot.caller.command;

import com.alphabetas.bot.caller.CallerBot;
import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerUser;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.util.List;
import java.io.File;

@NoArgsConstructor
@Slf4j
public class BackupCommand extends Command {

    @Override
    public void execute(Update update) {
        if(!user.getUserId().equals(CallerBot.MY_ID)) {
            return;
        }
        log.info("Entered into BackupCommand");
        try {
            Runtime.getRuntime().exec("sudo -iu postgres");
            Runtime.getRuntime().exec("/var/lib/postgresql/terrifficsprite/backup_to_file");
            Runtime.getRuntime().exec("cp /var/lib/postgresql/terrifficsprite/backup ./");

        } catch (IOException e) {
            e.printStackTrace();
        }

        SendDocument document = new SendDocument("731921794", new InputFile(new File("backup")));
        messageService.sendDocument(document);
    }

    @Override
    public String[] getSpecialArgs() {
        return new String[]{"/backup"};
    }

}
