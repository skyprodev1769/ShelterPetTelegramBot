package com.skypro.ShelterPetTelegramBot.utils.answers;

import com.vdurmont.emoji.EmojiParser;

/**
 * РЕАКЦИИ НА КОМАНДЫ ВЫБОРА ПРИЮТА
 */
public class AnswersForChoiceShelterCommands {

    public static String REACTION_TO_CHOICE_DOG_SHELTER(String userFirstName) {
        return EmojiParser.parseToUnicode(String.format("%s, Вы выбрали приют для собак. :dog: ", userFirstName));
    }

    public static String REACTION_TO_CHOICE_CAT_SHELTER(String userFirstName) {
        return EmojiParser.parseToUnicode(String.format("%s, Вы выбрали приют для кошек. :cat: ", userFirstName));
    }
}