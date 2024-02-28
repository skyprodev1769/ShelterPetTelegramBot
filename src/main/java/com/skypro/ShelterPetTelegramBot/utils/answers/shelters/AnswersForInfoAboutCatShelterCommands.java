package com.skypro.ShelterPetTelegramBot.utils.answers.shelters;

/**
 * РЕАКЦИИ НА ИНФОРМАЦИЮ О ПРИЮТЕ ДЛЯ КОШЕК
 */
public class AnswersForInfoAboutCatShelterCommands {

    public static String REACTION_TO_INFO_ABOUT_SECURITY_CONTACT_DETAILS_FOR_CAT_SHELTER(String userFirstName) {
        return String.format("""
                %s, для проезда на территорию приюта необходимо оформить пропуск.
                            
                Пожалуйста, свяжитесь со службой охраны по номеру телефона:
                            
                + 7 (495) 222-22-22""", userFirstName);
    }
}