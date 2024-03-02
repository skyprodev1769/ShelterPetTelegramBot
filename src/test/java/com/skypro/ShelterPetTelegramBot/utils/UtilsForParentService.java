package com.skypro.ShelterPetTelegramBot.utils;

import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Parent;

import java.util.List;

import static com.skypro.ShelterPetTelegramBot.utils.UtilsForPetService.PET_DOG;

public class UtilsForParentService {

    public static final Long ID_PARENT = 1L;
    public static final String FIRST_NAME = "Иван";
    public static final String LAST_NAME = "Иванов";
    public static final String PHONE_NUMBER = "89999999999";
    public static final String VALID_PHONE_NUMBER = "+7-999-999-99-99";
    public static final Parent PARENT =
            new Parent(FIRST_NAME,
                    LAST_NAME,
                    VALID_PHONE_NUMBER,
                    PET_DOG);

    public static List<Parent> getParents() {
        return List.of(PARENT);
    }

    public static final String INCORRECT_STRING = "abc";
}