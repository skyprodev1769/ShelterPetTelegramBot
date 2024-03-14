package com.skypro.ShelterPetTelegramBot.tests.controller.WebMvcTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skypro.ShelterPetTelegramBot.configuration.BotConfiguration;
import com.skypro.ShelterPetTelegramBot.controller.ShelterController;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Shelter;
import com.skypro.ShelterPetTelegramBot.model.enums.PetType;
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

import static com.skypro.ShelterPetTelegramBot.model.enums.PetType.CAT;
import static com.skypro.ShelterPetTelegramBot.model.enums.PetType.DOG;
import static com.skypro.ShelterPetTelegramBot.tests.Utils.*;
import static com.skypro.ShelterPetTelegramBot.utils.Exceptions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@EnableTransactionManagement(proxyTargetClass = true)
class ShelterControllerTest {

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
    private ShelterController controller;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void add_success() throws Exception {
        when(shelterService.add(DOG, ADDRESS))
                .thenReturn(SHELTER);

        SHELTER.setId(ID);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/shelter"
                                + "?Тип=" + DOG
                                + "&Адрес=" + ADDRESS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(SHELTER.getId()))
                .andExpect(jsonPath("$.type").value(SHELTER.getType().toString()))
                .andExpect(jsonPath("$.address").value(SHELTER.getAddress()));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForAddress")
    void add_InvalideInputException(String address) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/shelter"
                                + "?Тип=" + DOG
                                + "&Адрес=" + address))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));
    }

    @Test
    void add_ShelterAlreadyAddedException() throws Exception {
        getAll_success();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/shelter"
                                + "?Тип=" + DOG
                                + "&Адрес=" + ADDRESS))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, SHELTER_ALREADY_ADDED)));
    }

    @Test
    void getById_success() throws Exception {
        when(shelterRepository.findById(anyLong()))
                .thenReturn(Optional.of(SHELTER));

        SHELTER.setId(ID);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/shelter/" + SHELTER.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(SHELTER.getId()))
                .andExpect(jsonPath("$.type").value(SHELTER.getType().toString()))
                .andExpect(jsonPath("$.address").value(SHELTER.getAddress()));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForId")
    void getById_InvalideInputException(Long id) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/shelter/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));
    }

    @Test
    void getById_ShelterNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/shelter/" + ID))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(NOT_FOUND, SHELTER_NOT_FOUND)));
    }

    @Test
    void getAllByParameters_success() throws Exception {
        when(shelterRepository.getAllByType(any(PetType.class)))
                .thenReturn(SHELTERS);

        when(shelterRepository.getAllByAddressContainsIgnoreCase(anyString()))
                .thenReturn(SHELTERS);

        when(shelterRepository.getAllByTypeAndAddressContainsIgnoreCase(any(PetType.class), anyString()))
                .thenReturn(SHELTERS);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/shelter"
                                + "?Тип=" + DOG))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(SHELTERS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/shelter"
                                + "?Адрес=" + ADDRESS))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(SHELTERS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/shelter"
                                + "?Тип=" + DOG
                                + "&Адрес=" + ADDRESS))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(SHELTERS)));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForAddress")
    void getAllByParameters_InvalideInputException(String address) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/shelter"
                                + "?Адрес=" + address))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/shelter"
                                + "?Тип=" + DOG
                                + "&Адрес=" + address))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));
    }

    @Test
    void getAll_success() throws Exception {
        when(shelterService.getAll())
                .thenReturn(SHELTERS);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/shelter/all"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(SHELTERS)));
    }

    @Test
    void edit_success() throws Exception {
        getById_success();

        Shelter expected = new Shelter(CAT, NEW_ADDRESS);
        expected.setId(SHELTER.getId());

        when(shelterRepository.save(any(Shelter.class)))
                .thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/shelter/" + SHELTER.getId()
                                + "?Тип=" + CAT
                                + "&Адрес=" + NEW_ADDRESS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.type").value(expected.getType().toString()))
                .andExpect(jsonPath("$.address").value(expected.getAddress()));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForAddress")
    void edit_InvalideInputException(String address) throws Exception {
        getById_success();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/shelter/" + SHELTER.getId()
                                + "?Адрес=" + address))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));
    }

    @Test
    void edit_ShelterNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/shelter/" + ID))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(NOT_FOUND, SHELTER_NOT_FOUND)));
    }

    @Test
    void edit_ShelterAlreadyAddedException() throws Exception {
        getById_success();
        getAll_success();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/shelter/" + SHELTER.getId()
                                + "?Адрес=" + SHELTER.getAddress()))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, SHELTER_ALREADY_ADDED)));
    }

    @Test
    void delete_success() throws Exception {
        getById_success();

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/shelter/" + SHELTER.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(SHELTER.getId()))
                .andExpect(jsonPath("$.type").value(SHELTER.getType().toString()))
                .andExpect(jsonPath("$.address").value(SHELTER.getAddress()));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForId")
    void delete_InvalideInputException(Long id) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/shelter/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));
    }

    @Test
    void delete_ShelterNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/shelter/" + ID))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(NOT_FOUND, SHELTER_NOT_FOUND)));
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