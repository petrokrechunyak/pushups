package com.alphabetas.bot.caller.command;

import com.alphabetas.bot.caller.model.CallerChat;
import com.alphabetas.bot.caller.model.CallerUser;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.util.List;

@NoArgsConstructor
@Slf4j
public class BackupCommand extends Command {


    public BackupCommand(String msgText, CallerChat chat, CallerUser user, Integer threadId) {
        super(msgText, chat, user, threadId);
        if(!user.getUserId().equals(731921794L)) {
            return;
        }
        log.info("Entered into BackupCommand");
        try {
            Runtime.getRuntime().exec("/var/lib/postgresql/terrifficsprite/backup_to_file");
            Runtime.getRuntime().exec("cp /var/lib/postgresql/terrifficsprite/backup ./");

            SendDocument document = new SendDocument("731921794", new InputFile("backup"));
            messageService.sendDocument(document);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void execute(Update update) {

    }

    @Override
    public String[] getSpecialArgs() {
        return new String[]{"/backup"};
    }

}
