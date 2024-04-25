package com.skypro.ShelterPetTelegramBot.tests.controller.TestRestTemplate;

import com.skypro.ShelterPetTelegramBot.controller.ShelterController;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Shelter;
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
class ShelterControllerTest {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate template;
    @Autowired
    private ShelterController shelterController;

    @Test
    void contextLoads() {
        Assertions.assertThat(shelterController).isNotNull();
    }

    @Test
    void add_success() {
        Shelter actual = this.template.postForObject("http://localhost:" + port
                        + "/shelter"
                        + "?Тип=" + DOG
                        + "&Адрес=" + ADDRESS,
                SHELTER,
                Shelter.class);

        SHELTER.setId(actual.getId());

        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertEquals(SHELTER, actual);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForAddress")
    void add_InvalideInputException(String address) {
        String actual = this.template.postForObject("http://localhost:" + port
                        + "/shelter"
                        + "?Тип=" + DOG
                        + "&Адрес=" + address,
                SHELTER,
                String.class);

        assertNotNull(actual);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual);
    }

    @Test
    void add_ShelterAlreadyAddedException() {
        addShelter();

        String actual = this.template.postForObject("http://localhost:" + port
                        + "/shelter"
                        + "?Тип=" + DOG
                        + "&Адрес=" + ADDRESS,
                SHELTER,
                String.class);

        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertEquals(exception(BAD_REQUEST, SHELTER_ALREADY_ADDED), actual);
    }

    @Test
    void getById_success() {
        addShelter();

        Shelter actual = this.template.getForObject("http://localhost:" + port
                        + "/shelter/" + SHELTER.getId(),
                Shelter.class);

        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertEquals(SHELTER, actual);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForId")
    void getById_InvalideInputException(Long id) {
        String actual = this.template.getForObject("http://localhost:" + port
                        + "/shelter/" + id,
                String.class);

        assertNotNull(actual);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual);
    }

    @Test
    void getById_ShelterNotFoundException() {
        String actual = this.template.getForObject("http://localhost:" + port
                        + "/shelter/" + ID,
                String.class);

        assertNotNull(actual);
        assertEquals(exception(NOT_FOUND, SHELTER_NOT_FOUND), actual);
    }

    @Test
    void getAllByParameters_success() {
        addShelter();

        String actual_1 = this.template.getForObject("http://localhost:" + port
                        + "/shelter"
                        + "?Тип=" + DOG,
                String.class);

        String actual_2 = this.template.getForObject("http://localhost:" + port
                        + "/shelter"
                        + "?Адрес=" + ADDRESS,
                String.class);

        String actual_3 = this.template.getForObject("http://localhost:" + port
                        + "/shelter"
                        + "?Тип=" + DOG
                        + "&Адрес=" + ADDRESS,
                String.class);

        deleteShelter(SHELTER.getId());

        assertNotNull(actual_1);
        assertNotNull(actual_2);
        assertNotNull(actual_3);

        assertTrueForCollection(actual_1);
        assertTrueForCollection(actual_2);
        assertTrueForCollection(actual_3);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForAddress")
    void getAllByParameters_InvalideInputException(String address) {
        String actual_1 = this.template.getForObject("http://localhost:" + port
                        + "/shelter"
                        + "?Адрес=" + address,
                String.class);

        String actual_2 = this.template.getForObject("http://localhost:" + port
                        + "/shelter"
                        + "?Тип=" + DOG
                        + "&Адрес=" + address,
                String.class);

        assertNotNull(actual_1);
        assertNotNull(actual_2);

        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual_1);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual_2);
    }

    @Test
    void getAll_success() {
        addShelter();

        String actual = this.template.getForObject("http://localhost:" + port
                        + "/shelter/all",
                String.class);

        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertTrueForCollection(actual);
    }

    @Test
    void edit_success() {
        addShelter();

        Shelter expected = new Shelter(CAT, NEW_ADDRESS);
        expected.setId(SHELTER.getId());

        Shelter actual = this.template.exchange("http://localhost:" + port
                        + "/shelter/" + SHELTER.getId()
                        + "?Тип=" + CAT
                        + "&Адрес=" + NEW_ADDRESS,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                Shelter.class).getBody();

        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForAddress")
    void edit_InvalideInputException(String address) {
        addShelter();

        String actual = this.template.exchange("http://localhost:" + port
                        + "/shelter/" + SHELTER.getId()
                        + "?Адрес=" + address,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual);
    }

    @Test
    void edit_ShelterAlreadyAddedException() {
        addShelter();

        String actual = this.template.exchange("http://localhost:" + port
                        + "/shelter/" + SHELTER.getId()
                        + "?Адрес=" + ADDRESS,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertEquals(exception(BAD_REQUEST, SHELTER_ALREADY_ADDED), actual);
    }

    @Test
    void edit_ShelterNotFoundException() {
        String actual = this.template.exchange("http://localhost:" + port
                        + "/shelter/" + ID,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                String.class).getBody();

        assertNotNull(actual);
        assertEquals(exception(NOT_FOUND, SHELTER_NOT_FOUND), actual);
    }

    @Test
    void delete_success() {
        addShelter();

        Shelter actual = this.template.exchange("http://localhost:" + port
                        + "/shelter/" + SHELTER.getId(),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Shelter.class).getBody();

        assertNotNull(actual);
        assertEquals(SHELTER, actual);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForId")
    void delete_InvalideInputException(Long id) {
        String actual = this.template.exchange("http://localhost:" + port
                        + "/shelter/" + id,
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                String.class).getBody();

        assertNotNull(actual);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual);
    }

    @Test
    void delete_ShelterNotFoundException() {
        String actual = this.template.exchange("http://localhost:" + port
                        + "/shelter/" + ID,
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                String.class).getBody();

        assertNotNull(actual);
        assertEquals(exception(NOT_FOUND, SHELTER_NOT_FOUND), actual);
    }

    private void addShelter() {
        Long id = shelterController.add(DOG, ADDRESS).getId();
        SHELTER.setId(id);
    }

    private void deleteShelter(Long id) {
        shelterController.delete(id);
    }

    private void assertTrueForCollection(String actual) {
        assertTrue(actual.contains(SHELTER.getId().toString()));
        assertTrue(actual.contains(SHELTER.getType().toString()));
        assertTrue(actual.contains(SHELTER.getAddress()));
    }

    private static Stream<Arguments> provideParamsForAddress() {
        return Stream.of(
                null,
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