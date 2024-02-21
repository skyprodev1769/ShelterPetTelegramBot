package com.skypro.ShelterPetTelegramBot.service.impl;

import com.skypro.ShelterPetTelegramBot.service.interfaces.CreatingKeyBoards;
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

        return message;
    }

    @Override
    public final SendMessage createKeyBoardGeneralStepsForDogShelter(Long chatId, String answer) {
        SendMessage message = new SendMessage();

        message.setChatId(chatId);
        message.setText(answer);

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();

        List<KeyboardRow> rows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add(INFO_ABOUT_DOG_SHELTER);
        rows.add(row);

        row = new KeyboardRow();
        row.add(INFO_ABOUT_PROCESS_FOR_DOG_SHELTER);
        rows.add(row);

        row = new KeyboardRow();
        row.add(REPORT_ABOUT_DOG);
        rows.add(row);

        row = new KeyboardRow();
        row.add(CALL_VOLUNTEER);
        rows.add(row);

        keyboard.setKeyboard(rows);

        message.setReplyMarkup(keyboard);

        return message;
    }

    @Override
    public final SendMessage createKeyBoardGeneralStepsForCatShelter(Long chatId, String answer) {
        SendMessage message = new SendMessage();

        message.setChatId(chatId);
        message.setText(answer);

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();

        List<KeyboardRow> rows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add(INFO_ABOUT_CAT_SHELTER);
        rows.add(row);

        row = new KeyboardRow();
        row.add(INFO_ABOUT_PROCESS_FOR_CAT_SHELTER);
        rows.add(row);

        row = new KeyboardRow();
        row.add(REPORT_ABOUT_CAT);
        rows.add(row);

        row = new KeyboardRow();
        row.add(CALL_VOLUNTEER);
        rows.add(row);

        keyboard.setKeyboard(rows);

        message.setReplyMarkup(keyboard);

        return message;
    }

    @Override
    public final SendMessage createKeyBoardForDetailedInfoAboutDogShelter(Long chatId, String answer) {
        SendMessage message = new SendMessage();

        message.setChatId(chatId);
        message.setText(answer);

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();

        List<KeyboardRow> rows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add(INFO_ABOUT_WORK_SCHEDULE_AND_ADDRESS_FOR_DOG_SHELTER);
        rows.add(row);

        row = new KeyboardRow();
        row.add(INFO_ABOUT_SECURITY_CONTACT_DETAILS_FOR_DOG_SHELTER);
        rows.add(row);

        row = new KeyboardRow();
        row.add(INFO_ABOUT_GENERAL_SAFETY_RECOMMENDATION);
        rows.add(row);

        row = new KeyboardRow();
        row.add(RECORD_CONTACT_DETAILS);
        rows.add(row);

        row = new KeyboardRow();
        row.add(INFO_ABOUT_PROCESS_FOR_DOG_SHELTER);
        rows.add(row);

        row = new KeyboardRow();
        row.add(REPORT_ABOUT_DOG);
        rows.add(row);

        row = new KeyboardRow();
        row.add(CHANGE_SHELTER);
        rows.add(row);

        row = new KeyboardRow();
        row.add(CALL_VOLUNTEER);
        rows.add(row);

        keyboard.setKeyboard(rows);

        message.setReplyMarkup(keyboard);

        return message;
    }

    @Override
    public final SendMessage createKeyBoardForDetailedInfoAboutCatShelter(Long chatId, String answer) {
        SendMessage message = new SendMessage();

        message.setChatId(chatId);
        message.setText(answer);

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();

        List<KeyboardRow> rows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add(INFO_ABOUT_WORK_SCHEDULE_AND_ADDRESS_FOR_CAT_SHELTER);
        rows.add(row);

        row = new KeyboardRow();
        row.add(INFO_ABOUT_SECURITY_CONTACT_DETAILS_FOR_CAT_SHELTER);
        rows.add(row);

        row = new KeyboardRow();
        row.add(INFO_ABOUT_GENERAL_SAFETY_RECOMMENDATION);
        rows.add(row);

        row = new KeyboardRow();
        row.add(RECORD_CONTACT_DETAILS);
        rows.add(row);

        row = new KeyboardRow();
        row.add(INFO_ABOUT_PROCESS_FOR_CAT_SHELTER);
        rows.add(row);

        row = new KeyboardRow();
        row.add(REPORT_ABOUT_CAT);
        rows.add(row);

        row = new KeyboardRow();
        row.add(CHANGE_SHELTER);
        rows.add(row);

        row = new KeyboardRow();
        row.add(CALL_VOLUNTEER);
        rows.add(row);

        keyboard.setKeyboard(rows);

        message.setReplyMarkup(keyboard);

        return message;
    }
}