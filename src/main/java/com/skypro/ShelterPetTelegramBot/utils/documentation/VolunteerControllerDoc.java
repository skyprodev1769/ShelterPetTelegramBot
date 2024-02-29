package com.skypro.ShelterPetTelegramBot.utils.documentation;

import static com.skypro.ShelterPetTelegramBot.utils.Exceptions.*;

public class VolunteerControllerDoc {

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
                        "address": "ул. Планерная, д.1, стр.1"
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
                          "address": "ул. Планерная, д.1, стр.1"
                        }
                      }
                    ]
                                        
                    []""";

    public static final String EXAMPLE_ADD_VOLUNTEER_CODE_400 =
            "1.Передан некорректный id приюта для животных.\n" +
                    "2.Передано некорректное имя.\n" +
                    "3.Передана некорректная фамилия.\n" + INVALIDE_INPUT + "\n\n" +
                    "4.Передан некорректный номер телефона.\n" + INVALIDE_NUMBER + "\n\n" +
                    "5.Данный номер телефона уже был добавлен ранее.\n" + VOLUNTEER_ALREADY_ADDED;

    public static final String EXAMPLE_GET_BY_ID_VOLUNTEER_CODE_400 =
            "1.Передан некорректный id волонтера.\n" + INVALIDE_INPUT;

    public static final String EXAMPLE_GET_BY_ID_VOLUNTEER_CODE_404 =
            "1.Волонтер с данным id еще не был добавлен.\n" + VOLUNTEER_NOT_FOUND;

    public static final String EXAMPLE_GET_ALL_VOLUNTEERS_BY_PARAMETERS_CODE_400 =
            "1.Передан некорректный id приюта для животных.\n" +
                    "2.Передано некорректное имя.\n" +
                    "3.Передана некорректная фамилия.\n" + INVALIDE_INPUT;

    public static final String EXAMPLE_EDIT_VOLUNTEER_CODE_400 =
            "1.Передан некорректный id волонтера.\n" +
                    "2.Передан некорректный id приюта для животных.\n" +
                    "3.Передано некорректное имя.\n" +
                    "4.Передана некорректная фамилия.\n" + INVALIDE_INPUT + "\n\n" +
                    "5.Передан некорректный номер телефона.\n" + INVALIDE_NUMBER + "\n\n" +
                    "6.Данный номер телефона уже был добавлен ранее.\n" + VOLUNTEER_ALREADY_ADDED;

    public static final String EXAMPLE_EDIT_VOLUNTEER_CODE_404 =
            "1.Волонтер с данным id еще не был добавлен.\n" + VOLUNTEER_NOT_FOUND + "\n\n" +
                    "2.Приют с данным id еще не был добавлен.\n" + SHELTER_NOT_FOUND;
}