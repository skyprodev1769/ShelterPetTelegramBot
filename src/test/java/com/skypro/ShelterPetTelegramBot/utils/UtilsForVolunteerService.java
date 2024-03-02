package com.skypro.ShelterPetTelegramBot.utils;

import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Volunteer;

import java.util.List;

import static com.skypro.ShelterPetTelegramBot.utils.UtilsForShelterService.SHELTER;

public class UtilsForVolunteerService {

    public static final Long ID_VOLUNTEER = 1L;
    public static final String FIRST_NAME = "Иван";
    public static final String LAST_NAME = "Иванов";
    public static final String PHONE_NUMBER = "89999999999";
    public static final String VALID_PHONE_NUMBER = "+7-999-999-99-99";
    public static final Volunteer VOLUNTEER =
            new Volunteer(FIRST_NAME,
                    LAST_NAME,
                    VALID_PHONE_NUMBER,
                    SHELTER);

    public static List<Volunteer> getVolunteers() {
        return List.of(VOLUNTEER);
    }

    public static final String INCORRECT_STRING = "abc";
}