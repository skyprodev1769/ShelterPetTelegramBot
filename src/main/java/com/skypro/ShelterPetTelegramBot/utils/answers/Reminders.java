package com.skypro.ShelterPetTelegramBot.utils.answers;

import com.vdurmont.emoji.EmojiParser;

import java.time.LocalDate;

/**
 * НАПОМИНАНИЯ УСЫНОВИТЕЛЯМ
 */
public class Reminders {

    public static String REMINDER_NULL_REPORT(String firstName,
                                              String lastName,
                                              String phoneNumber,
                                              String petName) {
        return String.format("""
                %s %s,
                Ваш номер телефона - %s
                Вы взяли опеку над животным - %s
                                
                Сегодня Вы не отправили отчет!""", firstName, lastName, phoneNumber, petName);
    }

    public static String REMINDER_NULL_ATTACHMENT(String firstName,
                                                  String lastName,
                                                  String phoneNumber,
                                                  String petName) {
        return EmojiParser.parseToUnicode(String.format("""
                %s %s,
                :iphone:Ваш номер телефона - %s
                :dog:Вы взяли опеку над животным - %s
                                
                Отправленный Вами сегодняшний отчет не полон!""", firstName, lastName, phoneNumber, petName));
    }

    public static String WARNING(String firstName,
                                 String lastName,
                                 String phoneNumber,
                                 String petName,
                                 LocalDate date) {
        return EmojiParser.parseToUnicode(String.format("""
                Дорогой усыновитель,
                                
                %s %s,
                :iphone:Ваш номер телефона - %s
                :dog:Вы взяли опеку над животным - %s
                Последний отправленный отчет от %s
                                
                мы заметили, что Вы заполняете отчет не так подробно, как необходимо.
                Пожалуйста, подойдите ответственнее к этому занятию.
                В противном случае волонтеры приюта будут обязаны самолично проверять условия содержания животного""", firstName, lastName, phoneNumber, petName, date));
    }
}