package com.skypro.ShelterPetTelegramBot.tests.service;

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
import static com.skypro.ShelterPetTelegramBot.tests.Utils.*;
import static com.skypro.ShelterPetTelegramBot.utils.Exceptions.*;
import static com.skypro.ShelterPetTelegramBot.utils.documentation.Codes.CODE_400;
import static org.junit.jupiter.api.Assertions.*;

class CheckServiceImplTest {

    private final CheckService service = new CheckServiceImpl();

    @Test
    void checkStatus_success() {
        assertTrue(service.checkStatus(FREE));
    }

    @Test
    void checkStatus_PetStatusException() {
        Exception actual = assertThrows(
                PetStatusException.class,
                () -> service.checkStatus(ADOPTED)
        );

        assertEquals(exception(CODE_400, PET_STATUS), actual.getMessage());
    }

    @Test
    void checkTypes_success() {
        assertTrue(service.checkTypes(DOG, DOG));
    }

    @Test
    void checkTypes_DifferentTypesException() {
        Exception actual = assertThrows(
                DifferentTypesException.class,
                () -> service.checkTypes(DOG, CAT)
        );

        assertEquals(exception(CODE_400, DIFFERENT_TYPES), actual.getMessage());
    }

    @Test
    void checkPhoneNumber_success() {
        assertTrue(service.checkPhoneNumber(PHONE_NUMBER));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForCheckPhoneNumberTest")
    void checkPhoneNumber_InvalideNumberException(String phoneNumber) {
        Exception actual = assertThrows(
                InvalideNumberException.class,
                () -> service.checkPhoneNumber(phoneNumber)
        );

        assertEquals(exception(CODE_400, INVALIDE_NUMBER), actual.getMessage());
    }

    @Test
    void validatePhoneNumber_success() {
        String actual = service.validatePhoneNumber(PHONE_NUMBER);
        assertEquals(VALID_PHONE_NUMBER, actual);
    }

    @Test
    void checkName_success() {
        assertTrue(service.checkName(NAME));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForCheckNameAndAddressTest")
    void checkName_InvalideInputException(String name) {
        Exception actual = assertThrows(
                InvalideInputException.class,
                () -> service.checkName(name)
        );

        assertEquals(exception(CODE_400, INVALIDE_INPUT), actual.getMessage());
    }

    @Test
    void checkAddress_success() {
        assertTrue(service.checkAddress(ADDRESS));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForCheckNameAndAddressTest")
    void checkAddress_InvalideInputException(String address) {
        Exception actual = assertThrows(
                InvalideInputException.class,
                () -> service.checkAddress(address)
        );

        assertEquals(exception(CODE_400, INVALIDE_INPUT), actual.getMessage());
    }

    @Test
    void checkValue_success() {
        assertTrue(service.checkValue(ID));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForCheckValueTest")
    void checkValue_InvalideInputException(Long value) {
        Exception actual = assertThrows(
                InvalideInputException.class,
                () -> service.checkValue(value)
        );

        assertEquals(exception(CODE_400, INVALIDE_INPUT), actual.getMessage());
    }

    @Test
    void checkParentAlreadyAdded_success() {
        assertTrue(service.checkParentAlreadyAdded(EMPTY_PARENTS, PARENT));
    }

    @Test
    void checkParentAlreadyAdded_ParentAlreadyAddedException() {
        Exception actual = assertThrows(
                ParentAlreadyAddedException.class,
                () -> service.checkParentAlreadyAdded(PARENTS, PARENT)
        );

        assertEquals(exception(CODE_400, PARENT_ALREADY_ADDED), actual.getMessage());
    }

    @Test
    void checkPetAlreadyAdded_success() {
        assertTrue(service.checkPetAlreadyAdded(EMPTY_PETS, PET));
    }

    @Test
    void checkPetAlreadyAdded_PetAlreadyAddedException() {
        Exception actual = assertThrows(
                PetAlreadyAddedException.class,
                () -> service.checkPetAlreadyAdded(PETS, PET)
        );

        assertEquals(exception(CODE_400, PET_ALREADY_ADDED), actual.getMessage());
    }

    @Test
    void checkVolunteerAlreadyAdded_success() {
        assertTrue(service.checkVolunteerAlreadyAdded(EMPTY_VOLUNTEERS, VOLUNTEER));
    }

    @Test
    void checkVolunteerAlreadyAdded_VolunteerAlreadyAddedException() {
        Exception actual = assertThrows(
                VolunteerAlreadyAddedException.class,
                () -> service.checkVolunteerAlreadyAdded(VOLUNTEERS, VOLUNTEER)
        );

        assertEquals(exception(CODE_400, VOLUNTEER_ALREADY_ADDED), actual.getMessage());
    }

    @Test
    void checkShelterAlreadyAdded_success() {
        assertTrue(service.checkShelterAlreadyAdded(EMPTY_SHELTERS, SHELTER));
    }

    @Test
    void checkShelterAlreadyAdded_ShelterAlreadyAddedException() {
        Exception actual = assertThrows(
                ShelterAlreadyAddedException.class,
                () -> service.checkShelterAlreadyAdded(SHELTERS, SHELTER)
        );

        assertEquals(exception(CODE_400, SHELTER_ALREADY_ADDED), actual.getMessage());
    }

    private static Stream<Arguments> provideParamsForCheckPhoneNumberTest() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(EMPTY),
                Arguments.of(SHORT_PHONE_NUMBER),
                Arguments.of(LONG_PHONE_NUMBER),
                Arguments.of(INCORRECT_STRING)
        );
    }

    private static Stream<Arguments> provideParamsForCheckNameAndAddressTest() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(EMPTY),
                Arguments.of(INCORRECT_STRING)
        );
    }

    private static Stream<Arguments> provideParamsForCheckValueTest() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(ZERO),
                Arguments.of(INCORRECT_ID)
        );
    }
}