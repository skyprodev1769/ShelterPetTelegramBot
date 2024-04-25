package com.skypro.ShelterPetTelegramBot.utils.answers;

import com.vdurmont.emoji.EmojiParser;

/**
 * РЕАКЦИИ НА ОБЩИЕ КОМАНДЫ
 */
public class AnswersForGeneralCommands {

    public static String REACTION_TO_DETAILED_INFO(String userFirstName) {
        return EmojiParser.parseToUnicode(String.format(":information_desk_person: %s, пожалуйста, выберите, что бы Вы хотели узнать.", userFirstName));
    }

    public static String REACTION_TO_CHANGED_SHELTER(String userFirstName) {
        return EmojiParser.parseToUnicode(String.format(":information_desk_person: %s, пожалуйста, выберите приют.", userFirstName));
    }

    public static String REACTION_TO_CALL_VOLUNTEER(String userFirstName) {
        return EmojiParser.parseToUnicode(String.format(":information_desk_person: %s, пожалуйста, воспользуйтесь следующими номерами для связи с нашими волонтерами:", userFirstName));
    }
}