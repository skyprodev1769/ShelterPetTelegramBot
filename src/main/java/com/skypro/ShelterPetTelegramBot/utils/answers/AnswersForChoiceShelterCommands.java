package com.skypro.ShelterPetTelegramBot.utils.answers;

/**
 * РЕАКЦИИ НА КОМАНДЫ ВЫБОРА ПРИЮТА
 */
public class AnswersForChoiceShelterCommands {

    public static String REACTION_TO_CHOICE_DOG_SHELTER(String userFirstName) {
        return String.format("%s, Вы выбрали приют для собак.", userFirstName);
    }

    public static String REACTION_TO_CHOICE_CAT_SHELTER(String userFirstName) {
        return String.format("%s, Вы выбрали приют для кошек.", userFirstName);
    }
}