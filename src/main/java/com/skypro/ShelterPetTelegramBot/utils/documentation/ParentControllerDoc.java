package com.skypro.ShelterPetTelegramBot.utils.documentation;

import static com.skypro.ShelterPetTelegramBot.utils.Exceptions.*;

public class ParentControllerDoc {

    public static final String EXAMPLE_PARENT =
            """
                    {
                      "id": 1,
                      "firstName": "Петр",
                      "lastName": "Петров",
                      "phoneNumber": "87777777777",
                      "volunteer": {
                        "id": 1,
                        "firstName": "Иван",
                        "lastName": "Иванов",
                        "phoneNumber": "89999999999",
                        "shelter": {
                          "id": 1,
                          "type": "DOG",
                          "address": "ул. Планерная, д.1, стр.1"
                        }
                      },
                      "pet": {
                        "id": 1,
                        "type": "DOG",
                        "name": "Бобик",
                        "shelter": {
                          "id": 1,
                          "type": "DOG",
                          "address": "ул. Планерная, д.1, стр.1"
                        }
                      }
                    }""";

    public static final String EXAMPLE_ARRAYS_PARENTS =
            """
                    [
                      {
                        "id": 1,
                        "firstName": "Петр",
                        "lastName": "Петров",
                        "phoneNumber": "87777777777",
                        "volunteer": {
                          "id": 1,
                          "firstName": "Иван",
                          "lastName": "Иванов",
                          "phoneNumber": "89999999999",
                          "shelter": {
                            "id": 1,
                            "type": "DOG",
                            "address": "ул. Планерная, д.1, стр.1"
                          }
                        },
                        "pet": {
                          "id": 1,
                          "type": "DOG",
                          "name": "Бобик",
                          "shelter": {
                            "id": 1,
                            "type": "DOG",
                            "address": "ул. Планерная, д.1, стр.1"
                          }
                        }
                      }
                    ]
                                        
                    []""";

    public static final String EXAMPLE_ADD_PARENT_CODE_400 =
            "1.Передан некорректный id волонтера.\n" +
                    "2.Передан некорректный id животного.\n" +
                    "3.Передано некорректное имя.\n" +
                    "4.Передана некорректная фамилия.\n" + INVALIDE_INPUT + "\n\n" +
                    "5.Передан некорректный номер телефона.\n" + INVALIDE_NUMBER + "\n\n" +
                    "6.Несоответствуют приюты животного и волонтера.\n" + DIFFERENT_SHELTERS + "\n\n" +
                    "7.Данный номер телефона уже был добавлен ранее.\n" + NUMBER_ALREADY_ADDED + "\n\n" +
                    "8.Данный усыновитель уже был добавлен ранее.\n" + PARENT_ALREADY_ADDED;

    public static final String EXAMPLE_ADD_PARENT_CODE_404 =
            "1.Волонтер с данным id еще не был добавлен.\n" + VOLUNTEER_NOT_FOUND + "\n\n" +
                    "2.Животное с данным id еще не было добавлено.\n" + PET_NOT_FOUND;

    public static final String EXAMPLE_GET_BY_ID_PARENT_CODE_400 =
            "1.Передан некорректный id усыновителя.\n" + INVALIDE_INPUT;

    public static final String EXAMPLE_GET_BY_ID_PARENT_CODE_404 =
            "1.Усыновитель с данным id еще не был добавлен.\n" + PARENT_NOT_FOUND;

    public static final String EXAMPLE_GET_ALL_PARENTS_BY_PARAMETERS_CODE_400 =
            "1.Передан некорректный id волонтера.\n" +
                    "2.Передано некорректное имя.\n" +
                    "3.Передана некорректная фамилия.\n" + INVALIDE_INPUT;

    public static final String EXAMPLE_EDIT_PARENT_CODE_400 =
            "1.Передан некорректный id усыновителя.\n" +
                    "2.Передан некорректный id волонтера.\n" +
                    "3.Передан некорректный id животного.\n" +
                    "4.Передано некорректное имя.\n" +
                    "5.Передана некорректная фамилия.\n" + INVALIDE_INPUT + "\n\n" +
                    "6.Передан некорректный номер телефона.\n" + INVALIDE_NUMBER + "\n\n" +
                    "6.Несоответствуют приюты животного и волонтера.\n" + DIFFERENT_SHELTERS + "\n\n" +
                    "8.Данный номер телефона уже был добавлен ранее.\n" + NUMBER_ALREADY_ADDED + "\n\n" +
                    "9.Данный усыновитель уже был добавлен ранее.\n" + PARENT_ALREADY_ADDED;

    public static final String EXAMPLE_EDIT_PARENT_CODE_404 =
            "1.Усыновитель с данным id еще не был добавлен.\n" + PARENT_NOT_FOUND + "\n\n" +
                    "1.Волонтер с данным id еще не был добавлен.\n" + VOLUNTEER_NOT_FOUND + "\n\n" +
                    "2.Животное с данным id еще не было добавлено.\n" + PET_NOT_FOUND;
}