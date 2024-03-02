package com.skypro.ShelterPetTelegramBot.utils;

import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Shelter;

import java.util.List;

import static com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType.DOG;

public class UtilsForShelterService {

    public static final Long ID_SHELTER = 1L;
    public static final String ADDRESS = "г. Астана, ул. Планерная, д.1, стр.1";
    public static final Shelter SHELTER = new Shelter(DOG, ADDRESS);

    public static List<Shelter> getShelters() {
        return List.of(SHELTER);
    }

    public static final String NEW_ADDRESS = "г. Астана, ул. Ленина, д.13, стр.4";
    public static final String INCORRECT_ADDRESS = "abc";
}