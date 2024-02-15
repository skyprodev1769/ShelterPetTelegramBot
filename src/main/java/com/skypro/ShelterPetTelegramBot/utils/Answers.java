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

    public static String REACTION_TO_COMMAND_HELP_FOR_UNREGISTERED_USERS(String userFirstName) {
        return String.format("""
                %s, этот телеграм бот предназначен для людей,которые заинтересованы в том, чтобы забрать из приюта домашнее животное.
                                
                Нажмите /start, чтобы вывелось стартовое сообщение
                                
                Нажмите /help, чтобы вывелось данное сообщение снова
                                
                Нажмите /settings, чтобы вывелась клавиатура""", userFirstName);
    }

    public static String REACTION_TO_COMMAND_HELP_FOR_REGISTERED_USERS(String userFirstName) {
        return String.format("""
                %s, этот телеграм бот предназначен для людей,которые заинтересованы в том, чтобы забрать из приюта домашнее животное.
                                
                Нажмите /start, чтобы вывелось стартовое сообщение
                                
                Нажмите /help, чтобы вывелось данное сообщение снова
                                
                Нажмите /settings, чтобы вывелась клавиатура
                                
                или воспользуйтесь кнопкой вызова волонтера""", userFirstName);
    }

    public static String REACTION_TO_COMMAND_SETTINGS(String userFirstname) {
        return String.format("%s, пожалуйста, выберите команду из клавиатуры ниже.", userFirstname);
    }

    public static String DEFAULT_REACTION_FOR_UNREGISTERED_USERS(String userFirstName) {
        return String.format("%s, пожалуйста, выберите команду из списка меню.", userFirstName);
    }

    public static String DEFAULT_REACTION_FOR_REGISTERED_USERS(String userFirstName) {
        return String.format("%s, пожалуйста, выберите команду из списка меню или воспользуйтесь кнопкой вызова волонтера.", userFirstName);
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

    public final static String REACTION_TO_INFO_ABOUT_WORK_SCHEDULE_AND_ADDRESS = """
            Наш приют работает ежедневно с 8:00 до 20:00.
                        
            Мы находимся по адресу:
                        
            г. Астана, ул. Планерная 1, стр.1.""";

    public final static String REACTION_TO_INFO_ABOUT_SECURITY_CONTACT_DETAILS = """
            Для проезда на территорию приюта необходимо оформить пропуск.
                        
            Пожалуйста, свяжитесь со службой охраны по номеру телефона:
                        
            + 7 (495) 111-11-11""";

    public final static String REACTION_TO_INFO_ABOUT_GENERAL_SAFETY_RECOMMENDATION = """
            Соблюдайте технику безопасности.
            На территории приюта ЗАПРЕЩАЕТСЯ:
                        
            1. Самостоятельно открывать выгулы и вольеры без разрешения работника приюта.
                        
            2. Кормить животных.
                        
            3. Оставлять после себя мусор на территории приюта и прилегающей территории.
                        
            4. Кричать, размахивать руками, бегать между будками или вольерами, пугать и дразнить животных.""";

    public final static String REACTION_TO_RECORD_CONTACT_DETAILS = """
            Вы можете оставить свои контактные данные.
                                
            Введите Ваши:
                                
            ИМЯ
                                
            ФАМИЛИЮ
                                
            НОМЕР ТЕЛЕФОНА""";

            /*
            РЕАКЦИЯ НА ВЫЗОВ ВОЛОНТЕРА
             */

    public static final String REACTION_TO_CALL_VOLUNTEER = """
            Воспользуйтесь следующими номерами для связи с нашими волонтерами:
                        
            +7 (495) 777-77-77
            +7 (495) 888-88-88
            +7 (495) 999-99-99""";
}