package com.skypro.ShelterPetTelegramBot.service;

import com.skypro.ShelterPetTelegramBot.configuration.BotConfiguration;
import com.skypro.ShelterPetTelegramBot.model.PotentialParent;
import com.skypro.ShelterPetTelegramBot.model.User;
import com.skypro.ShelterPetTelegramBot.model.repository.PotentialParentRepository;
import com.skypro.ShelterPetTelegramBot.model.repository.UserRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.skypro.ShelterPetTelegramBot.utils.Answers.REACTION_TO_SUCCESSFUL_RECORD_CONTACT;
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
    @Lazy
    @Autowired
    final ReactionOnBoardAndButton reaction;

    @Lazy
    @Autowired
    protected CreateButtonAndKeyBoard create = null;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PotentialParentRepository parentRepository;

    private final static Pattern pattern = Pattern.compile("([\\W+]+)(\\s)([\\W+]+)(\\s)([0-9]{11})");

    public TelegramBot(BotConfiguration configuration, ReactionOnBoardAndButton reaction) {
        super(configuration.getToken());
        this.configuration = configuration;
        this.reaction = reaction;
        this.create = create;


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

                reaction.getReactionsForUnregisteredUsers(chatId, text, userFirstName);

            } else {

                Matcher matcher = pattern.matcher(text);

                if (parentRepository.findById(chatId).isEmpty() && matcher.matches()) {

                    create.savePotentialParentToDB(chatId, userFirstName, matcher);
                    return;
                }

                reaction.getReactionsForRegisteredUsers(chatId, text, userFirstName);
            }

        } else if (update.hasCallbackQuery()) {

            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            String callbackData = update.getCallbackQuery().getData();

            reaction.getReactionsForKeyBoardButtons(chatId, callbackData);
        }
    }


    /**
     * Метод {@code saveNewUserToDB(Long chatId, String userFirstName)} <br>
     * Сохраняет нового пользователя {@link User} в БД
     *
     * @param chatId        <i> является идентификатором пользователя (его id в telegram) </i>
     * @param userFirstName <i> является именем пользователя </i>
     */
    void saveNewUserToDB(Long chatId, String userFirstName) {
        User user = new User();

        user.setChatId(chatId);
        user.setFirstName(userFirstName);
        user.setRegisterAt(new Timestamp(System.currentTimeMillis()));

        userRepository.save(user);
    }
    /**
     * Метод {@code savePotentialParentToDB(Long chatId, String userFirstName, Matcher matcher)} <br>
     * Сохраняет потенциального усыновителя {@link PotentialParent} в БД
     *
     * @param chatId        <i> является идентификатором пользователя (его id в telegram) </i>
     * @param userFirstName <i> является именем пользователя </i>
     * @param matcher       <i> является объектом класса {@link Matcher}</i>
     */
    void savePotentialParentToDB(Long chatId, String userFirstName, Matcher matcher) {
        String firstName = matcher.group(1);
        String lastName = matcher.group(3);
        String phoneNumber = matcher.group(5);

        PotentialParent parent = new PotentialParent();

        parent.setFirstName(firstName);
        parent.setLastName(lastName);
        parent.setPhoneNumber(phoneNumber);

        parentRepository.save(parent);

        String answer = REACTION_TO_SUCCESSFUL_RECORD_CONTACT(userFirstName);
        create.createButtonInfoAboutProcess(chatId, answer);
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
     * Метод {@code sendMessage(Long chatId, String text)} <br>
     * Создает и возвращает новый объект типа {@link SendMessage}
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i>
     * @param text   <i> является текстом для отправки пользователю </i>
     */
    SendMessage sendMessage(Long chatId, String text) {
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
    void executeMessage(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("ERROR: {}", e.getMessage());
        }
    }
    /**
     * Метод {@code  sendDocument(long chatId,String caption, String documentPath)} <br>
     * отправляет документы пользователю
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i>
     * @param documentPath   <i> является адресом отправляемого документа  </i>
     */
    void sendDocument(long chatId, String caption, String documentPath) {
        SendDocument sendDocument = new SendDocument();
        sendDocument.setChatId(chatId);
        sendDocument.setCaption(caption);
        sendDocument.setDocument(transformsFilePathToInputFile(documentPath));
        try {
            execute(sendDocument);
        } catch (TelegramApiException e) {
            log.error("ERROR: {}", e.getMessage());
        }
    }
    /**
     * Метод {@code  sendFoto(long chatId,String caption, String fotoPath)} <br>
     * отправляет фотографии пользователю
     *
     * @param chatId <i> является идентификатором пользователя (его id в telegram) </i>
     * @param fotoPath   <i> является адресом отправляемой фотографии  </i>
     */

    void sendFoto(long chatId, String fotoPath){
        SendPhoto sendfoto = new SendPhoto();
        sendfoto.setChatId(chatId);
        sendfoto.setPhoto(transformsFilePathToInputFile(fotoPath));
        try {
            execute(sendfoto);
        } catch (TelegramApiException e) {
            log.error("ERROR: {}", e.getMessage());
        }
    }

    /**
     * Метод {@code  transformsFilePathToInputFile(String path)} <br>
     * преобразовывает ссылку на файл в файл формата InputFile
     * @SneakyThrows  это аннотация Lombok, которая используется для обработки функций с отмеченными исключениями
     *
     * @param path   <i> является адресом отправляемого файла  </i>
     */
    @SneakyThrows
    private InputFile transformsFilePathToInputFile(String path){
        File file = ResourceUtils.getFile("classpath:" + path);
        InputFile sendFile = new InputFile(file);
        return sendFile;
    }

}