package com.skypro.ShelterPetTelegramBot.utils.answers.shelters;

import com.vdurmont.emoji.EmojiParser;

/**
 * РЕАКЦИИ НА ИНФОРМАЦИЮ О ЛЮБОМ ПРИЮТЕ
 */
public class AnswersForInfoAboutAnyShelterCommands {

    public static String REACTION_TO_INFO_ABOUT_WORK_SCHEDULE_AND_ADDRESS(String userFirstName) {
        return EmojiParser.parseToUnicode(String.format("""
                :stopwatch: %s, наш приют работает ежедневно с 8:00 до 20:00.
                                
                Мы находимся по адресу:""", userFirstName));
    }

    public static String REACTION_TO_INFO_ABOUT_GENERAL_SAFETY_RECOMMENDATION(String userFirstName) {
        return EmojiParser.parseToUnicode(String.format("""
                %s, соблюдайте технику безопасности!
                :no_entry_sign: На территории приюта ЗАПРЕЩАЕТСЯ:
                            
                1. Самостоятельно открывать выгулы и вольеры без разрешения работника приюта.
                            
                2. Кормить животных.
                            
                3. Оставлять после себя мусор на территории приюта и прилегающей территории.
                            
                4. Кричать, размахивать руками, бегать между будками или вольерами, пугать и дразнить животных.""", userFirstName));
    }

    public static String REACTION_TO_SCHEME_DRIVING(String userFirstName) {
        return String.format("%s, это схема проезда к нашему приюту", userFirstName);
    }
}