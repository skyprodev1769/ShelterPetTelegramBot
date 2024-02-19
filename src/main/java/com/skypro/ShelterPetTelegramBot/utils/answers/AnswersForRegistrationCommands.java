package com.skypro.ShelterPetTelegramBot.utils.answers;

/**
 * РЕАКЦИИ НА КОМАНДЫ РЕГИСТРАЦИИ
 */
public class AnswersForRegistrationCommands {

    public static String REACTION_TO_AGREEMENT_REGISTRATION(String userFirstName) {
        return String.format("%s, для дальнейшей регистрации нажмите кнопку \"Зарегистрироваться\"", userFirstName);
    }

    public static String REACTION_TO_DISAGREEMENT_REGISTRATION(String userFirstName) {
        return String.format("%s, Вы можете зарегистрироваться в любой удобный для Вас момент, вызвав команду /start, а пока Вам доступен ограниченный функционал.", userFirstName);
    }

    public static String REACTION_TO_SUCCESSFUL_REGISTRATION(String userFirstName) {
        return String.format("Поздравляю, %s, Вы успешно зарегистрированы!", userFirstName);
    }
}