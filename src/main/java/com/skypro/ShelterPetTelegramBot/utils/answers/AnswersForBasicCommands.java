package com.skypro.ShelterPetTelegramBot.utils.answers;

import com.vdurmont.emoji.EmojiParser;

/**
 * РЕАКЦИИ НА БАЗОВЫЕ КОМАНДЫ
 */
public class AnswersForBasicCommands {

    public static String REACTION_TO_COMMAND_START_FOR_UNREGISTERED_USERS(String userFirstName) {
        return EmojiParser.parseToUnicode(String.format("""
                :wave: Добро пожаловать, %s!
                                
                Этот телеграм бот предназначен для людей, заинтересованных в том, чтобы забрать из приюта домашнее животное.
                                
                Прежде чем продолжить, рекомендую Вам зарегистрироваться, иначе Вам не будет доступен весь функционал.
                                
                Желаете продолжить и зарегистрироваться?""", userFirstName));
    }

    public static String REACTION_TO_COMMAND_START_FOR_REGISTERED_USERS(String userFirstName) {
        return EmojiParser.parseToUnicode(String.format("""
                :wave: С возвращением, %s!
                                
                Пожалуйста, выберите приют.""", userFirstName));
    }

    public static String REACTION_TO_COMMAND_HELP_FOR_UNREGISTERED_USERS(String userFirstName) {
        return EmojiParser.parseToUnicode(String.format("""
                %s, этот телеграм бот предназначен для людей, заинтересованных в том, чтобы забрать из приюта домашнее животное.
                                
                :checkered_flag: Нажмите /start, чтобы вывелось стартовое сообщение
                                
                :sos: Нажмите /help, чтобы вывелось данное сообщение снова
                                
                :gear: Нажмите /settings, чтобы вывелась интерактивная клавиатура для регистрации""", userFirstName));
    }

    public static String REACTION_TO_COMMAND_HELP_FOR_REGISTERED_USERS(String userFirstName) {
        return EmojiParser.parseToUnicode(String.format("""
                %s, этот телеграм бот предназначен для людей, заинтересованных в том, чтобы забрать из приюта домашнее животное.
                                
                :checkered_flag: Нажмите /start, чтобы вывелось стартовое сообщение
                                
                :sos: Нажмите /help, чтобы вывелось данное сообщение снова
                                
                :gear: Нажмите /settings, чтобы вывелась интерактивная клавиатура выбора приюта
                                
                или воспользуйтесь кнопкой вызова волонтера""", userFirstName));
    }

    public static String REACTION_TO_COMMAND_SETTINGS(String userFirstname) {
        return String.format("%s, если желаете зарегистрироваться - нажмите кнопу \"Зарегистрироваться\" из клавиатуры ниже.", userFirstname);
    }

    public static String DEFAULT_REACTION_FOR_UNREGISTERED_USERS(String userFirstName) {
        return String.format("%s, пожалуйста, выберите команду из списка меню.", userFirstName);
    }

    public static String DEFAULT_REACTION_FOR_REGISTERED_USERS(String userFirstName) {
        return String.format("%s, пожалуйста, выберите команду из списка меню или воспользуйтесь кнопкой вызова волонтера.", userFirstName);
    }
}