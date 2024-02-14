package com.skypro.ShelterPetTelegramBot.utils;

public class Answers {


    /*
    РЕАКЦИИ НА БАЗОВЫЕ КОМАНДЫ
     */

    public static String REACTION_TO_FIRST_COMMAND_START(String userFirstName) {
        return String.format("""
                Добро пожаловать, %s!
                                
                Этот телеграм бот предназначен для людей, которые заинтересованы в том, чтобы забрать из приюта домашнее животное.
                                
                Прежде чем продолжить, рекомендую Вам зарегистрироваться, иначе Вам будет доступен не весь функционал.
                                
                Желаете продолжить и зарегистрироваться?""", userFirstName);
    }

    public static String REACTION_TO_COMMAND_START(String userFirstName) {
        return String.format("С возвращением, %s!", userFirstName);
    }

    public static String REACTION_TO_COMMAND_HELP(String userFirstName) {
        return String.format("""
                %s, этот телеграм бот предназначен для людей,которые заинтересованы в том, чтобы забрать из приюта домашнее животное.
                                
                Нажмите /start, чтобы вывелось стартовое сообщение
                                
                Нажмите /help, чтобы вывелось данное сообщение снова
                                
                Нажмите /settings, чтобы вывелась клавиатура""", userFirstName);
    }

    public static String REACTION_TO_COMMAND_SETTINGS(String userFirstname) {
        return String.format("%s, пожалуйста, выберите команду из клавиатуры ниже.", userFirstname);
    }

    public static String DEFAULT_REACTION(String userFirstName) {
        return String.format("%s, пожалуйста, выберите команду из списка меню.", userFirstName);
    }

    /*
    РЕАКЦИИ НА КОМАНДЫ РЕГИСТРАЦИИ
     */

    public static final String AGREEMENT_REGISTRATION = "Для дальнейшей регистрации нажмите или введите команду /registration";

    public static final String DISAGREEMENT_REGISTRATION = "Вы можете зарегистрироваться в любой удобный для Вас момент, " +
            "вызвав команду /registration, а пока Вам доступен ограниченный функционал.";

    public static String REACTION_TO_SUCCESSFUL_REGISTRATION(String userFirstName) {
        return String.format("Поздравляю, %s, Вы успешно зарегистрированы!", userFirstName);
    }

    /*
    РЕАКЦИИ НА ЗАПРОСЫ ИНФОРМАЦИИ
     */

    public static final String REACTION_TO_REQUEST = "Укажите, нужна ли Вам подробная информация или " +
            "перейдите к следующему этапу.";

    public static final String REACTION_TO_DETAILED_INFO = "Выберите, что бы Вы хотели узнать.";

    /*
    РЕАКЦИЯ НА ВЫЗОВ ВОЛОНТЕРА
     */

    public static final String REACTION_TO_CALL_VOLUNTEER = """
            Воспользуйтесь следующими номерами для связи с нашими волонтерами:
                        
            + 7 (495) 777-77-77
            + 7 (495) 888-88-88
            + 7 (495) 999-99-99""";
}