package com.skypro.ShelterPetTelegramBot.tests.controller.WebMvcTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skypro.ShelterPetTelegramBot.configuration.BotConfiguration;
import com.skypro.ShelterPetTelegramBot.controller.VolunteerController;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Volunteer;
import com.skypro.ShelterPetTelegramBot.model.repository.*;
import com.skypro.ShelterPetTelegramBot.service.TelegramBot;
import com.skypro.ShelterPetTelegramBot.service.impl.CheckServiceImpl;
import com.skypro.ShelterPetTelegramBot.service.impl.bot_service.BasicMethodsImpl;
import com.skypro.ShelterPetTelegramBot.service.impl.bot_service.SendingMessageImpl;
import com.skypro.ShelterPetTelegramBot.service.impl.entity_service.*;
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

import java.util.Optional;
import java.util.stream.Stream;

import static com.skypro.ShelterPetTelegramBot.tests.Utils.*;
import static com.skypro.ShelterPetTelegramBot.utils.Exceptions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@EnableTransactionManagement(proxyTargetClass = true)
class VolunteerControllerTest {

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
    private VolunteerController controller;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void add_success() throws Exception {
        getShelter();

        when(volunteerService.add(FIRST_NAME, LAST_NAME, PHONE_NUMBER, SHELTER.getId()))
                .thenReturn(VOLUNTEER);

        VOLUNTEER.setId(ID);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/volunteer"
                                + "?Имя=" + FIRST_NAME
                                + "&Фамилия=" + LAST_NAME
                                + "&Номер=" + PHONE_NUMBER
                                + "&Приют=" + SHELTER.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VOLUNTEER.getId()))
                .andExpect(jsonPath("$.firstName").value(VOLUNTEER.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(VOLUNTEER.getLastName()))
                .andExpect(jsonPath("$.phoneNumber").value(VOLUNTEER.getPhoneNumber()))
                .andExpect(jsonPath("$.shelter.id").value(SHELTER.getId()))
                .andExpect(jsonPath("$.shelter.type").value(SHELTER.getType().toString()))
                .andExpect(jsonPath("$.shelter.address").value(SHELTER.getAddress()));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForName")
    void add_InvalideInputException(String name) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/volunteer"
                                + "?Имя=" + name
                                + "&Фамилия=" + LAST_NAME
                                + "&Номер=" + PHONE_NUMBER
                                + "&Приют=" + SHELTER.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/volunteer"
                                + "?Имя=" + FIRST_NAME
                                + "&Фамилия=" + name
                                + "&Номер=" + PHONE_NUMBER
                                + "&Приют=" + SHELTER.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForId")
    void add_InvalideInputException(Long id) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/volunteer"
                                + "?Имя=" + FIRST_NAME
                                + "&Фамилия=" + LAST_NAME
                                + "&Номер=" + PHONE_NUMBER
                                + "&Приют=" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForPhoneNumber")
    void add_InvalideNumberException(String number) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/volunteer"
                                + "?Имя=" + FIRST_NAME
                                + "&Фамилия=" + LAST_NAME
                                + "&Номер=" + number
                                + "&Приют=" + SHELTER.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_NUMBER)));
    }

    @Test
    void add_ShelterNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/volunteer"
                                + "?Имя=" + FIRST_NAME
                                + "&Фамилия=" + LAST_NAME
                                + "&Номер=" + PHONE_NUMBER
                                + "&Приют=" + ID))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(NOT_FOUND, SHELTER_NOT_FOUND)));
    }

    @Test
    void add_VolunteerAlreadyAddedException() throws Exception {
        getShelter();
        getAll_success();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/volunteer"
                                + "?Имя=" + FIRST_NAME
                                + "&Фамилия=" + LAST_NAME
                                + "&Номер=" + PHONE_NUMBER
                                + "&Приют=" + SHELTER.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, VOLUNTEER_ALREADY_ADDED)));
    }

    @Test
    void getById_success() throws Exception {
        getShelter();
        getVolunteer();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer/" + VOLUNTEER.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VOLUNTEER.getId()))
                .andExpect(jsonPath("$.firstName").value(VOLUNTEER.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(VOLUNTEER.getLastName()))
                .andExpect(jsonPath("$.phoneNumber").value(VOLUNTEER.getPhoneNumber()))
                .andExpect(jsonPath("$.shelter.id").value(SHELTER.getId()))
                .andExpect(jsonPath("$.shelter.type").value(SHELTER.getType().toString()))
                .andExpect(jsonPath("$.shelter.address").value(SHELTER.getAddress()));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForId")
    void getById_InvalideInputException(Long id) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));
    }

    @Test
    void getById_VolunteerNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer/" + ID))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(NOT_FOUND, VOLUNTEER_NOT_FOUND)));
    }

    @Test
    void getAllByParameters_success() throws Exception {
        when(volunteerRepository.getAllByFirstNameContainsIgnoreCase(anyString()))
                .thenReturn(VOLUNTEERS);

        when(volunteerRepository.getAllByLastNameContainsIgnoreCase(anyString()))
                .thenReturn(VOLUNTEERS);

        when(volunteerRepository.getAllByPhoneNumberContains(anyString()))
                .thenReturn(VOLUNTEERS);

        when(volunteerRepository.getAllByShelterId(anyLong()))
                .thenReturn(VOLUNTEERS);

        when(volunteerRepository.getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCase(anyString(), anyString()))
                .thenReturn(VOLUNTEERS);

        when(volunteerRepository.getAllByFirstNameContainsIgnoreCaseAndPhoneNumberContains(anyString(), anyString()))
                .thenReturn(VOLUNTEERS);

        when(volunteerRepository.getAllByFirstNameContainsIgnoreCaseAndShelterId(anyString(), anyLong()))
                .thenReturn(VOLUNTEERS);

        when(volunteerRepository.getAllByLastNameContainsIgnoreCaseAndPhoneNumberContains(anyString(), anyString()))
                .thenReturn(VOLUNTEERS);

        when(volunteerRepository.getAllByLastNameContainsIgnoreCaseAndShelterId(anyString(), anyLong()))
                .thenReturn(VOLUNTEERS);

        when(volunteerRepository.getAllByPhoneNumberContainsAndShelterId(anyString(), anyLong()))
                .thenReturn(VOLUNTEERS);

        when(volunteerRepository.getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCaseAndPhoneNumberContains(anyString(), anyString(), anyString()))
                .thenReturn(VOLUNTEERS);

        when(volunteerRepository.getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCaseAndShelterId(anyString(), anyString(), anyLong()))
                .thenReturn(VOLUNTEERS);

        when(volunteerRepository.getAllByFirstNameContainsIgnoreCaseAndPhoneNumberContainsAndShelterId(anyString(), anyString(), anyLong()))
                .thenReturn(VOLUNTEERS);

        when(volunteerRepository.getAllByLastNameContainsIgnoreCaseAndPhoneNumberContainsAndShelterId(anyString(), anyString(), anyLong()))
                .thenReturn(VOLUNTEERS);

        when(volunteerRepository.getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCaseAndPhoneNumberContainsAndShelterId(anyString(), anyString(), anyString(), anyLong()))
                .thenReturn(VOLUNTEERS);


        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer"
                                + "?Имя=" + FIRST_NAME))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(VOLUNTEERS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer"
                                + "?Фамилия=" + LAST_NAME))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(VOLUNTEERS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer"
                                + "?Номер=" + NUMBER_FOR_SEARCH))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(VOLUNTEERS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer"
                                + "?Приют=" + ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(VOLUNTEERS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer"
                                + "?Имя=" + FIRST_NAME
                                + "&Фамилия=" + LAST_NAME))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(VOLUNTEERS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer"
                                + "?Имя=" + FIRST_NAME
                                + "&Номер=" + NUMBER_FOR_SEARCH))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(VOLUNTEERS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer"
                                + "?Имя=" + FIRST_NAME
                                + "&Приют=" + ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(VOLUNTEERS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer"
                                + "?Фамилия=" + LAST_NAME
                                + "&Номер=" + NUMBER_FOR_SEARCH))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(VOLUNTEERS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer"
                                + "?Фамилия=" + LAST_NAME
                                + "&Приют=" + ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(VOLUNTEERS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer"
                                + "?Номер=" + NUMBER_FOR_SEARCH
                                + "&Приют=" + ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(VOLUNTEERS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer"
                                + "?Имя=" + FIRST_NAME
                                + "&Фамилия=" + LAST_NAME
                                + "&Номер=" + NUMBER_FOR_SEARCH))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(VOLUNTEERS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer"
                                + "?Имя=" + FIRST_NAME
                                + "&Фамилия=" + LAST_NAME
                                + "&Приют=" + NUMBER_FOR_SEARCH))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(VOLUNTEERS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer"
                                + "?Имя=" + FIRST_NAME
                                + "&Номер=" + NUMBER_FOR_SEARCH
                                + "&Приют=" + ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(VOLUNTEERS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer"
                                + "?Фамилия=" + LAST_NAME
                                + "&Номер=" + NUMBER_FOR_SEARCH
                                + "&Приют=" + ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(VOLUNTEERS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer"
                                + "?Имя=" + FIRST_NAME
                                + "&Фамилия=" + LAST_NAME
                                + "&Номер=" + NUMBER_FOR_SEARCH
                                + "&Приют=" + ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(VOLUNTEERS)));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForName")
    void getAllByParameters_InvalideInputException(String name) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer"
                                + "?Имя=" + name))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer"
                                + "?Фамилия=" + name))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer"
                                + "?Имя=" + FIRST_NAME
                                + "&Фамилия=" + name))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer"
                                + "?Имя=" + name
                                + "&Фамилия=" + LAST_NAME))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer"
                                + "?Имя=" + name
                                + "&Номер=" + NUMBER_FOR_SEARCH))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer"
                                + "?Имя=" + name
                                + "&Приют=" + ID))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer"
                                + "?Фамилия=" + name
                                + "&Номер=" + NUMBER_FOR_SEARCH))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer"
                                + "?Фамилия=" + name
                                + "&Приют=" + ID))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer"
                                + "?Имя=" + FIRST_NAME
                                + "&Фамилия=" + name
                                + "&Номер=" + NUMBER_FOR_SEARCH))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer"
                                + "?Имя=" + name
                                + "&Фамилия=" + LAST_NAME
                                + "&Номер=" + NUMBER_FOR_SEARCH))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer"
                                + "?Имя=" + FIRST_NAME
                                + "&Фамилия=" + name
                                + "&Приют=" + NUMBER_FOR_SEARCH))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer"
                                + "?Имя=" + name
                                + "&Фамилия=" + LAST_NAME
                                + "&Приют=" + NUMBER_FOR_SEARCH))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer"
                                + "?Имя=" + name
                                + "&Номер=" + NUMBER_FOR_SEARCH
                                + "&Приют=" + ID))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer"
                                + "?Фамилия=" + name
                                + "&Номер=" + NUMBER_FOR_SEARCH
                                + "&Приют=" + ID))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer"
                                + "?Имя=" + FIRST_NAME
                                + "&Фамилия=" + name
                                + "&Номер=" + NUMBER_FOR_SEARCH
                                + "&Приют=" + ID))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer"
                                + "?Имя=" + name
                                + "&Фамилия=" + LAST_NAME
                                + "&Номер=" + NUMBER_FOR_SEARCH
                                + "&Приют=" + ID))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForId")
    void getAllByParameters_InvalideInputException(Long id) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer"
                                + "?Приют=" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer"
                                + "?Имя=" + FIRST_NAME
                                + "&Приют=" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer"
                                + "?Фамилия=" + LAST_NAME
                                + "&Приют=" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer"
                                + "?Номер=" + NUMBER_FOR_SEARCH
                                + "&Приют=" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer"
                                + "?Имя=" + FIRST_NAME
                                + "&Фамилия=" + LAST_NAME
                                + "&Приют=" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer"
                                + "?Имя=" + FIRST_NAME
                                + "&Номер=" + NUMBER_FOR_SEARCH
                                + "&Приют=" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer"
                                + "?Фамилия=" + LAST_NAME
                                + "&Номер=" + NUMBER_FOR_SEARCH
                                + "&Приют=" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer"
                                + "?Имя=" + FIRST_NAME
                                + "&Фамилия=" + LAST_NAME
                                + "&Номер=" + NUMBER_FOR_SEARCH
                                + "&Приют=" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));
    }

    @Test
    void getAll_success() throws Exception {
        when(volunteerService.getAll())
                .thenReturn(VOLUNTEERS);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/volunteer/all"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(VOLUNTEERS)));
    }

    @Test
    void edit_success() throws Exception {
        getById_success();

        when(shelterRepository.findById(anyLong()))
                .thenReturn(Optional.of(NEW_SHELTER));

        NEW_SHELTER.setId(ID);

        Volunteer expected = new Volunteer(NEW_FIRST_NAME, NEW_LAST_NAME, NEW_VALID_PHONE_NUMBER, NEW_SHELTER);
        expected.setId(VOLUNTEER.getId());

        when(volunteerRepository.save(any(Volunteer.class)))
                .thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/volunteer/" + VOLUNTEER.getId()
                                + "?Имя=" + NEW_FIRST_NAME
                                + "&Фамилия=" + NEW_LAST_NAME
                                + "&Номер=" + NEW_PHONE_NUMBER
                                + "&Приют=" + NEW_SHELTER.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.firstName").value(expected.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(expected.getLastName()))
                .andExpect(jsonPath("$.phoneNumber").value(expected.getPhoneNumber()))
                .andExpect(jsonPath("$.shelter.id").value(expected.getShelter().getId()))
                .andExpect(jsonPath("$.shelter.type").value(expected.getShelter().getType().toString()))
                .andExpect(jsonPath("$.shelter.address").value(expected.getShelter().getAddress()));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForName")
    void edit_InvalideInputException(String name) throws Exception {
        getById_success();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/volunteer/" + VOLUNTEER.getId()
                                + "?Имя=" + name))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/volunteer/" + VOLUNTEER.getId()
                                + "?Фамилия=" + name))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForId")
    void edit_InvalideInputException(Long id) throws Exception {
        getById_success();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/volunteer/" + VOLUNTEER.getId()
                                + "?Приют=" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForPhoneNumber")
    void edit_InvalideNumberException(String number) throws Exception {
        getById_success();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/volunteer/" + VOLUNTEER.getId()
                                + "?Номер=" + number))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_NUMBER)));
    }

    @Test
    void edit_ShelterNotFoundException() throws Exception {
        getVolunteer();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/volunteer/" + VOLUNTEER.getId()
                                + "?Приют=" + ID))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(NOT_FOUND, SHELTER_NOT_FOUND)));
    }

    @Test
    void edit_VolunteerNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/volunteer/" + ID))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(NOT_FOUND, VOLUNTEER_NOT_FOUND)));
    }

    @Test
    void edit_VolunteerAlreadyAddedException() throws Exception {
        getById_success();
        getAll_success();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/volunteer/" + VOLUNTEER.getId()
                                + "?Номер=" + PHONE_NUMBER))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, VOLUNTEER_ALREADY_ADDED)));
    }

    @Test
    void delete_success() throws Exception {
        getById_success();

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/volunteer/" + VOLUNTEER.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(VOLUNTEER.getId()))
                .andExpect(jsonPath("$.firstName").value(VOLUNTEER.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(VOLUNTEER.getLastName()))
                .andExpect(jsonPath("$.phoneNumber").value(VOLUNTEER.getPhoneNumber()))
                .andExpect(jsonPath("$.shelter.id").value(SHELTER.getId()))
                .andExpect(jsonPath("$.shelter.type").value(SHELTER.getType().toString()))
                .andExpect(jsonPath("$.shelter.address").value(SHELTER.getAddress()));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForId")
    void delete_InvalideInputException(Long id) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/volunteer/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));
    }

    @Test
    void delete_VolunteerNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/volunteer/" + ID))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(NOT_FOUND, VOLUNTEER_NOT_FOUND)));
    }

    private void getShelter() {
        when(shelterRepository.findById(anyLong()))
                .thenReturn(Optional.of(SHELTER));

        SHELTER.setId(ID);
    }

    private void getVolunteer() {
        when(volunteerRepository.findById(anyLong()))
                .thenReturn(Optional.of(VOLUNTEER));

        VOLUNTEER.setId(ID);
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