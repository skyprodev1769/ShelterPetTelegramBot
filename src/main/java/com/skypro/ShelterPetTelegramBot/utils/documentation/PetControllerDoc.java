package com.skypro.ShelterPetTelegramBot.utils.documentation;

import static com.skypro.ShelterPetTelegramBot.utils.Exceptions.*;
import static com.skypro.ShelterPetTelegramBot.utils.documentation.ShelterControllerDoc.ID_SHELTER_INCORRECT;
import static com.skypro.ShelterPetTelegramBot.utils.documentation.ShelterControllerDoc.ID_SHELTER_NOT_FOUND;

public class PetControllerDoc {

    public static final String ADD_PET = "Добавление нового животного";
    public static final String GET_PET = "Получение животного по ID";
    public static final String GET_ALL_PETS_BY_PARAMETERS = "Получение списка животных по имени, статусу, типу или ID приюта для животных";
    public static final String GET_ALL_PETS = "Получение списка всех животных";
    public static final String EDIT_PET = "Изменение данных животного по ID";
    public static final String DELETE_PET = "Удаление животного по ID";

    public static final String ID_PET = "ID животного";
    public static final String NAME_PET = "Имя животного";
    public static final String TYPE_PET = "Тип животного";
    public static final String STATUS_PET = "Статус животного";

    public static final String ARG_NAME = "Имя";
    public static final String ARG_STATUS = "Статус";

    public static final String EXAMPLE_NAME = "Бобик";

    public static final String EXAMPLE_PET =
            """
                    {
                      "id": 1,
                      "type": "DOG",
                      "status": "FREE",
                      "name": "Бобик",
                      "shelter": {
                        "id": 1,
                        "type": "DOG",
                        "address": "г. Астана, ул. Планерная, д. 1, стр. 1"
                      }
                    }""";

    public static final String EXAMPLE_ARRAYS_PETS =
            """
                    [
                      {
                        "id": 1,
                        "type": "DOG",
                        "status": "FREE",
                        "name": "Бобик",
                        "shelter": {
                          "id": 1,
                          "type": "DOG",
                          "address": "г. Астана, ул. Планерная, д. 1, стр. 1"
                        }
                      }
                    ]
                                        
                    []""";

    public static final String ID_PET_INCORRECT = "ПРИЧИНА: Передан некорректный ID животного.\n";
    public static final String ID_PET_NOT_FOUND = "ПРИЧИНА: Животное с данным ID еще не было добавлено.\n";
    public static final String NAME_PET_INCORRECT = "ПРИЧИНА: Передано некорректное имя животного.\n";
    public static final String TYPES_INCORRECT = "ПРИЧИНА: Несоответствуют типы животного и приюта для животных.\n";
    public static final String NAME_ALREADY_ADDED = "ПРИЧИНА: Данное имя уже было добавлено ранее.\n";

    public static final String ADD_PET_CODE_400 =
            ID_SHELTER_INCORRECT + NAME_PET_INCORRECT + INVALIDE_INPUT + "\n\n" +
                    TYPES_INCORRECT + DIFFERENT_TYPES + "\n\n" +
                    NAME_ALREADY_ADDED + PET_ALREADY_ADDED;

    public static final String GET_PET_CODE_400 =
            ID_PET_INCORRECT + INVALIDE_INPUT;

    public static final String GET_PET_CODE_404 =
            ID_PET_NOT_FOUND + PET_NOT_FOUND;

    public static final String GET_ALL_PETS_BY_PARAMETERS_CODE_400 =
            ID_SHELTER_INCORRECT + NAME_PET_INCORRECT + INVALIDE_INPUT;

    public static final String EDIT_PET_CODE_400 =
            ID_PET_INCORRECT + ID_SHELTER_INCORRECT + NAME_PET_INCORRECT + INVALIDE_INPUT + "\n\n" +
                    TYPES_INCORRECT + DIFFERENT_TYPES + "\n\n" +
                    NAME_ALREADY_ADDED + PET_ALREADY_ADDED;

    public static final String EDIT_PET_CODE_404 =
            ID_PET_NOT_FOUND + PET_NOT_FOUND + "\n\n" +
                    ID_SHELTER_NOT_FOUND + SHELTER_NOT_FOUND;
}