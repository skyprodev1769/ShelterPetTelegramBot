package com.skypro.ShelterPetTelegramBot.utils.answers;

/**
 * РЕАКЦИИ НА ОБЩИЕ КОМАНДЫ
 */
public class AnswersForGeneralCommands {

    public static String REACTION_TO_DETAILED_INFO(String userFirstName) {
        return String.format("%s, пожалуйста, выберите, что бы Вы хотели узнать.", userFirstName);
    }

    public static String REACTION_TO_CHANGED_SHELTER(String userFirstName) {
        return String.format("%s, пожалуйста, выберите приют.", userFirstName);
    }

    public static String REACTION_TO_CALL_VOLUNTEER(String userFirstName) {
        return String.format("%s, пожалуйста, воспользуйтесь следующими номерами для связи с нашими волонтерами:", userFirstName);
    }
}