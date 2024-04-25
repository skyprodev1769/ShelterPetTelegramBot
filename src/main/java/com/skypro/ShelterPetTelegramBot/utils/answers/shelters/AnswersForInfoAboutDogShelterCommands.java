package com.skypro.ShelterPetTelegramBot.utils.answers.shelters;

/**
 * РЕАКЦИИ НА ИНФОРМАЦИЮ О ПРИЮТЕ ДЛЯ СОБАК
 */
public class AnswersForInfoAboutDogShelterCommands {

    public static String REACTION_TO_INFO_ABOUT_SECURITY_CONTACT_DETAILS_FOR_DOG_SHELTER(String userFirstName) {
        return String.format("""
                %s, для проезда на территорию приюта необходимо оформить пропуск.
                            
                Пожалуйста, свяжитесь со службой охраны по номеру телефона:
                            
                + 7 (495) 111-11-11""", userFirstName);
    }
}