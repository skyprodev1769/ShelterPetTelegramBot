package com.skypro.ShelterPetTelegramBot.service;

import com.skypro.ShelterPetTelegramBot.exception.DifferentTypesException;
import com.skypro.ShelterPetTelegramBot.exception.InvalideInputException;
import com.skypro.ShelterPetTelegramBot.exception.InvalideNumberException;
import com.skypro.ShelterPetTelegramBot.exception.PetStatusException;
import com.skypro.ShelterPetTelegramBot.exception.parent.ParentAlreadyAddedException;
import com.skypro.ShelterPetTelegramBot.exception.pet.PetAlreadyAddedException;
import com.skypro.ShelterPetTelegramBot.exception.shelter.ShelterAlreadyAddedException;
import com.skypro.ShelterPetTelegramBot.exception.volunteer.VolunteerAlreadyAddedException;
import com.skypro.ShelterPetTelegramBot.service.impl.CheckServiceImpl;
import com.skypro.ShelterPetTelegramBot.service.interfaces.CheckService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.skypro.ShelterPetTelegramBot.model.entity.enums.PetStatus.ADOPTED;
import static com.skypro.ShelterPetTelegramBot.model.entity.enums.PetStatus.FREE;
import static com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType.CAT;
import static com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType.DOG;
import static com.skypro.ShelterPetTelegramBot.utils.Exceptions.*;
import static com.skypro.ShelterPetTelegramBot.utils.UtilsForCheckService.*;
import static com.skypro.ShelterPetTelegramBot.utils.documentation.Codes.CODE_400;
import static org.junit.jupiter.api.Assertions.*;

class CheckServiceImplTest {

    private final CheckService service = new CheckServiceImpl();

    public static Stream<Arguments> provideParamsForCheckPhoneNumberTest() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(" "),
                Arguments.of(LONG_PHONE_NUMBER),
                Arguments.of(INCORRECT_STRING)
        );
    }

    public static Stream<Arguments> provideParamsForCheckNameAndAddressTest() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(" "),
                Arguments.of(INCORRECT_STRING)
        );
    }

    public static Stream<Arguments> provideParamsForCheckValueTest() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(0L),
                Arguments.of(-1L)
        );
    }

    @Test
    public void checkStatus_success() {
        assertTrue(service.checkStatus(FREE));
    }

    @Test
    public void checkStatus_PetStatusException() {
        String expected = String.format("%s %s", CODE_400, PET_STATUS);

        Exception actual = assertThrows(
                PetStatusException.class,
                () -> service.checkStatus(ADOPTED)
        );

        assertEquals(expected, actual.getMessage());
    }

    @Test
    public void checkTypes_success() {
        assertTrue(service.checkTypes(DOG, DOG));
    }

    @Test
    public void checkTypes_DifferentTypesException() {
        String expected = String.format("%s %s", CODE_400, DIFFERENT_TYPES);

        Exception actual = assertThrows(
                DifferentTypesException.class,
                () -> service.checkTypes(DOG, CAT)
        );

        assertEquals(expected, actual.getMessage());
    }

    @Test
    public void validatePhoneNumber_success() {
        String actual = service.validatePhoneNumber(CORRECT_PHONE_NUMBER);
        assertEquals(VALID_PHONE_NUMBER, actual);
    }

    @Test
    public void checkPhoneNumber_success() {
        assertTrue(service.checkPhoneNumber(CORRECT_PHONE_NUMBER));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForCheckPhoneNumberTest")
    public void checkPhoneNumber_InvalideNumberException(String phoneNumber) {
        String expected = String.format("%s %s", CODE_400, INVALIDE_NUMBER);

        Exception actual = assertThrows(
                InvalideNumberException.class,
                () -> service.checkPhoneNumber(phoneNumber)
        );

        assertEquals(expected, actual.getMessage());
    }

    @Test
    public void checkName_success() {
        assertTrue(service.checkName(CORRECT_NAME));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForCheckNameAndAddressTest")
    public void checkName_InvalideInputException(String name) {
        String expected = String.format("%s %s", CODE_400, INVALIDE_INPUT);

        Exception actual = assertThrows(
                InvalideInputException.class,
                () -> service.checkName(name)
        );

        assertEquals(expected, actual.getMessage());
    }

    @Test
    public void checkAddress_success() {
        assertTrue(service.checkAddress(CORRECT_ADDRESS));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForCheckNameAndAddressTest")
    public void checkAddress_InvalideInputException(String address) {
        String expected = String.format("%s %s", CODE_400, INVALIDE_INPUT);

        Exception actual = assertThrows(
                InvalideInputException.class,
                () -> service.checkAddress(address)
        );

        assertEquals(expected, actual.getMessage());
    }

    @Test
    public void checkValue_success() {
        assertTrue(service.checkValue(1L));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForCheckValueTest")
    public void checkValue_InvalideInputException(Long value) {
        String expected = String.format("%s %s", CODE_400, INVALIDE_INPUT);

        Exception actual = assertThrows(
                InvalideInputException.class,
                () -> service.checkValue(value)
        );

        assertEquals(expected, actual.getMessage());
    }

    @Test
    public void checkParentAlreadyAdded_success() {
        assertTrue(service.checkParentAlreadyAdded(PARENTS, PARENT));
    }

    @Test
    public void checkParentAlreadyAdded_ParentAlreadyAddedException() {
        PARENTS.add(PARENT);

        String expected = String.format("%s %s", CODE_400, PARENT_ALREADY_ADDED);

        Exception actual = assertThrows(
                ParentAlreadyAddedException.class,
                () -> service.checkParentAlreadyAdded(PARENTS, PARENT)
        );

        assertEquals(expected, actual.getMessage());

        PARENTS.remove(PARENT);
    }

    @Test
    public void checkPetAlreadyAdded_success() {
        assertTrue(service.checkPetAlreadyAdded(PETS, PET));
    }

    @Test
    public void checkPetAlreadyAdded_PetAlreadyAddedException() {
        PETS.add(PET);

        String expected = String.format("%s %s", CODE_400, PET_ALREADY_ADDED);

        Exception actual = assertThrows(
                PetAlreadyAddedException.class,
                () -> service.checkPetAlreadyAdded(PETS, PET)
        );

        assertEquals(expected, actual.getMessage());

        PETS.remove(PET);
    }

    @Test
    public void checkVolunteerAlreadyAdded_success() {
        assertTrue(service.checkVolunteerAlreadyAdded(VOLUNTEERS, VOLUNTEER));
    }

    @Test
    public void checkVolunteerAlreadyAdded_VolunteerAlreadyAddedException() {
        VOLUNTEERS.add(VOLUNTEER);

        String expected = String.format("%s %s", CODE_400, VOLUNTEER_ALREADY_ADDED);

        Exception actual = assertThrows(
                VolunteerAlreadyAddedException.class,
                () -> service.checkVolunteerAlreadyAdded(VOLUNTEERS, VOLUNTEER)
        );

        assertEquals(expected, actual.getMessage());

        VOLUNTEERS.remove(VOLUNTEER);
    }

    @Test
    public void checkShelterAlreadyAdded_success() {
        assertTrue(service.checkShelterAlreadyAdded(SHELTERS, SHELTER));
    }

    @Test
    public void checkShelterAlreadyAdded_ShelterAlreadyAddedException() {
        SHELTERS.add(SHELTER);

        String expected = String.format("%s %s", CODE_400, SHELTER_ALREADY_ADDED);

        Exception actual = assertThrows(
                ShelterAlreadyAddedException.class,
                () -> service.checkShelterAlreadyAdded(SHELTERS, SHELTER)
        );

        assertEquals(expected, actual.getMessage());

        SHELTERS.remove(SHELTER);
    }
}