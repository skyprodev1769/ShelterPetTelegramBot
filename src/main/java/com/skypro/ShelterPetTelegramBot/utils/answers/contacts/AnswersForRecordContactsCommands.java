package com.skypro.ShelterPetTelegramBot.utils.answers.contacts;

import com.vdurmont.emoji.EmojiParser;

/**
 * РЕАКЦИИ НА КОМАНДЫ ЗАПИСИ КОНТАКТНЫХ ДАННЫХ
 */
public class AnswersForRecordContactsCommands {

    public static String REACTION_TO_RECORD_CONTACT_DETAILS(String userFirstName) {
        return String.format("""
                %s, Вы можете оставить свои контактные данные для обратной связи.
                
                Если Вы захотите, удалить Ваши контактные данные можно в любой момент.
                                    
                Пожалуйста, введите необходимую информацию в УКАЗАННОМ ФОРМАТЕ
                                    
                "ИМЯ"   "ФАМИЛИЯ"   8**********""", userFirstName);
    }

    public static String REACTION_TO_SUCCESSFUL_RECORD_CONTACT(String userFirstName) {
        return EmojiParser.parseToUnicode(String.format(":white_check_mark: %s, переданные данные успешно записаны!", userFirstName));
    }

    public static String REACTION_TO_REPEAT_RECORD_CONTACT(String userFirstName) {
        return EmojiParser.parseToUnicode(String.format(":grey_exclamation: %s, Данный номер телефона уже был записан ранее!", userFirstName));
    }
}