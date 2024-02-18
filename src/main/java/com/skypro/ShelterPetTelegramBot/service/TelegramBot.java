package com.skypro.ShelterPetTelegramBot.service;

import com.skypro.ShelterPetTelegramBot.configuration.BotConfiguration;
import com.skypro.ShelterPetTelegramBot.model.PotentialParent;
import com.skypro.ShelterPetTelegramBot.model.User;
import com.skypro.ShelterPetTelegramBot.model.repository.PotentialParentRepository;
import com.skypro.ShelterPetTelegramBot.model.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.skypro.ShelterPetTelegramBot.utils.Answers.*;
import static com.skypro.ShelterPetTelegramBot.utils.Buttons.*;
import static com.skypro.ShelterPetTelegramBot.utils.Commands.*;
import static com.skypro.ShelterPetTelegramBot.utils.Descriptions.*;

/**
 * Класс {@link TelegramBot} является главным сервисным классом,
 * в котором происходит создание бота и обработка всех входящих {@code update}
 */
@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfiguration configuration;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PotentialParentRepository parentRepository;

    private final static Pattern pattern = Pattern.compile("([\\W+]+)(\\s)([\\W+]+)(\\s)([0-9]{11})");

    public TelegramBot(BotConfiguration configuration) {
        super(configuration.getToken());
        this.configuration = configuration;
        createMainMenu();
    }

    @Override
    public String getBotUsername() {
        return configuration.getName();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {

            Long chatId = update.getMessage().getChatId();
            String userFirstName = update.getMessage().getChat().getFirstName();
            String text = update.getMessage().getText();

            if (userRepository.findById(chatId).isEmpty()) {

                getReactionsForUnregisteredUsers(chatId, text, userFirstName);

            } else {

                Matcher matcher = pattern.matcher(text);

                if (parentRepository.findById(chatId).isEmpty() && matcher.matches()) {

                    savePotentialParentToDB(chatId, userFirstName, matcher);
                    return;
                }

                getReactionsForRegisteredUsers(chatId, text, userFirstName);
            }

        } else if (update.hasCallbackQuery()) {

            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            String callbackData = update.getCallbackQuery().getData();

            getReactionsForKeyBoardButtons(chatId, callbackData);
        }
    }

    /**
     * Метод {@code getReactionsForUnregisteredUsers(Long chatId, String text, String userFirstName)} <br>
     * Возвращает реакции на команды для <b>НЕЗАРЕГИСТРИРОВАННЫХ пользователей</b>
     *
     * @param chatId        <i> является идентификатором пользователя (его id в telegram) </i>
     * @param text          <i> является текстом для отправки пользователю </i>
     * @param userFirstName <i> является именем пользователя </i>
     */
    private void getReactionsForUnregisteredUsers(Long chatId, String text, String userFirstName) {
        String answer;

        switch (text) {

            case START:
                answer = REACTION_TO_FIRST_COMMAND_START(userFirstName);
                createButtonsForRegistration(chatId, answer);
                break;

            case HELP:
                answer = REACTION_TO_COMMAND_HELP_FOR_UNREGISTERED_USERS(userFirstName);
                reactionToCommand(chatId, answer);
                break;

            case SETTINGS:
                answer = REACTION_TO_COMMAND_SETTINGS(userFirstName);
                createKeyBoardForUnregisteredUsers(chatId, answer);
                break;

            case REGISTRATION:
                answer = REACTION_TO_SUCCESSFUL_REGISTRATION(userFirstName);
                saveNewUserToDB(chatId, userFirstName);
                createButtonInfoAboutShelter(chatId, answer);
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
                createButtonInfoAboutShelter(chatId, answer);
                break;

            case HELP:
                answer = REACTION_TO_COMMAND_HELP_FOR_REGISTERED_USERS(userFirstName);
                createButtonCallVolunteer(chatId, answer);
                break;

            case SETTINGS:
                answer = REACTION_TO_COMMAND_SETTINGS(userFirstName);
                createKeyBoardForRegisteredUsers(chatId, answer);
                break;

            /*
            КОМАНДЫ ДЛЯ ПОЛУЧЕНИЯ ИНФОРМАЦИИ О ПРИЮТЕ
             */

            case INFO_ABOUT_SHELTER:
                answer = REACTION_TO_REQUEST;
                createButtonInfoAboutProcess(chatId, answer);
                break;

            case INFO_ABOUT_WORK_SCHEDULE_AND_ADDRESS:
                answer = REACTION_TO_INFO_ABOUT_WORK_SCHEDULE_AND_ADDRESS;
                reactionToCommand(chatId, answer);
                break;

            case INFO_ABOUT_SECURITY_CONTACT_DETAILS:
                answer = REACTION_TO_INFO_ABOUT_SECURITY_CONTACT_DETAILS;
                reactionToCommand(chatId, answer);
                break;

            case INFO_ABOUT_GENERAL_SAFETY_RECOMMENDATION:
                answer = REACTION_TO_INFO_ABOUT_GENERAL_SAFETY_RECOMMENDATION;
                reactionToCommand(chatId, answer);
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
                createButtonCallVolunteer(chatId, answer);
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
                createButtonInfoAboutProcess(chatId, answer);
                break;

            case DETAILED_INFO_PART_1_BUTTON:
                answer = REACTION_TO_DETAILED_INFO;
                createKeyBoardForDetailedInfoAboutShelter(chatId, answer);
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
     * Метод {@code createKeyBoardForUnregisteredUsers(Long chatId, String text)} <br>
     * Создает интерактивную клавиатуру для выбора команд для <b>НЕЗАРЕГИСТРИРОВАННЫХ пользователей</b>
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i>
     * @param text   <i> является текстом для отправки пользователю </i>
     */
    private void createKeyBoardForUnregisteredUsers(Long chatId, String text) {
        SendMessage message = sendMessage(chatId, text);

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
        executeMessage(message);
    }

    /**
     * Метод {@code createKeyBoardForRegisteredUsers(Long chatId, String text)} <br>
     * Создает интерактивную клавиатуру для выбора команд для <b>ЗАРЕГИСТРИРОВАННЫХ пользователей</b>
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i>
     * @param text   <i> является текстом для отправки пользователю </i>
     */
    private void createKeyBoardForRegisteredUsers(Long chatId, String text) {
        SendMessage message = sendMessage(chatId, text);

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
        executeMessage(message);
    }

    /**
     * Метод {@code createKeyBoardForDetailedInfoAboutShelter(Long chatId, String text)} <br>
     * Создает интерактивную клавиатуру для выбора команд для получения подробной информации о приюте
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i>
     * @param text   <i> является текстом для отправки пользователю </i>
     */
    private void createKeyBoardForDetailedInfoAboutShelter(Long chatId, String text) {
        SendMessage message = sendMessage(chatId, text);

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
        executeMessage(message);
    }

    /**
     * Метод {@code createButtonsForRegistration(Long chatId, String text)} <br>
     * Создает кнопки выбора под сообщением для дальнейшей регистрации нового пользователя
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i>
     * @param text   <i> является текстом для отправки пользователю </i>
     */
    private void createButtonsForRegistration(Long chatId, String text) {
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
    private void createButtonInfoAboutShelter(Long chatId, String text) {
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
    private void createButtonInfoAboutProcess(Long chatId, String text) {
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
    private void createButtonCallVolunteer(Long chatId, String text) {
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
        SendMessage message = sendMessage(chatId, text);

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
        executeMessage(message);
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

        SendMessage message = sendMessage(chatId, text);

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
        executeMessage(message);
    }

    /**
     * Метод {@code savePotentialParentToDB(Long chatId, String userFirstName, Matcher matcher)} <br>
     * Сохраняет потенциального усыновителя {@link PotentialParent} в БД
     *
     * @param chatId        <i> является идентификатором пользователя (его id в telegram) </i>
     * @param userFirstName <i> является именем пользователя </i>
     * @param matcher       <i> является объектом класса {@link Matcher}</i>
     */
    private void savePotentialParentToDB(Long chatId, String userFirstName, Matcher matcher) {
        String firstName = matcher.group(1);
        String lastName = matcher.group(3);
        String phoneNumber = matcher.group(5);

        PotentialParent parent = new PotentialParent();

        parent.setFirstName(firstName);
        parent.setLastName(lastName);
        parent.setPhoneNumber(phoneNumber);

        parentRepository.save(parent);

        String answer = REACTION_TO_SUCCESSFUL_RECORD_CONTACT(userFirstName);
        createButtonInfoAboutProcess(chatId, answer);
    }

    /**
     * Метод {@code saveNewUserToDB(Long chatId, String userFirstName)} <br>
     * Сохраняет нового пользователя {@link User} в БД
     *
     * @param chatId        <i> является идентификатором пользователя (его id в telegram) </i>
     * @param userFirstName <i> является именем пользователя </i>
     */
    private void saveNewUserToDB(Long chatId, String userFirstName) {
        User user = new User();

        user.setChatId(chatId);
        user.setFirstName(userFirstName);
        user.setRegisterAt(new Timestamp(System.currentTimeMillis()));

        userRepository.save(user);
    }

    /**
     * Метод {@code createMainMenu()} <br>
     * Создает меню с основными командами: <br>
     * <br>
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
            log.error("ERROR: setting bot`s command list {}", e.getMessage());
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

    /**
     * Метод {@code sendMessage(Long chatId, String text)} <br>
     * Создает и возвращает новый объект типа {@link SendMessage}
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i>
     * @param text   <i> является текстом для отправки пользователю </i>
     */
    private SendMessage sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        return message;
    }

    /**
     * Метод {@code executeMessage(SendMessage message)} <br>
     * Отправляет объект типа {@link SendMessage} пользователю
     *
     * @param message <i> является отправляемым message </i>
     */
    private void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("ERROR: {}", e.getMessage());
        }
    }
}