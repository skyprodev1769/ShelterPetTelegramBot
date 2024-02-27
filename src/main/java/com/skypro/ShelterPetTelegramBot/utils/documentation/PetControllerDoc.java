package com.skypro.ShelterPetTelegramBot.utils.documentation;

import static com.skypro.ShelterPetTelegramBot.utils.Exceptions.*;

public class PetControllerDoc {

    public static final String EXAMPLE_PET =
            """
                    {
                      "id": 1,
                      "type": "DOG",
                      "name": "Бобик",
                      "shelter": {
                        "id": 1,
                        "type": "DOG",
                        "address": "ул. Планерная, д.1, стр.1"
                      }
                    }""";

    public static final String EXAMPLE_ARRAYS_PETS =
            """
                    [
                      {
                        "id": 1,
                        "type": "DOG",
                        "name": "Бобик",
                        "shelter": {
                          "id": 1,
                          "type": "DOG",
                          "address": "ул. Планерная, д.1, стр.1"
                        }
                      }
                    ]
                                        
                    []""";

    public static final String EXAMPLE_ADD_PET_CODE_400 =
            "1.Передан некорректный id приюта для животных.\n" +
                    "2.Передано некорректное имя.\n" + INVALIDE_INPUT + "\n\n" +
                    "3.Несоответствуют типы животного и приюта для животных.\n" + DIFFERENT_TYPES + "\n\n" +
                    "4.Данное животное уже было добавлено ранее.\n" + PET_ALREADY_ADDED;

    public static final String EXAMPLE_GET_BY_ID_PET_CODE_400 =
            "1.Передан некорректный id животного.\n" + INVALIDE_INPUT;

    public static final String EXAMPLE_GET_BY_ID_PET_CODE_404 =
            "1.Животное с данным id еще не было добавлено.\n" + PET_NOT_FOUND;

    public static final String EXAMPLE_GET_ALL_PETS_BY_PARAMETERS_CODE_400 =
            "1.Передан некорректный id приюта для животных.\n" +
                    "2.Передано некорректное имя.\n" + INVALIDE_INPUT;

    public static final String EXAMPLE_EDIT_PET_CODE_400 =
            "1.Передан некорректный id животного.\n" +
                    "2.Передан некорректный id приюта для животных.\n" +
                    "3.Передано некорректное имя.\n" + INVALIDE_INPUT + "\n\n" +
                    "4.Несоответствуют типы животного и приюта для животных.\n" + DIFFERENT_TYPES + "\n\n" +
                    "5.Данное животное уже было добавлено ранее.\n" + PET_ALREADY_ADDED;

    public static final String EXAMPLE_EDIT_PET_CODE_404 =
            "1.Животное с данным id еще не было добавлено.\n" + PET_NOT_FOUND + "\n\n" +
                    "2.Приют с данным id еще не был добавлен.\n" + SHELTER_NOT_FOUND;
}