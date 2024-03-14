package com.skypro.ShelterPetTelegramBot.service.impl.bot_service;

import com.skypro.ShelterPetTelegramBot.service.interfaces.bot_service.CreatingKeyBoards;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
@Service
public class CreatingKeyBoardsImpl implements CreatingKeyBoards {

    @Override
    public SendMessage createKeyBoardForRegistration(Long chatId, String answer) {
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
    public SendMessage createKeyBoardForChoiceShelter(Long chatId, String answer) {
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
    public SendMessage createKeyBoardForGeneralInfo(Long chatId, String answer) {
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
    public SendMessage createKeyBoardForDetailedInfoAboutShelter(Long chatId, String answer) {
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
        row.add(REMOVE_CONTACT_DETAILS);
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

    @Override
    public SendMessage createKeyBoardForDetailedInfoAboutProcessForDog(Long chatId, String answer) {
        SendMessage message = new SendMessage();

        message.setChatId(chatId);
        message.setText(answer);

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();

        List<KeyboardRow> rows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add(LIST_PETS);
        rows.add(row);

        row = new KeyboardRow();
        row.add(RULES_DATING);
        rows.add(row);

        row = new KeyboardRow();
        row.add(LIST_REQUIRED_DOCS);
        rows.add(row);

        row = new KeyboardRow();
        row.add(RECOMMENDATIONS_FOR_TRANSPORTATION);
        rows.add(row);

        row = new KeyboardRow();
        row.add(RECOMMENDATIONS_FOR_HOME_IMPROVEMENT_FOR_YOUNG_PET);
        rows.add(row);

        row = new KeyboardRow();
        row.add(RECOMMENDATIONS_FOR_HOME_IMPROVEMENT_FOR_ADULT_PET);
        rows.add(row);

        row = new KeyboardRow();
        row.add(RECOMMENDATIONS_FOR_HOME_IMPROVEMENT_FOR_DISABLED_PET);
        rows.add(row);

        row = new KeyboardRow();
        row.add(RECOMMENDATIONS_FROM_DOG_HANDLER);
        rows.add(row);

        row = new KeyboardRow();
        row.add(RECOMMENDATIONS_FOR_TRUSTED_DOG_HANDLER);
        rows.add(row);

        row = new KeyboardRow();
        row.add(LIST_REASONS_FOR_REFUSAL);
        rows.add(row);

        row = new KeyboardRow();
        row.add(RECORD_CONTACT_DETAILS);
        rows.add(row);

        row = new KeyboardRow();
        row.add(REMOVE_CONTACT_DETAILS);
        rows.add(row);

        row = new KeyboardRow();
        row.add(INFO_ABOUT_SHELTER);
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

        log.info("СОЗДАНА КЛАВИАТУРА ДЛЯ ПОЛУЧЕНИЯ ДЕТАЛЬНОЙ ИНФОРМАЦИИ О ПРОЦЕССЕ ПОЛУЧЕНИЯ СОБАКИ");
        return message;
    }

    @Override
    public SendMessage createKeyBoardForDetailedInfoAboutProcessForCat(Long chatId, String answer) {
        SendMessage message = new SendMessage();

        message.setChatId(chatId);
        message.setText(answer);

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();

        List<KeyboardRow> rows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add(LIST_PETS);
        rows.add(row);

        row = new KeyboardRow();
        row.add(RULES_DATING);
        rows.add(row);

        row = new KeyboardRow();
        row.add(LIST_REQUIRED_DOCS);
        rows.add(row);

        row = new KeyboardRow();
        row.add(RECOMMENDATIONS_FOR_TRANSPORTATION);
        rows.add(row);

        row = new KeyboardRow();
        row.add(RECOMMENDATIONS_FOR_HOME_IMPROVEMENT_FOR_YOUNG_PET);
        rows.add(row);

        row = new KeyboardRow();
        row.add(RECOMMENDATIONS_FOR_HOME_IMPROVEMENT_FOR_ADULT_PET);
        rows.add(row);

        row = new KeyboardRow();
        row.add(RECOMMENDATIONS_FOR_HOME_IMPROVEMENT_FOR_DISABLED_PET);
        rows.add(row);

        row = new KeyboardRow();
        row.add(LIST_REASONS_FOR_REFUSAL);
        rows.add(row);

        row = new KeyboardRow();
        row.add(RECORD_CONTACT_DETAILS);
        rows.add(row);

        row = new KeyboardRow();
        row.add(REMOVE_CONTACT_DETAILS);
        rows.add(row);

        row = new KeyboardRow();
        row.add(INFO_ABOUT_SHELTER);
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

        log.info("СОЗДАНА КЛАВИАТУРА ДЛЯ ПОЛУЧЕНИЯ ДЕТАЛЬНОЙ ИНФОРМАЦИИ О ПРОЦЕССЕ ПОЛУЧЕНИЯ КОШКИ");
        return message;
    }

    @Override
    public SendMessage createKeyBoardForReportPet(Long chatId, String answer) {
        SendMessage message = new SendMessage();

        message.setChatId(chatId);
        message.setText(answer);

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();

        List<KeyboardRow> rows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add(PATTERN_REPORT);
        rows.add(row);

        row = new KeyboardRow();
        row.add(RECORD_CONTACT_DETAILS);
        rows.add(row);

        row = new KeyboardRow();
        row.add(REMOVE_CONTACT_DETAILS);
        rows.add(row);

        row = new KeyboardRow();
        row.add(INFO_ABOUT_SHELTER);
        rows.add(row);

        row = new KeyboardRow();
        row.add(INFO_ABOUT_PROCESS);
        rows.add(row);

        row = new KeyboardRow();
        row.add(CHANGE_SHELTER);
        rows.add(row);

        row = new KeyboardRow();
        row.add(CALL_VOLUNTEER);
        rows.add(row);

        keyboard.setKeyboard(rows);

        message.setReplyMarkup(keyboard);

        log.info("СОЗДАНА КЛАВИАТУРА ДЛЯ ПОЛУЧЕНИЯ ИНФОРМАЦИИ ОБ ОТПРАВКЕ ОТЧЕТА О ЖИВОТНОМ");
        return message;
    }
}