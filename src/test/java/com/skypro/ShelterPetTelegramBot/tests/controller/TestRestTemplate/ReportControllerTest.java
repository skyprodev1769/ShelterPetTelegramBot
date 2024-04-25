package com.skypro.ShelterPetTelegramBot.tests.controller.TestRestTemplate;

import com.skypro.ShelterPetTelegramBot.controller.ParentController;
import com.skypro.ShelterPetTelegramBot.controller.PetController;
import com.skypro.ShelterPetTelegramBot.controller.ReportController;
import com.skypro.ShelterPetTelegramBot.controller.ShelterController;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Report;
import com.skypro.ShelterPetTelegramBot.model.entity.without_controller.PotentialParent;
import com.skypro.ShelterPetTelegramBot.model.enums.MessageContent;
import com.skypro.ShelterPetTelegramBot.model.repository.PotentialParentRepository;
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

import static com.skypro.ShelterPetTelegramBot.model.enums.MessageContent.*;
import static com.skypro.ShelterPetTelegramBot.model.enums.PetStatus.FREE;
import static com.skypro.ShelterPetTelegramBot.model.enums.PetType.DOG;
import static com.skypro.ShelterPetTelegramBot.model.enums.ReportStatus.NOT_VIEWED;
import static com.skypro.ShelterPetTelegramBot.tests.Utils.*;
import static com.skypro.ShelterPetTelegramBot.utils.Exceptions.*;
import static com.skypro.ShelterPetTelegramBot.utils.documentation.ReportControllerDoc.EXAMPLE_SEND_MESSAGE;
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
    private ReportRepository reportRepository;
    @Autowired
    private PotentialParentRepository potentialParentRepository;
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

    @ParameterizedTest
    @MethodSource("provideParamsForMessage")
    void sendAnswer_success(MessageContent content) {
        addRecipient();
        addShelter();
        addPet();
        addParent();

        String actual = this.template.getForObject("http://localhost:" + port
                        + "/report/answer"
                        + "?Сообщение=" + content
                        + "&Имя=" + PARENT.getFirstName()
                        + "&Фамилия=" + PARENT.getLastName()
                        + "&Номер=" + RECIPIENT.getPhoneNumber()
                        + "&Животное=" + PET.getName(),
                String.class);

        deleteParent(PARENT.getId());
        deletePet(PET.getId());
        deleteShelter(SHELTER.getId());
        deleteRecipient();

        assertNotNull(actual);
        assertEquals(EXAMPLE_SEND_MESSAGE, actual);
    }

    @ParameterizedTest
    @MethodSource("provideParamsForName")
    void sendAnswer_InvalideInputException(String name) {
        String actual_1 = this.template.getForObject("http://localhost:" + port
                        + "/report/answer"
                        + "?Сообщение=" + EXTENSION_14
                        + "&Имя=" + name
                        + "&Фамилия=" + LAST_NAME
                        + "&Номер=" + PHONE_NUMBER
                        + "&Животное=" + NAME,
                String.class);

        String actual_2 = this.template.getForObject("http://localhost:" + port
                        + "/report/answer"
                        + "?Сообщение=" + EXTENSION_14
                        + "&Имя=" + FIRST_NAME
                        + "&Фамилия=" + name
                        + "&Номер=" + PHONE_NUMBER
                        + "&Животное=" + NAME,
                String.class);

        String actual_3 = this.template.getForObject("http://localhost:" + port
                        + "/report/answer"
                        + "?Сообщение=" + EXTENSION_14
                        + "&Имя=" + FIRST_NAME
                        + "&Фамилия=" + LAST_NAME
                        + "&Номер=" + PHONE_NUMBER
                        + "&Животное=" + name,
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
    void sendAnswer_InvalideNumberException(String number) {
        String actual = this.template.getForObject("http://localhost:" + port
                        + "/report/answer"
                        + "?Сообщение=" + EXTENSION_14
                        + "&Имя=" + FIRST_NAME
                        + "&Фамилия=" + LAST_NAME
                        + "&Номер=" + number
                        + "&Животное=" + NAME,
                String.class);

        assertNotNull(actual);
        assertEquals(exception(BAD_REQUEST, INVALIDE_NUMBER), actual);
    }

    @Test
    void sendAnswer_ParentNotFoundException() {
        addRecipient();
        addShelter();
        addPet();

        String actual = this.template.getForObject("http://localhost:" + port
                        + "/report/answer"
                        + "?Сообщение=" + EXTENSION_14
                        + "&Имя=" + FIRST_NAME
                        + "&Фамилия=" + LAST_NAME
                        + "&Номер=" + RECIPIENT.getPhoneNumber()
                        + "&Животное=" + PET.getName(),
                String.class);

        deletePet(PET.getId());
        deleteShelter(SHELTER.getId());
        deleteRecipient();

        assertNotNull(actual);
        assertEquals(exception(NOT_FOUND, PARENT_NOT_FOUND), actual);
    }

    @Test
    void sendAnswer_RecipientNotFoundException() {
        addShelter();
        addPet();
        addParent();

        String actual = this.template.getForObject("http://localhost:" + port
                        + "/report/answer"
                        + "?Сообщение=" + EXTENSION_14
                        + "&Имя=" + PARENT.getFirstName()
                        + "&Фамилия=" + PARENT.getLastName()
                        + "&Номер=" + PHONE_NUMBER
                        + "&Животное=" + PET.getName(),
                String.class);

        deleteParent(PARENT.getId());
        deletePet(PET.getId());
        deleteShelter(SHELTER.getId());

        assertNotNull(actual);
        assertEquals(exception(NOT_FOUND, RECIPIENT_NOT_FOUND), actual);
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
        Long id = reportRepository.save(REPORT).getId();
        REPORT.setId(id);
    }

    private void addRecipient() {
        potentialParentRepository.save(RECIPIENT);
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
        reportRepository.delete(reportController.getById(REPORT.getId()));
    }

    private void deleteRecipient() {
        PotentialParent recipient = potentialParentRepository.getByPhoneNumber(RECIPIENT.getPhoneNumber());
        potentialParentRepository.delete(recipient);
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

    private static Stream<Arguments> provideParamsForMessage() {
        return Stream.of(
                Arguments.of(EXTENSION_14),
                Arguments.of(EXTENSION_30),
                Arguments.of(CONGRATULATION),
                Arguments.of(REFUSAL)
        );
    }
}