package com.alphabetas.bot.caller.command;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.*;

@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class StartCommand extends Command {

    String s = "Привіт, я бот Кликун, створений для поклику людей.\n" +
            "\n" +
            "Ось довідник по тому, як правильно користуватися Кликуном: %s";

    @Override
    public void execute(Update update) {
        if(!update.getMessage().getChat().getType().equals("private")) {
            return;
        }
        File file = new File("link.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String url = reader.readLine();
            messageService.sendMessage(chat.getId(), String.format(s, url), threadId);
        } catch (FileNotFoundException e) {
            messageService.sendErrorMessage(e, update);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String[] getSpecialArgs() {
        return new String[]{"/start"};
    }
}
