package com.skypro.ShelterPetTelegramBot.controller.WebMvcTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skypro.ShelterPetTelegramBot.Utils;
import com.skypro.ShelterPetTelegramBot.controller.ShelterController;
import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Shelter;
import com.skypro.ShelterPetTelegramBot.model.repository.*;
import com.skypro.ShelterPetTelegramBot.service.impl.CheckServiceImpl;
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

import java.util.Optional;
import java.util.stream.Stream;

import static com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType.CAT;
import static com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType.DOG;
import static com.skypro.ShelterPetTelegramBot.utils.Exceptions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
class ShelterControllerTest {

    @Autowired
    private MockMvc mockMvc;
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
    @InjectMocks
    private ShelterController controller;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void add_success() throws Exception {
        when(shelterService.add(DOG, Utils.ADDRESS))
                .thenReturn(Utils.SHELTER);

        Utils.SHELTER.setId(Utils.ID);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/shelter"
                                + "?Тип=" + DOG
                                + "&Адрес=" + Utils.ADDRESS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(Utils.SHELTER.getId()))
                .andExpect(jsonPath("$.type").value(Utils.SHELTER.getType().toString()))
                .andExpect(jsonPath("$.address").value(Utils.SHELTER.getAddress()));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForAddress")
    void add_InvalideInputException(String address) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/shelter"
                                + "?Тип=" + DOG
                                + "&Адрес=" + address))
                .andExpect(status().isOk())
                .andExpect(content().string(Utils.exception(BAD_REQUEST, INVALIDE_INPUT)));
    }

    @Test
    void add_ShelterAlreadyAddedException() throws Exception {
        getAll_success();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/shelter"
                                + "?Тип=" + DOG
                                + "&Адрес=" + Utils.ADDRESS))
                .andExpect(status().isOk())
                .andExpect(content().string(Utils.exception(BAD_REQUEST, SHELTER_ALREADY_ADDED)));
    }

    @Test
    void getById_success() throws Exception {
        when(shelterRepository.findById(anyLong()))
                .thenReturn(Optional.of(Utils.SHELTER));

        Utils.SHELTER.setId(Utils.ID);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/shelter/" + Utils.SHELTER.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(Utils.SHELTER.getId()))
                .andExpect(jsonPath("$.type").value(Utils.SHELTER.getType().toString()))
                .andExpect(jsonPath("$.address").value(Utils.SHELTER.getAddress()));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForId")
    void getById_InvalideInputException(Long id) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/shelter/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(Utils.exception(BAD_REQUEST, INVALIDE_INPUT)));
    }

    @Test
    void getById_ShelterNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/shelter/" + Utils.ID))
                .andExpect(status().isOk())
                .andExpect(content().string(Utils.exception(NOT_FOUND, SHELTER_NOT_FOUND)));
    }

    @Test
    void getAllByParameters_success() throws Exception {
        when(shelterRepository.getAllByType(any(PetType.class)))
                .thenReturn(Utils.SHELTERS);

        when(shelterRepository.getAllByAddressContainsIgnoreCase(anyString()))
                .thenReturn(Utils.SHELTERS);

        when(shelterRepository.getAllByTypeAndAddressContainsIgnoreCase(any(PetType.class), anyString()))
                .thenReturn(Utils.SHELTERS);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/shelter"
                                + "?Тип=" + DOG))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Utils.SHELTERS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/shelter"
                                + "?Адрес=" + Utils.ADDRESS))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Utils.SHELTERS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/shelter"
                                + "?Тип=" + DOG
                                + "&Адрес=" + Utils.ADDRESS))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Utils.SHELTERS)));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForAddress")
    void getAllByParameters_InvalideInputException(String address) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/shelter"
                                + "?Адрес=" + address))
                .andExpect(status().isOk())
                .andExpect(content().string(Utils.exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/shelter"
                                + "?Тип=" + DOG
                                + "&Адрес=" + address))
                .andExpect(status().isOk())
                .andExpect(content().string(Utils.exception(BAD_REQUEST, INVALIDE_INPUT)));
    }

    @Test
    void getAll_success() throws Exception {
        when(shelterService.getAll())
                .thenReturn(Utils.SHELTERS);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/shelter/all"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Utils.SHELTERS)));
    }

    @Test
    void edit_success() throws Exception {
        getById_success();

        Shelter expected = new Shelter(CAT, Utils.NEW_ADDRESS);
        expected.setId(Utils.SHELTER.getId());

        when(shelterRepository.save(any(Shelter.class)))
                .thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/shelter/" + Utils.SHELTER.getId()
                                + "?Тип=" + CAT
                                + "&Адрес=" + Utils.NEW_ADDRESS))
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
                        .put("/shelter/" + Utils.SHELTER.getId()
                                + "?Адрес=" + address))
                .andExpect(status().isOk())
                .andExpect(content().string(Utils.exception(BAD_REQUEST, INVALIDE_INPUT)));
    }

    @Test
    void edit_ShelterNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/shelter/" + Utils.ID))
                .andExpect(status().isOk())
                .andExpect(content().string(Utils.exception(NOT_FOUND, SHELTER_NOT_FOUND)));
    }

    @Test
    void edit_ShelterAlreadyAddedException() throws Exception {
        getById_success();
        getAll_success();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/shelter/" + Utils.SHELTER.getId()
                                + "?Адрес=" + Utils.SHELTER.getAddress()))
                .andExpect(status().isOk())
                .andExpect(content().string(Utils.exception(BAD_REQUEST, SHELTER_ALREADY_ADDED)));
    }

    @Test
    void delete_success() throws Exception {
        getById_success();

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/shelter/" + Utils.SHELTER.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(Utils.SHELTER.getId()))
                .andExpect(jsonPath("$.type").value(Utils.SHELTER.getType().toString()))
                .andExpect(jsonPath("$.address").value(Utils.SHELTER.getAddress()));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForId")
    void delete_InvalideInputException(Long id) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/shelter/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(Utils.exception(BAD_REQUEST, INVALIDE_INPUT)));
    }

    @Test
    void delete_ShelterNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/shelter/" + Utils.ID))
                .andExpect(status().isOk())
                .andExpect(content().string(Utils.exception(NOT_FOUND, SHELTER_NOT_FOUND)));
    }

    private static Stream<Arguments> provideParamsForAddress() {
        return Stream.of(
                null,
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