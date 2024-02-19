package com.skypro.ShelterPetTelegramBot.utils.answers;

/**
 * РЕАКЦИИ НА ОБЩИЕ КОМАНДЫ
 */
public class AnswersForGeneralCommands {

    public static String REACTION_TO_RECORD_CONTACT_DETAILS(String userFirstName) {
        return String.format("""
                %s, Вы можете оставить свои контактные данные для обратной связи.
                                    
                Введите необходимую информацию в УКАЗАННОМ ФОРМАТЕ
                                    
                "ИМЯ"   "ФАМИЛИЯ"   8**********""", userFirstName);
    }

    public static String REACTION_TO_SUCCESSFUL_RECORD_CONTACT(String userFirstName) {
        return String.format("%s, Ваши данные успешно записаны!", userFirstName);
    }

    public static String REACTION_TO_REPEAT_RECORD_CONTACT(String userFirstName) {
        return String.format("%s, Ваши данные уже были записаны ранее!", userFirstName);
    }

    public static String REACTION_TO_REQUEST(String userFirstName) {
        return String.format("%s, укажите, нужна ли Вам подробная информация или перейдите к следующему этапу.", userFirstName);
    }

    public static String REACTION_TO_DETAILED_INFO(String userFirstName) {
        return String.format("%s, выберите, что бы Вы хотели узнать.", userFirstName);
    }

    public static String REACTION_TO_CALL_VOLUNTEER(String userFirstName) {
        return String.format("""
                %s, воспользуйтесь следующими номерами для связи с нашими волонтерами:
                            
                +7 (495) 777-77-77
                +7 (495) 888-88-88
                +7 (495) 999-99-99""", userFirstName);
    }
}