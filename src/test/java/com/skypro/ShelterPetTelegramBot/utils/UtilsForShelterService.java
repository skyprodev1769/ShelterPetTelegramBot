package com.skypro.ShelterPetTelegramBot.utils;

import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Shelter;

import java.util.List;

import static com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType.CAT;
import static com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType.DOG;

public class UtilsForShelterService {

    public static final Long ID_DOG_SHELTER = 1L;
    public static final String ADDRESS_DOG_SHELTER = "г. Астана, ул. Планерная, д.1, стр.1";
    public static final Shelter DOG_SHELTER = new Shelter(DOG, ADDRESS_DOG_SHELTER);

    public static final Long ID_CAT_SHELTER = 2L;
    public static final String ADDRESS_CAT_SHELTER = "г. Астана, ул. Ленина, д.13, стр.4";
    public static final Shelter CAT_SHELTER = new Shelter(CAT, ADDRESS_CAT_SHELTER);

    public static List<Shelter> getShelters() {
        return List.of(DOG_SHELTER);
    }

    public static final String INCORRECT_ADDRESS = "abc";
}