package com.skypro.ShelterPetTelegramBot.tests.controller.TestRestTemplate;

import com.skypro.ShelterPetTelegramBot.controller.PetController;
import com.skypro.ShelterPetTelegramBot.controller.ShelterController;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Pet;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import java.util.stream.Stream;

import static com.skypro.ShelterPetTelegramBot.model.enums.PetStatus.ADOPTED;
import static com.skypro.ShelterPetTelegramBot.model.enums.PetStatus.FREE;
import static com.skypro.ShelterPetTelegramBot.model.enums.PetType.CAT;
import static com.skypro.ShelterPetTelegramBot.model.enums.PetType.DOG;
import static com.skypro.ShelterPetTelegramBot.tests.Utils.*;
import static com.skypro.ShelterPetTelegramBot.utils.Exceptions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PetControllerTest {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate template;
    @Autowired
    private PetController petController;
    @Autowired
    private ShelterController shelterController;

    @Test
    void contextLoads() {
        Assertions.assertThat(petController).isNotNull();
        Assertions.assertThat(shelterController).isNotNull();
    }

    @Test
    void add_success() {
        addShelter();

        Pet actual = this.template.postForObject("http://localhost:" + port
                        + "/pet"
                        + "?Тип=" + DOG
                        + "&Статус=" + FREE
                        + "&Имя=" + NAME
                        + "&Приют=" + SHELTER.getId(),
                PET,
                Pet.class);

        PET.setId(actual.getId());

        deletePet(PET.getId());
        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertEquals(PET, actual);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForName")
    void add_InvalideInputException(String name) {
        String actual = this.template.postForObject("http://localhost:" + port
                        + "/pet"
                        + "?Тип=" + DOG
                        + "&Статус=" + FREE
                        + "&Имя=" + name
                        + "&Приют=" + ID,
                PET,
                String.class);

        assertNotNull(actual);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForId")
    void add_InvalideInputException(Long id) {
        String actual = this.template.postForObject("http://localhost:" + port
                        + "/pet"
                        + "?Тип=" + DOG
                        + "&Статус=" + FREE
                        + "&Имя=" + NAME
                        + "&Приют=" + id,
                PET,
                String.class);

        assertNotNull(actual);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual);
    }

    @Test
    void add_ShelterNotFoundException() {
        String actual = this.template.postForObject("http://localhost:" + port
                        + "/pet"
                        + "?Тип=" + DOG
                        + "&Статус=" + FREE
                        + "&Имя=" + NAME
                        + "&Приют=" + ID,
                PET,
                String.class);

        assertNotNull(actual);
        assertEquals(exception(NOT_FOUND, SHELTER_NOT_FOUND), actual);
    }

    @Test
    void add_DifferentTypesException() {
        addShelter();
        addNewShelter();

        String actual_1 = this.template.postForObject("http://localhost:" + port
                        + "/pet"
                        + "?Тип=" + CAT
                        + "&Статус=" + FREE
                        + "&Имя=" + NAME
                        + "&Приют=" + SHELTER.getId(),
                PET,
                String.class);

        String actual_2 = this.template.postForObject("http://localhost:" + port
                        + "/pet"
                        + "?Тип=" + DOG
                        + "&Статус=" + FREE
                        + "&Имя=" + NAME
                        + "&Приют=" + NEW_SHELTER.getId(),
                PET,
                String.class);

        deleteShelter(NEW_SHELTER.getId());
        deleteShelter(SHELTER.getId());

        assertNotNull(actual_1);
        assertNotNull(actual_2);
        assertEquals(exception(BAD_REQUEST, DIFFERENT_TYPES), actual_1);
        assertEquals(exception(BAD_REQUEST, DIFFERENT_TYPES), actual_2);
    }

    @Test
    void add_PetAlreadyAddedException() {
        addShelter();
        addPet();

        String actual = this.template.postForObject("http://localhost:" + port
                        + "/pet"
                        + "?Тип=" + DOG
                        + "&Статус=" + FREE
                        + "&Имя=" + NAME
                        + "&Приют=" + SHELTER.getId(),
                PET,
                String.class);

        deletePet(PET.getId());
        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertEquals(exception(BAD_REQUEST, PET_ALREADY_ADDED), actual);
    }

    @Test
    void getById_success() {
        addShelter();
        addPet();

        Pet actual = this.template.getForObject("http://localhost:" + port
                        + "/pet/" + PET.getId(),
                Pet.class);

        deletePet(PET.getId());
        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertEquals(PET, actual);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForId")
    void getById_InvalideInputException(Long id) {
        String actual = this.template.getForObject("http://localhost:" + port
                        + "/pet/" + id,
                String.class);

        assertNotNull(actual);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual);
    }

    @Test
    void getById_PetNotFoundException() {
        String actual = this.template.getForObject("http://localhost:" + port
                        + "/pet/" + ID,
                String.class);

        assertNotNull(actual);
        assertEquals(exception(NOT_FOUND, PET_NOT_FOUND), actual);
    }

    @Test
    void getAllByParameters_success() {
        addShelter();
        addPet();

        String actual_1 = this.template.getForObject("http://localhost:" + port
                        + "/pet"
                        + "?Тип=" + DOG,
                String.class);

        String actual_2 = this.template.getForObject("http://localhost:" + port
                        + "/pet"
                        + "?Статус=" + FREE,
                String.class);

        String actual_3 = this.template.getForObject("http://localhost:" + port
                        + "/pet"
                        + "?Имя=" + NAME,
                String.class);

        String actual_4 = this.template.getForObject("http://localhost:" + port
                        + "/pet"
                        + "?Приют=" + SHELTER.getId(),
                String.class);

        String actual_5 = this.template.getForObject("http://localhost:" + port
                        + "/pet"
                        + "?Тип=" + DOG
                        + "&Статус=" + FREE,
                String.class);

        String actual_6 = this.template.getForObject("http://localhost:" + port
                        + "/pet"
                        + "?Тип=" + DOG
                        + "&Имя=" + NAME,
                String.class);

        String actual_7 = this.template.getForObject("http://localhost:" + port
                        + "/pet"
                        + "?Тип=" + DOG
                        + "&Приют=" + SHELTER.getId(),
                String.class);

        String actual_8 = this.template.getForObject("http://localhost:" + port
                        + "/pet"
                        + "?Статус=" + FREE
                        + "&Имя=" + NAME,
                String.class);

        String actual_9 = this.template.getForObject("http://localhost:" + port
                        + "/pet"
                        + "?Статус=" + FREE
                        + "&Приют=" + SHELTER.getId(),
                String.class);

        String actual_10 = this.template.getForObject("http://localhost:" + port
                        + "/pet"
                        + "?Имя=" + NAME
                        + "&Приют=" + SHELTER.getId(),
                String.class);

        String actual_11 = this.template.getForObject("http://localhost:" + port
                        + "/pet"
                        + "?Тип=" + DOG
                        + "&Статус=" + FREE
                        + "&Имя=" + NAME,
                String.class);

        String actual_12 = this.template.getForObject("http://localhost:" + port
                        + "/pet"
                        + "?Тип=" + DOG
                        + "&Статус=" + FREE
                        + "&Приют=" + SHELTER.getId(),
                String.class);

        String actual_13 = this.template.getForObject("http://localhost:" + port
                        + "/pet"
                        + "?Тип=" + DOG
                        + "&Имя=" + NAME
                        + "&Приют=" + SHELTER.getId(),
                String.class);

        String actual_14 = this.template.getForObject("http://localhost:" + port
                        + "/pet"
                        + "?Статус=" + FREE
                        + "&Имя=" + NAME
                        + "&Приют=" + SHELTER.getId(),
                String.class);

        String actual_15 = this.template.getForObject("http://localhost:" + port
                        + "/pet"
                        + "?Тип=" + DOG
                        + "&Статус=" + FREE
                        + "&Имя=" + NAME
                        + "&Приют=" + SHELTER.getId(),
                String.class);

        deletePet(PET.getId());
        deleteShelter(SHELTER.getId());

        assertNotNull(actual_1);
        assertNotNull(actual_2);
        assertNotNull(actual_3);
        assertNotNull(actual_4);
        assertNotNull(actual_5);
        assertNotNull(actual_6);
        assertNotNull(actual_7);
        assertNotNull(actual_8);
        assertNotNull(actual_9);
        assertNotNull(actual_10);
        assertNotNull(actual_11);
        assertNotNull(actual_12);
        assertNotNull(actual_13);
        assertNotNull(actual_14);
        assertNotNull(actual_15);

        assertTrueForCollection(actual_1);
        assertTrueForCollection(actual_2);
        assertTrueForCollection(actual_3);
        assertTrueForCollection(actual_4);
        assertTrueForCollection(actual_5);
        assertTrueForCollection(actual_6);
        assertTrueForCollection(actual_7);
        assertTrueForCollection(actual_8);
        assertTrueForCollection(actual_9);
        assertTrueForCollection(actual_10);
        assertTrueForCollection(actual_11);
        assertTrueForCollection(actual_12);
        assertTrueForCollection(actual_13);
        assertTrueForCollection(actual_14);
        assertTrueForCollection(actual_15);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForName")
    void getAllByParameters_InvalideInputException(String name) {
        String actual_1 = this.template.getForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Имя=" + name,
                String.class);

        String actual_2 = this.template.getForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Фамилия=" + name,
                String.class);

        String actual_3 = this.template.getForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Имя=" + name
                        + "&Фамилия=" + LAST_NAME,
                String.class);

        String actual_4 = this.template.getForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Имя=" + FIRST_NAME
                        + "&Фамилия=" + name,
                String.class);

        String actual_5 = this.template.getForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Имя=" + name
                        + "&Номер=" + NUMBER_FOR_SEARCH,
                String.class);

        String actual_6 = this.template.getForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Имя=" + name
                        + "&Приют=" + ID,
                String.class);

        String actual_7 = this.template.getForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Фамилия=" + name
                        + "&Номер=" + NUMBER_FOR_SEARCH,
                String.class);

        String actual_8 = this.template.getForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Фамилия=" + name
                        + "&Приют=" + ID,
                String.class);

        String actual_9 = this.template.getForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Имя=" + name
                        + "&Фамилия=" + LAST_NAME
                        + "&Номер=" + NUMBER_FOR_SEARCH,
                String.class);

        String actual_10 = this.template.getForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Имя=" + FIRST_NAME
                        + "&Фамилия=" + name
                        + "&Номер=" + NUMBER_FOR_SEARCH,
                String.class);

        String actual_11 = this.template.getForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Имя=" + name
                        + "&Фамилия=" + LAST_NAME
                        + "&Приют=" + ID,
                String.class);

        String actual_12 = this.template.getForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Имя=" + FIRST_NAME
                        + "&Фамилия=" + name
                        + "&Приют=" + ID,
                String.class);

        String actual_13 = this.template.getForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Имя=" + name
                        + "&Номер=" + NUMBER_FOR_SEARCH
                        + "&Приют=" + ID,
                String.class);

        String actual_14 = this.template.getForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Фамилия=" + name
                        + "&Номер=" + NUMBER_FOR_SEARCH
                        + "&Приют=" + ID,
                String.class);

        String actual_15 = this.template.getForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Имя=" + name
                        + "&Фамилия=" + LAST_NAME
                        + "&Номер=" + NUMBER_FOR_SEARCH
                        + "&Приют=" + ID,
                String.class);

        String actual_16 = this.template.getForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Имя=" + FIRST_NAME
                        + "&Фамилия=" + name
                        + "&Номер=" + NUMBER_FOR_SEARCH
                        + "&Приют=" + ID,
                String.class);

        assertNotNull(actual_1);
        assertNotNull(actual_2);
        assertNotNull(actual_3);
        assertNotNull(actual_4);
        assertNotNull(actual_5);
        assertNotNull(actual_6);
        assertNotNull(actual_7);
        assertNotNull(actual_8);
        assertNotNull(actual_9);
        assertNotNull(actual_10);
        assertNotNull(actual_11);
        assertNotNull(actual_12);
        assertNotNull(actual_13);
        assertNotNull(actual_14);
        assertNotNull(actual_15);
        assertNotNull(actual_16);

        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual_1);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual_2);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual_3);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual_4);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual_5);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual_6);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual_7);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual_8);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual_9);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual_10);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual_11);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual_12);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual_13);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual_14);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual_15);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual_16);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForId")
    void getAllByParameters_InvalideInputException(Long id) {
        String actual_1 = this.template.getForObject("http://localhost:" + port
                        + "/pet"
                        + "?Приют=" + id,
                String.class);

        String actual_2 = this.template.getForObject("http://localhost:" + port
                        + "/pet"
                        + "?Тип=" + DOG
                        + "&Приют=" + id,
                String.class);

        String actual_3 = this.template.getForObject("http://localhost:" + port
                        + "/pet"
                        + "?Статус=" + FREE
                        + "&Приют=" + id,
                String.class);

        String actual_4 = this.template.getForObject("http://localhost:" + port
                        + "/pet"
                        + "?Имя=" + NAME
                        + "&Приют=" + id,
                String.class);

        String actual_5 = this.template.getForObject("http://localhost:" + port
                        + "/pet"
                        + "?Тип=" + DOG
                        + "&Статус=" + FREE
                        + "&Приют=" + id,
                String.class);

        String actual_6 = this.template.getForObject("http://localhost:" + port
                        + "/pet"
                        + "?Тип=" + DOG
                        + "&Имя=" + NAME
                        + "&Приют=" + id,
                String.class);

        String actual_7 = this.template.getForObject("http://localhost:" + port
                        + "/pet"
                        + "?Статус=" + FREE
                        + "&Имя=" + NAME
                        + "&Приют=" + id,
                String.class);

        String actual_8 = this.template.getForObject("http://localhost:" + port
                        + "/pet"
                        + "?Тип=" + DOG
                        + "&Статус=" + FREE
                        + "&Имя=" + NAME
                        + "&Приют=" + id,
                String.class);

        assertNotNull(actual_1);
        assertNotNull(actual_2);
        assertNotNull(actual_3);
        assertNotNull(actual_4);
        assertNotNull(actual_5);
        assertNotNull(actual_6);
        assertNotNull(actual_7);
        assertNotNull(actual_8);

        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual_1);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual_2);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual_3);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual_4);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual_5);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual_6);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual_7);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual_8);
    }

    @Test
    void getAll_success() {
        addShelter();
        addPet();

        String actual = this.template.getForObject("http://localhost:" + port
                        + "/pet/all",
                String.class);

        deletePet(PET.getId());
        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertTrueForCollection(actual);
    }

    @Test
    void edit_success() {
        addShelter();
        addPet();
        addNewShelter();

        Pet expected = new Pet(CAT, ADOPTED, NEW_NAME, NEW_SHELTER);
        expected.setId(PET.getId());

        Pet actual = this.template.exchange("http://localhost:" + port
                        + "/pet/" + PET.getId()
                        + "?Тип=" + CAT
                        + "&Статус=" + ADOPTED
                        + "&Имя=" + NEW_NAME
                        + "&Приют=" + NEW_SHELTER.getId(),
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                Pet.class).getBody();

        deletePet(PET.getId());
        deleteShelter(NEW_SHELTER.getId());
        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForName")
    void edit_InvalideInputException(String name) {
        addShelter();
        addPet();

        String actual = this.template.exchange("http://localhost:" + port
                        + "/pet/" + PET.getId()
                        + "?Имя=" + name,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        deletePet(PET.getId());
        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForId")
    void edit_InvalideInputException(Long id) {
        addShelter();
        addPet();

        String actual = this.template.exchange("http://localhost:" + port
                        + "/pet/" + PET.getId()
                        + "?Приют=" + id,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        deletePet(PET.getId());
        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual);
    }

    @Test
    void edit_ShelterNotFoundException() {
        addShelter();
        addPet();

        String actual = this.template.exchange("http://localhost:" + port
                        + "/pet/" + PET.getId()
                        + "?Приют=" + ID,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        deletePet(PET.getId());
        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertEquals(exception(NOT_FOUND, SHELTER_NOT_FOUND), actual);
    }

    @Test
    void edit_PetNotFoundException() {
        String actual = this.template.exchange("http://localhost:" + port
                        + "/pet/" + ID,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        assertNotNull(actual);
        assertEquals(exception(NOT_FOUND, PET_NOT_FOUND), actual);
    }

    @Test
    void edit_DifferentTypesException() {
        addShelter();
        addPet();
        addNewShelter();

        String actual = this.template.exchange("http://localhost:" + port
                        + "/pet/" + PET.getId()
                        + "?Тип=" + DOG
                        + "&Приют=" + NEW_SHELTER.getId(),
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        deletePet(PET.getId());
        deleteShelter(NEW_SHELTER.getId());
        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertEquals(exception(BAD_REQUEST, DIFFERENT_TYPES), actual);
    }

    @Test
    void edit_PetAlreadyAddedException() {
        addShelter();
        addPet();

        String actual = this.template.exchange("http://localhost:" + port
                        + "/pet/" + PET.getId()
                        + "?Имя=" + NAME,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        deletePet(PET.getId());
        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertEquals(exception(BAD_REQUEST, PET_ALREADY_ADDED), actual);
    }

    @Test
    void delete_success() {
        addShelter();
        addPet();

        Pet actual = this.template.exchange("http://localhost:" + port
                        + "/pet/" + PET.getId(),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Pet.class).getBody();

        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertEquals(PET, actual);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForId")
    void delete_InvalideInputException(Long id) {
        String actual = this.template.exchange("http://localhost:" + port
                        + "/pet/" + id,
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                String.class).getBody();

        assertNotNull(actual);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual);
    }

    @Test
    void delete_PetNotFoundException() {
        String actual = this.template.exchange("http://localhost:" + port
                        + "/pet/" + ID,
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                String.class).getBody();

        assertNotNull(actual);
        assertEquals(exception(NOT_FOUND, PET_NOT_FOUND), actual);
    }

    private void addShelter() {
        Long id = shelterController.add(DOG, ADDRESS).getId();
        SHELTER.setId(id);
    }

    private void addNewShelter() {
        Long id = shelterController.add(CAT, NEW_ADDRESS).getId();
        NEW_SHELTER.setId(id);
    }

    private void addPet() {
        Long id = petController.add(DOG, FREE, NAME, SHELTER.getId()).getId();
        PET.setShelter(SHELTER);
        PET.setId(id);
    }

    private void deleteShelter(Long id) {
        shelterController.delete(id);
    }

    private void deletePet(Long id) {
        petController.delete(id);
    }

    private void assertTrueForCollection(String actual) {
        assertTrue(actual.contains(PET.getId().toString()));
        assertTrue(actual.contains(PET.getType().toString()));
        assertTrue(actual.contains(PET.getStatus().toString()));
        assertTrue(actual.contains(PET.getName()));
        assertTrue(actual.contains(PET.getShelter().getId().toString()));
        assertTrue(actual.contains(PET.getShelter().getType().toString()));
        assertTrue(actual.contains(PET.getShelter().getAddress()));
    }

    private static Stream<Arguments> provideParamsForName() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(EMPTY),
                Arguments.of(INCORRECT_STRING)
        );
    }

    private static Stream<Arguments> provideParamsForId() {
        return Stream.of(
                Arguments.of(ZERO),
                Arguments.of(INCORRECT_ID)
        );
    }
}