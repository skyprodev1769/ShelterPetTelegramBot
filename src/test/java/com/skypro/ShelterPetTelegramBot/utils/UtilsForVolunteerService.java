package com.skypro.ShelterPetTelegramBot.utils;

import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Volunteer;

import java.util.List;

import static com.skypro.ShelterPetTelegramBot.utils.UtilsForShelterService.CAT_SHELTER;
import static com.skypro.ShelterPetTelegramBot.utils.UtilsForShelterService.DOG_SHELTER;

public class UtilsForVolunteerService {

    public static final Long ID_VOLUNTEER_DOG_SHELTER = 1L;
    public static final String FIRST_NAME_VOLUNTEER_DOG_SHELTER = "Иван";
    public static final String LAST_NAME_VOLUNTEER_DOG_SHELTER = "Иванов";
    public static final String PHONE_NUMBER_VOLUNTEER_DOG_SHELTER = "89999999999";
    public static final String VALID_PHONE_NUMBER_VOLUNTEER_DOG_SHELTER = "+7-999-999-99-99";
    public static final Volunteer VOLUNTEER_DOG_SHELTER =
            new Volunteer(FIRST_NAME_VOLUNTEER_DOG_SHELTER,
                    LAST_NAME_VOLUNTEER_DOG_SHELTER,
                    VALID_PHONE_NUMBER_VOLUNTEER_DOG_SHELTER,
                    DOG_SHELTER);

    public static final Long ID_VOLUNTEER_CAT_SHELTER = 2L;
    public static final String FIRST_NAME_VOLUNTEER_CAT_SHELTER = "Петр";
    public static final String LAST_NAME_VOLUNTEER_CAT_SHELTER = "Петров";
    public static final String PHONE_NUMBER_VOLUNTEER_CAT_SHELTER = "85555555555";
    public static final String VALID_PHONE_NUMBER_VOLUNTEER_CAT_SHELTER = "+7-555-555-55-55";
    public static final Volunteer VOLUNTEER_CAT_SHELTER =
            new Volunteer(FIRST_NAME_VOLUNTEER_CAT_SHELTER,
                    LAST_NAME_VOLUNTEER_CAT_SHELTER,
                    VALID_PHONE_NUMBER_VOLUNTEER_CAT_SHELTER,
                    CAT_SHELTER);

    public static List<Volunteer> getVolunteers() {
        return List.of(VOLUNTEER_DOG_SHELTER);
    }

    public static final String INCORRECT_STRING = "abc";
}