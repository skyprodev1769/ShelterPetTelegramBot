package com.skypro.ShelterPetTelegramBot.service;

import com.skypro.ShelterPetTelegramBot.configuration.BotConfiguration;
import com.skypro.ShelterPetTelegramBot.model.User;
import com.skypro.ShelterPetTelegramBot.model.repository.PotentialParentRepository;
import com.skypro.ShelterPetTelegramBot.model.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private final ReactionOnBoardAndButton reaction;

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

}