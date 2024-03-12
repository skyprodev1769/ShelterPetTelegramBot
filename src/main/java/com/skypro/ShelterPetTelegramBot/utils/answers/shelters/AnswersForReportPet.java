package com.skypro.ShelterPetTelegramBot.utils.answers.shelters;

/**
 * РЕАКЦИИ НА КОМАНДЫ ОТПРАВКИ ОТЧЕТА
 */
public class AnswersForReportPet {

    public static String REACTION_TO_PATTERN_REPORT(String userFirstName) {
        return String.format("""
                %s, в отчете о домашнем животном должна быть отображена следующая информация:
                                
                1.Фото животного
                2.Рацион животного
                3.Общее самочувствие и привыкание к новому месту
                4.Изменения в поведении: отказ от старых привычек, приобретение новых""", userFirstName);
    }

    public static String REACTION_TO_SAVE_ONLY_PHOTO(String userFirstName) {
        return String.format("""
                %s, фото животного успешно сохранено в Ваш отчет!
                                
                Не забудьте приложить документ с описательной частью, если Вы не сделали этого ранее.""", userFirstName);
    }

    public static String REACTION_TO_SAVE_ONLY_DOCUMENT(String userFirstName) {
        return String.format("""
                %s, документ с описательной частью успешно сохранен в Ваш отчет!
                                
                Не забудьте приложить фото, если Вы не сделали этого ранее.""", userFirstName);
    }
}