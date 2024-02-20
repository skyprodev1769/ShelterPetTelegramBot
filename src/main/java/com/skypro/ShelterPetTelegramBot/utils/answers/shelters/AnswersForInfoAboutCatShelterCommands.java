package com.skypro.ShelterPetTelegramBot.utils.answers.shelters;

/**
 * РЕАКЦИИ НА ИНФОРМАЦИЮ О ПРИЮТЕ ДЛЯ КОШЕК
 */
public class AnswersForInfoAboutCatShelterCommands {

    public static String REACTION_TO_INFO_ABOUT_WORK_SCHEDULE_AND_ADDRESS_FOR_CAT_SHELTER(String userFirstName) {
        return String.format("""
                %s, наш приют работает ежедневно с 8:00 до 20:00.
                                
                Мы находимся по адресу:
                                
                г. Астана, ул. Ленина 13, стр.4.""", userFirstName);
    }

    public static String REACTION_TO_INFO_ABOUT_SECURITY_CONTACT_DETAILS_FOR_CAT_SHELTER(String userFirstName) {
        return String.format("""
                %s, для проезда на территорию приюта необходимо оформить пропуск.
                            
                Пожалуйста, свяжитесь со службой охраны по номеру телефона:
                            
                + 7 (495) 222-22-22""", userFirstName);
    }
}