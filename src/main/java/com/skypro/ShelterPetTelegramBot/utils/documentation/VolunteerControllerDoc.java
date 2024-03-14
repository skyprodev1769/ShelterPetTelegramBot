package com.skypro.ShelterPetTelegramBot.utils.documentation;

import static com.skypro.ShelterPetTelegramBot.utils.Exceptions.*;
import static com.skypro.ShelterPetTelegramBot.utils.documentation.ShelterControllerDoc.ID_SHELTER_INCORRECT;
import static com.skypro.ShelterPetTelegramBot.utils.documentation.ShelterControllerDoc.ID_SHELTER_NOT_FOUND;

public class VolunteerControllerDoc {

    public static final String ADD_VOLUNTEER = "Добавление нового волонтера";
    public static final String GET_VOLUNTEER = "Получение волонтера по ID";
    public static final String GET_ALL_VOLUNTEERS_BY_PARAMETERS = "Получение списка волонтеров по имени, фамилии, номеру телефона или ID приюта для животных";
    public static final String GET_ALL_VOLUNTEERS = "Получение списка всех волонтеров";
    public static final String EDIT_VOLUNTEER = "Изменение данных волонтера по ID";
    public static final String DELETE_VOLUNTEER = "Удаление волонтера по ID";

    public static final String ID_VOLUNTEER = "ID волонтера";
    public static final String FIRST_NAME_VOLUNTEER = "Имя волонтера";
    public static final String LAST_NAME_VOLUNTEER = "Фамилия волонтера";
    public static final String PHONE_NUMBER_VOLUNTEER = "Номер телефона волонтера";

    public static final String ARG_FIRST_NAME = "Имя";
    public static final String ARG_LAST_NAME = "Фамилия";
    public static final String ARG_PHONE_NUMBER = "Номер";
    public static final String ARG_SHELTER = "Приют";

    public static final String EXAMPLE_FIRST_NAME = "Иван";
    public static final String EXAMPLE_LAST_NAME = "Иванов";
    public static final String EXAMPLE_PHONE_NUMBER = "89999999999";
    public static final String EXAMPLE_SEARCH_PHONE_NUMBER = "+7-999-999-99-99";

    public static final String EXAMPLE_VOLUNTEER =
            """
                    {
                      "id": 1,
                      "firstName": "Иван",
                      "lastName": "Иванов",
                      "phoneNumber": "+7-999-999-99-99",
                      "shelter": {
                        "id": 1,
                        "type": "DOG",
                        "address": "г. Астана, ул. Планерная, д. 1, стр. 1"
                      }
                    }""";

    public static final String EXAMPLE_ARRAYS_VOLUNTEERS =
            """
                    [
                      {
                        "id": 1,
                        "firstName": "Иван",
                        "lastName": "Иванов",
                        "phoneNumber": "+7-999-999-99-99",
                        "shelter": {
                          "id": 1,
                          "type": "DOG",
                          "address": "г. Астана, ул. Планерная, д. 1, стр. 1"
                        }
                      }
                    ]
                                        
                    []""";

    public static final String ID_VOLUNTEER_INCORRECT = "ПРИЧИНА: Передан некорректный ID волонтера.\n";
    public static final String ID_VOLUNTEER_NOT_FOUND = "ПРИЧИНА: Волонтер с данным ID еще не был добавлен.\n";
    public static final String FIRST_NAME_VOLUNTEER_INCORRECT = "ПРИЧИНА: Передано некорректное имя волонтера.\n";
    public static final String LAST_NAME_VOLUNTEER_INCORRECT = "ПРИЧИНА: Передана некорректная фамилия волонтера.\n";
    public static final String PHONE_NUMBER_VOLUNTEER_INCORRECT = "ПРИЧИНА: Передан некорректный номер телефона волонтера.\n";
    public static final String PHONE_NUMBER_ALREADY_ADDED = "ПРИЧИНА: Данный номер телефона уже был добавлен ранее.\n";

    public static final String ADD_VOLUNTEER_CODE_400 =
            ID_SHELTER_INCORRECT + FIRST_NAME_VOLUNTEER_INCORRECT + LAST_NAME_VOLUNTEER_INCORRECT + INVALIDE_INPUT + "\n\n" +
                    PHONE_NUMBER_VOLUNTEER_INCORRECT + INVALIDE_NUMBER + "\n\n" +
                    PHONE_NUMBER_ALREADY_ADDED + VOLUNTEER_ALREADY_ADDED;

    public static final String GET_VOLUNTEER_CODE_400 =
            ID_VOLUNTEER_INCORRECT + INVALIDE_INPUT;

    public static final String GET_VOLUNTEER_CODE_404 =
            ID_VOLUNTEER_NOT_FOUND + VOLUNTEER_NOT_FOUND;

    public static final String GET_ALL_VOLUNTEERS_BY_PARAMETERS_CODE_400 =
            ID_SHELTER_INCORRECT + FIRST_NAME_VOLUNTEER_INCORRECT + LAST_NAME_VOLUNTEER_INCORRECT + INVALIDE_INPUT;

    public static final String EDIT_VOLUNTEER_CODE_400 =
            ID_VOLUNTEER_INCORRECT + ID_SHELTER_INCORRECT + FIRST_NAME_VOLUNTEER_INCORRECT + LAST_NAME_VOLUNTEER_INCORRECT + INVALIDE_INPUT + "\n\n" +
                    PHONE_NUMBER_VOLUNTEER_INCORRECT + INVALIDE_NUMBER + "\n\n" +
                    PHONE_NUMBER_ALREADY_ADDED + VOLUNTEER_ALREADY_ADDED;

    public static final String EDIT_VOLUNTEER_CODE_404 =
            ID_VOLUNTEER_NOT_FOUND + VOLUNTEER_NOT_FOUND + "\n\n" +
                    ID_SHELTER_NOT_FOUND + SHELTER_NOT_FOUND;
}