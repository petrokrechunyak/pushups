package com.alphabetas.bot.caller.utils;

import com.alphabetas.bot.caller.service.MessageService;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.*;

import java.io.*;
import java.net.URL;
import java.util.List;

public class SpaceUtils {

    private final String botToken;
    private final MessageService messageService;

    private final String testId = "731921794";

    public SpaceUtils(MessageService messageService, String botToken) {
        this.messageService = messageService;
        this.botToken = botToken;
    }

    private static void write(Update update, String text) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("target/chat" + update.getMessage().getChatId().toString() + update.getMessage().getChat().getTitle(), true))) {
            if (text == null)
                text = update.getMessage().getText();
            writer.write(update.getMessage().getFrom().getFirstName() + "\t||\t" + text);
            writer.newLine();
        } catch (IOException e) {
		e.printStackTrace();

        }
    }

    public static void saveImage(String imageUrl, String destinationFile) {
        try {
            URL url = new URL(imageUrl);
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(destinationFile);

            byte[] b = new byte[2048];
            int length;

            while ((length = is.read(b)) != -1) {
                os.write(b, 0, length);
            }

            is.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String trimSpaces(Update update) {
        try {
            if (update.getMessage().hasText()) {
                write(update, null);
            }
            if (update.getMessage().hasPhoto()) {
                List<PhotoSize> photos = update.getMessage().getPhoto();
                PhotoSize photo = photos.get(photos.size() - 1);
                GetFile getFile = new GetFile(photo.getFileId());
                File file = messageService.getFile(getFile);

                String fileUrl = "https://api.telegram.org/file/bot" + botToken + "/" + file.getFilePath();
                saveImage(fileUrl, file.getFileId());
                SendPhoto sendPhoto = new SendPhoto(testId, new InputFile(new java.io.File(file.getFileId())));
                messageService.sendPhoto(sendPhoto);

                new java.io.File(file.getFileId()).delete();
                write(update, "sentPhoto " + file.getFilePath());
            }
            if (update.getMessage().hasVideo()) {
                Video video = update.getMessage().getVideo();
                GetFile getFile = new GetFile(video.getFileId());
                File file = messageService.getFile(getFile);


                String fileUrl = "https://api.telegram.org/file/bot" + botToken + "/" + file.getFilePath();
                saveImage(fileUrl, file.getFileId());

                SendVideo sendVideo = new SendVideo(testId, new InputFile(new java.io.File(file.getFileId())));
                messageService.sendVideo(sendVideo);

                new java.io.File(file.getFileId()).delete();
                write(update, "sentVideo " + file.getFilePath());
            }
        } catch (Exception e) {
		e.printStackTrace();
        }

        return " text ".trim();
    }

}
