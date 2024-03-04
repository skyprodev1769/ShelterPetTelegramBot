package com.skypro.ShelterPetTelegramBot.controller.TestRestTemplate;

import com.skypro.ShelterPetTelegramBot.Utils;
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

import static com.skypro.ShelterPetTelegramBot.model.entity.enums.PetStatus.ADOPTED;
import static com.skypro.ShelterPetTelegramBot.model.entity.enums.PetStatus.FREE;
import static com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType.DOG;
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
                        + "?Имя=" + Utils.FIRST_NAME
                        + "&Фамилия=" + Utils.LAST_NAME
                        + "&Номер=" + Utils.PHONE_NUMBER
                        + "&Животное=" + Utils.PET.getName(),
                Utils.PARENT,
                Parent.class);

        Utils.PET.setStatus(ADOPTED);
        Utils.PARENT.setId(actual.getId());

        deleteParent(Utils.PARENT.getId());
        deletePet(Utils.PET.getId());
        deleteShelter(Utils.SHELTER.getId());

        assertNotNull(actual);
        org.junit.jupiter.api.Assertions.assertEquals(Utils.PARENT, actual);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForName")
    void add_InvalideInputException(String name) {
        String actual_1 = this.template.postForObject("http://localhost:" + port
                        + "/parent"
                        + "?Имя=" + name
                        + "&Фамилия=" + Utils.LAST_NAME
                        + "&Номер=" + Utils.PHONE_NUMBER
                        + "&Животное=" + Utils.NAME,
                Utils.PARENT,
                String.class);

        String actual_2 = this.template.postForObject("http://localhost:" + port
                        + "/parent"
                        + "?Имя=" + Utils.FIRST_NAME
                        + "&Фамилия=" + name
                        + "&Номер=" + Utils.PHONE_NUMBER
                        + "&Животное=" + Utils.NAME,
                Utils.PARENT,
                String.class);

        String actual_3 = this.template.postForObject("http://localhost:" + port
                        + "/parent"
                        + "?Имя=" + Utils.FIRST_NAME
                        + "&Фамилия=" + Utils.LAST_NAME
                        + "&Номер=" + Utils.PHONE_NUMBER
                        + "&Животное=" + name,
                Utils.PARENT,
                String.class);

        assertNotNull(actual_1);
        assertNotNull(actual_2);
        assertNotNull(actual_3);

        org.junit.jupiter.api.Assertions.assertEquals(Utils.exception(BAD_REQUEST, INVALIDE_INPUT), actual_1);
        org.junit.jupiter.api.Assertions.assertEquals(Utils.exception(BAD_REQUEST, INVALIDE_INPUT), actual_2);
        org.junit.jupiter.api.Assertions.assertEquals(Utils.exception(BAD_REQUEST, INVALIDE_INPUT), actual_2);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForPhoneNumber")
    void add_InvalideNumberException(String number) {
        String actual = this.template.postForObject("http://localhost:" + port
                        + "/parent"
                        + "?Имя=" + Utils.FIRST_NAME
                        + "&Фамилия=" + Utils.LAST_NAME
                        + "&Номер=" + number
                        + "&Животное=" + Utils.NAME,
                Utils.PARENT,
                String.class);

        assertNotNull(actual);
        org.junit.jupiter.api.Assertions.assertEquals(Utils.exception(BAD_REQUEST, INVALIDE_NUMBER), actual);
    }

    @Test
    void add_PetNotFoundException() {
        String actual = this.template.postForObject("http://localhost:" + port
                        + "/parent"
                        + "?Имя=" + Utils.FIRST_NAME
                        + "&Фамилия=" + Utils.LAST_NAME
                        + "&Номер=" + Utils.PHONE_NUMBER
                        + "&Животное=" + Utils.NAME,
                Utils.PARENT,
                String.class);

        assertNotNull(actual);
        org.junit.jupiter.api.Assertions.assertEquals(Utils.exception(NOT_FOUND, PET_NOT_FOUND), actual);
    }

    @Test
    void add_PetStatusException() {
        addShelter();
        addPet();
        setStatus();

        String actual = this.template.postForObject("http://localhost:" + port
                        + "/parent"
                        + "?Имя=" + Utils.FIRST_NAME
                        + "&Фамилия=" + Utils.LAST_NAME
                        + "&Номер=" + Utils.PHONE_NUMBER
                        + "&Животное=" + Utils.PET.getName(),
                Utils.PARENT,
                String.class);

        deletePet(Utils.PET.getId());
        deleteShelter(Utils.SHELTER.getId());

        assertNotNull(actual);
        org.junit.jupiter.api.Assertions.assertEquals(Utils.exception(BAD_REQUEST, PET_STATUS), actual);
    }

    @Test
    void add_ParentAlreadyAddedException() {
        addShelter();
        addPet();
        addParent();

        String actual = this.template.postForObject("http://localhost:" + port
                        + "/parent"
                        + "?Имя=" + Utils.FIRST_NAME
                        + "&Фамилия=" + Utils.LAST_NAME
                        + "&Номер=" + Utils.PHONE_NUMBER
                        + "&Животное=" + Utils.PET.getName(),
                Utils.PARENT,
                String.class);

        deleteParent(Utils.PARENT.getId());
        deletePet(Utils.PET.getId());
        deleteShelter(Utils.SHELTER.getId());

        assertNotNull(actual);
        org.junit.jupiter.api.Assertions.assertEquals(Utils.exception(BAD_REQUEST, PARENT_ALREADY_ADDED), actual);
    }

    @Test
    void getById_success() {
        addShelter();
        addPet();
        addParent();
        setStatus();

        Parent actual = this.template.getForObject("http://localhost:" + port
                        + "/parent/" + Utils.PARENT.getId(),
                Parent.class);

        deleteParent(Utils.PARENT.getId());
        deletePet(Utils.PET.getId());
        deleteShelter(Utils.SHELTER.getId());

        assertNotNull(actual);
        org.junit.jupiter.api.Assertions.assertEquals(Utils.PARENT, actual);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForId")
    void getById_InvalideInputException(Long id) {
        String actual = this.template.getForObject("http://localhost:" + port
                        + "/parent/" + id,
                String.class);

        assertNotNull(actual);
        org.junit.jupiter.api.Assertions.assertEquals(Utils.exception(BAD_REQUEST, INVALIDE_INPUT), actual);
    }

    @Test
    void getById_ParentNotFoundException() {
        String actual = this.template.getForObject("http://localhost:" + port
                        + "/parent/" + Utils.ID,
                String.class);

        assertNotNull(actual);
        org.junit.jupiter.api.Assertions.assertEquals(Utils.exception(NOT_FOUND, PARENT_NOT_FOUND), actual);
    }

    @Test
    void getAllByParameters_success() {
        addShelter();
        addPet();
        addParent();
        setStatus();

        String actual_1 = this.template.getForObject("http://localhost:" + port
                        + "/parent"
                        + "?Имя=" + Utils.FIRST_NAME,
                String.class);

        String actual_2 = this.template.getForObject("http://localhost:" + port
                        + "/parent"
                        + "?Фамилия=" + Utils.LAST_NAME,
                String.class);

        String actual_3 = this.template.getForObject("http://localhost:" + port
                        + "/parent"
                        + "?Номер=" + Utils.NUMBER_FOR_SEARCH,
                String.class);

        String actual_4 = this.template.getForObject("http://localhost:" + port
                        + "/parent"
                        + "?Имя=" + Utils.FIRST_NAME
                        + "&Фамилия=" + Utils.LAST_NAME,
                String.class);

        String actual_5 = this.template.getForObject("http://localhost:" + port
                        + "/parent"
                        + "?Имя=" + Utils.FIRST_NAME
                        + "&Номер=" + Utils.NUMBER_FOR_SEARCH,
                String.class);

        String actual_6 = this.template.getForObject("http://localhost:" + port
                        + "/parent"
                        + "?Фамилия=" + Utils.LAST_NAME
                        + "&Номер=" + Utils.NUMBER_FOR_SEARCH,
                String.class);

        String actual_7 = this.template.getForObject("http://localhost:" + port
                        + "/parent"
                        + "?Имя=" + Utils.FIRST_NAME
                        + "&Фамилия=" + Utils.LAST_NAME
                        + "&Номер=" + Utils.NUMBER_FOR_SEARCH,
                String.class);

        deleteParent(Utils.PARENT.getId());
        deletePet(Utils.PET.getId());
        deleteShelter(Utils.SHELTER.getId());

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
                        + "&Фамилия=" + Utils.LAST_NAME,
                String.class);

        String actual_4 = this.template.getForObject("http://localhost:" + port
                        + "/parent"
                        + "?Имя=" + Utils.FIRST_NAME
                        + "&Фамилия=" + name,
                String.class);

        String actual_5 = this.template.getForObject("http://localhost:" + port
                        + "/parent"
                        + "?Имя=" + name
                        + "&Номер=" + Utils.NUMBER_FOR_SEARCH,
                String.class);

        String actual_6 = this.template.getForObject("http://localhost:" + port
                        + "/parent"
                        + "?Фамилия=" + name
                        + "&Номер=" + Utils.NUMBER_FOR_SEARCH,
                String.class);

        String actual_7 = this.template.getForObject("http://localhost:" + port
                        + "/parent"
                        + "?Имя=" + Utils.FIRST_NAME
                        + "&Фамилия=" + name
                        + "&Номер=" + Utils.NUMBER_FOR_SEARCH,
                String.class);

        String actual_8 = this.template.getForObject("http://localhost:" + port
                        + "/parent"
                        + "?Имя=" + name
                        + "&Фамилия=" + Utils.LAST_NAME
                        + "&Номер=" + Utils.NUMBER_FOR_SEARCH,
                String.class);

        assertNotNull(actual_1);
        assertNotNull(actual_2);
        assertNotNull(actual_3);
        assertNotNull(actual_4);
        assertNotNull(actual_5);
        assertNotNull(actual_6);
        assertNotNull(actual_7);
        assertNotNull(actual_8);

        org.junit.jupiter.api.Assertions.assertEquals(Utils.exception(BAD_REQUEST, INVALIDE_INPUT), actual_1);
        org.junit.jupiter.api.Assertions.assertEquals(Utils.exception(BAD_REQUEST, INVALIDE_INPUT), actual_2);
        org.junit.jupiter.api.Assertions.assertEquals(Utils.exception(BAD_REQUEST, INVALIDE_INPUT), actual_3);
        org.junit.jupiter.api.Assertions.assertEquals(Utils.exception(BAD_REQUEST, INVALIDE_INPUT), actual_4);
        org.junit.jupiter.api.Assertions.assertEquals(Utils.exception(BAD_REQUEST, INVALIDE_INPUT), actual_5);
        org.junit.jupiter.api.Assertions.assertEquals(Utils.exception(BAD_REQUEST, INVALIDE_INPUT), actual_6);
        org.junit.jupiter.api.Assertions.assertEquals(Utils.exception(BAD_REQUEST, INVALIDE_INPUT), actual_7);
        org.junit.jupiter.api.Assertions.assertEquals(Utils.exception(BAD_REQUEST, INVALIDE_INPUT), actual_8);
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

        deleteParent(Utils.PARENT.getId());
        deletePet(Utils.PET.getId());
        deleteShelter(Utils.SHELTER.getId());

        assertNotNull(actual);
        assertTrueForCollection(actual);
    }

    @Test
    void edit_success() {
        addShelter();
        addPet();
        addParent();

        Long id = petController.add(DOG, FREE, Utils.NEW_NAME, Utils.SHELTER.getId()).getId();
        Utils.NEW_PET.setShelter(Utils.SHELTER);
        Utils.NEW_PET.setId(id);

        Parent expected = new Parent(Utils.NEW_FIRST_NAME, Utils.NEW_LAST_NAME, Utils.NEW_VALID_PHONE_NUMBER, Utils.NEW_PET);
        expected.setId(Utils.PARENT.getId());

        Parent actual = this.template.exchange("http://localhost:" + port
                        + "/parent/" + Utils.PARENT.getId()
                        + "?Имя=" + Utils.NEW_FIRST_NAME
                        + "&Фамилия=" + Utils.NEW_LAST_NAME
                        + "&Номер=" + Utils.NEW_PHONE_NUMBER
                        + "&Животное=" + Utils.NEW_PET.getName(),

                HttpMethod.PUT,
                HttpEntity.EMPTY,
                Parent.class).getBody();

        Utils.NEW_PET.setStatus(ADOPTED);

        deleteParent(Utils.PARENT.getId());
        deletePet(Utils.PET.getId());
        deletePet(Utils.NEW_PET.getId());
        deleteShelter(Utils.SHELTER.getId());

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
                        + "/parent/" + Utils.PARENT.getId()
                        + "?Имя=" + name,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        String actual_2 = this.template.exchange("http://localhost:" + port
                        + "/parent/" + Utils.PARENT.getId()
                        + "?Фамилия=" + name,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        String actual_3 = this.template.exchange("http://localhost:" + port
                        + "/parent/" + Utils.PARENT.getId()
                        + "?Животное=" + name,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        deleteParent(Utils.PARENT.getId());
        deletePet(Utils.PET.getId());
        deleteShelter(Utils.SHELTER.getId());

        assertNotNull(actual_1);
        assertNotNull(actual_2);
        assertNotNull(actual_3);

        org.junit.jupiter.api.Assertions.assertEquals(Utils.exception(BAD_REQUEST, INVALIDE_INPUT), actual_1);
        org.junit.jupiter.api.Assertions.assertEquals(Utils.exception(BAD_REQUEST, INVALIDE_INPUT), actual_2);
        org.junit.jupiter.api.Assertions.assertEquals(Utils.exception(BAD_REQUEST, INVALIDE_INPUT), actual_3);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForPhoneNumber")
    void edit_InvalideNumberException(String number) {
        addShelter();
        addPet();
        addParent();

        String actual = this.template.exchange("http://localhost:" + port
                        + "/parent/" + Utils.PARENT.getId()
                        + "?Номер=" + number,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        deleteParent(Utils.PARENT.getId());
        deletePet(Utils.PET.getId());
        deleteShelter(Utils.SHELTER.getId());

        assertNotNull(actual);
        org.junit.jupiter.api.Assertions.assertEquals(Utils.exception(BAD_REQUEST, INVALIDE_NUMBER), actual);
    }

    @Test
    void edit_PetNotFoundException() {
        addShelter();
        addPet();
        addParent();

        String actual = this.template.exchange("http://localhost:" + port
                        + "/parent/" + Utils.PARENT.getId()
                        + "?Животное=" + Utils.NEW_NAME,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        deleteParent(Utils.PARENT.getId());
        deletePet(Utils.PET.getId());
        deleteShelter(Utils.SHELTER.getId());

        assertNotNull(actual);
        org.junit.jupiter.api.Assertions.assertEquals(Utils.exception(NOT_FOUND, PET_NOT_FOUND), actual);
    }

    @Test
    void edit_ParentNotFoundException() {
        String actual = this.template.exchange("http://localhost:" + port
                        + "/parent/" + Utils.ID,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        assertNotNull(actual);
        org.junit.jupiter.api.Assertions.assertEquals(Utils.exception(NOT_FOUND, PARENT_NOT_FOUND), actual);
    }

    @Test
    void edit_PetStatusException() {
        addShelter();
        addPet();
        addParent();
        setStatus();

        String actual = this.template.exchange("http://localhost:" + port
                        + "/parent/" + Utils.PARENT.getId()
                        + "?Животное=" + Utils.PET.getName(),
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        deleteParent(Utils.PARENT.getId());
        deletePet(Utils.PET.getId());
        deleteShelter(Utils.SHELTER.getId());

        assertNotNull(actual);
        org.junit.jupiter.api.Assertions.assertEquals(Utils.exception(BAD_REQUEST, PET_STATUS), actual);
    }

    @Test
    void edit_ParentAlreadyAddedException() {
        addShelter();
        addPet();
        addParent();

        String actual = this.template.exchange("http://localhost:" + port
                        + "/parent/" + Utils.PARENT.getId()
                        + "?Номер=" + Utils.PHONE_NUMBER,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        deleteParent(Utils.PARENT.getId());
        deletePet(Utils.PET.getId());
        deleteShelter(Utils.SHELTER.getId());

        assertNotNull(actual);
        org.junit.jupiter.api.Assertions.assertEquals(Utils.exception(BAD_REQUEST, PARENT_ALREADY_ADDED), actual);
    }

    @Test
    void delete_success() {
        addShelter();
        addPet();
        addParent();

        Parent actual = this.template.exchange("http://localhost:" + port
                        + "/parent/" + Utils.PARENT.getId(),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Parent.class).getBody();

        deletePet(Utils.PET.getId());
        deleteShelter(Utils.SHELTER.getId());

        assertNotNull(actual);
        org.junit.jupiter.api.Assertions.assertEquals(Utils.PARENT, actual);
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
        org.junit.jupiter.api.Assertions.assertEquals(Utils.exception(BAD_REQUEST, INVALIDE_INPUT), actual);
    }

    @Test
    void delete_ParentNotFoundException() {
        String actual = this.template.exchange("http://localhost:" + port
                        + "/parent/" + Utils.ID,
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                String.class).getBody();

        assertNotNull(actual);
        org.junit.jupiter.api.Assertions.assertEquals(Utils.exception(NOT_FOUND, PARENT_NOT_FOUND), actual);
    }

    private void addShelter() {
        Long id = shelterController.add(DOG, Utils.ADDRESS).getId();
        Utils.SHELTER.setId(id);
    }

    private void addPet() {
        Long id = petController.add(DOG, FREE, Utils.NAME, Utils.SHELTER.getId()).getId();
        Utils.PET.setShelter(Utils.SHELTER);
        Utils.PET.setId(id);
    }

    private void setStatus() {
        petController.edit(Utils.PET.getId(), null, ADOPTED, null, null);
    }

    private void addParent() {
        Long id = parentController.add(Utils.FIRST_NAME, Utils.LAST_NAME, Utils.PHONE_NUMBER, Utils.NAME).getId();
        Utils.PARENT.setPet(Utils.PET);
        Utils.PARENT.setId(id);
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
        assertTrue(actual.contains(Utils.PARENT.getId().toString()));
        assertTrue(actual.contains(Utils.PARENT.getFirstName()));
        assertTrue(actual.contains(Utils.PARENT.getLastName()));
        assertTrue(actual.contains(Utils.PARENT.getPhoneNumber()));
        assertTrue(actual.contains(Utils.PARENT.getPet().getId().toString()));
        assertTrue(actual.contains(Utils.PARENT.getPet().getType().toString()));
        assertTrue(actual.contains(Utils.PARENT.getPet().getStatus().toString()));
        assertTrue(actual.contains(Utils.PARENT.getPet().getName()));
        assertTrue(actual.contains(Utils.PARENT.getPet().getShelter().getId().toString()));
        assertTrue(actual.contains(Utils.PARENT.getPet().getShelter().getType().toString()));
        assertTrue(actual.contains(Utils.PARENT.getPet().getShelter().getAddress()));
    }

    private static Stream<Arguments> provideParamsForPhoneNumber() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(Utils.EMPTY),
                Arguments.of(Utils.SHORT_PHONE_NUMBER),
                Arguments.of(Utils.LONG_PHONE_NUMBER),
                Arguments.of(Utils.INCORRECT_STRING)
        );
    }

    private static Stream<Arguments> provideParamsForName() {
        return Stream.of(
                Arguments.of((Object) null),
                Arguments.of(Utils.EMPTY),
                Arguments.of(Utils.INCORRECT_STRING)
        );
    }

    private static Stream<Arguments> provideParamsForId() {
        return Stream.of(
                Arguments.of(Utils.ZERO),
                Arguments.of(Utils.INCORRECT_ID)
        );
    }
}