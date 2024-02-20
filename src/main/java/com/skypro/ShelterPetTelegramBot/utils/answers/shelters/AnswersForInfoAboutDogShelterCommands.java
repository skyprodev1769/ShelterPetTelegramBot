package com.skypro.ShelterPetTelegramBot.utils.answers.shelters;

/**
 * РЕАКЦИИ НА ИНФОРМАЦИЮ О ПРИЮТЕ ДЛЯ СОБАК
 */
public class AnswersForInfoAboutDogShelterCommands {

    public static String REACTION_TO_INFO_ABOUT_WORK_SCHEDULE_AND_ADDRESS_FOR_DOG_SHELTER(String userFirstName) {
        return String.format("""
                %s, наш приют работает ежедневно с 8:00 до 20:00.
                                
                Мы находимся по адресу:
                                
                г. Астана, ул. Планерная 1, стр.1.""", userFirstName);
    }

    public static String REACTION_TO_INFO_ABOUT_SECURITY_CONTACT_DETAILS_FOR_DOG_SHELTER(String userFirstName) {
        return String.format("""
                %s, для проезда на территорию приюта необходимо оформить пропуск.
                            
                Пожалуйста, свяжитесь со службой охраны по номеру телефона:
                            
                + 7 (495) 111-11-11""", userFirstName);
    }
}