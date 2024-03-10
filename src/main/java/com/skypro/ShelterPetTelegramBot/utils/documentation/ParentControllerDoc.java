package com.skypro.ShelterPetTelegramBot.utils.documentation;

import static com.skypro.ShelterPetTelegramBot.utils.Exceptions.*;
import static com.skypro.ShelterPetTelegramBot.utils.documentation.PetControllerDoc.*;
import static com.skypro.ShelterPetTelegramBot.utils.documentation.VolunteerControllerDoc.PHONE_NUMBER_ALREADY_ADDED;

public class ParentControllerDoc {

    public static final String ADD_PARENT = "Добавление нового усыновителя";
    public static final String GET_PARENT = "Получение усыновителя по ID";
    public static final String GET_ALL_PARENTS_BY_PARAMETERS = "Получение списка усыновителей по имени, фамилии или номеру телефона";
    public static final String GET_ALL_PARENTS = "Получение списка всех усыновителей";
    public static final String EDIT_PARENT = "Изменение данных усыновителя по ID";
    public static final String DELETE_PARENT = "Удаление усыновителя по ID";

    public static final String ID_PARENT = "ID усыновителя";
    public static final String FIRST_NAME_PARENT = "Имя усыновителя";
    public static final String LAST_NAME_PARENT = "Фамилия усыновителя";
    public static final String PHONE_NUMBER_PARENT = "Номер телефона усыновителя";

    public static final String ARG_PET = "Животное";

    public static final String EXAMPLE_PARENT =
            """
                    {
                      "id": 1,
                      "firstName": "Иван",
                      "lastName": "Иванов",
                      "phoneNumber": "+7-999-999-99-99",
                      "pet": {
                        "id": 1,
                        "type": "DOG",
                        "status": "ADOPTED",
                        "name": "Бобик",
                        "shelter": {
                          "id": 1,
                          "type": "DOG",
                          "address": "ул. Планерная, д. 1, стр. 1"
                        }
                      }
                    }""";

    public static final String EXAMPLE_ARRAYS_PARENTS =
            """
                    [
                      {
                        "id": 1,
                        "firstName": "Иван",
                        "lastName": "Иванов",
                        "phoneNumber": "+7-999-999-99-99",
                        "pet": {
                          "id": 1,
                          "type": "DOG",
                          "status": "ADOPTED",
                          "name": "Бобик",
                          "shelter": {
                            "id": 1,
                            "type": "DOG",
                            "address": "ул. Планерная, д. 1, стр. 1"
                          }
                        }
                      }
                    ]
                                        
                    []""";

    public static final String ID_PARENT_INCORRECT = "ПРИЧИНА: Передан некорректный ID усыновителя.\n";
    public static final String ID_PARENT_NOT_FOUND = "ПРИЧИНА: Усыновитель с данным ID еще не был добавлен.\n";
    public static final String FIRST_NAME_PARENT_INCORRECT = "ПРИЧИНА: Передано некорректное имя усыновителя.\n";
    public static final String LAST_NAME_PARENT_INCORRECT = "ПРИЧИНА: Передана некорректная фамилия усыновителя.\n";
    public static final String PHONE_NUMBER_PARENT_INCORRECT = "ПРИЧИНА: Передан некорректный номер телефона усыновителя.\n";
    public static final String STATUS_INCORRECT = "ПРИЧИНА: Статус животного не позволяет его усыновить.\n";

    public static final String ADD_PARENT_CODE_400 =
            FIRST_NAME_PARENT_INCORRECT + LAST_NAME_PARENT_INCORRECT + NAME_PET_INCORRECT + INVALIDE_INPUT + "\n\n" +
                    PHONE_NUMBER_PARENT_INCORRECT + INVALIDE_NUMBER + "\n\n" +
                    STATUS_INCORRECT + PET_STATUS + "\n\n" +
                    PHONE_NUMBER_ALREADY_ADDED + PARENT_ALREADY_ADDED;

    public static final String ADD_PARENT_CODE_404 =
            ID_PET_NOT_FOUND + PET_NOT_FOUND;

    public static final String GET_CODE_400 =
            ID_PARENT_INCORRECT + INVALIDE_INPUT;

    public static final String GET_CODE_404 =
            ID_PARENT_NOT_FOUND + PARENT_NOT_FOUND;

    public static final String GET_ALL_PARENTS_BY_PARAMETERS_CODE_400 =
            FIRST_NAME_PARENT_INCORRECT + LAST_NAME_PARENT_INCORRECT + INVALIDE_INPUT;

    public static final String EDIT_PARENT_CODE_400 =
            ID_PARENT_INCORRECT + FIRST_NAME_PARENT_INCORRECT + LAST_NAME_PARENT_INCORRECT + NAME_PET_INCORRECT + INVALIDE_INPUT + "\n\n" +
                    PHONE_NUMBER_PARENT_INCORRECT + INVALIDE_NUMBER + "\n\n" +
                    STATUS_INCORRECT + PET_STATUS + "\n\n" +
                    PHONE_NUMBER_ALREADY_ADDED + PARENT_ALREADY_ADDED;

    public static final String EDIT_PARENT_CODE_404 =
            ID_PARENT_NOT_FOUND + PARENT_NOT_FOUND + "\n\n" +
                    ID_PET_NOT_FOUND + PET_NOT_FOUND;
}