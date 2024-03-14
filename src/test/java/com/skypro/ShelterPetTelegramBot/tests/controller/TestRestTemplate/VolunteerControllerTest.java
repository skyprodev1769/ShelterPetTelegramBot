package com.skypro.ShelterPetTelegramBot.tests.controller.TestRestTemplate;

import com.skypro.ShelterPetTelegramBot.controller.ShelterController;
import com.skypro.ShelterPetTelegramBot.controller.VolunteerController;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Volunteer;
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

import static com.skypro.ShelterPetTelegramBot.model.enums.PetType.CAT;
import static com.skypro.ShelterPetTelegramBot.model.enums.PetType.DOG;
import static com.skypro.ShelterPetTelegramBot.tests.Utils.*;
import static com.skypro.ShelterPetTelegramBot.utils.Exceptions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class VolunteerControllerTest {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate template;
    @Autowired
    private VolunteerController volunteerController;
    @Autowired
    private ShelterController shelterController;

    @Test
    void contextLoads() {
        Assertions.assertThat(volunteerController).isNotNull();
        Assertions.assertThat(shelterController).isNotNull();
    }

    @Test
    void add_success() {
        addShelter();

        Volunteer actual = this.template.postForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Имя=" + FIRST_NAME
                        + "&Фамилия=" + LAST_NAME
                        + "&Номер=" + PHONE_NUMBER
                        + "&Приют=" + SHELTER.getId(),
                VOLUNTEER,
                Volunteer.class);

        VOLUNTEER.setId(actual.getId());

        deleteVolunteer(VOLUNTEER.getId());
        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertEquals(VOLUNTEER, actual);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForName")
    void add_InvalideInputException(String name) {
        String actual_1 = this.template.postForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Имя=" + name
                        + "&Фамилия=" + LAST_NAME
                        + "&Номер=" + PHONE_NUMBER
                        + "&Приют=" + ID,
                VOLUNTEER,
                String.class);

        String actual_2 = this.template.postForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Имя=" + FIRST_NAME
                        + "&Фамилия=" + name
                        + "&Номер=" + PHONE_NUMBER
                        + "&Приют=" + ID,
                VOLUNTEER,
                String.class);

        assertNotNull(actual_1);
        assertNotNull(actual_2);

        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual_1);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual_2);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForId")
    void add_InvalideInputException(Long id) {
        String actual = this.template.postForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Имя=" + FIRST_NAME
                        + "&Фамилия=" + LAST_NAME
                        + "&Номер=" + PHONE_NUMBER
                        + "&Приют=" + id,
                VOLUNTEER,
                String.class);

        assertNotNull(actual);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForPhoneNumber")
    void add_InvalideNumberException(String number) {
        String actual = this.template.postForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Имя=" + FIRST_NAME
                        + "&Фамилия=" + LAST_NAME
                        + "&Номер=" + number
                        + "&Приют=" + ID,
                VOLUNTEER,
                String.class);

        assertNotNull(actual);
        assertEquals(exception(BAD_REQUEST, INVALIDE_NUMBER), actual);
    }

    @Test
    void add_ShelterNotFoundException() {
        String actual = this.template.postForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Имя=" + FIRST_NAME
                        + "&Фамилия=" + LAST_NAME
                        + "&Номер=" + PHONE_NUMBER
                        + "&Приют=" + ID,
                VOLUNTEER,
                String.class);

        assertNotNull(actual);
        assertEquals(exception(NOT_FOUND, SHELTER_NOT_FOUND), actual);
    }

    @Test
    void add_VolunteerAlreadyAddedException() {
        addShelter();
        addVolunteer();

        String actual = this.template.postForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Имя=" + FIRST_NAME
                        + "&Фамилия=" + LAST_NAME
                        + "&Номер=" + PHONE_NUMBER
                        + "&Приют=" + SHELTER.getId(),
                VOLUNTEER,
                String.class);

        deleteVolunteer(VOLUNTEER.getId());
        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertEquals(exception(BAD_REQUEST, VOLUNTEER_ALREADY_ADDED), actual);
    }

    @Test
    void getById_success() {
        addShelter();
        addVolunteer();

        Volunteer actual = this.template.getForObject("http://localhost:" + port
                        + "/volunteer/" + VOLUNTEER.getId(),
                Volunteer.class);

        deleteVolunteer(VOLUNTEER.getId());
        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertEquals(VOLUNTEER, actual);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForId")
    void getById_InvalideInputException(Long id) {
        String actual = this.template.getForObject("http://localhost:" + port
                        + "/volunteer/" + id,
                String.class);

        assertNotNull(actual);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual);
    }

    @Test
    void getById_VolunteerNotFoundException() {
        String actual = this.template.getForObject("http://localhost:" + port
                        + "/volunteer/" + ID,
                String.class);

        assertNotNull(actual);
        assertEquals(exception(NOT_FOUND, VOLUNTEER_NOT_FOUND), actual);
    }

    @Test
    void getAllByParameters_success() {
        addShelter();
        addVolunteer();

        String actual_1 = this.template.getForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Имя=" + FIRST_NAME,
                String.class);

        String actual_2 = this.template.getForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Фамилия=" + LAST_NAME,
                String.class);

        String actual_3 = this.template.getForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Номер=" + NUMBER_FOR_SEARCH,
                String.class);

        String actual_4 = this.template.getForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Приют=" + SHELTER.getId(),
                String.class);

        String actual_5 = this.template.getForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Имя=" + FIRST_NAME
                        + "&Фамилия=" + LAST_NAME,
                String.class);

        String actual_6 = this.template.getForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Имя=" + FIRST_NAME
                        + "&Номер=" + NUMBER_FOR_SEARCH,
                String.class);

        String actual_7 = this.template.getForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Имя=" + FIRST_NAME
                        + "&Приют=" + SHELTER.getId(),
                String.class);

        String actual_8 = this.template.getForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Фамилия=" + LAST_NAME
                        + "&Номер=" + NUMBER_FOR_SEARCH,
                String.class);

        String actual_9 = this.template.getForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Фамилия=" + LAST_NAME
                        + "&Приют=" + SHELTER.getId(),
                String.class);

        String actual_10 = this.template.getForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Номер=" + NUMBER_FOR_SEARCH
                        + "&Приют=" + SHELTER.getId(),
                String.class);

        String actual_11 = this.template.getForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Имя=" + FIRST_NAME
                        + "&Фамилия=" + LAST_NAME
                        + "&Номер=" + NUMBER_FOR_SEARCH,
                String.class);

        String actual_12 = this.template.getForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Имя=" + FIRST_NAME
                        + "&Фамилия=" + LAST_NAME
                        + "&Приют=" + SHELTER.getId(),
                String.class);

        String actual_13 = this.template.getForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Имя=" + FIRST_NAME
                        + "&Номер=" + NUMBER_FOR_SEARCH
                        + "&Приют=" + SHELTER.getId(),
                String.class);

        String actual_14 = this.template.getForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Фамилия=" + LAST_NAME
                        + "&Номер=" + NUMBER_FOR_SEARCH
                        + "&Приют=" + SHELTER.getId(),
                String.class);

        String actual_15 = this.template.getForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Имя=" + FIRST_NAME
                        + "&Фамилия=" + LAST_NAME
                        + "&Номер=" + NUMBER_FOR_SEARCH
                        + "&Приют=" + SHELTER.getId(),
                String.class);

        deleteVolunteer(VOLUNTEER.getId());
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
                        + "?Имя=" + FIRST_NAME
                        + "&Фамилия=" + name
                        + "&Номер=" + NUMBER_FOR_SEARCH,
                String.class);

        String actual_10 = this.template.getForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Имя=" + name
                        + "&Фамилия=" + LAST_NAME
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
                        + "/volunteer"
                        + "?Приют=" + id,
                String.class);

        String actual_2 = this.template.getForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Имя=" + FIRST_NAME
                        + "&Приют=" + id,
                String.class);

        String actual_3 = this.template.getForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Фамилия=" + LAST_NAME
                        + "&Приют=" + id,
                String.class);

        String actual_4 = this.template.getForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Номер=" + NUMBER_FOR_SEARCH
                        + "&Приют=" + id,
                String.class);

        String actual_5 = this.template.getForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Имя=" + FIRST_NAME
                        + "&Фамилия=" + LAST_NAME
                        + "&Приют=" + id,
                String.class);

        String actual_6 = this.template.getForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Имя=" + FIRST_NAME
                        + "&Номер=" + NUMBER_FOR_SEARCH
                        + "&Приют=" + id,
                String.class);

        String actual_7 = this.template.getForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Фамилия=" + LAST_NAME
                        + "&Номер=" + NUMBER_FOR_SEARCH
                        + "&Приют=" + id,
                String.class);

        String actual_8 = this.template.getForObject("http://localhost:" + port
                        + "/volunteer"
                        + "?Имя=" + FIRST_NAME
                        + "&Фамилия=" + LAST_NAME
                        + "&Номер=" + NUMBER_FOR_SEARCH
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
        addVolunteer();

        String actual = this.template.getForObject("http://localhost:" + port
                        + "/volunteer/all",
                String.class);

        deleteVolunteer(VOLUNTEER.getId());
        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertTrueForCollection(actual);
    }

    @Test
    void edit_success() {
        addShelter();
        addVolunteer();

        Long id = shelterController.add(CAT, NEW_ADDRESS).getId();
        NEW_SHELTER.setId(id);

        Volunteer expected = new Volunteer(NEW_FIRST_NAME, NEW_LAST_NAME, NEW_VALID_PHONE_NUMBER, NEW_SHELTER);
        expected.setId(VOLUNTEER.getId());

        Volunteer actual = this.template.exchange("http://localhost:" + port
                        + "/volunteer/" + VOLUNTEER.getId()
                        + "?Имя=" + NEW_FIRST_NAME
                        + "&Фамилия=" + NEW_LAST_NAME
                        + "&Номер=" + NEW_PHONE_NUMBER
                        + "&Приют=" + NEW_SHELTER.getId(),

                HttpMethod.PUT,
                HttpEntity.EMPTY,
                Volunteer.class).getBody();

        deleteVolunteer(VOLUNTEER.getId());
        deleteShelter(NEW_SHELTER.getId());
        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForName")
    void edit_InvalideInputException(String name) {
        addShelter();
        addVolunteer();

        String actual_1 = this.template.exchange("http://localhost:" + port
                        + "/volunteer/" + VOLUNTEER.getId()
                        + "?Имя=" + name,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        String actual_2 = this.template.exchange("http://localhost:" + port
                        + "/volunteer/" + VOLUNTEER.getId()
                        + "?Фамилия=" + name,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        deleteVolunteer(VOLUNTEER.getId());
        deleteShelter(SHELTER.getId());

        assertNotNull(actual_1);
        assertNotNull(actual_2);

        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual_1);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual_2);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForId")
    void edit_InvalideInputException(Long id) {
        addShelter();
        addVolunteer();

        String actual = this.template.exchange("http://localhost:" + port
                        + "/volunteer/" + VOLUNTEER.getId()
                        + "?Приют=" + id,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        deleteVolunteer(VOLUNTEER.getId());
        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForPhoneNumber")
    void edit_InvalideNumberException(String number) {
        addShelter();
        addVolunteer();

        String actual = this.template.exchange("http://localhost:" + port
                        + "/volunteer/" + VOLUNTEER.getId()
                        + "?Номер=" + number,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        deleteVolunteer(VOLUNTEER.getId());
        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertEquals(exception(BAD_REQUEST, INVALIDE_NUMBER), actual);
    }

    @Test
    void edit_ShelterNotFoundException() {
        addShelter();
        addVolunteer();

        String actual = this.template.exchange("http://localhost:" + port
                        + "/volunteer/" + VOLUNTEER.getId()
                        + "?Приют=" + ID,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        deleteVolunteer(VOLUNTEER.getId());
        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertEquals(exception(NOT_FOUND, SHELTER_NOT_FOUND), actual);
    }

    @Test
    void edit_VolunteerNotFoundException() {
        String actual = this.template.exchange("http://localhost:" + port
                        + "/volunteer/" + ID,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        assertNotNull(actual);
        assertEquals(exception(NOT_FOUND, VOLUNTEER_NOT_FOUND), actual);
    }

    @Test
    void edit_VolunteerAlreadyAddedException() {
        addShelter();
        addVolunteer();

        String actual = this.template.exchange("http://localhost:" + port
                        + "/volunteer/" + VOLUNTEER.getId()
                        + "?Номер=" + PHONE_NUMBER,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        deleteVolunteer(VOLUNTEER.getId());
        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertEquals(exception(BAD_REQUEST, VOLUNTEER_ALREADY_ADDED), actual);
    }

    @Test
    void delete_success() {
        addShelter();
        addVolunteer();

        Volunteer actual = this.template.exchange("http://localhost:" + port
                        + "/volunteer/" + VOLUNTEER.getId(),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Volunteer.class).getBody();

        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertEquals(VOLUNTEER, actual);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForId")
    void delete_InvalideInputException(Long id) {
        String actual = this.template.exchange("http://localhost:" + port
                        + "/volunteer/" + id,
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                String.class).getBody();

        assertNotNull(actual);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual);
    }

    @Test
    void delete_VolunteerNotFoundException() {
        String actual = this.template.exchange("http://localhost:" + port
                        + "/volunteer/" + ID,
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                String.class).getBody();

        assertNotNull(actual);
        assertEquals(exception(NOT_FOUND, VOLUNTEER_NOT_FOUND), actual);
    }

    private void addShelter() {
        Long id = shelterController.add(DOG, ADDRESS).getId();
        SHELTER.setId(id);
    }

    private void addVolunteer() {
        Long id = volunteerController.add(FIRST_NAME, LAST_NAME, PHONE_NUMBER, SHELTER.getId()).getId();
        VOLUNTEER.setShelter(SHELTER);
        VOLUNTEER.setId(id);
    }

    private void deleteShelter(Long id) {
        shelterController.delete(id);
    }

    private void deleteVolunteer(Long id) {
        volunteerController.delete(id);
    }

    private void assertTrueForCollection(String actual) {
        assertTrue(actual.contains(VOLUNTEER.getId().toString()));
        assertTrue(actual.contains(VOLUNTEER.getFirstName()));
        assertTrue(actual.contains(VOLUNTEER.getLastName()));
        assertTrue(actual.contains(VOLUNTEER.getPhoneNumber()));
        assertTrue(actual.contains(VOLUNTEER.getShelter().getId().toString()));
        assertTrue(actual.contains(VOLUNTEER.getShelter().getType().toString()));
        assertTrue(actual.contains(VOLUNTEER.getShelter().getAddress()));
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