package com.skypro.ShelterPetTelegramBot.service.impl.bot_service;

import com.skypro.ShelterPetTelegramBot.service.interfaces.bot_service.CreatingButtons;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static com.skypro.ShelterPetTelegramBot.utils.Buttons.*;
import static com.skypro.ShelterPetTelegramBot.utils.Commands.*;

/**
 * Класс {@link CreatingButtonsImpl}
 * является сервисным классом для создания кнопок под сообщениями
 */
@Slf4j
@Service
public class CreatingButtonsImpl implements CreatingButtons {

    @Override
    public SendMessage createButtonsForChoiceRegistration(Long chatId, String answer) {
        log.info("СОЗДАНЫ КНОПКИ СОГЛАСИЯ НА РЕГИСТРАЦИЮ");
        return createTwoButtons(chatId, answer, YES, YES_BUTTON, NO, NO_BUTTON);
    }

    @Override
    public SendMessage createButtonForRegistration(Long chatId, String answer) {
        log.info("СОЗДАНА КНОПКА РЕГИСТРАЦИИ");
        return createOneButton(chatId, answer, REGISTRATION, REGISTRATION_BUTTON);
    }

    @Override
    public SendMessage createButtonsForChoiceShelter(Long chatId, String answer) {
        log.info("СОЗДАНЫ КНОПКИ ВЫБОРА ПРИЮТА");
        return createTwoButtons(chatId, answer, DOG_SHELTER, DOG_SHELTER_BUTTON, CAT_SHELTER, CAT_SHELTER_BUTTON);
    }

    @Override
    public SendMessage createButtonForSchemeDrivingToShelter(Long chatId, String answer) {
        log.info("СОЗДАНА КНОПКА ПОЛУЧЕНИЯ СХЕМЫ ПРОЕЗДА К ПРИЮТУ");
        return createOneButton(chatId, answer, SCHEME_DRIVING, SCHEME_DRIVING_BUTTON);
    }

    @Override
    public SendMessage createButtonForRemoveContactDetails(Long chatId, String answer) {
        log.info("СОЗДАНА КНОПКА УДАЛЕНИЯ КОНТАКТНЫХ ДАННЫХ");
        return createOneButton(chatId, answer, REMOVE, REMOVE_BUTTON);
    }

    @Override
    public SendMessage createButtonForCallVolunteer(Long chatId, String answer) {
        log.info("СОЗДАНА КНОПКА ВЫЗОВА ВОЛОНТЕРА");
        return createOneButton(chatId, answer, CALL_VOLUNTEER, CALL_VOLUNTEER_BUTTON);
    }

    /**
     * Метод является шаблоном для создания <b> ОДНОЙ </b> кнопки под сообщением
     *
     * @param chatId       <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param answer       <i> является текстом для отправки пользователю </i> <br>
     * @param nameButton   <i> является наименованием кнопки </i> <br>
     * @param callbackData <i> является данными для идентификации нажатой кнопки </i>
     */
    private SendMessage createOneButton(Long chatId, String answer,
                                        String nameButton,
                                        String callbackData) {

        SendMessage message = new SendMessage();

        message.setChatId(chatId);
        message.setText(answer);

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

        return message;
    }

    /**
     * Метод является шаблоном для создания <b> ДВУХ </b> кнопок под сообщением
     *
     * @param chatId             <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param answer             <i> является текстом для отправки пользователю </i> <br>
     * @param nameFirstButton    <i> является наименованием первой кнопки </i> <br>
     * @param firstCallbackData  <i> является данными для идентификации нажатой первой кнопки </i> <br>
     * @param nameSecondButton   <i> является наименованием второй кнопки </i> <br>
     * @param secondCallbackData <i> является данными для идентификации нажатой второй кнопки </i>
     */
    private SendMessage createTwoButtons(Long chatId, String answer,
                                         String nameFirstButton,
                                         String firstCallbackData,
                                         String nameSecondButton,
                                         String secondCallbackData) {

        SendMessage message = new SendMessage();

        message.setChatId(chatId);
        message.setText(answer);

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

        return message;
    }
}