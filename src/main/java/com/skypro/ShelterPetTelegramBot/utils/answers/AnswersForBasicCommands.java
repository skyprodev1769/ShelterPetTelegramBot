package com.skypro.ShelterPetTelegramBot.utils.answers;

/**
 * РЕАКЦИИ НА БАЗОВЫЕ КОМАНДЫ
 */
public class AnswersForBasicCommands {

    public static String REACTION_TO_COMMAND_START_FOR_UNREGISTERED_USERS(String userFirstName) {
        return String.format("""
                Добро пожаловать, %s!
                                
                Этот телеграм бот предназначен для людей, заинтересованных в том, чтобы забрать из приюта домашнее животное.
                                
                Прежде чем продолжить, рекомендую Вам зарегистрироваться, иначе Вам не будет доступен весь функционал.
                                
                Желаете продолжить и зарегистрироваться?""", userFirstName);
    }

    public static String REACTION_TO_COMMAND_START_FOR_REGISTERED_USERS(String userFirstName) {
        return String.format("С возвращением, %s!", userFirstName);
    }

    public static String REACTION_TO_COMMAND_HELP_FOR_UNREGISTERED_USERS(String userFirstName) {
        return String.format("""
                %s, этот телеграм бот предназначен для людей, заинтересованных в том, чтобы забрать из приюта домашнее животное.
                                
                Нажмите /start, чтобы вывелось стартовое сообщение
                                
                Нажмите /help, чтобы вывелось данное сообщение снова
                                
                Нажмите /settings, чтобы вывелась интерактивная клавиатура""", userFirstName);
    }

    public static String REACTION_TO_COMMAND_HELP_FOR_REGISTERED_USERS(String userFirstName) {
        return String.format("""
                %s, этот телеграм бот предназначен для людей, заинтересованных в том, чтобы забрать из приюта домашнее животное.
                                
                Нажмите /start, чтобы вывелось стартовое сообщение
                                
                Нажмите /help, чтобы вывелось данное сообщение снова
                                
                Нажмите /settings, чтобы вывелась интерактивная клавиатура
                                
                или воспользуйтесь кнопкой вызова волонтера""", userFirstName);
    }

    public static String REACTION_TO_COMMAND_SETTINGS(String userFirstname) {
        return String.format("%s, пожалуйста, выберите команду из клавиатуры ниже.", userFirstname);
    }

    public static String DEFAULT_REACTION_FOR_UNREGISTERED_USERS(String userFirstName) {
        return String.format("%s, пожалуйста, выберите команду из списка меню или вызовите команду /help.", userFirstName);
    }

    public static String DEFAULT_REACTION_FOR_REGISTERED_USERS(String userFirstName) {
        return String.format("%s, пожалуйста, выберите команду из списка меню, вызовите команду /help или воспользуйтесь кнопкой вызова волонтера.", userFirstName);
    }
}