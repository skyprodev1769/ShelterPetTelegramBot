package com.skypro.ShelterPetTelegramBot.utils.documentation;

import static com.skypro.ShelterPetTelegramBot.utils.Exceptions.*;

public class ShelterControllerDoc {

    public static final String ADD_SHELTER = "Добавление нового приюта для животных";
    public static final String GET_SHELTER = "Получение приюта для животных по ID";
    public static final String GET_ALL_SHELTERS_BY_PARAMETERS = "Получение списка приютов для животных по типу или адресу";
    public static final String GET_ALL_SHELTERS = "Получение списка всех приютов для животных";
    public static final String EDIT_SHELTER = "Изменение данных приюта для животных по ID";
    public static final String DELETE_SHELTER = "Удаление приюта для животных по ID";

    public static final String ID_SHELTER = "ID приюта для животных";
    public static final String ADDRESS = "Адрес приюта для животных";
    public static final String TYPE_SHELTER = "Тип приюта для животных";

    public static final String ARG_ID = "ID";
    public static final String ARG_ADDRESS = "Адрес";
    public static final String ARG_TYPE = "Тип";

    public static final String EXAMPLE_ID = "1";
    public static final String EXAMPLE_ADDRESS = "г. Астана, ул. Планерная, д. 1, стр. 1";

    public static final String EXAMPLE_SHELTER =
            """
                    {
                      "id": 1,
                      "type": "DOG",
                      "address": "г. Астана, ул. Планерная, д. 1, стр. 1"
                    }""";

    public static final String EXAMPLE_ARRAYS_SHELTERS =
            """
                    [
                      {
                        "id": 1,
                        "type": "DOG",
                        "address": "г. Астана, ул. Планерная, д. 1, стр. 1"
                      }
                    ]
                                        
                    []""";

    public static final String ID_SHELTER_INCORRECT = "ПРИЧИНА: Передан некорректный ID приюта.\n";
    public static final String ID_SHELTER_NOT_FOUND = "ПРИЧИНА: Приют с данным ID еще не был добавлен.\n";
    public static final String ADDRESS_INCORRECT = "ПРИЧИНА: Передан некорректный адрес приюта.\n";
    public static final String ADDRESS_ALREADY_ADDED = "ПРИЧИНА: Данный адрес уже был добавлен ранее.\n";

    public static final String ADD_SHELTER_CODE_400 =
            ADDRESS_INCORRECT + INVALIDE_INPUT + "\n\n" +
                    ADDRESS_ALREADY_ADDED + SHELTER_ALREADY_ADDED;

    public static final String GET_SHELTER_CODE_400 =
            ID_SHELTER_INCORRECT + INVALIDE_INPUT;

    public static final String GET_SHELTER_CODE_404 =
            ID_SHELTER_NOT_FOUND + SHELTER_NOT_FOUND;

    public static final String GET_ALL_SHELTERS_BY_PARAMETERS_CODE_400 =
            ADDRESS_INCORRECT + INVALIDE_INPUT;

    public static final String EDIT_SHELTER_CODE_400 =
            ID_SHELTER_INCORRECT + ADDRESS_INCORRECT + INVALIDE_INPUT + "\n\n" +
                    ADDRESS_ALREADY_ADDED + SHELTER_ALREADY_ADDED;
}