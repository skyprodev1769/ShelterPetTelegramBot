package com.skypro.ShelterPetTelegramBot.service;

import com.skypro.ShelterPetTelegramBot.configuration.BotConfiguration;
import com.skypro.ShelterPetTelegramBot.model.entity.PotentialParent;
import com.skypro.ShelterPetTelegramBot.model.entity.User;
import com.skypro.ShelterPetTelegramBot.model.repository.PotentialParentRepository;
import com.skypro.ShelterPetTelegramBot.model.repository.UserRepository;
import com.skypro.ShelterPetTelegramBot.service.impl.CreatingButtonsImpl;
import com.skypro.ShelterPetTelegramBot.service.impl.CreatingKeyBoardsImpl;
import com.skypro.ShelterPetTelegramBot.service.impl.RecordingContactsImpl;
import com.skypro.ShelterPetTelegramBot.service.interfaces.CreatingButtons;
import com.skypro.ShelterPetTelegramBot.service.interfaces.CreatingKeyBoards;
import com.skypro.ShelterPetTelegramBot.service.interfaces.RecordingContacts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.skypro.ShelterPetTelegramBot.utils.Buttons.*;
import static com.skypro.ShelterPetTelegramBot.utils.Commands.*;
import static com.skypro.ShelterPetTelegramBot.utils.Descriptions.*;
import static com.skypro.ShelterPetTelegramBot.utils.answers.AnswersForBasicCommands.*;
import static com.skypro.ShelterPetTelegramBot.utils.answers.AnswersForChoiceShelterCommands.REACTION_TO_CHOICE_CAT_SHELTER;
import static com.skypro.ShelterPetTelegramBot.utils.answers.AnswersForChoiceShelterCommands.REACTION_TO_CHOICE_DOG_SHELTER;
import static com.skypro.ShelterPetTelegramBot.utils.answers.AnswersForGeneralCommands.*;
import static com.skypro.ShelterPetTelegramBot.utils.answers.AnswersForRecordContactsCommands.*;
import static com.skypro.ShelterPetTelegramBot.utils.answers.AnswersForRegistrationCommands.*;
import static com.skypro.ShelterPetTelegramBot.utils.answers.shelters.AnswersForInfoAboutAnyShelterCommands.REACTION_TO_INFO_ABOUT_GENERAL_SAFETY_RECOMMENDATION;
import static com.skypro.ShelterPetTelegramBot.utils.answers.shelters.AnswersForInfoAboutAnyShelterCommands.REACTION_TO_SCHEME_DRIVING;
import static com.skypro.ShelterPetTelegramBot.utils.answers.shelters.AnswersForInfoAboutCatShelterCommands.REACTION_TO_INFO_ABOUT_SECURITY_CONTACT_DETAILS_FOR_CAT_SHELTER;
import static com.skypro.ShelterPetTelegramBot.utils.answers.shelters.AnswersForInfoAboutCatShelterCommands.REACTION_TO_INFO_ABOUT_WORK_SCHEDULE_AND_ADDRESS_FOR_CAT_SHELTER;
import static com.skypro.ShelterPetTelegramBot.utils.answers.shelters.AnswersForInfoAboutDogShelterCommands.REACTION_TO_INFO_ABOUT_SECURITY_CONTACT_DETAILS_FOR_DOG_SHELTER;
import static com.skypro.ShelterPetTelegramBot.utils.answers.shelters.AnswersForInfoAboutDogShelterCommands.REACTION_TO_INFO_ABOUT_WORK_SCHEDULE_AND_ADDRESS_FOR_DOG_SHELTER;

/**
 * Класс {@link TelegramBot}
 * является основным сервисным классом, в котором происходит обработка всех входящих {@code update}
 */
@Slf4j
@Component
@PropertySource("application.properties")
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfiguration configuration;

    private final CreatingButtons buttons;
    private final RecordingContacts recordingContacts;
    private final CreatingKeyBoards keyBoards;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PotentialParentRepository parentRepository;

    private static Boolean isDogShelter = true;
    private final static Pattern PATTERN = Pattern.compile("([\\W+]+)(\\s)([\\W+]+)(\\s)([0-9]{11})");
    private final static String PATH_FOR_DOG_SHELTER = "/Users/Иван/Documents/GitHub/ShelterPetTelegramBot/src/main/resources/schemes/SCHEME_FOR_DOG_SHELTER.png";
    private final static String PATH_FOR_CAT_SHELTER = "/Users/Иван/Documents/GitHub/ShelterPetTelegramBot/src/main/resources/schemes/SCHEME_FOR_CAT_SHELTER.png";


    public TelegramBot(BotConfiguration configuration) {
        super(configuration.getToken());
        this.configuration = configuration;
        this.buttons = new CreatingButtonsImpl();
        this.keyBoards = new CreatingKeyBoardsImpl();
        this.recordingContacts = new RecordingContactsImpl();
        createMainMenu();
    }

    @Override
    public String getBotUsername() {
        return configuration.getName();
    }

    @Override
    public void onUpdateReceived(Update update) {

        Long chatId;
        String userFirstName;

        // Если приходит сообщение и оно содержит текст
        if (update.hasMessage() && update.getMessage().hasText()) {

            chatId = update.getMessage().getChatId();
            userFirstName = update.getMessage().getChat().getFirstName();
            String text = update.getMessage().getText();

            // Если нет записи о пользователе в БД
            if (userRepository.findById(chatId).isEmpty()) {

                getReactionsForUnregisteredUsers(chatId, userFirstName, text);

                // Если есть запись о пользователе в БД
            } else {

                Matcher matcher = PATTERN.matcher(text);
                String answer;
                SendMessage message;

                // Если выбран приют для собак
                if (isDogShelter) {

                    // Если нет записи о потенциальном усыновителе в БД
                    if (parentRepository.findById(chatId).isEmpty() && matcher.matches()) {

                        PotentialParent parent = recordingContacts.recordContact(chatId, matcher);
                        parentRepository.save(parent);

                        log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАПИСАЛСЯ КАК ПОТЕНЦИАЛЬНЫЙ УСЫНОВИТЕЛЬ", chatId, userFirstName);

                        answer = REACTION_TO_SUCCESSFUL_RECORD_CONTACT(userFirstName);
                        message = keyBoards.createKeyBoardForDetailedInfoAboutDogShelter(chatId, answer);
                        executeMessage(message);

                        return;

                        // Если есть запись о потенциальном усыновителе в БД
                    } else if (parentRepository.findById(chatId).isPresent() && matcher.matches()) {

                        answer = REACTION_TO_REPEAT_RECORD_CONTACT(userFirstName);
                        message = keyBoards.createKeyBoardForDetailedInfoAboutDogShelter(chatId, answer);
                        executeMessage(message);
                        return;
                    }

                    getReactionsForDogShelter(chatId, userFirstName, text);

                    // Если выбран приют для кошек
                } else {

                    // Если нет записи о потенциальном усыновителе в БД
                    if (parentRepository.findById(chatId).isEmpty() && matcher.matches()) {

                        PotentialParent parent = recordingContacts.recordContact(chatId, matcher);
                        parentRepository.save(parent);

                        log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАПИСАЛСЯ КАК ПОТЕНЦИАЛЬНЫЙ УСЫНОВИТЕЛЬ", chatId, userFirstName);

                        answer = REACTION_TO_SUCCESSFUL_RECORD_CONTACT(userFirstName);
                        message = keyBoards.createKeyBoardForDetailedInfoAboutCatShelter(chatId, answer);
                        executeMessage(message);

                        return;

                        // Если есть запись о потенциальном усыновителе в БД
                    } else if (parentRepository.findById(chatId).isPresent() && matcher.matches()) {

                        answer = REACTION_TO_REPEAT_RECORD_CONTACT(userFirstName);
                        message = keyBoards.createKeyBoardForDetailedInfoAboutCatShelter(chatId, answer);
                        executeMessage(message);
                        return;
                    }

                    getReactionsForCatShelter(chatId, userFirstName, text);
                }
            }

            // Если приходит отклик от нажатия кнопки
        } else if (update.hasCallbackQuery()) {

            chatId = update.getCallbackQuery().getMessage().getChatId();
            userFirstName = update.getCallbackQuery().getFrom().getFirstName();
            String callbackData = update.getCallbackQuery().getData();

            getReactionsToClickingButtons(chatId, userFirstName, callbackData);
        }
    }

    /**
     * Метод возвращает реакции на команды для <b> НЕЗАРЕГИСТРИРОВАННЫХ </b> пользователей
     *
     * @param chatId        <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param userFirstName <i> является именем пользователя </i> <br>
     * @param text          <i> является полученным текстом от пользователя </i>
     */
    private void getReactionsForUnregisteredUsers(Long chatId, String userFirstName, String text) {

        String answer;
        SendMessage message;

        switch (text) {

            case START -> {
                log.info("ПОЛЬЗОВАТЕЛЬ ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ СТАРТОВОЕ СООБЩЕНИЕ", chatId, userFirstName);
                answer = REACTION_TO_COMMAND_START_FOR_UNREGISTERED_USERS(userFirstName);
                message = buttons.createButtonsForChoiceRegistration(chatId, answer);
                executeMessage(message);
            }

            case HELP -> {
                answer = REACTION_TO_COMMAND_HELP_FOR_UNREGISTERED_USERS(userFirstName);
                reactionToCommand(chatId, answer);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ ОПИСАТЕЛЬНОЕ СООБЩЕНИЕ", chatId, userFirstName);
            }

            case SETTINGS -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАПРОСИЛ КЛАВИАТУРУ ДЛЯ РЕГИСТРАЦИИ", chatId, userFirstName);
                answer = REACTION_TO_COMMAND_SETTINGS(userFirstName);
                message = keyBoards.createKeyBoardForRegistration(chatId, answer);
                executeMessage(message);
            }

            case REGISTRATION -> {
                saveNewUserToDB(chatId, userFirstName);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАРЕГИСТРИРОВАЛСЯ", chatId, userFirstName);
                answer = REACTION_TO_SUCCESSFUL_REGISTRATION(userFirstName);
                message = buttons.createButtonsForChoiceShelter(chatId, answer);
                executeMessage(message);
            }

            default -> {
                answer = DEFAULT_REACTION_FOR_UNREGISTERED_USERS(userFirstName);
                reactionToCommand(chatId, answer);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ ДЕФОЛТНОЕ СООБЩЕНИЕ", chatId, userFirstName);
            }
        }
    }

    /**
     * Метод возвращает реакции на команды для <b> ЗАРЕГИСТРИРОВАННЫХ </b> пользователей
     * и запросов информации о приюте <b> ДЛЯ СОБАК </b>
     *
     * @param chatId        <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param userFirstName <i> является именем пользователя </i> <br>
     * @param text          <i> является полученным текстом от пользователя </i>
     */
    private void getReactionsForDogShelter(Long chatId, String userFirstName, String text) {

        String answer;
        SendMessage message;

        switch (text) {

            // БАЗОВЫЕ КОМАНДЫ

            case START -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ СТАРТОВОЕ СООБЩЕНИЕ", chatId, userFirstName);
                answer = REACTION_TO_COMMAND_START_FOR_REGISTERED_USERS(userFirstName);
                message = buttons.createButtonsForChoiceShelter(chatId, answer);
                executeMessage(message);
            }

            case HELP -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ ОПИСАТЕЛЬНОЕ СООБЩЕНИЕ", chatId, userFirstName);
                answer = REACTION_TO_COMMAND_HELP_FOR_REGISTERED_USERS(userFirstName);
                message = buttons.createButtonForCallVolunteer(chatId, answer);
                executeMessage(message);
            }

            case SETTINGS -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАПРОСИЛ КЛАВИАТУРУ ДЛЯ ВЫБОРА ПРИЮТА", chatId, userFirstName);
                answer = REACTION_TO_COMMAND_SETTINGS(userFirstName);
                message = keyBoards.createKeyBoardForChoiceShelter(chatId, answer);
                executeMessage(message);
            }

            // КОМАНДЫ ВЫБОРА ПРИЮТА

            case DOG_SHELTER -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ВЫБРАЛ ПРИЮТ ДЛЯ СОБАК", chatId, userFirstName);
                isDogShelter = true;
                answer = REACTION_TO_CHOICE_DOG_SHELTER(userFirstName);
                message = keyBoards.createKeyBoardGeneralStepsForDogShelter(chatId, answer);
                executeMessage(message);
            }

            case CAT_SHELTER -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ВЫБРАЛ ПРИЮТ ДЛЯ КОШЕК", chatId, userFirstName);
                isDogShelter = false;
                answer = REACTION_TO_CHOICE_CAT_SHELTER(userFirstName);
                message = keyBoards.createKeyBoardGeneralStepsForCatShelter(chatId, answer);
                executeMessage(message);
            }

            // КОМАНДЫ ИНФОРМАЦИИ О ЛЮБОМ ПРИЮТЕ

            case INFO_ABOUT_GENERAL_SAFETY_RECOMMENDATION -> {
                answer = REACTION_TO_INFO_ABOUT_GENERAL_SAFETY_RECOMMENDATION(userFirstName);
                reactionToCommand(chatId, answer);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ ИНФОРМАЦИЮ О ТРЕБОВАНИЯХ БЕЗОПАСНОСТИ НА ТЕРРИТОРИИ ПРИЮТА", chatId, userFirstName);
            }

            // КОМАНДЫ ИНФОРМАЦИИ О ПРИЮТЕ ДЛЯ СОБАК

            case INFO_ABOUT_DOG_SHELTER -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАПРОСИЛ ИНФОРМАЦИЮ О ПРИЮТЕ ДЛЯ СОБАК", chatId, userFirstName);
                answer = REACTION_TO_REQUEST(userFirstName);
                message = buttons.createButtonForGetDetailedInfoAboutDogShelter(chatId, answer);
                executeMessage(message);
            }

            case INFO_ABOUT_WORK_SCHEDULE_AND_ADDRESS_FOR_DOG_SHELTER -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ АДРЕС И РЕЖИМ РАБОТЫ ПРИЮТА ДЛЯ СОБАК", chatId, userFirstName);
                answer = REACTION_TO_INFO_ABOUT_WORK_SCHEDULE_AND_ADDRESS_FOR_DOG_SHELTER(userFirstName);
                message = buttons.createButtonForSchemeDrivingForDogShelter(chatId, answer);
                executeMessage(message);
            }

            case INFO_ABOUT_SECURITY_CONTACT_DETAILS_FOR_DOG_SHELTER -> {
                answer = REACTION_TO_INFO_ABOUT_SECURITY_CONTACT_DETAILS_FOR_DOG_SHELTER(userFirstName);
                reactionToCommand(chatId, answer);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ НОМЕРА ОХРАНЫ ПРИЮТА ДЛЯ СОБАК", chatId, userFirstName);
            }

            // КОМАНДА ЗАПИСИ КОНТАКТНЫХ ДАННЫХ

            case RECORD_CONTACT_DETAILS -> {
                answer = REACTION_TO_RECORD_CONTACT_DETAILS(userFirstName);
                reactionToCommand(chatId, answer);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАПРОСИЛ ФОРМУ ДЛЯ ОТПРАВКИ КОНТАКТНЫХ ДАННЫХ", chatId, userFirstName);
            }

            // КОМАНДА СМЕНЫ ПРИЮТА

            case CHANGE_SHELTER -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАПРОСИЛ ИЗМЕНЕНИЯ ВЫБОРА ПРИЮТА", chatId, userFirstName);
                answer = REACTION_TO_CHANGED_SHELTER(userFirstName);
                message = keyBoards.createKeyBoardForChoiceShelter(chatId, answer);
                executeMessage(message);
            }

            // КОМАНДА ВЫЗОВА ВОЛОНТЕРА

            case CALL_VOLUNTEER -> {
                answer = REACTION_TO_CALL_VOLUNTEER(userFirstName);
                reactionToCommand(chatId, answer);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ НОМЕРА ВОЛОНТЕРОВ", chatId, userFirstName);
            }

            // ДЕФОЛТНАЯ КОМАНДА

            default -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ ДЕФОЛТНОЕ СООБЩЕНИЕ", chatId, userFirstName);
                answer = DEFAULT_REACTION_FOR_REGISTERED_USERS(userFirstName);
                message = buttons.createButtonForCallVolunteer(chatId, answer);
                executeMessage(message);
            }
        }
    }

    /**
     * Метод возвращает реакции на команды для <b> ЗАРЕГИСТРИРОВАННЫХ </b> пользователей
     * и запросов информации о приюте <b> ДЛЯ КОШЕК </b>
     *
     * @param chatId        <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param userFirstName <i> является именем пользователя </i> <br>
     * @param text          <i> является полученным текстом от пользователя </i>
     */
    private void getReactionsForCatShelter(Long chatId, String userFirstName, String text) {

        String answer;
        SendMessage message;

        switch (text) {

            // БАЗОВЫЕ КОМАНДЫ

            case START -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ СТАРТОВОЕ СООБЩЕНИЕ", chatId, userFirstName);
                answer = REACTION_TO_COMMAND_START_FOR_REGISTERED_USERS(userFirstName);
                message = buttons.createButtonsForChoiceShelter(chatId, answer);
                executeMessage(message);
            }

            case HELP -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ ОПИСАТЕЛЬНОЕ СООБЩЕНИЕ", chatId, userFirstName);
                answer = REACTION_TO_COMMAND_HELP_FOR_REGISTERED_USERS(userFirstName);
                message = buttons.createButtonForCallVolunteer(chatId, answer);
                executeMessage(message);
            }

            case SETTINGS -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАПРОСИЛ КЛАВИАТУРУ ДЛЯ ВЫБОРА ПРИЮТА", chatId, userFirstName);
                answer = REACTION_TO_COMMAND_SETTINGS(userFirstName);
                message = keyBoards.createKeyBoardForChoiceShelter(chatId, answer);
                executeMessage(message);
            }

            // КОМАНДЫ ВЫБОРА ПРИЮТА

            case DOG_SHELTER -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ВЫБРАЛ ПРИЮТ ДЛЯ СОБАК", chatId, userFirstName);
                isDogShelter = true;
                answer = REACTION_TO_CHOICE_DOG_SHELTER(userFirstName);
                message = keyBoards.createKeyBoardGeneralStepsForDogShelter(chatId, answer);
                executeMessage(message);
            }

            case CAT_SHELTER -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ВЫБРАЛ ПРИЮТ ДЛЯ КОШЕК", chatId, userFirstName);
                isDogShelter = false;
                answer = REACTION_TO_CHOICE_CAT_SHELTER(userFirstName);
                message = keyBoards.createKeyBoardGeneralStepsForCatShelter(chatId, answer);
                executeMessage(message);
            }

            // КОМАНДЫ ИНФОРМАЦИИ О ЛЮБОМ ПРИЮТЕ

            case INFO_ABOUT_GENERAL_SAFETY_RECOMMENDATION -> {
                answer = REACTION_TO_INFO_ABOUT_GENERAL_SAFETY_RECOMMENDATION(userFirstName);
                reactionToCommand(chatId, answer);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ ИНФОРМАЦИЮ О ТРЕБОВАНИЯХ БЕЗОПАСНОСТИ НА ТЕРРИТОРИИ ПРИЮТА", chatId, userFirstName);
            }

            // КОМАНДЫ ИНФОРМАЦИИ О ПРИЮТЕ ДЛЯ КОШЕК

            case INFO_ABOUT_CAT_SHELTER -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАПРОСИЛ ИНФОРМАЦИЮ О ПРИЮТЕ ДЛЯ КОШЕК", chatId, userFirstName);
                answer = REACTION_TO_REQUEST(userFirstName);
                message = buttons.createButtonForGetDetailedInfoAboutCatShelter(chatId, answer);
                executeMessage(message);
            }

            case INFO_ABOUT_WORK_SCHEDULE_AND_ADDRESS_FOR_CAT_SHELTER -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ АДРЕС И РЕЖИМ РАБОТЫ ПРИЮТА ДЛЯ КОШЕК", chatId, userFirstName);
                answer = REACTION_TO_INFO_ABOUT_WORK_SCHEDULE_AND_ADDRESS_FOR_CAT_SHELTER(userFirstName);
                message = buttons.createButtonForSchemeDrivingForCatShelter(chatId, answer);
                executeMessage(message);
            }

            case INFO_ABOUT_SECURITY_CONTACT_DETAILS_FOR_CAT_SHELTER -> {
                answer = REACTION_TO_INFO_ABOUT_SECURITY_CONTACT_DETAILS_FOR_CAT_SHELTER(userFirstName);
                reactionToCommand(chatId, answer);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ НОМЕРА ОХРАНЫ ПРИЮТА ДЛЯ КОШЕК", chatId, userFirstName);
            }

            // КОМАНДА ЗАПИСИ КОНТАКТНЫХ ДАННЫХ

            case RECORD_CONTACT_DETAILS -> {
                answer = REACTION_TO_RECORD_CONTACT_DETAILS(userFirstName);
                reactionToCommand(chatId, answer);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАПРОСИЛ ФОРМУ ДЛЯ ОТПРАВКИ КОНТАКТНЫХ ДАННЫХ", chatId, userFirstName);
            }

            // КОМАНДА СМЕНЫ ПРИЮТА

            case CHANGE_SHELTER -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАПРОСИЛ ИЗМЕНЕНИЯ ВЫБОРА ПРИЮТА", chatId, userFirstName);
                answer = REACTION_TO_CHANGED_SHELTER(userFirstName);
                message = keyBoards.createKeyBoardForChoiceShelter(chatId, answer);
                executeMessage(message);
            }

            // КОМАНДА ВЫЗОВА ВОЛОНТЕРА

            case CALL_VOLUNTEER -> {
                answer = REACTION_TO_CALL_VOLUNTEER(userFirstName);
                reactionToCommand(chatId, answer);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ НОМЕРА ВОЛОНТЕРОВ", chatId, userFirstName);
            }

            // ДЕФОЛТНАЯ КОМАНДА

            default -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ ДЕФОЛТНОЕ СООБЩЕНИЕ", chatId, userFirstName);
                answer = DEFAULT_REACTION_FOR_REGISTERED_USERS(userFirstName);
                message = buttons.createButtonForCallVolunteer(chatId, answer);
                executeMessage(message);
            }
        }
    }

    /**
     * Метод возвращает реакции на нажатие кнопок пользователями
     *
     * @param chatId        <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param userFirstName <i> является именем пользователя </i> <br>
     * @param callbackData  <i> является данными для идентификации нажатой кнопки </i>
     */
    private void getReactionsToClickingButtons(Long chatId, String userFirstName, String callbackData) {

        String answer;
        SendMessage message;

        switch (callbackData) {

            // КНОПКИ РЕГИСТРАЦИИ

            case YES_BUTTON -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} СОГЛАСИЛСЯ НА РЕГИСТРАЦИЮ", chatId, userFirstName);
                answer = REACTION_TO_AGREEMENT_REGISTRATION(userFirstName);
                message = buttons.createButtonForRegistration(chatId, answer);
                executeMessage(message);
            }

            case NO_BUTTON -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ОТКАЗАЛСЯ ОТ РЕГИСТРАЦИИ", chatId, userFirstName);
                answer = REACTION_TO_DISAGREEMENT_REGISTRATION(userFirstName);
                message = keyBoards.createKeyBoardForRegistration(chatId, answer);
                executeMessage(message);
            }

            case REGISTRATION_BUTTON -> {
                saveNewUserToDB(chatId, userFirstName);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАРЕГИСТРИРОВАЛСЯ", chatId, userFirstName);
                answer = REACTION_TO_SUCCESSFUL_REGISTRATION(userFirstName);
                message = buttons.createButtonsForChoiceShelter(chatId, answer);
                executeMessage(message);
            }

            // КНОПКИ ВЫБОРА ПРИЮТА

            case DOG_SHELTER_BUTTON -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ВЫБРАЛ ПРИЮТ ДЛЯ СОБАК", chatId, userFirstName);
                isDogShelter = true;
                answer = REACTION_TO_CHOICE_DOG_SHELTER(userFirstName);
                message = keyBoards.createKeyBoardGeneralStepsForDogShelter(chatId, answer);
                executeMessage(message);
            }

            case CAT_SHELTER_BUTTON -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ВЫБРАЛ ПРИЮТ ДЛЯ КОШЕК", chatId, userFirstName);
                isDogShelter = false;
                answer = REACTION_TO_CHOICE_CAT_SHELTER(userFirstName);
                message = keyBoards.createKeyBoardGeneralStepsForCatShelter(chatId, answer);
                executeMessage(message);
            }

            // КНОПКИ ИНФОРМАЦИИ О ПРИЮТЕ ДЛЯ СОБАК

            case INFO_DOG_SHELTER_BUTTON -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАПРОСИЛ ИНФОРМАЦИЮ О ПРИЮТЕ ДЛЯ СОБАК", chatId, userFirstName);
                answer = REACTION_TO_REQUEST(userFirstName);
                executeMessage(buttons.createButtonForGetDetailedInfoAboutDogShelter(chatId, answer));
            }

            case SCHEME_DRIVING_FOR_DOG_SHELTER_BUTTON -> {
                answer = REACTION_TO_SCHEME_DRIVING(userFirstName);
                sendPhoto(chatId, answer, PATH_FOR_DOG_SHELTER);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ СХЕМУ ПРОЕЗДА К ПРИЮТУ ДЛЯ СОБАК", chatId, userFirstName);
            }

            case DETAILED_INFO_FOR_DOG_SHELTER_PART_1_BUTTON -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАПРОСИЛ ДЕТАЛЬНУЮ ИНФОРМАЦИЮ О ПРИЮТЕ ДЛЯ СОБАК", chatId, userFirstName);
                answer = REACTION_TO_DETAILED_INFO(userFirstName);
                executeMessage(keyBoards.createKeyBoardForDetailedInfoAboutDogShelter(chatId, answer));
            }

            // КНОПКИ ИНФОРМАЦИИ О ПРИЮТЕ ДЛЯ КОШЕК

            case INFO_CAT_SHELTER_BUTTON -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАПРОСИЛ ИНФОРМАЦИЮ О ПРИЮТЕ ДЛЯ КОШЕК", chatId, userFirstName);
                answer = REACTION_TO_REQUEST(userFirstName);
                executeMessage(buttons.createButtonForGetDetailedInfoAboutCatShelter(chatId, answer));
            }

            case SCHEME_DRIVING_FOR_CAT_SHELTER_BUTTON -> {
                answer = REACTION_TO_SCHEME_DRIVING(userFirstName);
                sendPhoto(chatId, answer, PATH_FOR_CAT_SHELTER);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ СХЕМУ ПРОЕЗДА К ПРИЮТУ ДЛЯ КОШЕК", chatId, userFirstName);
            }

            case DETAILED_INFO_FOR_CAT_SHELTER_PART_1_BUTTON -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАПРОСИЛ ДЕТАЛЬНУЮ ИНФОРМАЦИЮ О ПРИЮТЕ ДЛЯ КОШЕК", chatId, userFirstName);
                answer = REACTION_TO_DETAILED_INFO(userFirstName);
                executeMessage(keyBoards.createKeyBoardForDetailedInfoAboutCatShelter(chatId, answer));
            }

            // КНОПКА ВЫЗОВА ВОЛОНТЕРА

            case CALL_VOLUNTEER_BUTTON -> {
                answer = REACTION_TO_CALL_VOLUNTEER(userFirstName);
                reactionToCommand(chatId, answer);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ НОМЕРА ВОЛОНТЕРОВ", chatId, userFirstName);
            }
        }
    }

    /**
     * Метод сохраняет нового пользователя {@link User} в БД
     *
     * @param chatId        <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param userFirstName <i> является именем пользователя </i>
     */
    private void saveNewUserToDB(Long chatId, String userFirstName) {
        User user = new User();

        user.setChatId(chatId);
        user.setFirstName(userFirstName);
        user.setRegisterAt(new Timestamp(System.currentTimeMillis()));

        userRepository.save(user);

        log.info("ДОБАВЛЕН НОВЫЙ ПОЛЬЗОВАТЕЛЬ: {} {}", chatId, userFirstName);
    }

    /**
     * Метод создает меню с основными командами: <br>
     *
     * <b>/start</b> <br>
     * <b>/help</b> <br>
     * <b>/settings</b> <br>
     */
    private void createMainMenu() {
        List<BotCommand> listOfCommands = new ArrayList<>();

        listOfCommands.add(new BotCommand(START, DESCRIPTION_START));
        listOfCommands.add(new BotCommand(HELP, DESCRIPTION_HELP));
        listOfCommands.add(new BotCommand(SETTINGS, DESCRIPTION_SETTINGS));

        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("ОШИБКА СОЗДАНИЯ ГЛАВНОГО МЕНЮ: {}", e.getMessage());
        }
    }

    /**
     * Метод отправляет изображение пользователю: <br>
     *
     * @param chatId  <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param caption <i> является подписью под картинкой для отправки пользователю </i>
     * @param path    <i> является путем к расположению изображения </i>
     */
    private void sendPhoto(Long chatId, String caption, String path) {
        File image;

        try {
            image = ResourceUtils.getFile(path);
        } catch (FileNotFoundException e) {
            log.error("ОШИБКА ПОИСКА ИЗОБРАЖЕНИЯ: {}", e.getMessage());
            throw new RuntimeException(e);
        }

        SendPhoto photo = new SendPhoto();

        photo.setPhoto(new InputFile(image));
        photo.setChatId(chatId);
        photo.setCaption(caption);

        try {
            execute(photo);
        } catch (TelegramApiException e) {
            log.error("ОШИБКА ОТПРАВКИ ИЗОБРАЖЕНИЯ: {}", e.getMessage());
        }
    }

    /**
     * Метод является ответной реакцией в виде текстового сообщения на действие пользователя
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param answer <i> является текстом для отправки пользователю </i>
     */
    private void reactionToCommand(Long chatId, String answer) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(answer);
        executeMessage(message);
    }

    /**
     * Метод отправляет текстовое сообщение пользователю
     *
     * @param message <i> является объектом класса {@link SendMessage} </i>
     */
    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("ОШИБКА ОТПРАВКИ ТЕКСТОВОГО СООБЩЕНИЯ: {}", e.getMessage());
        }
    }
}