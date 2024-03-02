package com.skypro.ShelterPetTelegramBot.controller.TestRestTemplate;

import com.skypro.ShelterPetTelegramBot.controller.ShelterController;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Shelter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import static com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType.CAT;
import static com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType.DOG;
import static com.skypro.ShelterPetTelegramBot.utils.Exceptions.*;
import static com.skypro.ShelterPetTelegramBot.utils.UtilsForShelterService.*;
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

    @Test
    void add_InvalideInputException() {
        String actual = this.template.postForObject("http://localhost:" + port
                        + "/shelter"
                        + "?Тип=" + DOG
                        + "&Адрес=" + INCORRECT_ADDRESS,
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

    @Test
    void getById_InvalideInputException() {
        String actual = this.template.getForObject("http://localhost:" + port
                        + "/shelter/" + 0L,
                String.class);

        assertNotNull(actual);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual);
    }

    @Test
    void getById_ShelterNotFoundException() {
        String actual = this.template.getForObject("http://localhost:" + port
                        + "/shelter/" + ID_SHELTER,
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

        asserTrueForCollection(actual_1);
        asserTrueForCollection(actual_2);
        asserTrueForCollection(actual_3);
    }

    @Test
    void getAllByParameters_InvalideInputException() {
        String actual_1 = this.template.getForObject("http://localhost:" + port
                        + "/shelter"
                        + "?Адрес=" + INCORRECT_ADDRESS,
                String.class);

        String actual_2 = this.template.getForObject("http://localhost:" + port
                        + "/shelter"
                        + "?Тип=" + DOG
                        + "&Адрес=" + INCORRECT_ADDRESS,
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
        asserTrueForCollection(actual);
    }

    @Test
    void edit_success() {
        addShelter();

        Shelter expected_1 = new Shelter(CAT, ADDRESS);
        expected_1.setId(SHELTER.getId());

        Shelter actual_1 = this.template.exchange("http://localhost:" + port
                        + "/shelter/" + SHELTER.getId()
                        + "?Тип=" + CAT,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                Shelter.class).getBody();

        deleteShelter(SHELTER.getId());

//======================================================================================================================

        addShelter();

        Shelter expected_2 = new Shelter(DOG, NEW_ADDRESS);
        expected_2.setId(SHELTER.getId());

        Shelter actual_2 = this.template.exchange("http://localhost:" + port
                        + "/shelter/" + SHELTER.getId()
                        + "?Адрес=" + NEW_ADDRESS,
                HttpMethod.PUT,
                HttpEntity.EMPTY,
                Shelter.class).getBody();

        deleteShelter(SHELTER.getId());

        assertNotNull(actual_1);
        assertNotNull(actual_2);

        assertEquals(expected_1, actual_1);
        assertEquals(expected_2, actual_2);
    }

    @Test
    void edit_InvalideInputException() {
        addShelter();

        String actual = this.template.exchange("http://localhost:" + port
                        + "/shelter/" + SHELTER.getId()
                        + "?Адрес=" + INCORRECT_ADDRESS,
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

    private void addShelter() {
        Long id = shelterController.add(DOG, ADDRESS).getId();
        SHELTER.setId(id);
    }

    private void deleteShelter(Long id) {
        shelterController.delete(id);
    }

    private String exception(HttpStatus status, String message) {
        return String.format("Code: %s. Error: %s", status, message);
    }

    private void asserTrueForCollection(String actual) {
        assertTrue(actual.contains(SHELTER.getId().toString()));
        assertTrue(actual.contains(SHELTER.getType().toString()));
        assertTrue(actual.contains(SHELTER.getAddress()));
    }
}