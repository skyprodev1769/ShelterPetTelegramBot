package com.skypro.ShelterPetTelegramBot.tests.controller.WebMvcTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skypro.ShelterPetTelegramBot.configuration.BotConfiguration;
import com.skypro.ShelterPetTelegramBot.controller.ReportController;
import com.skypro.ShelterPetTelegramBot.model.enums.MessageContent;
import com.skypro.ShelterPetTelegramBot.model.enums.ReportStatus;
import com.skypro.ShelterPetTelegramBot.model.repository.*;
import com.skypro.ShelterPetTelegramBot.service.TelegramBot;
import com.skypro.ShelterPetTelegramBot.service.impl.CheckServiceImpl;
import com.skypro.ShelterPetTelegramBot.service.impl.bot_service.BasicMethodsImpl;
import com.skypro.ShelterPetTelegramBot.service.impl.bot_service.SendingMessageImpl;
import com.skypro.ShelterPetTelegramBot.service.impl.entity_service.*;
import com.skypro.ShelterPetTelegramBot.tests.Utils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import static com.skypro.ShelterPetTelegramBot.model.enums.MessageContent.*;
import static com.skypro.ShelterPetTelegramBot.model.enums.ReportStatus.NOT_VIEWED;
import static com.skypro.ShelterPetTelegramBot.tests.Utils.*;
import static com.skypro.ShelterPetTelegramBot.utils.Exceptions.*;
import static com.skypro.ShelterPetTelegramBot.utils.documentation.ReportControllerDoc.EXAMPLE_SEND_MESSAGE;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@EnableTransactionManagement(proxyTargetClass = true)
public class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PotentialParentRepository potentialParentRepository;
    @MockBean
    private ShelterRepository shelterRepository;
    @MockBean
    private VolunteerRepository volunteerRepository;
    @MockBean
    private PetRepository petRepository;
    @MockBean
    private ParentRepository parentRepository;
    @MockBean
    private ReportRepository reportRepository;
    @SpyBean
    private ShelterServiceImpl shelterService;
    @SpyBean
    private VolunteerServiceImpl volunteerService;
    @SpyBean
    private PetServiceImpl petService;
    @SpyBean
    private ParentServiceImpl parentService;
    @SpyBean
    private ReportServiceImpl reportService;
    @SpyBean
    private CheckServiceImpl checkService;
    @SpyBean
    private TelegramBot bot;
    @SpyBean
    private BasicMethodsImpl methods;
    @SpyBean
    private SendingMessageImpl message;
    @SpyBean
    private BotConfiguration configuration;
    @InjectMocks
    private ReportController controller;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getById_success() throws Exception {
        when(reportRepository.findById(anyLong()))
                .thenReturn(Optional.of(REPORT));

        REPORT.setId(ID);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/report/" + REPORT.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(REPORT.getId()))
                .andExpect(jsonPath("$.photo").value(REPORT.getPhoto()))
                .andExpect(jsonPath("$.document").value(REPORT.getDocument()))
                .andExpect(jsonPath("$.status").value(REPORT.getStatus().toString()));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForId")
    void getById_InvalideInputException(Long id) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/report/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));
    }

    @Test
    void getById_ReportNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/report/" + ID))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(NOT_FOUND, REPORT_NOT_FOUND)));
    }

    @Test
    void getAllByParameters_success() throws Exception {
        when(reportRepository.getAllByDate(any(LocalDate.class)))
                .thenReturn(REPORTS);

        when(reportRepository.getAllByStatus(any(ReportStatus.class)))
                .thenReturn(REPORTS);

        when(reportRepository.getAllByDateAndStatus(any(LocalDate.class), any(ReportStatus.class)))
                .thenReturn(REPORTS);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/report"
                                + "?Дата=" + LocalDate.now()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(REPORTS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/report"
                                + "?Статус=" + NOT_VIEWED))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(REPORTS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/report"
                                + "?Дата=" + LocalDate.now()
                                + "&Статус=" + NOT_VIEWED))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(REPORTS)));
    }

    @Test
    void getAll_success() throws Exception {
        when(reportService.getAll())
                .thenReturn(REPORTS);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/report/all"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(REPORTS)));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForMessage")
    void sendAnswer_success(MessageContent content) throws Exception {
        getParent();

        when(potentialParentRepository.findAll())
                .thenReturn(RECIPIENTS);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/report/answer"
                                + "?Сообщение=" + content
                                + "&Имя=" + PARENT.getFirstName()
                                + "&Фамилия=" + PARENT.getLastName()
                                + "&Номер=" + RECIPIENT.getPhoneNumber()
                                + "&Животное=" + PARENT.getPet().getName()))
                .andExpect(status().isOk())
                .andExpect(content().string(EXAMPLE_SEND_MESSAGE));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForName")
    void sendAnswer_InvalideInputException(String name) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/report/answer"
                                + "?Сообщение=" + EXTENSION_14
                                + "&Имя=" + name
                                + "&Фамилия=" + LAST_NAME
                                + "&Номер=" + PHONE_NUMBER
                                + "&Животное=" + NAME))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/report/answer"
                                + "?Сообщение=" + EXTENSION_14
                                + "&Имя=" + FIRST_NAME
                                + "&Фамилия=" + name
                                + "&Номер=" + PHONE_NUMBER
                                + "&Животное=" + NAME))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/report/answer"
                                + "?Сообщение=" + EXTENSION_14
                                + "&Имя=" + FIRST_NAME
                                + "&Фамилия=" + LAST_NAME
                                + "&Номер=" + PHONE_NUMBER
                                + "&Животное=" + name))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForPhoneNumber")
    void sendAnswer_InvalideNumberException(String number) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/report/answer"
                                + "?Сообщение=" + EXTENSION_14
                                + "&Имя=" + FIRST_NAME
                                + "&Фамилия=" + LAST_NAME
                                + "&Номер=" + number
                                + "&Животное=" + NAME))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_NUMBER)));
    }

    @Test
    void sendAnswer_ParentNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/report/answer"
                                + "?Сообщение=" + EXTENSION_14
                                + "&Имя=" + FIRST_NAME
                                + "&Фамилия=" + LAST_NAME
                                + "&Номер=" + PHONE_NUMBER
                                + "&Животное=" + NAME))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(NOT_FOUND, PARENT_NOT_FOUND)));
    }

    @Test
    void sendAnswer_RecipientNotFoundException() throws Exception {
        getParent();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/report/answer"
                                + "?Сообщение=" + EXTENSION_14
                                + "&Имя=" + PARENT.getFirstName()
                                + "&Фамилия=" + PARENT.getLastName()
                                + "&Номер=" + PHONE_NUMBER
                                + "&Животное=" + PARENT.getPet().getName()))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(NOT_FOUND, RECIPIENT_NOT_FOUND)));
    }

    private void getParent() {
        when(parentRepository.getByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCaseAndPetNameContainsIgnoreCase(anyString(), anyString(), anyString()))
                .thenReturn(Optional.of(PARENT));
    }

    private static Stream<Arguments> provideParamsForId() {
        return Stream.of(
                Arguments.of(Utils.ZERO),
                Arguments.of(Utils.INCORRECT_ID)
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