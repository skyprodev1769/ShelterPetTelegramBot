package com.skypro.ShelterPetTelegramBot.tests.controller.TestRestTemplate;

import com.skypro.ShelterPetTelegramBot.controller.ParentController;
import com.skypro.ShelterPetTelegramBot.controller.PetController;
import com.skypro.ShelterPetTelegramBot.controller.ShelterController;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Parent;
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
import static com.skypro.ShelterPetTelegramBot.model.enums.PetType.DOG;
import static com.skypro.ShelterPetTelegramBot.tests.Utils.*;
import static com.skypro.ShelterPetTelegramBot.utils.Exceptions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ParentControllerTest {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate template;
    @Autowired
    private ParentController parentController;
    @Autowired
    private PetController petController;
    @Autowired
    private ShelterController shelterController;

    @Test
    void contextLoads() {
        Assertions.assertThat(parentController).isNotNull();
        Assertions.assertThat(shelterController).isNotNull();
        Assertions.assertThat(petController).isNotNull();
    }

    @Test
    void add_success() {
        addShelter();
        addPet();

        Parent actual = this.template.postForObject("http://localhost:" + port
                        + "/parent"
                        + "?Имя=" + FIRST_NAME
                        + "&Фамилия=" + LAST_NAME
                        + "&Номер=" + PHONE_NUMBER
                        + "&Животное=" + PET.getName(),
                PARENT,
                Parent.class);

        PET.setStatus(ADOPTED);
        PARENT.setId(actual.getId());

        deleteParent(PARENT.getId());
        deletePet(PET.getId());
        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertEquals(PARENT, actual);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForName")
    void add_InvalideInputException(String name) {
        String actual_1 = this.template.postForObject("http://localhost:" + port
                        + "/parent"
                        + "?Имя=" + name
                        + "&Фамилия=" + LAST_NAME
                        + "&Номер=" + PHONE_NUMBER
                        + "&Животное=" + NAME,
                PARENT,
                String.class);

        String actual_2 = this.template.postForObject("http://localhost:" + port
                        + "/parent"
                        + "?Имя=" + FIRST_NAME
                        + "&Фамилия=" + name
                        + "&Номер=" + PHONE_NUMBER
                        + "&Животное=" + NAME,
                PARENT,
                String.class);

        String actual_3 = this.template.postForObject("http://localhost:" + port
                        + "/parent"
                        + "?Имя=" + FIRST_NAME
                        + "&Фамилия=" + LAST_NAME
                        + "&Номер=" + PHONE_NUMBER
                        + "&Животное=" + name,
                PARENT,
                String.class);

        assertNotNull(actual_1);
        assertNotNull(actual_2);
        assertNotNull(actual_3);

        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual_1);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual_2);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual_3);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForPhoneNumber")
    void add_InvalideNumberException(String number) {
        String actual = this.template.postForObject("http://localhost:" + port
                        + "/parent"
                        + "?Имя=" + FIRST_NAME
                        + "&Фамилия=" + LAST_NAME
                        + "&Номер=" + number
                        + "&Животное=" + NAME,
                PARENT,
                String.class);

        assertNotNull(actual);
        assertEquals(exception(BAD_REQUEST, INVALIDE_NUMBER), actual);
    }

    @Test
    void add_PetNotFoundException() {
        String actual = this.template.postForObject("http://localhost:" + port
                        + "/parent"
                        + "?Имя=" + FIRST_NAME
                        + "&Фамилия=" + LAST_NAME
                        + "&Номер=" + PHONE_NUMBER
                        + "&Животное=" + NAME,
                PARENT,
                String.class);

        assertNotNull(actual);
        assertEquals(exception(NOT_FOUND, PET_NOT_FOUND), actual);
    }

    @Test
    void add_PetStatusException() {
        addShelter();
        addPet();
        setStatus();

        String actual = this.template.postForObject("http://localhost:" + port
                        + "/parent"
                        + "?Имя=" + FIRST_NAME
                        + "&Фамилия=" + LAST_NAME
                        + "&Номер=" + PHONE_NUMBER
                        + "&Животное=" + PET.getName(),
                PARENT,
                String.class);

        deletePet(PET.getId());
        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertEquals(exception(BAD_REQUEST, PET_STATUS), actual);
    }

    @Test
    void add_ParentAlreadyAddedException() {
        addShelter();
        addPet();
        addParent();

        String actual = this.template.postForObject("http://localhost:" + port
                        + "/parent"
                        + "?Имя=" + FIRST_NAME
                        + "&Фамилия=" + LAST_NAME
                        + "&Номер=" + PHONE_NUMBER
                        + "&Животное=" + PET.getName(),
                PARENT,
                String.class);

        deleteParent(PARENT.getId());
        deletePet(PET.getId());
        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertEquals(exception(BAD_REQUEST, PARENT_ALREADY_ADDED), actual);
    }

    @Test
    void getById_success() {
        addShelter();
        addPet();
        addParent();
        setStatus();

        Parent actual = this.template.getForObject("http://localhost:" + port
                        + "/parent/" + PARENT.getId(),
                Parent.class);

        deleteParent(PARENT.getId());
        deletePet(PET.getId());
        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertEquals(PARENT, actual);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForId")
    void getById_InvalideInputException(Long id) {
        String actual = this.template.getForObject("http://localhost:" + port
                        + "/parent/" + id,
                String.class);

        assertNotNull(actual);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual);
    }

    @Test
    void getById_ParentNotFoundException() {
        String actual = this.template.getForObject("http://localhost:" + port
                        + "/parent/" + ID,
                String.class);

        assertNotNull(actual);
        assertEquals(exception(NOT_FOUND, PARENT_NOT_FOUND), actual);
    }

    @Test
    void getAllByParameters_success() {
        addShelter();
        addPet();
        addParent();
        setStatus();

        String actual_1 = this.template.getForObject("http://localhost:" + port
                        + "/parent"
                        + "?Имя=" + FIRST_NAME,
                String.class);

        String actual_2 = this.template.getForObject("http://localhost:" + port
                        + "/parent"
                        + "?Фамилия=" + LAST_NAME,
                String.class);

        String actual_3 = this.template.getForObject("http://localhost:" + port
                        + "/parent"
                        + "?Номер=" + NUMBER_FOR_SEARCH,
                String.class);

        String actual_4 = this.template.getForObject("http://localhost:" + port
                        + "/parent"
                        + "?Имя=" + FIRST_NAME
                        + "&Фамилия=" + LAST_NAME,
                String.class);

        String actual_5 = this.template.getForObject("http://localhost:" + port
                        + "/parent"
                        + "?Имя=" + FIRST_NAME
                        + "&Номер=" + NUMBER_FOR_SEARCH,
                String.class);

        String actual_6 = this.template.getForObject("http://localhost:" + port
                        + "/parent"
                        + "?Фамилия=" + LAST_NAME
                        + "&Номер=" + NUMBER_FOR_SEARCH,
                String.class);

        String actual_7 = this.template.getForObject("http://localhost:" + port
                        + "/parent"
                        + "?Имя=" + FIRST_NAME
                        + "&Фамилия=" + LAST_NAME
                        + "&Номер=" + NUMBER_FOR_SEARCH,
                String.class);

        deleteParent(PARENT.getId());
        deletePet(PET.getId());
        deleteShelter(SHELTER.getId());

        assertNotNull(actual_1);
        assertNotNull(actual_2);
        assertNotNull(actual_3);
        assertNotNull(actual_4);
        assertNotNull(actual_5);
        assertNotNull(actual_6);
        assertNotNull(actual_7);

        assertTrueForCollection(actual_1);
        assertTrueForCollection(actual_2);
        assertTrueForCollection(actual_3);
        assertTrueForCollection(actual_4);
        assertTrueForCollection(actual_5);
        assertTrueForCollection(actual_6);
        assertTrueForCollection(actual_7);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForName")
    void getAllByParameters_InvalideInputException(String name) {
        String actual_1 = this.template.getForObject("http://localhost:" + port
                        + "/parent"
                        + "?Имя=" + name,
                String.class);

        String actual_2 = this.template.getForObject("http://localhost:" + port
                        + "/parent"
                        + "?Фамилия=" + name,
                String.class);

        String actual_3 = this.template.getForObject("http://localhost:" + port
                        + "/parent"
                        + "?Имя=" + name
                        + "&Фамилия=" + LAST_NAME,
                String.class);

        String actual_4 = this.template.getForObject("http://localhost:" + port
                        + "/parent"
                        + "?Имя=" + FIRST_NAME
                        + "&Фамилия=" + name,
                String.class);

        String actual_5 = this.template.getForObject("http://localhost:" + port
                        + "/parent"
                        + "?Имя=" + name
                        + "&Номер=" + NUMBER_FOR_SEARCH,
                String.class);

        String actual_6 = this.template.getForObject("http://localhost:" + port
                        + "/parent"
                        + "?Фамилия=" + name
                        + "&Номер=" + NUMBER_FOR_SEARCH,
                String.class);

        String actual_7 = this.template.getForObject("http://localhost:" + port
                        + "/parent"
                        + "?Имя=" + FIRST_NAME
                        + "&Фамилия=" + name
                        + "&Номер=" + NUMBER_FOR_SEARCH,
                String.class);

        String actual_8 = this.template.getForObject("http://localhost:" + port
                        + "/parent"
                        + "?Имя=" + name
                        + "&Фамилия=" + LAST_NAME
                        + "&Номер=" + NUMBER_FOR_SEARCH,
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
        addParent();
        setStatus();

        String actual = this.template.getForObject("http://localhost:" + port
                        + "/parent/all",
                String.class);

        deleteParent(PARENT.getId());
        deletePet(PET.getId());
        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertTrueForCollection(actual);
    }

    @Test
    void edit_success() {
        addShelter();
        addPet();
        addParent();
        setStatus();

        Long id = petController.add(DOG, FREE, NEW_NAME, SHELTER.getId()).getId();
        NEW_PET.setShelter(SHELTER);
        NEW_PET.setId(id);

        Parent expected = new Parent(NEW_FIRST_NAME, NEW_LAST_NAME, NEW_VALID_PHONE_NUMBER, NEW_PET);
        expected.setId(PARENT.getId());

        Parent actual = this.template.exchange("http://localhost:" + port
                        + "/parent/" + PARENT.getId()
                        + "?Имя=" + NEW_FIRST_NAME
                        + "&Фамилия=" + NEW_LAST_NAME
                        + "&Номер=" + NEW_PHONE_NUMBER
                        + "&Животное=" + NEW_PET.getName(),

                HttpMethod.PUT,
                HttpEntity.EMPTY,
                Parent.class).getBody();

        NEW_PET.setStatus(ADOPTED);

        deleteParent(PARENT.getId());
        deletePet(PET.getId());
        deletePet(NEW_PET.getId());
        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForName")
    void edit_InvalideInputException(String name) {
        addShelter();
        addPet();
        addParent();

        String actual_1 = this.template.exchange("http://localhost:" + port
                        + "/parent/" + PARENT.getId()
                        + "?Имя=" + name,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        String actual_2 = this.template.exchange("http://localhost:" + port
                        + "/parent/" + PARENT.getId()
                        + "?Фамилия=" + name,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        String actual_3 = this.template.exchange("http://localhost:" + port
                        + "/parent/" + PARENT.getId()
                        + "?Животное=" + name,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        deleteParent(PARENT.getId());
        deletePet(PET.getId());
        deleteShelter(SHELTER.getId());

        assertNotNull(actual_1);
        assertNotNull(actual_2);
        assertNotNull(actual_3);

        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual_1);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual_2);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual_3);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForPhoneNumber")
    void edit_InvalideNumberException(String number) {
        addShelter();
        addPet();
        addParent();

        String actual = this.template.exchange("http://localhost:" + port
                        + "/parent/" + PARENT.getId()
                        + "?Номер=" + number,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        deleteParent(PARENT.getId());
        deletePet(PET.getId());
        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertEquals(exception(BAD_REQUEST, INVALIDE_NUMBER), actual);
    }

    @Test
    void edit_PetNotFoundException() {
        addShelter();
        addPet();
        addParent();

        String actual = this.template.exchange("http://localhost:" + port
                        + "/parent/" + PARENT.getId()
                        + "?Животное=" + NEW_NAME,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        deleteParent(PARENT.getId());
        deletePet(PET.getId());
        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertEquals(exception(NOT_FOUND, PET_NOT_FOUND), actual);
    }

    @Test
    void edit_ParentNotFoundException() {
        String actual = this.template.exchange("http://localhost:" + port
                        + "/parent/" + ID,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        assertNotNull(actual);
        assertEquals(exception(NOT_FOUND, PARENT_NOT_FOUND), actual);
    }

    @Test
    void edit_PetStatusException() {
        addShelter();
        addPet();
        addParent();

        petController.edit(PET.getId(), null, ADOPTED, null, null);

        String actual = this.template.exchange("http://localhost:" + port
                        + "/parent/" + PARENT.getId()
                        + "?Животное=" + PET.getName(),
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        deleteParent(PARENT.getId());
        deletePet(PET.getId());
        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertEquals(exception(BAD_REQUEST, PET_STATUS), actual);
    }

    @Test
    void edit_ParentAlreadyAddedException() {
        addShelter();
        addPet();
        addParent();

        String actual = this.template.exchange("http://localhost:" + port
                        + "/parent/" + PARENT.getId()
                        + "?Номер=" + PHONE_NUMBER,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        deleteParent(PARENT.getId());
        deletePet(PET.getId());
        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertEquals(exception(BAD_REQUEST, PARENT_ALREADY_ADDED), actual);
    }

    @Test
    void delete_success() {
        addShelter();
        addPet();
        addParent();

        Parent actual = this.template.exchange("http://localhost:" + port
                        + "/parent/" + PARENT.getId(),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Parent.class).getBody();

        deletePet(PET.getId());
        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertEquals(PARENT, actual);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForId")
    void delete_InvalideInputException(Long id) {
        String actual = this.template.exchange("http://localhost:" + port
                        + "/parent/" + id,
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                String.class).getBody();

        assertNotNull(actual);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual);
    }

    @Test
    void delete_ParentNotFoundException() {
        String actual = this.template.exchange("http://localhost:" + port
                        + "/parent/" + ID,
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                String.class).getBody();

        assertNotNull(actual);
        assertEquals(exception(NOT_FOUND, PARENT_NOT_FOUND), actual);
    }

    private void addShelter() {
        Long id = shelterController.add(DOG, ADDRESS).getId();
        SHELTER.setId(id);
    }

    private void addPet() {
        Long id = petController.add(DOG, FREE, NAME, SHELTER.getId()).getId();
        PET.setShelter(SHELTER);
        PET.setId(id);
    }

    private void addParent() {
        Long id = parentController.add(FIRST_NAME, LAST_NAME, PHONE_NUMBER, NAME).getId();
        PARENT.setPet(PET);
        PARENT.setId(id);
    }

    private void setStatus() {
        petController.edit(PET.getId(), null, ADOPTED, null, null);
    }

    private void deleteShelter(Long id) {
        shelterController.delete(id);
    }

    private void deletePet(Long id) {
        petController.delete(id);
    }

    private void deleteParent(Long id) {
        parentController.delete(id);
    }

    private void assertTrueForCollection(String actual) {
        assertTrue(actual.contains(PARENT.getId().toString()));
        assertTrue(actual.contains(PARENT.getFirstName()));
        assertTrue(actual.contains(PARENT.getLastName()));
        assertTrue(actual.contains(PARENT.getPhoneNumber()));
        assertTrue(actual.contains(PARENT.getPet().getId().toString()));
        assertTrue(actual.contains(PARENT.getPet().getType().toString()));
        assertTrue(actual.contains(PARENT.getPet().getStatus().toString()));
        assertTrue(actual.contains(PARENT.getPet().getName()));
        assertTrue(actual.contains(PARENT.getPet().getShelter().getId().toString()));
        assertTrue(actual.contains(PARENT.getPet().getShelter().getType().toString()));
        assertTrue(actual.contains(PARENT.getPet().getShelter().getAddress()));
    }

    private static Stream<Arguments> provideParamsForPhoneNumber() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(EMPTY),
                Arguments.of(SHORT_PHONE_NUMBER),
                Arguments.of(LONG_PHONE_NUMBER),
                Arguments.of(INCORRECT_STRING)
        );
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