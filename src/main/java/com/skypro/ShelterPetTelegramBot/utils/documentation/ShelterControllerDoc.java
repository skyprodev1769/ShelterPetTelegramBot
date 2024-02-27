package com.skypro.ShelterPetTelegramBot.utils.documentation;

import static com.skypro.ShelterPetTelegramBot.utils.Exceptions.*;

public class ShelterControllerDoc {

    public static final String EXAMPLE_SHELTER =
            """
                    {
                      "id": 1,
                      "type": "DOG",
                      "address": "ул. Планерная, д.1, стр.1"
                    }""";

    public static final String EXAMPLE_ARRAYS_SHELTERS =
            """
                    [
                      {
                        "id": 1,
                        "type": "DOG",
                        "address": "ул. Планерная, д.1, стр.1"
                      }
                    ]
                                        
                    []""";

    public static final String EXAMPLE_ADD_SHELTER_CODE_400 =
            "1.Передан некорректный адрес.\n" + INVALIDE_INPUT + "\n\n" +
                    "2.Данный приют уже был добавлен ранее.\n" + SHELTER_ALREADY_ADDED;

    public static final String EXAMPLE_GET_BY_ID_SHELTER_CODE_400 =
            "1.Передан некорректный id.\n" + INVALIDE_INPUT;

    public static final String EXAMPLE_GET_BY_ID_SHELTER_CODE_404 =
            "1.Приют с данным id еще не был добавлен.\n" + SHELTER_NOT_FOUND;

    public static final String EXAMPLE_GET_ALL_SHELTERS_BY_PARAMETERS_CODE_400 =
            "1.Передан некорректный адрес.\n" + INVALIDE_INPUT;

    public static final String EXAMPLE_EDIT_SHELTER_CODE_400 =
            "1.Передан некорректный id.\n" +
                    "2.Передан некорректный адрес.\n" + INVALIDE_INPUT + "\n\n" +
                    "3.Данный приют уже был добавлен ранее.\n" + SHELTER_ALREADY_ADDED;
}