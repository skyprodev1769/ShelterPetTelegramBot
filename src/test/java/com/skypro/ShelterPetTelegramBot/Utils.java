package com.skypro.ShelterPetTelegramBot;

import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Parent;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Pet;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Shelter;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Volunteer;

import java.util.ArrayList;
import java.util.Collection;

import static com.skypro.ShelterPetTelegramBot.model.entity.enums.PetStatus.FREE;
import static com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType.DOG;

public class Utils {

    public static final String INCORRECT_STRING = "abc";
    public static final String LONG_PHONE_NUMBER = "899999999999";

    public static final String CORRECT_ADDRESS = "г. Астана, ул. Планерная, д. 1, стр. 1";
    public static final String CORRECT_NAME = "Бобик";
    public static final String CORRECT_FIRST_NAME = "Иван";
    public static final String CORRECT_LAST_NAME = "Иванов";
    public static final String CORRECT_PHONE_NUMBER = "89999999999";
    public static final String VALID_PHONE_NUMBER = "+7-999-999-99-99";

    public static final Shelter SHELTER = new Shelter(DOG, CORRECT_ADDRESS);
    public static final Pet PET = new Pet(DOG, FREE, CORRECT_NAME, SHELTER);
    public static final Volunteer VOLUNTEER = new Volunteer(CORRECT_FIRST_NAME, CORRECT_LAST_NAME, VALID_PHONE_NUMBER, SHELTER);
    public static final Parent PARENT = new Parent(CORRECT_FIRST_NAME, CORRECT_LAST_NAME, VALID_PHONE_NUMBER, PET);

    public static final Collection<Shelter> SHELTERS = new ArrayList<>();
    public static final Collection<Pet> PETS = new ArrayList<>();
    public static final Collection<Volunteer> VOLUNTEERS = new ArrayList<>();
    public static final Collection<Parent> PARENTS = new ArrayList<>();
}