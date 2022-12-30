package com.alphabetas.caller.service.impl;

import com.alphabetas.caller.CallerBot;
import com.alphabetas.caller.service.MessageService;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.groupadministration.GetChatAdministrators;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.chatmember.ChatMember;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

public class MessageServiceImpl implements MessageService {
    private CallerBot bot;

    public MessageServiceImpl(CallerBot bot) {
        this.bot = bot;
    }

    @Override
    public Message sendMessage(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage(chatId.toString(), message);
        sendMessage.enableHtml(true);

        return sendMessage(sendMessage);
    }

    @Override
    public Message sendMessage(BotApiMethod sendMessage) {
        try {
            return (Message) bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public File getFile(GetFile getFile) {
        try {
            return bot.execute(getFile);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Message sendDocument(SendDocument sendDocument) {
        return null;
    }

    @Override
    public void deleteMessage(String chatId, Integer messageId) {
        DeleteMessage deleteMessage = new DeleteMessage(chatId, messageId);
        try {
            bot.execute(deleteMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void editMessage(Long chatId, Long msgToUpdate, String text) {
        EditMessageText editMessage = new EditMessageText(text);
        editMessage.setChatId(chatId.toString());
        editMessage.setMessageId(msgToUpdate.intValue());
        editMessage.enableHtml(true);
        try {
            bot.execute(editMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ChatMember> getAdminsByChatId(Long chatId) {
        GetChatAdministrators chatAdministrators = new GetChatAdministrators(chatId.toString());
        try {
            return bot.execute(chatAdministrators);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendPhoto(SendPhoto photo) {
        try {
            bot.execute(photo);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendVideo(SendVideo video) {
        try {
            bot.execute(video);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
