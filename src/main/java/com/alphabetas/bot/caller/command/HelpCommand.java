package com.alphabetas.bot.caller.command;

import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.*;

@Slf4j
public class HelpCommand extends Command{
    String s = "Ось довідник по тому, як правильно користуватися Кликуном: %s";

    @Override
    public void execute(Update update) {
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
        return new String[]{"/help"};
    }
}
