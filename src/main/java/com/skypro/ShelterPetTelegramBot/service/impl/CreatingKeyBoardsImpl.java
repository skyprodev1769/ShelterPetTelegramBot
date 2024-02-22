package com.skypro.ShelterPetTelegramBot.service.impl;

import com.skypro.ShelterPetTelegramBot.service.interfaces.CreatingKeyBoards;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static com.skypro.ShelterPetTelegramBot.utils.Commands.*;

/**
 * Класс {@link CreatingKeyBoardsImpl}
 * является сервисным классом для создания интерактивных клавиатур
 */
@Slf4j
@Component
public class CreatingKeyBoardsImpl implements CreatingKeyBoards {

    @Override
    public final SendMessage createKeyBoardForRegistration(Long chatId, String answer) {
        SendMessage message = new SendMessage();

        message.setChatId(chatId);
        message.setText(answer);

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();

        List<KeyboardRow> rows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add(REGISTRATION);
        rows.add(row);

        keyboard.setKeyboard(rows);

        message.setReplyMarkup(keyboard);

        log.info("СОЗДАНА КЛАВИАТУРА ДЛЯ РЕГИСТРАЦИИ");
        return message;
    }

    @Override
    public final SendMessage createKeyBoardForChoiceShelter(Long chatId, String answer) {
        SendMessage message = new SendMessage();

        message.setChatId(chatId);
        message.setText(answer);

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();

        List<KeyboardRow> rows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add(DOG_SHELTER);
        rows.add(row);

        row = new KeyboardRow();
        row.add(CAT_SHELTER);
        rows.add(row);

        keyboard.setKeyboard(rows);

        message.setReplyMarkup(keyboard);

        log.info("СОЗДАНА КЛАВИАТУРА ДЛЯ ВЫБОРА ПРИЮТА");
        return message;
    }

    @Override
    public final SendMessage createKeyBoardForGeneralInfo(Long chatId, String answer) {
        SendMessage message = new SendMessage();

        message.setChatId(chatId);
        message.setText(answer);

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();

        List<KeyboardRow> rows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add(INFO_ABOUT_SHELTER);
        rows.add(row);

        row = new KeyboardRow();
        row.add(INFO_ABOUT_PROCESS);
        rows.add(row);

        row = new KeyboardRow();
        row.add(REPORT_ABOUT_PET);
        rows.add(row);

        row = new KeyboardRow();
        row.add(CALL_VOLUNTEER);
        rows.add(row);

        keyboard.setKeyboard(rows);

        message.setReplyMarkup(keyboard);

        log.info("СОЗДАНА КЛАВИАТУРА ДЛЯ ПОЛУЧЕНИЯ ОБЩИХ ЗАПРОСОВ ИНФОРМАЦИИ О ПРИЮТЕ");
        return message;
    }

    @Override
    public final SendMessage createKeyBoardForDetailedInfoAboutShelter(Long chatId, String answer) {
        SendMessage message = new SendMessage();

        message.setChatId(chatId);
        message.setText(answer);

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();

        List<KeyboardRow> rows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add(INFO_ABOUT_WORK_SCHEDULE_AND_ADDRESS);
        rows.add(row);

        row = new KeyboardRow();
        row.add(INFO_ABOUT_SECURITY_CONTACT_DETAILS);
        rows.add(row);

        row = new KeyboardRow();
        row.add(INFO_ABOUT_GENERAL_SAFETY_RECOMMENDATION);
        rows.add(row);

        row = new KeyboardRow();
        row.add(RECORD_CONTACT_DETAILS);
        rows.add(row);

        row = new KeyboardRow();
        row.add(INFO_ABOUT_PROCESS);
        rows.add(row);

        row = new KeyboardRow();
        row.add(REPORT_ABOUT_PET);
        rows.add(row);

        row = new KeyboardRow();
        row.add(CHANGE_SHELTER);
        rows.add(row);

        row = new KeyboardRow();
        row.add(CALL_VOLUNTEER);
        rows.add(row);

        keyboard.setKeyboard(rows);

        message.setReplyMarkup(keyboard);

        log.info("СОЗДАНА КЛАВИАТУРА ДЛЯ ПОЛУЧЕНИЯ ДЕТАЛЬНОЙ ИНФОРМАЦИИ О ПРИЮТЕ");
        return message;
    }
}