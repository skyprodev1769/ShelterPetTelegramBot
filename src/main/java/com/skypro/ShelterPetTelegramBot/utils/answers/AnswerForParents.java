package com.skypro.ShelterPetTelegramBot.utils.answers;

import com.vdurmont.emoji.EmojiParser;

/**
 * ОТВЕТЫ УСЫНОВИТЕЛЯМ НА ИХ ОТЧЕТЫ
 */
public class AnswerForParents {

    public static String REACTION_TO_EXTENSION_14_DAYS(String firstName,
                                                       String lastName,
                                                       String phoneNumber,
                                                       String petName) {
        return EmojiParser.parseToUnicode(String.format("""
                %s %s,
                :iphone:Ваш номер телефона - %s
                :dog:Вы взяли опеку над животным - %s 
                Было принято решение продлить Ваш испытательный срок на 14 дней!""", firstName, lastName, phoneNumber, petName));
    }

    public static String REACTION_TO_EXTENSION_30_DAYS(String firstName,
                                                       String lastName,
                                                       String phoneNumber,
                                                       String petName) {
        return EmojiParser.parseToUnicode(String.format("""
                %s %s,
                :iphone:Ваш номер телефона - %s
                :dog:Вы взяли опеку над животным - %s    
                                          
                Было принято решение продлить Ваш испытательный срок на 30 дней!""", firstName, lastName, phoneNumber, petName));
    }

    public static String REACTION_TO_CONGRATULATIONS(String firstName,
                                                     String lastName,
                                                     String phoneNumber,
                                                     String petName) {
        return EmojiParser.parseToUnicode(String.format("""
                %s %s,
                :iphone:Ваш номер телефона - %s
                :dog:Вы взяли опеку над животным - %s
                                
                Поздравляем, Ваш испытательный срок успешно закончен!""", firstName, lastName, phoneNumber, petName));
    }

    public static String REACTION_TO_REFUSAL(String firstName,
                                             String lastName,
                                             String phoneNumber,
                                             String petName) {
        return EmojiParser.parseToUnicode(String.format("""
                %s %s,
                :iphone:Ваш номер телефона - %s
                :dog:Вы взяли опеку над животным - %s
                                
                К сожалению, Вы не прошли испытательный срок!
                Для дальнейших действий с Вами свяжется наш волонтер.""", firstName, lastName, phoneNumber, petName));
    }
}