package com.skypro.ShelterPetTelegramBot.utils.answers;

import com.vdurmont.emoji.EmojiParser;

/**
 * РЕАКЦИИ НА КОМАНДЫ РЕГИСТРАЦИИ
 */
public class AnswersForRegistrationCommands {

    public static String REACTION_TO_AGREEMENT_REGISTRATION(String userFirstName) {
        return EmojiParser.parseToUnicode(String.format(":information_desk_person: %s, для дальнейшей регистрации нажмите кнопку \"Зарегистрироваться\"", userFirstName));
    }

    public static String REACTION_TO_DISAGREEMENT_REGISTRATION(String userFirstName) {
        return String.format("%s, Вы можете зарегистрироваться в любой удобный для Вас момент, но пока Вам не будет доступен весь функционал бота.", userFirstName);
    }

    public static String REACTION_TO_SUCCESSFUL_REGISTRATION(String userFirstName) {
        return EmojiParser.parseToUnicode(String.format("""
                :tada: Поздравляю, %s, Вы успешно зарегистрированы!
                    
                Пожалуйста, Выберите приют.""", userFirstName));
    }
}