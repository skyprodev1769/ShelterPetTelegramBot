package com.skypro.ShelterPetTelegramBot.service.impl.bot_service;

import com.skypro.ShelterPetTelegramBot.configuration.AppConfiguration;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Parent;
import com.skypro.ShelterPetTelegramBot.model.entity.without_controller.PotentialParent;
import com.skypro.ShelterPetTelegramBot.model.repository.ParentRepository;
import com.skypro.ShelterPetTelegramBot.model.repository.PotentialParentRepository;
import com.skypro.ShelterPetTelegramBot.model.repository.UserRepository;
import com.skypro.ShelterPetTelegramBot.service.interfaces.bot_service.BasicMethods;
import com.skypro.ShelterPetTelegramBot.service.interfaces.bot_service.CreatingButtons;
import com.skypro.ShelterPetTelegramBot.service.interfaces.bot_service.WorkInBot;
import com.skypro.ShelterPetTelegramBot.service.interfaces.bot_service.WorkInDataBase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.regex.Matcher;

import static com.skypro.ShelterPetTelegramBot.utils.answers.AnswersForBasicCommands.DEFAULT_REACTION_FOR_REGISTERED_USERS;
import static com.skypro.ShelterPetTelegramBot.utils.answers.AnswersForBasicCommands.DEFAULT_REACTION_FOR_UNREGISTERED_USERS;
import static com.skypro.ShelterPetTelegramBot.utils.answers.shelters.AnswersForReportPet.REACTION_TO_SAVE_ONLY_DOCUMENT;
import static com.skypro.ShelterPetTelegramBot.utils.answers.shelters.AnswersForReportPet.REACTION_TO_SAVE_ONLY_PHOTO;

/**
 * Класс {@link WorkInBotImpl}
 * является сервисным классом для выполнения работы телеграм бота
 */
@Slf4j
@Service
public class WorkInBotImpl implements WorkInBot {

    private final AppConfiguration configuration;
    private final BasicMethods methods;
    private final UserRepository userRepository;
    private final PotentialParentRepository potentialParentRepository;
    private final ParentRepository parentRepository;
    private final CreatingButtons buttons;
    private final WorkInDataBase base;
    private final GetReactionsImpl reactions;

    public WorkInBotImpl(AppConfiguration configuration,
                         BasicMethods methods,
                         UserRepository userRepository,
                         PotentialParentRepository potentialParentRepository,
                         ParentRepository parentRepository,
                         CreatingButtons buttons,
                         WorkInDataBase base,
                         GetReactionsImpl reactions) {

        this.methods = methods;
        this.configuration = configuration;
        this.userRepository = userRepository;
        this.potentialParentRepository = potentialParentRepository;
        this.parentRepository = parentRepository;
        this.buttons = buttons;
        this.base = base;
        this.reactions = reactions;
    }

    @Override
    public void work(Update update) {

        Long chatId;
        String userFirstName;

        if (update.hasMessage()) { // ЕСЛИ ПРИХОДИТ СООБЩЕНИЕ

            chatId = update.getMessage().getChatId();
            userFirstName = update.getMessage().getChat().getFirstName();
            Message message = update.getMessage();

            workWithUnregisteredUsers(chatId, userFirstName, message);

        } else if (update.hasCallbackQuery()) { // ЕСЛИ ПРИХОДИТ ОТКЛИК ОТ НАЖАТИЯ КНОПКИ

            chatId = update.getCallbackQuery().getMessage().getChatId();
            userFirstName = update.getCallbackQuery().getFrom().getFirstName();
            String callbackData = update.getCallbackQuery().getData();

            reactions.getReactionsToClickingButtons(chatId, userFirstName, callbackData);
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

        if (userRepository.findById(chatId).isEmpty()) { // ЕСЛИ ПОЛЬЗОВАТЕЛЬ РАНЕЕ НЕ РЕГИСТРИРОВАЛСЯ

            if (message.hasText()) { // ЕСЛИ ПРИХОДИТ ТЕКСТОВОЕ СООБЩЕНИЕ
                String text = message.getText();
                reactions.getReactionsForUnregisteredUsers(chatId, userFirstName, text);

            } else { // ЕСЛИ ПРИХОДИТ НЕ ТЕКСТОВОЕ СООБЩЕНИЕ
                String answer = DEFAULT_REACTION_FOR_UNREGISTERED_USERS(userFirstName);
                methods.reaction(chatId, answer);
            }

        } else { // ЕСЛИ ПОЛЬЗОВАТЕЛЬ УЖЕ ЗАРЕГИСТРИРОВАЛСЯ
            workWithRegisteredUsers(chatId, userFirstName, message);
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
            Matcher matcher = configuration.getPattern().matcher(text);

            if (matcher.matches()) { // ЕСЛИ СООБЩЕНИЕ СОДЕРЖИТ ФОРМУ ЗАПИСИ КОНТАКТНЫХ ДАННЫХ
                base.savePotentialParentToDB(chatId, userFirstName, matcher);
                return;
            }

            reactions.getReactionsForRegisteredUsers(chatId, userFirstName, text);

        } else { // ЕСЛИ ПРИХОДИТ НЕ ТЕКСТОВОЕ СООБЩЕНИЕ

            workWithParents(chatId, userFirstName, message);
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

                    workWithReports(chatId, userFirstName, message, parent);
                    return;
                }
            }
        }

        String answer = DEFAULT_REACTION_FOR_REGISTERED_USERS(userFirstName);
        SendMessage sendMessage = buttons.createButtonForCallVolunteer(chatId, answer);
        methods.sendText(sendMessage);
    }

    /**
     * Метод содержит логику работы для отчетов о животных
     *
     * @param chatId        <i> является идентификатором пользователя (его id в telegram) </i> <br>
     * @param userFirstName <i> является именем пользователя </i> <br>
     * @param message       <i> является получаемым сообщением </i> <br>
     * @param parent        <i> является именем пользователя </i> <br>
     */
    private void workWithReports(Long chatId, String userFirstName, Message message, Parent parent) {

        String answer;
        String photosDir = configuration.getPhotosDir();

        if (message.hasPhoto()) { // ЕСЛИ ПРИХОДИТ ФОТО

            List<PhotoSize> photos = message.getPhoto();
            base.saveReport(parent, photos, null, photosDir);

            answer = REACTION_TO_SAVE_ONLY_PHOTO(userFirstName);
            methods.reaction(chatId, answer);

        } else if (message.hasDocument()) { // ЕСЛИ ПРИХОДИТ ДОКУМЕНТ

            Document document = message.getDocument();
            base.saveReport(parent, null, document, photosDir);

            answer = REACTION_TO_SAVE_ONLY_DOCUMENT(userFirstName);
            methods.reaction(chatId, answer);

        } else { // ЕСЛИ ПРИХОДИТ НЕ ФОТО И НЕ ДОКУМЕНТ

            answer = DEFAULT_REACTION_FOR_REGISTERED_USERS(userFirstName);
            SendMessage sendMessage = buttons.createButtonForCallVolunteer(chatId, answer);
            methods.sendText(sendMessage);
        }
    }
}