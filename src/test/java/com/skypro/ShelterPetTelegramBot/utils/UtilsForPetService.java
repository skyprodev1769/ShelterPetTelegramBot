package com.skypro.ShelterPetTelegramBot.utils;

import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetStatus;
import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Pet;

import java.util.List;

import static com.skypro.ShelterPetTelegramBot.utils.UtilsForShelterService.SHELTER;

public class UtilsForPetService {

    public static final Long ID_PET = 1L;
    public static final String NAME = "Бобик";
    public static final Pet PET_DOG = new Pet(PetType.DOG, PetStatus.FREE, NAME, SHELTER);

    public static List<Pet> getPets() {
        return List.of(PET_DOG);
    }

    public static final String INCORRECT_NAME = "abc";
}