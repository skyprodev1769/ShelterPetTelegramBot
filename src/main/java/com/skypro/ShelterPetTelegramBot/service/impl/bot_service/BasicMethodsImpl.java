package com.skypro.ShelterPetTelegramBot.service.impl.bot_service;

import com.skypro.ShelterPetTelegramBot.service.TelegramBot;
import com.skypro.ShelterPetTelegramBot.service.interfaces.bot_service.BasicMethods;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Класс {@link BasicMethodsImpl}
 * является сервисным классом с основными методам работы
 */
@Slf4j
@Service
public class BasicMethodsImpl implements BasicMethods {

    private final TelegramBot bot;

    public BasicMethodsImpl(TelegramBot bot) {
        this.bot = bot;
    }

    @Override
    public void reaction(Long chatId, String answer) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(answer);
        sendText(message);

    }

    @Override
    public void sendText(SendMessage message) {
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error("ОШИБКА ОТПРАВКИ: {}", e.getMessage());
        }

    }

    @Override
    public void sendImage(Long chatId, String caption, String path) {
        File image = getImage(path);
        SendPhoto photo = new SendPhoto();

        if (image != null) {
            photo.setPhoto(new InputFile(image));
        }

        photo.setChatId(chatId);
        photo.setCaption(caption);

        try {
            bot.execute(photo);
        } catch (TelegramApiException e) {
            log.error("ОШИБКА ОТПРАВКИ ИЗОБРАЖЕНИЯ: {}", e.getMessage());
        }
    }

    /**
     * Метод находит изображение по указанному пути расположения изображения: <br>
     *
     * @param path <i> является путем к расположению изображения </i>
     */
    private File getImage(String path) {
        File image = null;

        try {
            image = ResourceUtils.getFile(path);
        } catch (FileNotFoundException e) {
            log.error("ОШИБКА ПОИСКА ИЗОБРАЖЕНИЯ: {}", e.getMessage());
        }

        return image;
    }
}