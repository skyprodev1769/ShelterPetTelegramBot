package com.skypro.ShelterPetTelegramBot.tests.controller.TestRestTemplate;

import com.skypro.ShelterPetTelegramBot.controller.ParentController;
import com.skypro.ShelterPetTelegramBot.controller.PetController;
import com.skypro.ShelterPetTelegramBot.controller.ReportController;
import com.skypro.ShelterPetTelegramBot.controller.ShelterController;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Report;
import com.skypro.ShelterPetTelegramBot.model.repository.ReportRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.LocalDate;
import java.util.stream.Stream;

import static com.skypro.ShelterPetTelegramBot.model.enums.PetStatus.FREE;
import static com.skypro.ShelterPetTelegramBot.model.enums.PetType.DOG;
import static com.skypro.ShelterPetTelegramBot.model.enums.ReportStatus.NOT_VIEWED;
import static com.skypro.ShelterPetTelegramBot.tests.Utils.*;
import static com.skypro.ShelterPetTelegramBot.utils.Exceptions.INVALIDE_INPUT;
import static com.skypro.ShelterPetTelegramBot.utils.Exceptions.REPORT_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportControllerTest {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate template;
    @Autowired
    private ReportRepository repository;
    @Autowired
    private ShelterController shelterController;
    @Autowired
    private PetController petController;
    @Autowired
    private ParentController parentController;
    @Autowired
    private ReportController reportController;

    @Test
    void contextLoads() {
        Assertions.assertThat(shelterController).isNotNull();
        Assertions.assertThat(petController).isNotNull();
        Assertions.assertThat(parentController).isNotNull();
        Assertions.assertThat(reportController).isNotNull();
    }

    @Test
    void getById_success() {
        addShelter();
        addPet();
        addParent();
        addReport();

        Report actual = this.template.getForObject("http://localhost:" + port
                        + "/report/" + REPORT.getId(),
                Report.class);

        deleteReport();
        deleteParent(PARENT.getId());
        deletePet(PET.getId());
        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertEquals(REPORT.getId(), actual.getId());
        assertEquals(REPORT.getPhoto(), actual.getPhoto());
        assertEquals(REPORT.getDocument(), actual.getDocument());
        assertEquals(REPORT.getStatus(), actual.getStatus());
    }

    @ParameterizedTest
    @MethodSource("provideParamsForId")
    void getById_InvalideInputException(Long id) {
        String actual = this.template.getForObject("http://localhost:" + port
                        + "/report/" + id,
                String.class);

        assertNotNull(actual);
        assertEquals(exception(BAD_REQUEST, INVALIDE_INPUT), actual);
    }

    @Test
    void getById_ReportNotFoundException() {
        String actual = this.template.getForObject("http://localhost:" + port
                        + "/report/" + ID,
                String.class);

        assertNotNull(actual);
        assertEquals(exception(NOT_FOUND, REPORT_NOT_FOUND), actual);
    }

    @Test
    void getAllByParameters_success() {
        addShelter();
        addPet();
        addParent();
        addReport();

        String actual_1 = this.template.getForObject("http://localhost:" + port
                        + "/report"
                        + "?Дата=" + LocalDate.now(),
                String.class);

        String actual_2 = this.template.getForObject("http://localhost:" + port
                        + "/report"
                        + "?Статус=" + NOT_VIEWED,
                String.class);

        String actual_3 = this.template.getForObject("http://localhost:" + port
                        + "/report"
                        + "?Дата=" + LocalDate.now()
                        + "&Статус=" + NOT_VIEWED,
                String.class);

        deleteReport();
        deleteParent(PARENT.getId());
        deletePet(PET.getId());
        deleteShelter(SHELTER.getId());

        assertNotNull(actual_1);
        assertNotNull(actual_2);
        assertNotNull(actual_3);

        assertTrueForCollection(actual_1);
        assertTrueForCollection(actual_2);
        assertTrueForCollection(actual_3);
    }

    @Test
    void getAll_success() {
        addShelter();
        addPet();
        addParent();
        addReport();

        String actual = this.template.getForObject("http://localhost:" + port
                        + "/report/all",
                String.class);

        deleteReport();
        deleteParent(PARENT.getId());
        deletePet(PET.getId());
        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertTrueForCollection(actual);
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

    private void addReport() {
        Long id = repository.save(REPORT).getId();
        REPORT.setId(id);
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

    private void deleteReport() {
        repository.delete(reportController.getById(REPORT.getId()));
    }

    private void assertTrueForCollection(String actual) {
        assertTrue(actual.contains(REPORT.getId().toString()));
        assertTrue(actual.contains(REPORT.getPhoto()));
        assertTrue(actual.contains(REPORT.getDocument()));
        assertTrue(actual.contains(REPORT.getStatus().toString()));
    }

    private static Stream<Arguments> provideParamsForId() {
        return Stream.of(
                Arguments.of(ZERO),
                Arguments.of(INCORRECT_ID)
        );
    }
}