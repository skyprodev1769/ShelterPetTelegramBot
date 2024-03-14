package com.skypro.ShelterPetTelegramBot.utils.answers.contacts;

/**
 * РЕАКЦИИ НА УДАЛЕНИЕ ЗАПИСИ КОНТАКТНЫХ ДАННЫХ
 */
public class AnswersForRemovedContactsCommand {

    public static String REACTION_TO_REQUEST_TO_REMOVED_CONTACTS_DETAILS(String userFirstName) {
        return String.format("%s, Вы уверены, что хотите стереть все данные переданные через Ваш аккаунт?", userFirstName);
    }

    public static String REACTION_TO_REMOVED_CONTACTS_DETAILS(String userFirstName) {
        return String.format("%s, все Ваши ранее переданные данные успешно удалены.", userFirstName);
    }

    public static String REACTION_TO_REMOVED_EMPTY_CONTACTS_DETAILS(String userFirstName) {
        return String.format("%s, Вы еще не передавали данные для записи.", userFirstName);
    }
}