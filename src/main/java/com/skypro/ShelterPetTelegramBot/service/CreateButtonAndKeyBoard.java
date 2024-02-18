package com.skypro.ShelterPetTelegramBot.service;

import com.skypro.ShelterPetTelegramBot.configuration.BotConfiguration;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

import static com.skypro.ShelterPetTelegramBot.utils.Buttons.*;
import static com.skypro.ShelterPetTelegramBot.utils.Buttons.CALL_VOLUNTEER_BUTTON;
import static com.skypro.ShelterPetTelegramBot.utils.Commands.*;

@Component
public class CreateButtonAndKeyBoard extends TelegramBot {


    public CreateButtonAndKeyBoard(BotConfiguration configuration, @Lazy ReactionOnBoardAndButtonAndCommand reaction) {
        super(configuration);
        this.reaction=reaction;

    }


    /**
     * Метод {@code createKeyBoardForUnregisteredUsers(Long chatId, String text)} <br>
     * Создает интерактивную клавиатуру для выбора команд для <b>НЕЗАРЕГИСТРИРОВАННЫХ пользователей</b>
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i>
     * @param text   <i> является текстом для отправки пользователю </i>
     */
    void createKeyBoardForUnregisteredUsers(Long chatId, String text) {
        SendMessage message = reaction.sendMessage(chatId, text);

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

        row = new KeyboardRow();
        row.add(REGISTRATION);
        rows.add(row);

        keyboard.setKeyboard(rows);

        message.setReplyMarkup(keyboard);
        reaction.executeMessage(message);
    }

    /**
     * Метод {@code createKeyBoardForRegisteredUsers(Long chatId, String text)} <br>
     * Создает интерактивную клавиатуру для выбора команд для <b>ЗАРЕГИСТРИРОВАННЫХ пользователей</b>
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i>
     * @param text   <i> является текстом для отправки пользователю </i>
     */
    void createKeyBoardForRegisteredUsers(Long chatId, String text) {
        SendMessage message = reaction.sendMessage(chatId, text);

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
        reaction.executeMessage(message);
    }

    /**
     * Метод {@code createKeyBoardForDetailedInfoAboutShelter(Long chatId, String text)} <br>
     * Создает интерактивную клавиатуру для выбора команд для получения подробной информации о приюте
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i>
     * @param text   <i> является текстом для отправки пользователю </i>
     */
    void createKeyBoardForDetailedInfoAboutShelter(Long chatId, String text) {
        SendMessage message = reaction.sendMessage(chatId, text);

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
        reaction.executeMessage(message);
    }

    /**
     * Метод {@code createButtonsForRegistration(Long chatId, String text)} <br>
     * Создает кнопки выбора под сообщением для дальнейшей регистрации нового пользователя
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i>
     * @param text   <i> является текстом для отправки пользователю </i>
     */
    void createButtonsForRegistration(Long chatId, String text) {
        createOnlyTwoButton(chatId, text,
                "Да",
                YES_BUTTON,
                "Нет",
                NO_BUTTON);
    }

    /**
     * Метод {@code createButtonGetInfoAboutShelter(Long chatId, String text)} <br>
     * Создает кнопку под сообщением для получения информации о приюте
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i>
     * @param text   <i> является текстом для отправки пользователю </i>
     */
    void createButtonInfoAboutShelter(Long chatId, String text) {
        createOnlyOneButton(chatId, text, INFO_ABOUT_SHELTER, INFO_ABOUT_SHELTER_BUTTON);
    }

    /**
     * Метод {@code createButtonGetInfoAboutShelter(Long chatId, String text)} <br>
     * Создает кнопки под сообщением для получения информации о процессе получения животного
     * или получения подробной информации о приюте
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i>
     * @param text   <i> является текстом для отправки пользователю </i>
     */
    void createButtonInfoAboutProcess(Long chatId, String text) {
        createOnlyTwoButton(chatId, text,
                DETAILED_INFO,
                DETAILED_INFO_PART_1_BUTTON,
                INFO_ABOUT_PROCESS,
                INFO_ABOUT_PROCESS_BUTTON);
    }

    /**
     * Метод {@code createButtonCallVolunteer(Long chatId, String text)} <br>
     * Создает кнопку под сообщением для вызова волонтера
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i>
     * @param text   <i> является текстом для отправки пользователю </i>
     */
    void createButtonCallVolunteer(Long chatId, String text) {
        createOnlyOneButton(chatId, text, CALL_VOLUNTEER, CALL_VOLUNTEER_BUTTON);
    }

    /**
     * Метод {@code createOnlyOneButton(Long chatId, String text, String nameButton, String callbackData)} <br>
     * Является шаблоном для создания <b>ОДНОЙ</b> кнопки
     *
     * @param chatId       <i> является идентификатором пользователя (его id в telegram) </i>
     * @param text         <i> является текстом для отправки пользователю </i>
     * @param nameButton   <i> является наименованием кнопки </i>
     * @param callbackData <i> является данными для идентификации нажатой кнопки </i>
     */
    private void createOnlyOneButton(Long chatId, String text, String nameButton, String callbackData) {
        SendMessage message = reaction.sendMessage(chatId, text);

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();

        var button = new InlineKeyboardButton();

        button.setText(nameButton);
        button.setCallbackData(callbackData);

        row.add(button);

        rows.add(row);

        keyboard.setKeyboard(rows);

        message.setReplyMarkup(keyboard);
        reaction.executeMessage(message);
    }

    /**
     * Метод {@code createOnlyTwoButton(Long chatId, String text, String firstButton, String callbackData)} <br>
     * Является шаблоном для создания <b>ДВУХ</b> кнопок
     *
     * @param chatId             <i> является идентификатором пользователя (его id в telegram) </i>
     * @param text               <i> является текстом для отправки пользователю </i>
     * @param nameFirstButton    <i> является наименованием первой кнопки </i>
     * @param firstCallbackData  <i> является данными для идентификации нажатой первой кнопки </i>
     * @param nameSecondButton   <i> является наименованием второй кнопки </i>
     * @param secondCallbackData <i> является данными для идентификации нажатой второй кнопки </i>
     */
    private void createOnlyTwoButton(Long chatId, String text,
                                     String nameFirstButton,
                                     String firstCallbackData,
                                     String nameSecondButton,
                                     String secondCallbackData) {

        SendMessage message = reaction.sendMessage(chatId, text);

        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rows = new ArrayList<>();

        List<InlineKeyboardButton> row = new ArrayList<>();

        var firstButton = new InlineKeyboardButton();
        var secondButton = new InlineKeyboardButton();

        firstButton.setText(nameFirstButton);
        firstButton.setCallbackData(firstCallbackData);

        secondButton.setText(nameSecondButton);
        secondButton.setCallbackData(secondCallbackData);

        row.add(firstButton);
        row.add(secondButton);

        rows.add(row);

        keyboard.setKeyboard(rows);

        message.setReplyMarkup(keyboard);
        reaction.executeMessage(message);
    }

}
