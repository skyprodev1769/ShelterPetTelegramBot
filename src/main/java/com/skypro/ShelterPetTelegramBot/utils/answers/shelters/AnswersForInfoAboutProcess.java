package com.skypro.ShelterPetTelegramBot.utils.answers.shelters;

/**
 * РЕАКЦИИ НА ИНФОРМАЦИЮ О ПРОЦЕССЕ ПОЛУЧЕНИИ ЖИВОТНОГО
 */
public class AnswersForInfoAboutProcess {

    public static String REACTION_TO_LIST_PETS(String userFirstName) {
        return String.format("%s, ознакомьтесь со списком животных для усыновления:", userFirstName);
    }

    public static String REACTION_TO_RULES_DATING(String userFirstName) {
        return String.format("""
                %s, Вам необходимо каждый день посещать приют, чтобы познакомиться с питомцем и привыкнуть друг к другу.
                                
                Не стоит навязывать питомцу свое общение, начните с ним играть, когда он будет готов.""", userFirstName);
    }

    public static String REACTION_TO_LIST_REQUIRED_DOCS(String userFirstName) {
        return String.format("""
                %s, ознакомьтесь со списком документов, необходимых для того, чтобы взять животное из приюта:
                                
                1. Паспорт гражданина РФ
                2. Подписание договора об ответственности с приютом""", userFirstName);
    }

    public static String REACTION_TO_RECOMMENDATIONS_FOR_TRANSPORTATION(String userFirstName) {
        return String.format("%s, питомца, в зависимости от возраста, " +
                "нужно перевозить в переноске, специально сконструированной клетке или на поводке.", userFirstName);
    }

    public static String REACTION_TO_RECOMMENDATIONS_FOR_HOME_IMPROVEMENT_FOR_YOUNG_PET(String userFirstName) {
        return String.format("""
                %s, ознакомьтесь с минимальными условиями по обустройству дома для маленького животного:
                                
                1. Обустроить место для сна и отдыха
                2. Поставить миски для воды и еды
                3. Приобрести игрушки для развлечения животного""", userFirstName);
    }

    public static String REACTION_TO_RECOMMENDATIONS_FOR_HOME_IMPROVEMENT_FOR_ADULT_PET(String userFirstName) {
        return String.format("""
                %s, ознакомьтесь с рекомендованными условиями по обустройству дома для взрослого животного
                                
                1. Наличие большого свободного пространства для игр собаки
                2. Большое спальное место""", userFirstName);
    }

    public static String REACTION_TO_RECOMMENDATIONS_FOR_HOME_IMPROVEMENT_FOR_DISABLED_PET(String userFirstName) {
        return String.format("""
                %s, ознакомьтесь с минимальными условиями по обустройству дома для животного с ограниченными возможностями
                                
                1. Расположить место для кормления недалеко от спального
                2. Организовать туалет в досягаемости питомца""", userFirstName);
    }

    public static String REACTION_TO_RECOMMENDATIONS_FROM_DOG_HANDLER(String userFirstName) {
        return String.format("""
                %s, ознакомьтесь с советами кинолога по первичному общению с собакой
                                
                1. Разговаривайте с собакой
                2. Следите за своими эмоциями
                3. Гибко реагируйте на поведение собаки
                4. Поиграйте с собакой если она готова""", userFirstName);
    }

    public static String REACTION_TO_RECOMMENDATIONS_FOR_TRUSTED_DOG_HANDLER(String userFirstName) {
        return String.format("""
                %s, ознакомьтесь со списком профессиональных кинологов:
                                
                1. Иванов Иван, телефон +7-999-99-99
                2. Петров Пётр, телефон +7-666-66-66
                3. Павлов Павел, телефон +7-555-55-55""", userFirstName);
    }

    public static String REACTION_TO_LIST_REASONS_FOR_REFUSAL(String userFirstName) {
        return String.format("""
                %s, ознакомьтесь с причинами, почему могут отказать в передаче питомца:
                                
                1. Большое количество животных дома
                2. Нестабильные отношения в семье
                3. Наличие маленьких детей
                4. Съемное жилье
                5. Животное в подарок или для работы""", userFirstName);
    }
}