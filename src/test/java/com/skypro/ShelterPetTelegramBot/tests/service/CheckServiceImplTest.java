package com.skypro.ShelterPetTelegramBot.tests.service;

import com.skypro.ShelterPetTelegramBot.tests.Utils;
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.skypro.ShelterPetTelegramBot.model.enums.PetStatus.ADOPTED;
import static com.skypro.ShelterPetTelegramBot.model.enums.PetStatus.FREE;
import static com.skypro.ShelterPetTelegramBot.model.enums.PetType.CAT;
import static com.skypro.ShelterPetTelegramBot.model.enums.PetType.DOG;
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

        Assertions.assertEquals(Utils.exception(CODE_400, PET_STATUS), actual.getMessage());
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

        Assertions.assertEquals(Utils.exception(CODE_400, DIFFERENT_TYPES), actual.getMessage());
    }

    @Test
    void checkPhoneNumber_success() {
        assertTrue(service.checkPhoneNumber(Utils.PHONE_NUMBER));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForCheckPhoneNumberTest")
    void checkPhoneNumber_InvalideNumberException(String phoneNumber) {
        Exception actual = assertThrows(
                InvalideNumberException.class,
                () -> service.checkPhoneNumber(phoneNumber)
        );

        Assertions.assertEquals(Utils.exception(CODE_400, INVALIDE_NUMBER), actual.getMessage());
    }

    @Test
    void validatePhoneNumber_success() {
        String actual = service.validatePhoneNumber(Utils.PHONE_NUMBER);
        Assertions.assertEquals(Utils.VALID_PHONE_NUMBER, actual);
    }

    @Test
    void checkName_success() {
        assertTrue(service.checkName(Utils.NAME));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForCheckNameAndAddressTest")
    void checkName_InvalideInputException(String name) {
        Exception actual = assertThrows(
                InvalideInputException.class,
                () -> service.checkName(name)
        );

        Assertions.assertEquals(Utils.exception(CODE_400, INVALIDE_INPUT), actual.getMessage());
    }

    @Test
    void checkAddress_success() {
        assertTrue(service.checkAddress(Utils.ADDRESS));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForCheckNameAndAddressTest")
    void checkAddress_InvalideInputException(String address) {
        Exception actual = assertThrows(
                InvalideInputException.class,
                () -> service.checkAddress(address)
        );

        Assertions.assertEquals(Utils.exception(CODE_400, INVALIDE_INPUT), actual.getMessage());
    }

    @Test
    void checkValue_success() {
        assertTrue(service.checkValue(Utils.ID));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForCheckValueTest")
    void checkValue_InvalideInputException(Long value) {
        Exception actual = assertThrows(
                InvalideInputException.class,
                () -> service.checkValue(value)
        );

        Assertions.assertEquals(Utils.exception(CODE_400, INVALIDE_INPUT), actual.getMessage());
    }

    @Test
    void checkParentAlreadyAdded_success() {
        assertTrue(service.checkParentAlreadyAdded(Utils.EMPTY_PARENTS, Utils.PARENT));
    }

    @Test
    void checkParentAlreadyAdded_ParentAlreadyAddedException() {
        Exception actual = assertThrows(
                ParentAlreadyAddedException.class,
                () -> service.checkParentAlreadyAdded(Utils.PARENTS, Utils.PARENT)
        );

        Assertions.assertEquals(Utils.exception(CODE_400, PARENT_ALREADY_ADDED), actual.getMessage());
    }

    @Test
    void checkPetAlreadyAdded_success() {
        assertTrue(service.checkPetAlreadyAdded(Utils.EMPTY_PETS, Utils.PET));
    }

    @Test
    void checkPetAlreadyAdded_PetAlreadyAddedException() {
        Exception actual = assertThrows(
                PetAlreadyAddedException.class,
                () -> service.checkPetAlreadyAdded(Utils.PETS, Utils.PET)
        );

        Assertions.assertEquals(Utils.exception(CODE_400, PET_ALREADY_ADDED), actual.getMessage());
    }

    @Test
    void checkVolunteerAlreadyAdded_success() {
        assertTrue(service.checkVolunteerAlreadyAdded(Utils.EMPTY_VOLUNTEERS, Utils.VOLUNTEER));
    }

    @Test
    void checkVolunteerAlreadyAdded_VolunteerAlreadyAddedException() {
        Exception actual = assertThrows(
                VolunteerAlreadyAddedException.class,
                () -> service.checkVolunteerAlreadyAdded(Utils.VOLUNTEERS, Utils.VOLUNTEER)
        );

        Assertions.assertEquals(Utils.exception(CODE_400, VOLUNTEER_ALREADY_ADDED), actual.getMessage());
    }

    @Test
    void checkShelterAlreadyAdded_success() {
        assertTrue(service.checkShelterAlreadyAdded(Utils.EMPTY_SHELTERS, Utils.SHELTER));
    }

    @Test
    void checkShelterAlreadyAdded_ShelterAlreadyAddedException() {
        Exception actual = assertThrows(
                ShelterAlreadyAddedException.class,
                () -> service.checkShelterAlreadyAdded(Utils.SHELTERS, Utils.SHELTER)
        );

        Assertions.assertEquals(Utils.exception(CODE_400, SHELTER_ALREADY_ADDED), actual.getMessage());
    }

    private static Stream<Arguments> provideParamsForCheckPhoneNumberTest() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(Utils.EMPTY),
                Arguments.of(Utils.SHORT_PHONE_NUMBER),
                Arguments.of(Utils.LONG_PHONE_NUMBER),
                Arguments.of(Utils.INCORRECT_STRING)
        );
    }

    private static Stream<Arguments> provideParamsForCheckNameAndAddressTest() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(Utils.EMPTY),
                Arguments.of(Utils.INCORRECT_STRING)
        );
    }

    private static Stream<Arguments> provideParamsForCheckValueTest() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(Utils.ZERO),
                Arguments.of(Utils.INCORRECT_ID)
        );
    }
}