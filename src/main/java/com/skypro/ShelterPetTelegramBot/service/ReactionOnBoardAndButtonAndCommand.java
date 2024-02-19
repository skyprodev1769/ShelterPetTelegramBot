package com.skypro.ShelterPetTelegramBot.service;

import com.skypro.ShelterPetTelegramBot.configuration.BotConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static com.skypro.ShelterPetTelegramBot.utils.Answers.*;
import static com.skypro.ShelterPetTelegramBot.utils.Answers.REACTION_TO_CALL_VOLUNTEER;
import static com.skypro.ShelterPetTelegramBot.utils.Buttons.*;
import static com.skypro.ShelterPetTelegramBot.utils.Commands.*;

@Slf4j
@Component
public class ReactionOnBoardAndButtonAndCommand extends TelegramBot {


    private final CreateButtonAndKeyBoard create;

    public ReactionOnBoardAndButtonAndCommand(BotConfiguration configuration, @Lazy ReactionOnBoardAndButtonAndCommand reaction, @Lazy CreateButtonAndKeyBoard create) {
        super(configuration, reaction);
        this.create = create;
    }

    /**
     * Метод {@code getReactionsForUnregisteredUsers(Long chatId, String text, String userFirstName)} <br>
     * Возвращает реакции на команды для <b>НЕЗАРЕГИСТРИРОВАННЫХ пользователей</b>
     *
     * @param chatId        <i> является идентификатором пользователя (его id в telegram) </i>
     * @param text          <i> является текстом для отправки пользователю </i>
     * @param userFirstName <i> является именем пользователя </i>
     */
    void getReactionsForUnregisteredUsers(Long chatId, String text, String userFirstName) {
        String answer;

        switch (text) {

            case START:
                answer = REACTION_TO_FIRST_COMMAND_START(userFirstName);
                create.createButtonsForRegistration(chatId, answer);
                break;

            case HELP:
                answer = REACTION_TO_COMMAND_HELP_FOR_UNREGISTERED_USERS(userFirstName);
                reactionToCommand(chatId, answer);
                break;

            case SETTINGS:
                answer = REACTION_TO_COMMAND_SETTINGS(userFirstName);
                create.createKeyBoardForUnregisteredUsers(chatId, answer);
                break;

            case REGISTRATION:
                answer = REACTION_TO_SUCCESSFUL_REGISTRATION(userFirstName);
                saveNewUserToDB(chatId, userFirstName);
                create.createButtonInfoAboutShelter(chatId, answer);
                break;

            default:
                answer = DEFAULT_REACTION_FOR_UNREGISTERED_USERS(userFirstName);
                reactionToCommand(chatId, answer);
                break;
        }
    }

    /**
     * Метод {@code getReactionsForRegisteredUsers(Long chatId, String text, String userFirstName)} <br>
     * Возвращает реакции на команды для <b>ЗАРЕГИСТРИРОВАННЫХ пользователей</b>
     *
     * @param chatId        <i> является идентификатором пользователя (его id в telegram) </i>
     * @param text          <i> является текстом для отправки пользователю </i>
     * @param userFirstName <i> является именем пользователя </i>
     */
    public void getReactionsForRegisteredUsers(Long chatId, String text, String userFirstName) {
        String answer;

        switch (text) {

            /*
            БАЗОВЫЕ КОМАНДЫ
             */

            case START:
                answer = REACTION_TO_COMMAND_START(userFirstName);
                create.createButtonInfoAboutShelter(chatId, answer);
                break;

            case HELP:
                answer = REACTION_TO_COMMAND_HELP_FOR_REGISTERED_USERS(userFirstName);
                create.createButtonCallVolunteer(chatId, answer);
                break;

            case SETTINGS:
                answer = REACTION_TO_COMMAND_SETTINGS(userFirstName);
                create.createKeyBoardForRegisteredUsers(chatId, answer);
                break;

            /*
            КОМАНДЫ ДЛЯ ПОЛУЧЕНИЯ ИНФОРМАЦИИ О ПРИЮТЕ
             */

            case INFO_ABOUT_SHELTER:
                answer = REACTION_TO_REQUEST;
                create.createButtonInfoAboutProcess(chatId, answer);
                break;

            case INFO_ABOUT_WORK_SCHEDULE_AND_ADDRESS:
                answer = REACTION_TO_INFO_ABOUT_WORK_SCHEDULE_AND_ADDRESS;
                reactionToCommand(chatId, answer);
                sendFoto(chatId, "staticFile/foto/adres.jpg");
                break;

            case INFO_ABOUT_SECURITY_CONTACT_DETAILS:
                answer = REACTION_TO_INFO_ABOUT_SECURITY_CONTACT_DETAILS;
                reactionToCommand(chatId, answer);
                break;

            case INFO_ABOUT_GENERAL_SAFETY_RECOMMENDATION:
                answer = REACTION_TO_INFO_ABOUT_GENERAL_SAFETY_RECOMMENDATION;
                reactionToCommand(chatId, answer);
                sendDocument(chatId, "полные правила поведения и техника безопасности на " +
                                "территории приюта смотрите в документе",
                        "staticFile/docs/Safety_precautions.txt");
                break;

            case RECORD_CONTACT_DETAILS:
                answer = REACTION_TO_RECORD_CONTACT_DETAILS;
                reactionToCommand(chatId, answer);
                break;

            /*
            КОМАНДА ВЫЗОВА ВОЛОНТЕРА
             */

            case CALL_VOLUNTEER:
                answer = REACTION_TO_CALL_VOLUNTEER;
                reactionToCommand(chatId, answer);
                break;

            /*
            ДЕФОЛТНАЯ КОМАНДА
             */

            default:
                answer = DEFAULT_REACTION_FOR_REGISTERED_USERS(userFirstName);
                create.createButtonCallVolunteer(chatId, answer);
                break;
        }
    }

    /**
     * Метод {@code getReactionsForKeyBoardButtons(Long chatId, String callbackData)} <br>
     * Возвращает реакции на нажатие кнопок пользователями
     *
     * @param chatId       <i> является идентификатором пользователя (его id в telegram) </i>
     * @param callbackData <i> является данными для идентификации нажатой кнопки </i>
     */
    public void getReactionsForKeyBoardButtons(Long chatId, String callbackData) {
        String answer;

        switch (callbackData) {

            /*
            КНОПКИ РЕГИСТРАЦИИ
             */

            case YES_BUTTON:
                answer = AGREEMENT_REGISTRATION;
                reactionToCommand(chatId, answer);
                break;

            case NO_BUTTON:
                answer = DISAGREEMENT_REGISTRATION;
                reactionToCommand(chatId, answer);
                break;
            /*
            КНОПКИ ДЛЯ ПОЛУЧЕНИЯ ИНФОРМАЦИИ О ПРИЮТЕ
             */

            case INFO_ABOUT_SHELTER_BUTTON:
                answer = REACTION_TO_REQUEST;
                create.createButtonInfoAboutProcess(chatId, answer);
                break;

            case DETAILED_INFO_PART_1_BUTTON:
                answer = REACTION_TO_DETAILED_INFO;
                create.createKeyBoardForDetailedInfoAboutShelter(chatId, answer);
                break;

            /*
            КНОПКА ВЫЗОВА ВОЛОНТЕРА
             */

            case CALL_VOLUNTEER_BUTTON:
                answer = REACTION_TO_CALL_VOLUNTEER;
                reactionToCommand(chatId, answer);
                break;
        }
    }

    /**
     * Метод {@code reactionToCommand(Long chatId, String text)} <br>
     * Является ответной реакцией в виде текстового сообщения на действие пользователя
     * и объединяет методы: <br>
     * <br>
     * {@code  sendMessage(Long chatId, String text))} <br>
     * {@code  executeMessage(SendMessage message)}
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i>
     * @param text   <i> является текстом для отправки пользователю </i>
     */

    private void reactionToCommand(Long chatId, String text) {
        SendMessage message = sendMessage(chatId, text);
        executeMessage(message);
    }

}
