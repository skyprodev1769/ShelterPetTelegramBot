package com.skypro.ShelterPetTelegramBot.utils;

import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetStatus;
import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Pet;

import java.util.List;

import static com.skypro.ShelterPetTelegramBot.utils.UtilsForShelterService.CAT_SHELTER;
import static com.skypro.ShelterPetTelegramBot.utils.UtilsForShelterService.DOG_SHELTER;

public class UtilsForPetService {

    public static final Long ID_DOG = 1L;
    public static final String NAME_DOG = "Бобик";
    public static final Pet DOG = new Pet(PetType.DOG, PetStatus.FREE, NAME_DOG, DOG_SHELTER);

    public static final Long ID_CAT = 2L;
    public static final String NAME_CAT = "Рыжик";
    public static final Pet CAT = new Pet(PetType.CAT, PetStatus.FREE, NAME_CAT, CAT_SHELTER);

    public static List<Pet> getPets() {
        return List.of(DOG);
    }

    public static final String INCORRECT_NAME = "abc";
}