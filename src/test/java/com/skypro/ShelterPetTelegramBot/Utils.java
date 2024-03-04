package com.skypro.ShelterPetTelegramBot;

import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Parent;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Pet;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Shelter;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Volunteer;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

import static com.skypro.ShelterPetTelegramBot.model.entity.enums.PetStatus.FREE;
import static com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType.CAT;
import static com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType.DOG;

public class Utils {

    /*
    НЕКОРРЕКТНЫЕ ЗНАЧЕНИЯ
     */

    public static final String EMPTY = " ";
    public static final String INCORRECT_STRING = "abc";
    public static final String SHORT_PHONE_NUMBER = "8999999999";
    public static final String LONG_PHONE_NUMBER = "899999999999";
    public static final Long ZERO = 0L;
    public static final Long INCORRECT_ID = -1L;

    /*
    КОРРЕКТНЫЕ ЗНАЧЕНИЯ
     */

    public static final Long ID = 1L;
    public static final String ADDRESS = "г. Астана, ул. Планерная, д.1, стр.1";
    public static final String NAME = "Бобик";
    public static final String FIRST_NAME = "Иван";
    public static final String LAST_NAME = "Иванов";
    public static final String PHONE_NUMBER = "89999999999";
    public static final String VALID_PHONE_NUMBER = "+7-999-999-99-99";

    /*
    НОВЫЕ ЗНАЧЕНИЯ
     */

    public static final String NEW_ADDRESS = "г. Астана, ул. Ленина, д.13, стр.4";
    public static final String NEW_NAME = "Рыжик";
    public static final String NEW_FIRST_NAME = "Петр";
    public static final String NEW_LAST_NAME = "Петров";
    public static final String NEW_PHONE_NUMBER = "85555555555";
    public static final String NEW_VALID_PHONE_NUMBER = "+7-555-555-55-55";
    public static final String NUMBER_FOR_SEARCH = "999";

    /*
    ОБЪЕКТЫ
     */

    public static final Shelter SHELTER = new Shelter(DOG, ADDRESS);
    public static final Shelter NEW_SHELTER = new Shelter(CAT, NEW_ADDRESS);
    public static final Pet PET = new Pet(DOG, FREE, NAME, SHELTER);
    public static final Pet NEW_PET = new Pet(DOG, FREE, NEW_NAME, SHELTER);
    public static final Volunteer VOLUNTEER = new Volunteer(FIRST_NAME, LAST_NAME, VALID_PHONE_NUMBER, SHELTER);
    public static final Parent PARENT = new Parent(FIRST_NAME, LAST_NAME, VALID_PHONE_NUMBER, PET);

    /*
    КОЛЛЕКЦИИ
     */

    public static final List<Shelter> EMPTY_SHELTERS = new ArrayList<>();
    public static final List<Pet> EMPTY_PETS = new ArrayList<>();
    public static final List<Volunteer> EMPTY_VOLUNTEERS = new ArrayList<>();
    public static final List<Parent> EMPTY_PARENTS = new ArrayList<>();

    public static final List<Shelter> SHELTERS = List.of(SHELTER);
    public static final List<Pet> PETS = List.of(PET);
    public static final List<Volunteer> VOLUNTEERS = List.of(VOLUNTEER);
    public static final List<Parent> PARENTS = List.of(PARENT);

    /*
    ДОПОЛНИТЕЛЬНЫЕ МЕТОДЫ
     */

    public static String exception(HttpStatus status, String message) {
        return String.format("Code: %s. Error: %s", status, message);
    }

    public static String exception(String code, String message) {
        return String.format("%s %s", code, message);
    }
}