package com.skypro.ShelterPetTelegramBot.service;

import com.skypro.ShelterPetTelegramBot.configuration.AppConfiguration;
import com.skypro.ShelterPetTelegramBot.configuration.BotConfiguration;
import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetStatus;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Parent;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Pet;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Shelter;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Volunteer;
import com.skypro.ShelterPetTelegramBot.model.entity.without_controller.PotentialParent;
import com.skypro.ShelterPetTelegramBot.model.entity.without_controller.Report;
import com.skypro.ShelterPetTelegramBot.model.entity.without_controller.User;
import com.skypro.ShelterPetTelegramBot.model.repository.ParentRepository;
import com.skypro.ShelterPetTelegramBot.model.repository.PotentialParentRepository;
import com.skypro.ShelterPetTelegramBot.model.repository.ReportRepository;
import com.skypro.ShelterPetTelegramBot.model.repository.UserRepository;
import com.skypro.ShelterPetTelegramBot.service.impl.bot_service.CreatingButtonsImpl;
import com.skypro.ShelterPetTelegramBot.service.impl.bot_service.CreatingKeyBoardsImpl;
import com.skypro.ShelterPetTelegramBot.service.impl.bot_service.RecordingContactsImpl;
import com.skypro.ShelterPetTelegramBot.service.interfaces.bot_service.CreatingButtons;
import com.skypro.ShelterPetTelegramBot.service.interfaces.bot_service.CreatingKeyBoards;
import com.skypro.ShelterPetTelegramBot.service.interfaces.bot_service.RecordingContacts;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.PetService;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.ShelterService;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.VolunteerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import static com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType.DOG;
import static com.skypro.ShelterPetTelegramBot.utils.Buttons.*;
import static com.skypro.ShelterPetTelegramBot.utils.Commands.*;
import static com.skypro.ShelterPetTelegramBot.utils.Descriptions.*;
import static com.skypro.ShelterPetTelegramBot.utils.answers.AnswersForBasicCommands.*;
import static com.skypro.ShelterPetTelegramBot.utils.answers.AnswersForChoiceShelterCommands.REACTION_TO_CHOICE_CAT_SHELTER;
import static com.skypro.ShelterPetTelegramBot.utils.answers.AnswersForChoiceShelterCommands.REACTION_TO_CHOICE_DOG_SHELTER;
import static com.skypro.ShelterPetTelegramBot.utils.answers.AnswersForGeneralCommands.*;
import static com.skypro.ShelterPetTelegramBot.utils.answers.AnswersForRegistrationCommands.*;
import static com.skypro.ShelterPetTelegramBot.utils.answers.contacts.AnswersForRecordContactsCommands.*;
import static com.skypro.ShelterPetTelegramBot.utils.answers.contacts.AnswersForRemovedContactsCommand.*;
import static com.skypro.ShelterPetTelegramBot.utils.answers.shelters.AnswersForInfoAboutAnyShelterCommands.*;
import static com.skypro.ShelterPetTelegramBot.utils.answers.shelters.AnswersForInfoAboutCatShelterCommands.REACTION_TO_INFO_ABOUT_SECURITY_CONTACT_DETAILS_FOR_CAT_SHELTER;
import static com.skypro.ShelterPetTelegramBot.utils.answers.shelters.AnswersForInfoAboutDogShelterCommands.REACTION_TO_INFO_ABOUT_SECURITY_CONTACT_DETAILS_FOR_DOG_SHELTER;
import static com.skypro.ShelterPetTelegramBot.utils.answers.shelters.AnswersForInfoAboutProcess.*;
import static com.skypro.ShelterPetTelegramBot.utils.answers.shelters.AnswersForReportPet.*;

/**
 * Класс {@link TelegramBot}
 * является основным сервисным классом, в котором происходит обработка всех входящих {@code update}
 */
@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfiguration botConfiguration;
    private final AppConfiguration appConfiguration;

    private final CreatingButtons buttons;
    private final CreatingKeyBoards keyBoards;
    private final RecordingContacts contacts;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PotentialParentRepository potentialParentRepository;
    @Autowired
    private ParentRepository parentRepository;
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private ShelterService shelterService;
    @Autowired
    private PetService petService;
    @Autowired
    private VolunteerService volunteerService;

    public TelegramBot(BotConfiguration botConfiguration, AppConfiguration appConfiguration) {
        super(botConfiguration.getToken());
        this.botConfiguration = botConfiguration;
        this.appConfiguration = appConfiguration;
        this.buttons = new CreatingButtonsImpl();
        this.keyBoards = new CreatingKeyBoardsImpl();
        this.contacts = new RecordingContactsImpl();
        createMainMenu();
    }

    @Override
    public String getBotUsername() {
        return botConfiguration.getName();
    }

    @Override
    public void onUpdateReceived(Update update) {

        Long chatId;
        String userFirstName;

        if (update.hasMessage()) { // ЕСЛИ ПРИХОДИТ СООБЩЕНИЕ

            chatId = update.getMessage().getChatId();
            userFirstName = update.getMessage().getChat().getFirstName();
            Message message = update.getMessage();

            if (userRepository.findById(chatId).isEmpty()) { // ЕСЛИ ПОЛЬЗОВАТЕЛЬ РАНЕЕ НЕ РЕГИСТРИРОВАЛСЯ

                workWithUnregisteredUsers(chatId, userFirstName, message);

            } else { // ЕСЛИ ПОЛЬЗОВАТЕЛЬ УЖЕ ЗАРЕГИСТРИРОВАЛСЯ

                workWithRegisteredUsers(chatId, userFirstName, message);
            }

        } else if (update.hasCallbackQuery()) { // ЕСЛИ ПРИХОДИТ ОТКЛИК ОТ НАЖАТИЯ КНОПКИ

            chatId = update.getCallbackQuery().getMessage().getChatId();
            userFirstName = update.getCallbackQuery().getFrom().getFirstName();
            String callbackData = update.getCallbackQuery().getData();

            getReactionsToClickingButtons(chatId, userFirstName, callbackData);
        }
    }

    /**
     * Метод содержит логику работы для <b> НЕЗАРЕГИСТРИРОВАННЫХ </b> пользователей
     *
     * @param chatId        <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param userFirstName <i> является именем пользователя </i> <br>
     * @param message       <i> является получаемым сообщением </i>
     */
    private void workWithUnregisteredUsers(Long chatId, String userFirstName, Message message) {

        if (message.hasText()) { // ЕСЛИ ПРИХОДИТ ТЕКСТОВОЕ СООБЩЕНИЕ
            String text = message.getText();
            getReactionsForUnregisteredUsers(chatId, userFirstName, text);

        } else { // ЕСЛИ ПРИХОДИТ НЕ ТЕКСТОВОЕ СООБЩЕНИЕ
            String answer = DEFAULT_REACTION_FOR_UNREGISTERED_USERS(userFirstName);
            reactionToCommand(chatId, answer);
        }
    }

    /**
     * Метод содержит логику работы для <b> ЗАРЕГИСТРИРОВАННЫХ </b> пользователей
     *
     * @param chatId        <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param userFirstName <i> является именем пользователя </i> <br>
     * @param message       <i> является получаемым сообщением </i>
     */
    private void workWithRegisteredUsers(Long chatId, String userFirstName, Message message) {

        if (message.hasText()) { // ЕСЛИ ПРИХОДИТ ТЕКСТОВОЕ СООБЩЕНИЕ

            String text = message.getText();
            Matcher matcher = appConfiguration.getPattern().matcher(text);

            if (matcher.matches()) { // ЕСЛИ ВХОДЯЩЕЕ СООБЩЕНИЕ СОДЕРЖИТ ФОРМУ ОТПРАВКИ КОНТАКТНЫХ ДАННЫХ
                savePotentialParentToDB(chatId, userFirstName, matcher);
                return;
            }

            getReactionsForRegisteredUsers(chatId, userFirstName, text);

        } else if (message.hasPhoto() | message.hasDocument()) { // ЕСЛИ ПРИХОДИТ ФОТО ИЛИ ДОКУМЕНТ

            workWithParents(chatId, userFirstName, message);

        } else { // ЕСЛИ ПРИХОДИТ НЕ ТЕКСТОВОЕ СООБЩЕНИЕ, НЕ ФОТО И НЕ ДОКУМЕНТ

            String answer = DEFAULT_REACTION_FOR_REGISTERED_USERS(userFirstName);
            SendMessage sendMessage = buttons.createButtonForCallVolunteer(chatId, answer);
            executeMessage(sendMessage);
        }
    }

    /**
     * Метод содержит логику работы для усыновителей
     *
     * @param chatId        <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param userFirstName <i> является именем пользователя </i> <br>
     * @param message       <i> является получаемым сообщением </i>
     */
    private void workWithParents(Long chatId, String userFirstName, Message message) {

        List<PotentialParent> parents = potentialParentRepository.getAllByChatId(chatId);

        if (!parents.isEmpty()) { // ЕСЛИ ПОЛЬЗОВАТЕЛЬ ОСТАВИЛ СВОИ КОНТАКТНЫЕ ДАННЫЕ

            for (PotentialParent el : parents) {

                String firstName = el.getFirstName();
                String lastName = el.getLastName();

                Parent parent = parentRepository.getByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCase(firstName, lastName);

                if (parent != null) { // ЕСЛИ ВОЛОНТЕР ЗАНЕС УСЫНОВИТЕЛЯ В БД

                    uploadReport(chatId, userFirstName, message, parent);
                    return;
                }
            }
        }

        String answer = DEFAULT_REACTION_FOR_REGISTERED_USERS(userFirstName);
        SendMessage sendMessage = buttons.createButtonForCallVolunteer(chatId, answer);
        executeMessage(sendMessage);
    }

    /**
     * Метод содержит логику работы для загрузки отчетов о животных
     *
     * @param chatId        <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param userFirstName <i> является именем пользователя </i> <br>
     * @param message       <i> является получаемым сообщением </i> <br>
     * @param parent        <i> является именем пользователя </i> <br>
     */
    private void uploadReport(Long chatId, String userFirstName, Message message, Parent parent) {
        String answer;

        if (message.hasPhoto()) { // ЕСЛИ ПРИХОДИТ ФОТО

            List<PhotoSize> photos = message.getPhoto();
            saveReport(parent, photos, null);

            answer = REACTION_TO_SAVE_ONLY_PHOTO(userFirstName);
            reactionToCommand(chatId, answer);

        } else if (message.hasDocument()) { // ЕСЛИ ПРИХОДИТ ДОКУМЕНТ

            Document document = message.getDocument();
            saveReport(parent, null, document);

            answer = REACTION_TO_SAVE_ONLY_DOCUMENT(userFirstName);
            reactionToCommand(chatId, answer);

        } else { // ЕСЛИ ПРИХОДИТ НЕ ФОТО И НЕ ДОКУМЕНТ

            answer = DEFAULT_REACTION_FOR_REGISTERED_USERS(userFirstName);
            SendMessage sendMessage = buttons.createButtonForCallVolunteer(chatId, answer);
            executeMessage(sendMessage);
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
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ СТАРТОВОЕ СООБЩЕНИЕ", chatId, userFirstName);
                answer = REACTION_TO_COMMAND_START_FOR_UNREGISTERED_USERS(userFirstName);
                message = buttons.createButtonsForChoiceRegistration(chatId, answer);
                executeMessage(message);
            }

            case HELP -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ ОПИСАТЕЛЬНОЕ СООБЩЕНИЕ", chatId, userFirstName);
                answer = REACTION_TO_COMMAND_HELP_FOR_UNREGISTERED_USERS(userFirstName);
                reactionToCommand(chatId, answer);
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
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ ДЕФОЛТНОЕ СООБЩЕНИЕ", chatId, userFirstName);
                answer = DEFAULT_REACTION_FOR_UNREGISTERED_USERS(userFirstName);
                reactionToCommand(chatId, answer);
            }
        }
    }

    /**
     * Метод возвращает реакции на команды для <b> ЗАРЕГИСТРИРОВАННЫХ </b> пользователей
     *
     * @param chatId        <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param userFirstName <i> является именем пользователя </i> <br>
     * @param text          <i> является полученным текстом от пользователя </i>
     */
    private void getReactionsForRegisteredUsers(Long chatId, String userFirstName, String text) {

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
                answer = REACTION_TO_CHANGED_SHELTER(userFirstName);
                message = keyBoards.createKeyBoardForChoiceShelter(chatId, answer);
                executeMessage(message);
            }

            // КОМАНДЫ ВЫБОРА ПРИЮТА

            case DOG_SHELTER -> {
                appConfiguration.setIsDogShelter(true);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ВЫБРАЛ ПРИЮТ ДЛЯ СОБАК", chatId, userFirstName);
                answer = REACTION_TO_CHOICE_DOG_SHELTER(userFirstName);
                message = keyBoards.createKeyBoardForGeneralInfo(chatId, answer);
                executeMessage(message);
            }

            case CAT_SHELTER -> {
                appConfiguration.setIsDogShelter(false);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ВЫБРАЛ ПРИЮТ ДЛЯ КОШЕК", chatId, userFirstName);
                answer = REACTION_TO_CHOICE_CAT_SHELTER(userFirstName);
                message = keyBoards.createKeyBoardForGeneralInfo(chatId, answer);
                executeMessage(message);
            }

            // КОМАНДЫ ИНФОРМАЦИИ О ПРИЮТЕ (ЭТАП 1)

            case INFO_ABOUT_SHELTER -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАПРОСИЛ ИНФОРМАЦИЮ О ПРИЮТЕ", chatId, userFirstName);
                answer = REACTION_TO_DETAILED_INFO(userFirstName);
                message = keyBoards.createKeyBoardForDetailedInfoAboutShelter(chatId, answer);
                executeMessage(message);
            }

            case INFO_ABOUT_WORK_SCHEDULE_AND_ADDRESS -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАПРОСИЛ АДРЕС И РЕЖИМ РАБОТЫ ПРИЮТА", chatId, userFirstName);
                answer = String.valueOf(sendAddressShelter(chatId, userFirstName));
                message = buttons.createButtonForSchemeDrivingToShelter(chatId, answer);
                executeMessage(message);
            }

            case INFO_ABOUT_SECURITY_CONTACT_DETAILS -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ НОМЕР ОХРАНЫ ПРИЮТА", chatId, userFirstName);

                if (appConfiguration.getIsDogShelter()) {
                    answer = REACTION_TO_INFO_ABOUT_SECURITY_CONTACT_DETAILS_FOR_DOG_SHELTER(userFirstName);

                } else {
                    answer = REACTION_TO_INFO_ABOUT_SECURITY_CONTACT_DETAILS_FOR_CAT_SHELTER(userFirstName);
                }

                reactionToCommand(chatId, answer);
            }

            case INFO_ABOUT_GENERAL_SAFETY_RECOMMENDATION -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ ИНФОРМАЦИЮ О ТРЕБОВАНИЯХ БЕЗОПАСНОСТИ НА ТЕРРИТОРИИ ПРИЮТА", chatId, userFirstName);
                answer = REACTION_TO_INFO_ABOUT_GENERAL_SAFETY_RECOMMENDATION(userFirstName);
                reactionToCommand(chatId, answer);
            }

            // КОМАНДЫ ИНФОРМАЦИИ О ПРОЦЕССЕ ПОЛУЧЕНИИ ЖИВОТНОГО (ЭТАП 2)

            case INFO_ABOUT_PROCESS -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАПРОСИЛ ИНФОРМАЦИЮ О ПРОЦЕССЕ ПОЛУЧЕНИЯ ЖИВОТНОГО", chatId, userFirstName);
                answer = REACTION_TO_DETAILED_INFO(userFirstName);

                if (appConfiguration.getIsDogShelter()) {
                    message = keyBoards.createKeyBoardForDetailedInfoAboutProcessForDog(chatId, answer);

                } else {
                    message = keyBoards.createKeyBoardForDetailedInfoAboutProcessForCat(chatId, answer);
                }

                executeMessage(message);
            }

            case LIST_PETS -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАПРОСИЛ СПИСОК ЖИВОТНЫХ ДЛЯ УСЫНОВЛЕНИЯ", chatId, userFirstName);
                answer = String.valueOf(sendListPets(chatId, userFirstName));
                reactionToCommand(chatId, answer);
            }

            case RULES_DATING -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ СПИСОК ПРАВИЛ ДЛЯ ЗНАКОМСТВА С ЖИВОТНЫМ", chatId, userFirstName);
                answer = REACTION_TO_RULES_DATING(userFirstName);
                reactionToCommand(chatId, answer);
            }

            case LIST_REQUIRED_DOCS -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ СПИСОК НЕОБХОДИМЫХ ДОКУМЕНТОВ", chatId, userFirstName);
                answer = REACTION_TO_LIST_REQUIRED_DOCS(userFirstName);
                reactionToCommand(chatId, answer);
            }

            case RECOMMENDATIONS_FOR_TRANSPORTATION -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ РЕКОМЕНДАЦИИ ПО ТРАНСПОРТИРОВКЕ ЖИВОТНОГО", chatId, userFirstName);
                answer = REACTION_TO_RECOMMENDATIONS_FOR_TRANSPORTATION(userFirstName);
                reactionToCommand(chatId, answer);
            }

            case RECOMMENDATIONS_FOR_HOME_IMPROVEMENT_FOR_YOUNG_PET -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ РЕКОМЕНДАЦИИ ПО ОБУСТРОЙСТВУ ДОМА ДЛЯ МАЛЕНЬКОГО ЖИВОТНОГО", chatId, userFirstName);
                answer = REACTION_TO_RECOMMENDATIONS_FOR_HOME_IMPROVEMENT_FOR_YOUNG_PET(userFirstName);
                reactionToCommand(chatId, answer);
            }

            case RECOMMENDATIONS_FOR_HOME_IMPROVEMENT_FOR_ADULT_PET -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ РЕКОМЕНДАЦИИ ПО ОБУСТРОЙСТВУ ДОМА ДЛЯ ВЗРОСЛОГО ЖИВОТНОГО", chatId, userFirstName);
                answer = REACTION_TO_RECOMMENDATIONS_FOR_HOME_IMPROVEMENT_FOR_ADULT_PET(userFirstName);
                reactionToCommand(chatId, answer);
            }

            case RECOMMENDATIONS_FOR_HOME_IMPROVEMENT_FOR_DISABLED_PET -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ РЕКОМЕНДАЦИИ ПО ОБУСТРОЙСТВУ ДОМА ДЛЯ ЖИВОТНОГО С ОГРАНИЧЕННЫМИ ВОЗМОЖНОСТЯМИ", chatId, userFirstName);
                answer = REACTION_TO_RECOMMENDATIONS_FOR_HOME_IMPROVEMENT_FOR_DISABLED_PET(userFirstName);
                reactionToCommand(chatId, answer);
            }

            case RECOMMENDATIONS_FROM_DOG_HANDLER -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ СОВЕТЫ КИНОЛОГА ПО ПЕРВИЧНОМУ ОБЩЕНИЮ С СОБАКОЙ", chatId, userFirstName);
                answer = REACTION_TO_RECOMMENDATIONS_FROM_DOG_HANDLER(userFirstName);
                reactionToCommand(chatId, answer);
            }

            case RECOMMENDATIONS_FOR_TRUSTED_DOG_HANDLER -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ РЕКОМЕНДАЦИИ ПО ПРОВЕРЕННЫМ КИНОЛОГАМ", chatId, userFirstName);
                answer = REACTION_TO_RECOMMENDATIONS_FOR_TRUSTED_DOG_HANDLER(userFirstName);
                reactionToCommand(chatId, answer);
            }

            case LIST_REASONS_FOR_REFUSAL -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ СПИСОК ПРИЧИН ДЛЯ ОТКАЗА", chatId, userFirstName);
                answer = REACTION_TO_LIST_REASONS_FOR_REFUSAL(userFirstName);
                reactionToCommand(chatId, answer);
            }

            // КОМАНДЫ ИНФОРМАЦИИ ОБ ОТПРАВКЕ ОТЧЕТА О ЖИВОТНОМ (ЭТАП 3)

            case REPORT_ABOUT_PET -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАПРОСИЛ ИНФОРМАЦИЮ ОБ ОТПРАВКЕ ОТЧЕТА О ЖИВОТНОМ", chatId, userFirstName);
                answer = REACTION_TO_DETAILED_INFO(userFirstName);
                message = keyBoards.createKeyBoardForReportPet(chatId, answer);
                executeMessage(message);
            }

            case PATTERN_REPORT -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ ШАБЛОН ОТЧЕТА О ЖИВОТНОМ", chatId, userFirstName);
                answer = REACTION_TO_PATTERN_REPORT(userFirstName);
                reactionToCommand(chatId, answer);
            }

            // КОМАНДА ЗАПИСИ КОНТАКТНЫХ ДАННЫХ

            case RECORD_CONTACT_DETAILS -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ ФОРМУ ДЛЯ ОТПРАВКИ КОНТАКТНЫХ ДАННЫХ", chatId, userFirstName);
                answer = REACTION_TO_RECORD_CONTACT_DETAILS(userFirstName);
                reactionToCommand(chatId, answer);
            }

            // КОМАНДА УДАЛЕНИЯ КОНТАКТНЫХ ДАННЫХ

            case REMOVE_CONTACT_DETAILS -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАПРОСИЛ УДАЛЕНИЕ КОНТАКТНЫХ ДАННЫХ", chatId, userFirstName);
                answer = REACTION_TO_REQUEST_TO_REMOVED_CONTACTS_DETAILS(userFirstName);
                message = buttons.createButtonForRemoveContactDetails(chatId, answer);
                executeMessage(message);
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
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАПРОСИЛ КОНТАКТЫ ВОЛОНТЕРОВ", chatId, userFirstName);
                answer = String.valueOf(sendListVolunteers(chatId, userFirstName));
                reactionToCommand(chatId, answer);
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
                appConfiguration.setIsDogShelter(true);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ВЫБРАЛ ПРИЮТ ДЛЯ СОБАК", chatId, userFirstName);
                answer = REACTION_TO_CHOICE_DOG_SHELTER(userFirstName);
                message = keyBoards.createKeyBoardForGeneralInfo(chatId, answer);
                executeMessage(message);
            }

            case CAT_SHELTER_BUTTON -> {
                appConfiguration.setIsDogShelter(false);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ВЫБРАЛ ПРИЮТ ДЛЯ КОШЕК", chatId, userFirstName);
                answer = REACTION_TO_CHOICE_CAT_SHELTER(userFirstName);
                message = keyBoards.createKeyBoardForGeneralInfo(chatId, answer);
                executeMessage(message);
            }

            // КНОПКА СХЕМЫ ПРОЕЗДА

            case SCHEME_DRIVING_BUTTON -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ СХЕМУ ПРОЕЗДА К ПРИЮТУ", chatId, userFirstName);
                answer = REACTION_TO_SCHEME_DRIVING(userFirstName);

                if (appConfiguration.getIsDogShelter()) {
                    sendImage(chatId, answer, appConfiguration.getPathForDogShelter());

                } else {
                    sendImage(chatId, answer, appConfiguration.getPathForCatShelter());
                }
            }

            // КНОПКА УДАЛЕНИЯ КОНТАКТНЫХ ДАННЫХ

            case REMOVE_BUTTON -> {
                removePotentialParentFromDB(chatId, userFirstName);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} УДАЛИЛ КОНТАКТНЫЕ ДАННЫЕ", chatId, userFirstName);
            }

            // КНОПКА ВЫЗОВА ВОЛОНТЕРА

            case CALL_VOLUNTEER_BUTTON -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАПРОСИЛ КОНТАКТЫ ВОЛОНТЕРОВ", chatId, userFirstName);
                answer = String.valueOf(sendListVolunteers(chatId, userFirstName));
                reactionToCommand(chatId, answer);
            }
        }
    }

    /**
     * Метод сохраняет отчет о животном {@link Report} в БД
     *
     * @param parent   <i> является усыновителем, отправившим отчет </i> <br>
     * @param photos   <i> являются фото, отправляемыми усыновителем </i> <br>
     * @param document <i> является документом, отправляемым усыновителем </i>
     */
    public void saveReport(Parent parent, List<PhotoSize> photos, Document document) {

        String firstName = parent.getFirstName();
        String lastName = parent.getLastName();

        String firstPartFileName = firstName + "_" + lastName + "_" + LocalDate.now();
        String parentDir = appConfiguration.getPhotosDir() + "/" + firstName + "_" + lastName + "_" + parent.getPet().getName();

        Report report = reportRepository.getByDate(LocalDate.now()).orElseGet(Report::new);

        if (photos != null) {
            savePhoto(photos, firstPartFileName, parentDir, report);
        }

        if (document != null) {
            saveDocument(document, firstPartFileName, parentDir, report);
        }

        report.setDate(LocalDate.now());
        report.setParent(parent);
        reportRepository.save(report);
    }

    /**
     * Метод сохраняет фото животного в отчет о животном
     *
     * @param photos            <i> являются фото, отправляемыми усыновителем </i> <br>
     * @param firstPartFileName <i> является первой частью названия фото </i> <br>
     * @param parentDir         <i> является директорией сохранения фото </i> <br>
     * @param report            <i> является отчетом о животном </i>
     */
    private void savePhoto(List<PhotoSize> photos, String firstPartFileName, String parentDir, Report report) {

        PhotoSize photo = photos.get(photos.size() - 1);
        GetFile getFile = new GetFile(photo.getFileId());

        String fileName = firstPartFileName + ".png";
        File file = new File(parentDir, fileName);

        try {
            downloadFile(execute(getFile), file);
        } catch (TelegramApiException e) {
            log.error("ОШИБКА СОХРАНЕНИЯ ИЗОБРАЖЕНИЯ: {}", e.getMessage());
        }

        report.setPhoto(fileName);
        log.info("ДОБАВЛЕНО ФОТО {} В ОТЧЕТ О ЖИВОТНОМ {}", fileName, LocalDate.now());
    }

    /**
     * Метод сохраняет документ с описательной частью в отчет о животном
     *
     * @param document          <i> является документом, отправляемым усыновителем </i> <br>
     * @param firstPartFileName <i> является первой частью названия фото </i> <br>
     * @param parentDir         <i> является директорией сохранения фото </i> <br>
     * @param report            <i> является отчетом о животном </i>
     */
    private void saveDocument(Document document, String firstPartFileName, String parentDir, Report report) {

        GetFile getFile = new GetFile(document.getFileId());

        String fileName = firstPartFileName + ".docx";
        File file = new File(parentDir, fileName);

        try {
            downloadFile(execute(getFile), file);
        } catch (TelegramApiException e) {
            log.error("ОШИБКА СОХРАНЕНИЯ ДОКУМЕНТА: {}", e.getMessage());
        }

        report.setDocument(fileName);
        log.info("ДОБАВЛЕН ДОКУМЕНТ {} В ОТЧЕТ О ЖИВОТНОМ {}", fileName, LocalDate.now());
    }

    /**
     * Метод возвращает, в зависимости от выбранного типа, список животных для усыновления из БД
     *
     * @param chatId        <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param userFirstName <i> является именем пользователя </i>
     */
    private StringBuilder sendListPets(Long chatId, String userFirstName) {

        StringBuilder dogs = new StringBuilder(REACTION_TO_LIST_PETS(userFirstName) + "\n\n");
        StringBuilder cats = new StringBuilder(REACTION_TO_LIST_PETS(userFirstName) + "\n\n");

        for (Pet pet : petService.getAll()) {

            String data = String.format("%s\nПриют - %s\n", pet.getName(), pet.getShelter().getAddress());

            if (pet.getStatus() == PetStatus.FREE) {

                if (pet.getType() == DOG) {
                    dogs.append(data).append("\n");

                } else {
                    cats.append(data).append("\n");
                }
            }
        }

        if (appConfiguration.getIsDogShelter()) {
            log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ СПИСОК СОБАК ДЛЯ УСЫНОВЛЕНИЯ", chatId, userFirstName);
            return dogs;

        } else {
            log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ СПИСОК КОШЕК ДЛЯ УСЫНОВЛЕНИЯ", chatId, userFirstName);
            return cats;
        }
    }

    /**
     * Метод возвращает список контактов волонтеров из БД
     *
     * @param chatId        <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param userFirstName <i> является именем пользователя </i>
     */
    private StringBuilder sendListVolunteers(Long chatId, String userFirstName) {

        StringBuilder list = new StringBuilder(REACTION_TO_CALL_VOLUNTEER(userFirstName)).append("\n\n");

        for (Volunteer volunteer : volunteerService.getAll()) {

            String data = String.format("%s %s\nПриют - %s\n", volunteer.getPhoneNumber(), volunteer.getFirstName(), volunteer.getShelter().getAddress());
            list.append(data).append("\n");
        }

        log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ КОНТАКТЫ ВОЛОНТЕРОВ", chatId, userFirstName);
        return list;
    }

    /**
     * Метод возвращает список адресов приютов из БД
     *
     * @param chatId        <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param userFirstName <i> является именем пользователя </i>
     */
    private StringBuilder sendAddressShelter(Long chatId, String userFirstName) {

        StringBuilder cats = new StringBuilder(REACTION_TO_INFO_ABOUT_WORK_SCHEDULE_AND_ADDRESS(userFirstName) + "\n\n");
        StringBuilder dogs = new StringBuilder(REACTION_TO_INFO_ABOUT_WORK_SCHEDULE_AND_ADDRESS(userFirstName) + "\n\n");

        for (Shelter shelter : shelterService.getAll()) {

            if (shelter.getType() == DOG) {
                dogs.append(shelter.getAddress()).append("\n");

            } else {
                cats.append(shelter.getAddress()).append("\n");
            }
        }

        if (appConfiguration.getIsDogShelter()) {
            log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ АДРЕС И РЕЖИМ РАБОТЫ ПРИЮТА ДЛЯ СОБАК", chatId, userFirstName);
            return dogs;

        } else {
            log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ АДРЕС И РЕЖИМ РАБОТЫ ПРИЮТА ДЛЯ КОШЕК", chatId, userFirstName);
            return cats;
        }
    }

    /**
     * Метод сохраняет контакты потенциального усыновителя {@link PotentialParent} в БД
     *
     * @param chatId        <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param userFirstName <i> является именем пользователя </i> <br>
     * @param matcher       <i> является объектом класса {@link Matcher} </i>
     */
    private void savePotentialParentToDB(Long chatId, String userFirstName, Matcher matcher) {
        String answer;

        PotentialParent parent = contacts.recordContact(chatId, matcher);
        Long id = Long.valueOf(parent.getPhoneNumber());

        if (potentialParentRepository.findById(id).isEmpty()) { // ЕСЛИ ПОЛЬЗОВАТЕЛЬ РАНЕЕ НЕ ОТПРАВЛЯЛ КОНТАКТНЫЕ ДАННЫЕ

            potentialParentRepository.save(parent);
            log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАПИСАЛСЯ КАК ПОТЕНЦИАЛЬНЫЙ УСЫНОВИТЕЛЬ", chatId, userFirstName);
            answer = REACTION_TO_SUCCESSFUL_RECORD_CONTACT(userFirstName);
            SendMessage message = keyBoards.createKeyBoardForGeneralInfo(chatId, answer);
            executeMessage(message);

        } else { // ЕСЛИ ПОЛЬЗОВАТЕЛЬ УЖЕ ОТПРАВЛЯЛ КОНТАКТНЫЕ ДАННЫЕ

            log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОПЫТАЛСЯ ПОВТОРНО ОТПРАВИТЬ НОМЕР ТЕЛЕФОНА", chatId, userFirstName);
            answer = REACTION_TO_REPEAT_RECORD_CONTACT(userFirstName);
            reactionToCommand(chatId, answer);
        }
    }

    /**
     * Метод стирает все ранее переданные контакты усыновителя {@link PotentialParent} из БД
     *
     * @param chatId        <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param userFirstName <i> является именем пользователя </i>
     */
    private void removePotentialParentFromDB(Long chatId, String userFirstName) {
        String answer;

        List<PotentialParent> parent = potentialParentRepository.getAllByChatId(chatId);

        if (parent.isEmpty()) { // ЕСЛИ ПОЛЬЗОВАТЕЛЬ НЕ ОТПРАВЛЯЛ РАНЕЕ КОНТАКТНЫЕ ДАННЫЕ

            log.info("НЕ НАЙДЕНЫ КОНТАКТНЫЕ ДАННЫЕ ПОЛЬЗОВАТЕЛЯ: {} {}", chatId, userFirstName);
            answer = REACTION_TO_REMOVED_EMPTY_CONTACTS_DETAILS(userFirstName);
            reactionToCommand(chatId, answer);

        } else { // ЕСЛИ ПОЛЬЗОВАТЕЛЬ ОТПРАВЛЯЛ РАНЕЕ КОНТАКТНЫЕ ДАННЫЕ

            potentialParentRepository.deleteAll();
            log.info("УДАЛЕНЫ КОНТАКТНЫЕ ДАННЫЕ ПОЛЬЗОВАТЕЛЯ: {} {}", chatId, userFirstName);
            answer = REACTION_TO_REMOVED_CONTACTS_DETAILS(userFirstName);
            SendMessage message = keyBoards.createKeyBoardForGeneralInfo(chatId, answer);
            executeMessage(message);
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
     * <b>/settings</b>
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
     * @param caption <i> является подписью под картинкой для отправки пользователю </i> <br>
     * @param path    <i> является путем к расположению изображения </i>
     */
    private void sendImage(Long chatId, String caption, String path) {
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