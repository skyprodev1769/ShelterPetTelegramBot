package com.skypro.ShelterPetTelegramBot.service.impl.bot_service;

import com.skypro.ShelterPetTelegramBot.configuration.AppConfiguration;
import com.skypro.ShelterPetTelegramBot.service.interfaces.bot_service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static com.skypro.ShelterPetTelegramBot.utils.Buttons.*;
import static com.skypro.ShelterPetTelegramBot.utils.Commands.*;
import static com.skypro.ShelterPetTelegramBot.utils.answers.AnswersForBasicCommands.*;
import static com.skypro.ShelterPetTelegramBot.utils.answers.AnswersForChoiceShelterCommands.REACTION_TO_CHOICE_CAT_SHELTER;
import static com.skypro.ShelterPetTelegramBot.utils.answers.AnswersForChoiceShelterCommands.REACTION_TO_CHOICE_DOG_SHELTER;
import static com.skypro.ShelterPetTelegramBot.utils.answers.AnswersForGeneralCommands.REACTION_TO_CHANGED_SHELTER;
import static com.skypro.ShelterPetTelegramBot.utils.answers.AnswersForGeneralCommands.REACTION_TO_DETAILED_INFO;
import static com.skypro.ShelterPetTelegramBot.utils.answers.AnswersForRegistrationCommands.*;
import static com.skypro.ShelterPetTelegramBot.utils.answers.contacts.AnswersForRecordContactsCommands.REACTION_TO_RECORD_CONTACT_DETAILS;
import static com.skypro.ShelterPetTelegramBot.utils.answers.contacts.AnswersForRemovedContactsCommand.REACTION_TO_REQUEST_TO_REMOVED_CONTACTS_DETAILS;
import static com.skypro.ShelterPetTelegramBot.utils.answers.shelters.AnswersForInfoAboutAnyShelterCommands.REACTION_TO_INFO_ABOUT_GENERAL_SAFETY_RECOMMENDATION;
import static com.skypro.ShelterPetTelegramBot.utils.answers.shelters.AnswersForInfoAboutAnyShelterCommands.REACTION_TO_SCHEME_DRIVING;
import static com.skypro.ShelterPetTelegramBot.utils.answers.shelters.AnswersForInfoAboutCatShelterCommands.REACTION_TO_INFO_ABOUT_SECURITY_CONTACT_DETAILS_FOR_CAT_SHELTER;
import static com.skypro.ShelterPetTelegramBot.utils.answers.shelters.AnswersForInfoAboutDogShelterCommands.REACTION_TO_INFO_ABOUT_SECURITY_CONTACT_DETAILS_FOR_DOG_SHELTER;
import static com.skypro.ShelterPetTelegramBot.utils.answers.shelters.AnswersForInfoAboutProcess.*;
import static com.skypro.ShelterPetTelegramBot.utils.answers.shelters.AnswersForReportPet.REACTION_TO_PATTERN_REPORT;

/**
 * Класс {@link GetReactionsImpl}
 * является сервисным классом для получения ответных реакций на действие пользователя
 */
@Slf4j
@Service
public class GetReactionsImpl implements GetReactions {


    private final AppConfiguration configuration;
    private final BasicMethods methods;
    private final CreatingKeyBoards keyBoards;
    private final CreatingButtons buttons;
    private final WorkInDataBase base;
    private final SendingLists lists;

    public GetReactionsImpl(AppConfiguration configuration,
                            BasicMethods methods,
                            CreatingKeyBoards keyBoards,
                            CreatingButtons buttons,
                            WorkInDataBase base,
                            SendingLists lists) {

        this.configuration = configuration;
        this.methods = methods;
        this.keyBoards = keyBoards;
        this.buttons = buttons;
        this.base = base;
        this.lists = lists;
    }

    @Override
    public void getReactionsForUnregisteredUsers(Long chatId, String userFirstName, String text) {

        String answer;
        SendMessage message;

        switch (text) {

            case START -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ СТАРТОВОЕ СООБЩЕНИЕ", chatId, userFirstName);
                answer = REACTION_TO_COMMAND_START_FOR_UNREGISTERED_USERS(userFirstName);
                message = buttons.createButtonsForChoiceRegistration(chatId, answer);
                methods.sendText(message);
            }

            case HELP -> {
                answer = REACTION_TO_COMMAND_HELP_FOR_UNREGISTERED_USERS(userFirstName);
                methods.reaction(chatId, answer);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ ОПИСАТЕЛЬНОЕ СООБЩЕНИЕ", chatId, userFirstName);
            }

            case SETTINGS -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАПРОСИЛ КЛАВИАТУРУ ДЛЯ РЕГИСТРАЦИИ", chatId, userFirstName);
                answer = REACTION_TO_COMMAND_SETTINGS(userFirstName);
                message = keyBoards.createKeyBoardForRegistration(chatId, answer);
                methods.sendText(message);
            }

            case REGISTRATION -> {
                base.saveNewUserToDB(chatId, userFirstName);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАРЕГИСТРИРОВАЛСЯ", chatId, userFirstName);
                answer = REACTION_TO_SUCCESSFUL_REGISTRATION(userFirstName);
                message = buttons.createButtonsForChoiceShelter(chatId, answer);
                methods.sendText(message);
            }

            default -> {
                answer = DEFAULT_REACTION_FOR_UNREGISTERED_USERS(userFirstName);
                methods.reaction(chatId, answer);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ ДЕФОЛТНОЕ СООБЩЕНИЕ", chatId, userFirstName);
            }
        }
    }

    @Override
    public void getReactionsForRegisteredUsers(Long chatId, String userFirstName, String text) {

        String answer;
        SendMessage message;

        switch (text) {

            // БАЗОВЫЕ КОМАНДЫ

            case START -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ СТАРТОВОЕ СООБЩЕНИЕ", chatId, userFirstName);
                answer = REACTION_TO_COMMAND_START_FOR_REGISTERED_USERS(userFirstName);
                message = buttons.createButtonsForChoiceShelter(chatId, answer);
                methods.sendText(message);
            }

            case HELP -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ ОПИСАТЕЛЬНОЕ СООБЩЕНИЕ", chatId, userFirstName);
                answer = REACTION_TO_COMMAND_HELP_FOR_REGISTERED_USERS(userFirstName);
                message = buttons.createButtonForCallVolunteer(chatId, answer);
                methods.sendText(message);
            }

            case SETTINGS -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАПРОСИЛ КЛАВИАТУРУ ДЛЯ ВЫБОРА ПРИЮТА", chatId, userFirstName);
                answer = REACTION_TO_CHANGED_SHELTER(userFirstName);
                message = keyBoards.createKeyBoardForChoiceShelter(chatId, answer);
                methods.sendText(message);
            }

            // КОМАНДЫ ВЫБОРА ПРИЮТА

            case DOG_SHELTER -> {
                configuration.setIsDogShelter(true);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ВЫБРАЛ ПРИЮТ ДЛЯ СОБАК", chatId, userFirstName);
                answer = REACTION_TO_CHOICE_DOG_SHELTER(userFirstName);
                message = keyBoards.createKeyBoardForGeneralInfo(chatId, answer);
                methods.sendText(message);
            }

            case CAT_SHELTER -> {
                configuration.setIsDogShelter(false);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ВЫБРАЛ ПРИЮТ ДЛЯ КОШЕК", chatId, userFirstName);
                answer = REACTION_TO_CHOICE_CAT_SHELTER(userFirstName);
                message = keyBoards.createKeyBoardForGeneralInfo(chatId, answer);
                methods.sendText(message);
            }

            // КОМАНДЫ ИНФОРМАЦИИ О ПРИЮТЕ (ЭТАП 1)

            case INFO_ABOUT_SHELTER -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАПРОСИЛ ИНФОРМАЦИЮ О ПРИЮТЕ", chatId, userFirstName);
                answer = REACTION_TO_DETAILED_INFO(userFirstName);
                message = keyBoards.createKeyBoardForDetailedInfoAboutShelter(chatId, answer);
                methods.sendText(message);
            }

            case INFO_ABOUT_WORK_SCHEDULE_AND_ADDRESS -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАПРОСИЛ АДРЕС И РЕЖИМ РАБОТЫ ПРИЮТА", chatId, userFirstName);
                answer = String.valueOf(lists.sendListShelters(chatId, userFirstName, configuration.getIsDogShelter()));
                message = buttons.createButtonForSchemeDrivingToShelter(chatId, answer);
                methods.sendText(message);
            }

            case INFO_ABOUT_SECURITY_CONTACT_DETAILS -> {

                if (configuration.getIsDogShelter()) {
                    answer = REACTION_TO_INFO_ABOUT_SECURITY_CONTACT_DETAILS_FOR_DOG_SHELTER(userFirstName);
                    log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ НОМЕР ОХРАНЫ ПРИЮТА ДЛЯ СОБАК", chatId, userFirstName);

                } else {
                    answer = REACTION_TO_INFO_ABOUT_SECURITY_CONTACT_DETAILS_FOR_CAT_SHELTER(userFirstName);
                    log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ НОМЕР ОХРАНЫ ПРИЮТА ДЛЯ КОШЕК", chatId, userFirstName);
                }

                methods.reaction(chatId, answer);
            }

            case INFO_ABOUT_GENERAL_SAFETY_RECOMMENDATION -> {
                answer = REACTION_TO_INFO_ABOUT_GENERAL_SAFETY_RECOMMENDATION(userFirstName);
                methods.reaction(chatId, answer);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ ИНФОРМАЦИЮ О ТРЕБОВАНИЯХ БЕЗОПАСНОСТИ НА ТЕРРИТОРИИ ПРИЮТА", chatId, userFirstName);
            }

            // КОМАНДЫ ИНФОРМАЦИИ О ПРОЦЕССЕ ПОЛУЧЕНИИ ЖИВОТНОГО (ЭТАП 2)

            case INFO_ABOUT_PROCESS -> {
                answer = REACTION_TO_DETAILED_INFO(userFirstName);

                if (configuration.getIsDogShelter()) {
                    message = keyBoards.createKeyBoardForDetailedInfoAboutProcessForDog(chatId, answer);
                    log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ ИНФОРМАЦИЮ О ПРОЦЕССЕ УСЫНОВЛЕНИЯ СОБАКИ", chatId, userFirstName);

                } else {
                    message = keyBoards.createKeyBoardForDetailedInfoAboutProcessForCat(chatId, answer);
                    log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ ИНФОРМАЦИЮ О ПРОЦЕССЕ УСЫНОВЛЕНИЯ КОШКИ", chatId, userFirstName);
                }

                methods.sendText(message);
            }

            case LIST_PETS -> {
                answer = String.valueOf(lists.sendListPets(chatId, userFirstName, configuration.getIsDogShelter()));
                methods.reaction(chatId, answer);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ СПИСОК ЖИВОТНЫХ ДЛЯ УСЫНОВЛЕНИЯ", chatId, userFirstName);
            }

            case RULES_DATING -> {
                answer = REACTION_TO_RULES_DATING(userFirstName);
                methods.reaction(chatId, answer);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ СПИСОК ПРАВИЛ ДЛЯ ЗНАКОМСТВА С ЖИВОТНЫМ", chatId, userFirstName);
            }

            case LIST_REQUIRED_DOCS -> {
                answer = REACTION_TO_LIST_REQUIRED_DOCS(userFirstName);
                methods.reaction(chatId, answer);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ СПИСОК НЕОБХОДИМЫХ ДОКУМЕНТОВ", chatId, userFirstName);
            }

            case RECOMMENDATIONS_FOR_TRANSPORTATION -> {
                answer = REACTION_TO_RECOMMENDATIONS_FOR_TRANSPORTATION(userFirstName);
                methods.reaction(chatId, answer);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ РЕКОМЕНДАЦИИ ПО ТРАНСПОРТИРОВКЕ ЖИВОТНОГО", chatId, userFirstName);
            }

            case RECOMMENDATIONS_FOR_HOME_IMPROVEMENT_FOR_YOUNG_PET -> {
                answer = REACTION_TO_RECOMMENDATIONS_FOR_HOME_IMPROVEMENT_FOR_YOUNG_PET(userFirstName);
                methods.reaction(chatId, answer);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ РЕКОМЕНДАЦИИ ПО ОБУСТРОЙСТВУ ДОМА ДЛЯ МАЛЕНЬКОГО ЖИВОТНОГО", chatId, userFirstName);
            }

            case RECOMMENDATIONS_FOR_HOME_IMPROVEMENT_FOR_ADULT_PET -> {
                answer = REACTION_TO_RECOMMENDATIONS_FOR_HOME_IMPROVEMENT_FOR_ADULT_PET(userFirstName);
                methods.reaction(chatId, answer);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ РЕКОМЕНДАЦИИ ПО ОБУСТРОЙСТВУ ДОМА ДЛЯ ВЗРОСЛОГО ЖИВОТНОГО", chatId, userFirstName);
            }

            case RECOMMENDATIONS_FOR_HOME_IMPROVEMENT_FOR_DISABLED_PET -> {
                answer = REACTION_TO_RECOMMENDATIONS_FOR_HOME_IMPROVEMENT_FOR_DISABLED_PET(userFirstName);
                methods.reaction(chatId, answer);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ РЕКОМЕНДАЦИИ ПО ОБУСТРОЙСТВУ ДОМА ДЛЯ ЖИВОТНОГО С ОГРАНИЧЕННЫМИ ВОЗМОЖНОСТЯМИ", chatId, userFirstName);
            }

            case RECOMMENDATIONS_FROM_DOG_HANDLER -> {
                answer = REACTION_TO_RECOMMENDATIONS_FROM_DOG_HANDLER(userFirstName);
                methods.reaction(chatId, answer);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ СОВЕТЫ КИНОЛОГА ПО ПЕРВИЧНОМУ ОБЩЕНИЮ С СОБАКОЙ", chatId, userFirstName);
            }

            case RECOMMENDATIONS_FOR_TRUSTED_DOG_HANDLER -> {
                answer = REACTION_TO_RECOMMENDATIONS_FOR_TRUSTED_DOG_HANDLER(userFirstName);
                methods.reaction(chatId, answer);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ РЕКОМЕНДАЦИИ ПО ПРОВЕРЕННЫМ КИНОЛОГАМ", chatId, userFirstName);
            }

            case LIST_REASONS_FOR_REFUSAL -> {
                answer = REACTION_TO_LIST_REASONS_FOR_REFUSAL(userFirstName);
                methods.reaction(chatId, answer);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ СПИСОК ПРИЧИН ДЛЯ ОТКАЗА", chatId, userFirstName);
            }

            // КОМАНДЫ ИНФОРМАЦИИ ОБ ОТПРАВКЕ ОТЧЕТА О ЖИВОТНОМ (ЭТАП 3)

            case REPORT_ABOUT_PET -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАПРОСИЛ ИНФОРМАЦИЮ ОБ ОТПРАВКЕ ОТЧЕТА О ЖИВОТНОМ", chatId, userFirstName);
                answer = REACTION_TO_DETAILED_INFO(userFirstName);
                message = keyBoards.createKeyBoardForReportPet(chatId, answer);
                methods.sendText(message);
            }

            case PATTERN_REPORT -> {
                answer = REACTION_TO_PATTERN_REPORT(userFirstName);
                methods.reaction(chatId, answer);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ ШАБЛОН ОТЧЕТА О ЖИВОТНОМ", chatId, userFirstName);
            }

            // КОМАНДА ЗАПИСИ КОНТАКТНЫХ ДАННЫХ

            case RECORD_CONTACT_DETAILS -> {
                answer = REACTION_TO_RECORD_CONTACT_DETAILS(userFirstName);
                methods.reaction(chatId, answer);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ ФОРМУ ДЛЯ ОТПРАВКИ КОНТАКТНЫХ ДАННЫХ", chatId, userFirstName);
            }

            // КОМАНДА УДАЛЕНИЯ КОНТАКТНЫХ ДАННЫХ

            case REMOVE_CONTACT_DETAILS -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАПРОСИЛ УДАЛЕНИЕ КОНТАКТНЫХ ДАННЫХ", chatId, userFirstName);
                answer = REACTION_TO_REQUEST_TO_REMOVED_CONTACTS_DETAILS(userFirstName);
                message = buttons.createButtonForRemoveContactDetails(chatId, answer);
                methods.sendText(message);
            }

            // КОМАНДА СМЕНЫ ПРИЮТА

            case CHANGE_SHELTER -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАПРОСИЛ ИЗМЕНЕНИЯ ВЫБОРА ПРИЮТА", chatId, userFirstName);
                answer = REACTION_TO_CHANGED_SHELTER(userFirstName);
                message = keyBoards.createKeyBoardForChoiceShelter(chatId, answer);
                methods.sendText(message);
            }

            // КОМАНДА ВЫЗОВА ВОЛОНТЕРА

            case CALL_VOLUNTEER -> {
                answer = String.valueOf(lists.sendListVolunteers(chatId, userFirstName));
                methods.reaction(chatId, answer);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ КОНТАКТЫ ВОЛОНТЕРОВ", chatId, userFirstName);
            }

            // ДЕФОЛТНАЯ КОМАНДА

            default -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ ДЕФОЛТНОЕ СООБЩЕНИЕ", chatId, userFirstName);
                answer = DEFAULT_REACTION_FOR_REGISTERED_USERS(userFirstName);
                message = buttons.createButtonForCallVolunteer(chatId, answer);
                methods.sendText(message);
            }
        }
    }

    @Override
    public void getReactionsToClickingButtons(Long chatId, String userFirstName, String callbackData) {

        String answer;
        SendMessage message;

        switch (callbackData) {

            // КНОПКИ РЕГИСТРАЦИИ

            case YES_BUTTON -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} СОГЛАСИЛСЯ НА РЕГИСТРАЦИЮ", chatId, userFirstName);
                answer = REACTION_TO_AGREEMENT_REGISTRATION(userFirstName);
                message = buttons.createButtonForRegistration(chatId, answer);
                methods.sendText(message);
            }

            case NO_BUTTON -> {
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ОТКАЗАЛСЯ ОТ РЕГИСТРАЦИИ", chatId, userFirstName);
                answer = REACTION_TO_DISAGREEMENT_REGISTRATION(userFirstName);
                message = keyBoards.createKeyBoardForRegistration(chatId, answer);
                methods.sendText(message);
            }

            case REGISTRATION_BUTTON -> {
                base.saveNewUserToDB(chatId, userFirstName);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ЗАРЕГИСТРИРОВАЛСЯ", chatId, userFirstName);
                answer = REACTION_TO_SUCCESSFUL_REGISTRATION(userFirstName);
                message = buttons.createButtonsForChoiceShelter(chatId, answer);
                methods.sendText(message);
            }

            // КНОПКИ ВЫБОРА ПРИЮТА

            case DOG_SHELTER_BUTTON -> {
                configuration.setIsDogShelter(true);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ВЫБРАЛ ПРИЮТ ДЛЯ СОБАК", chatId, userFirstName);
                answer = REACTION_TO_CHOICE_DOG_SHELTER(userFirstName);
                message = keyBoards.createKeyBoardForGeneralInfo(chatId, answer);
                methods.sendText(message);
            }

            case CAT_SHELTER_BUTTON -> {
                configuration.setIsDogShelter(false);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ВЫБРАЛ ПРИЮТ ДЛЯ КОШЕК", chatId, userFirstName);
                answer = REACTION_TO_CHOICE_CAT_SHELTER(userFirstName);
                message = keyBoards.createKeyBoardForGeneralInfo(chatId, answer);
                methods.sendText(message);
            }

            // КНОПКА СХЕМЫ ПРОЕЗДА

            case SCHEME_DRIVING_BUTTON -> {
                answer = REACTION_TO_SCHEME_DRIVING(userFirstName);

                if (configuration.getIsDogShelter()) {
                    methods.sendImage(chatId, answer, configuration.getPathForDogShelter());
                    log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ СХЕМУ ПРОЕЗДА К ПРИЮТУ ДЛЯ СОБАК", chatId, userFirstName);

                } else {
                    methods.sendImage(chatId, answer, configuration.getPathForCatShelter());
                    log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ СХЕМУ ПРОЕЗДА К ПРИЮТУ ДЛЯ КОШЕК", chatId, userFirstName);
                }
            }

            // КНОПКА УДАЛЕНИЯ КОНТАКТНЫХ ДАННЫХ

            case REMOVE_BUTTON -> {
                base.removePotentialParentFromDB(chatId, userFirstName);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} УДАЛИЛ КОНТАКТНЫЕ ДАННЫЕ", chatId, userFirstName);
            }

            // КНОПКА ВЫЗОВА ВОЛОНТЕРА

            case CALL_VOLUNTEER_BUTTON -> {
                answer = String.valueOf(lists.sendListVolunteers(chatId, userFirstName));
                methods.reaction(chatId, answer);
                log.info("ПОЛЬЗОВАТЕЛЬ {} {} ПОЛУЧИЛ КОНТАКТЫ ВОЛОНТЕРОВ", chatId, userFirstName);
            }
        }
    }
}