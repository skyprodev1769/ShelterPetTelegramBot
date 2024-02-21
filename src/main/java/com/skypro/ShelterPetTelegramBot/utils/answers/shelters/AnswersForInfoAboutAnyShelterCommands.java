package com.skypro.ShelterPetTelegramBot.utils.answers.shelters;

/**
 * РЕАКЦИИ НА ИНФОРМАЦИЮ О ЛЮБОМ ПРИЮТЕ
 */
public class AnswersForInfoAboutAnyShelterCommands {

    public static String REACTION_TO_INFO_ABOUT_GENERAL_SAFETY_RECOMMENDATION(String userFirstName) {
        return String.format("""
                %s, соблюдайте технику безопасности!
                На территории приюта ЗАПРЕЩАЕТСЯ:
                            
                1. Самостоятельно открывать выгулы и вольеры без разрешения работника приюта.
                            
                2. Кормить животных.
                            
                3. Оставлять после себя мусор на территории приюта и прилегающей территории.
                            
                4. Кричать, размахивать руками, бегать между будками или вольерами, пугать и дразнить животных.""", userFirstName);
    }

    public static String REACTION_TO_SCHEME_DRIVING(String userFirstName) {
        return String.format("%s, это схема проезда к нашему приюту", userFirstName);
    }
}