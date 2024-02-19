package com.skypro.ShelterPetTelegramBot.utils.answers;

/**
 * РЕАКЦИИ НА КОМАНДЫ ПОЛУЧЕНИЯ ИНФОРМАЦИИ О ПРИЮТЕ
 */
public class AnswersForInfoAboutShelterCommands {

    public static String REACTION_TO_INFO_ABOUT_WORK_SCHEDULE_AND_ADDRESS(String userFirstName) {
        return String.format("""
                %s, наш приют работает ежедневно с 8:00 до 20:00.
                                
                Мы находимся по адресу:
                                
                г. Астана, ул. Планерная 1, стр.1.""", userFirstName);
    }

    public static String REACTION_TO_INFO_ABOUT_SECURITY_CONTACT_DETAILS(String userFirstName) {
        return String.format("""
                %s, для проезда на территорию приюта необходимо оформить пропуск.
                            
                Пожалуйста, свяжитесь со службой охраны по номеру телефона:
                            
                + 7 (495) 111-11-11""", userFirstName);
    }

    public static String REACTION_TO_INFO_ABOUT_GENERAL_SAFETY_RECOMMENDATION(String userFirstName) {
        return String.format("""
                %s, соблюдайте технику безопасности!
                На территории приюта ЗАПРЕЩАЕТСЯ:
                            
                1. Самостоятельно открывать выгулы и вольеры без разрешения работника приюта.
                            
                2. Кормить животных.
                            
                3. Оставлять после себя мусор на территории приюта и прилегающей территории.
                            
                4. Кричать, размахивать руками, бегать между будками или вольерами, пугать и дразнить животных.""", userFirstName);
    }
}