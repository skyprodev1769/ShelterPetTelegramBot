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

    /**
     * Метод <br> <br>
     * {@code createKeyBoardForUnregisteredUsers(Long chatId, String answer)} <br> <br>
     * <p>
     * Создает интерактивную клавиатуру для выбора команд для <b> НЕЗАРЕГИСТРИРОВАННЫХ </b> пользователей
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param answer <i> является текстом для отправки пользователю </i>
     */
    @Override
    public final SendMessage createKeyBoardForUnregisteredUsers(Long chatId, String answer) {
        SendMessage message = new SendMessage();

        message.setChatId(chatId);
        message.setText(answer);

        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();

        List<KeyboardRow> rows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add(START);
        rows.add(row);

        row = new KeyboardRow();
        row.add(HELP);
        rows.add(row);

        row = new KeyboardRow();
        row.add(SETTINGS);
        rows.add(row);

        keyboard.setKeyboard(rows);

        message.setReplyMarkup(keyboard);

        return message;
    }

    /**
     * Метод <br> <br>
     * {@code createKeyBoardForRegisteredUsers(Long chatId, String answer)} <br> <br>
     * <p>
     * Создает интерактивную клавиатуру для выбора команд для <b> ЗАРЕГИСТРИРОВАННЫХ </b> пользователей
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param answer <i> является текстом для отправки пользователю </i>
     */
    @Override
    public final SendMessage createKeyBoardForRegisteredUsers(Long chatId, String answer) {
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

        return message;
    }

    /**
     * Метод <br> <br>
     * {@code createKeyBoardForDetailedInfoAboutShelter(Long chatId, String answer)} <br> <br>
     * <p>
     * Создает интерактивную клавиатуру для выбора команд для получения подробной информации о приюте
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param answer <i> является текстом для отправки пользователю </i>
     */
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
        row.add(CALL_VOLUNTEER);
        rows.add(row);

        keyboard.setKeyboard(rows);

        message.setReplyMarkup(keyboard);

        return message;
    }
}