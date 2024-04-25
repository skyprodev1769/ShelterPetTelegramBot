package com.skypro.ShelterPetTelegramBot.tests.controller.WebMvcTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skypro.ShelterPetTelegramBot.configuration.BotConfiguration;
import com.skypro.ShelterPetTelegramBot.controller.PetController;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Pet;
import com.skypro.ShelterPetTelegramBot.model.enums.PetStatus;
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

import static com.skypro.ShelterPetTelegramBot.model.enums.PetStatus.ADOPTED;
import static com.skypro.ShelterPetTelegramBot.model.enums.PetStatus.FREE;
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
class PetControllerTest {

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
    private PetController controller;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void add_success() throws Exception {
        getShelter();

        when(petService.add(DOG, FREE, NAME, SHELTER.getId()))
                .thenReturn(PET);

        PET.setId(ID);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/pet"
                                + "?Тип=" + DOG
                                + "&Статус=" + FREE
                                + "&Имя=" + NAME
                                + "&Приют=" + SHELTER.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(PET.getId()))
                .andExpect(jsonPath("$.type").value(PET.getType().toString()))
                .andExpect(jsonPath("$.status").value(PET.getStatus().toString()))
                .andExpect(jsonPath("$.name").value(PET.getName()))
                .andExpect(jsonPath("$.shelter.id").value(SHELTER.getId()))
                .andExpect(jsonPath("$.shelter.type").value(SHELTER.getType().toString()))
                .andExpect(jsonPath("$.shelter.address").value(SHELTER.getAddress()));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForName")
    void add_InvalideInputException(String name) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/pet"
                                + "?Тип=" + DOG
                                + "&Статус=" + FREE
                                + "&Имя=" + name
                                + "&Приют=" + SHELTER.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForId")
    void add_InvalideInputException(Long id) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/pet"
                                + "?Тип=" + DOG
                                + "&Статус=" + FREE
                                + "&Имя=" + NAME
                                + "&Приют=" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));
    }

    @Test
    void add_ShelterNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/pet"
                                + "?Тип=" + DOG
                                + "&Статус=" + FREE
                                + "&Имя=" + NAME
                                + "&Приют=" + ID))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(NOT_FOUND, SHELTER_NOT_FOUND)));
    }

    @Test
    void add_DifferentTypesException() throws Exception {
        getShelter();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/pet"
                                + "?Тип=" + CAT
                                + "&Статус=" + FREE
                                + "&Имя=" + NAME
                                + "&Приют=" + SHELTER.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, DIFFERENT_TYPES)));
    }

    @Test
    void add_PetAlreadyAddedException() throws Exception {
        getShelter();
        getAll_success();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/pet"
                                + "?Тип=" + DOG
                                + "&Статус=" + FREE
                                + "&Имя=" + NAME
                                + "&Приют=" + SHELTER.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, PET_ALREADY_ADDED)));
    }

    @Test
    void getById_success() throws Exception {
        getShelter();
        getPet();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pet/" + PET.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(PET.getId()))
                .andExpect(jsonPath("$.type").value(PET.getType().toString()))
                .andExpect(jsonPath("$.status").value(PET.getStatus().toString()))
                .andExpect(jsonPath("$.name").value(PET.getName()))
                .andExpect(jsonPath("$.shelter.id").value(SHELTER.getId()))
                .andExpect(jsonPath("$.shelter.type").value(SHELTER.getType().toString()))
                .andExpect(jsonPath("$.shelter.address").value(SHELTER.getAddress()));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForId")
    void getById_InvalideInputException(Long id) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pet/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));
    }

    @Test
    void getById_PetNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pet/" + ID))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(NOT_FOUND, PET_NOT_FOUND)));
    }

    @Test
    void getAllByParameters_success() throws Exception {
        when(petRepository.getAllByType(any(PetType.class)))
                .thenReturn(PETS);

        when(petRepository.getAllByStatus(any(PetStatus.class)))
                .thenReturn(PETS);

        when(petRepository.getAllByNameContainsIgnoreCase(anyString()))
                .thenReturn(PETS);

        when(petRepository.getAllByShelterId(anyLong()))
                .thenReturn(PETS);

        when(petRepository.getAllByTypeAndStatus(any(PetType.class), any(PetStatus.class)))
                .thenReturn(PETS);

        when(petRepository.getAllByTypeAndNameContainsIgnoreCase(any(PetType.class), anyString()))
                .thenReturn(PETS);

        when(petRepository.getAllByTypeAndShelterId(any(PetType.class), anyLong()))
                .thenReturn(PETS);

        when(petRepository.getAllByStatusAndNameContainsIgnoreCase(any(PetStatus.class), anyString()))
                .thenReturn(PETS);

        when(petRepository.getAllByStatusAndShelterId(any(PetStatus.class), anyLong()))
                .thenReturn(PETS);

        when(petRepository.getAllByNameContainsIgnoreCaseAndShelterId(anyString(), anyLong()))
                .thenReturn(PETS);

        when(petRepository.getAllByTypeAndStatusAndNameContainsIgnoreCase(any(PetType.class), any(PetStatus.class), anyString()))
                .thenReturn(PETS);

        when(petRepository.getAllByTypeAndStatusAndShelterId(any(PetType.class), any(PetStatus.class), anyLong()))
                .thenReturn(PETS);

        when(petRepository.getAllByTypeAndNameContainsIgnoreCaseAndShelterId(any(PetType.class), anyString(), anyLong()))
                .thenReturn(PETS);

        when(petRepository.getAllByStatusAndNameContainsIgnoreCaseAndShelterId(any(PetStatus.class), anyString(), anyLong()))
                .thenReturn(PETS);

        when(petRepository.getAllByTypeAndStatusAndNameContainsIgnoreCaseAndShelterId(any(PetType.class), any(PetStatus.class), anyString(), anyLong()))
                .thenReturn(PETS);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pet"
                                + "?Тип=" + DOG))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(PETS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pet"
                                + "?Статус=" + FREE))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(PETS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pet"
                                + "?Имя=" + NAME))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(PETS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pet"
                                + "?Приют=" + ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(PETS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pet"
                                + "?Тип=" + DOG
                                + "&Статус=" + FREE))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(PETS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pet"
                                + "?Тип=" + DOG
                                + "&Имя=" + NAME))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(PETS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pet"
                                + "?Тип=" + DOG
                                + "&Приют=" + ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(PETS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pet"
                                + "?Статус=" + FREE
                                + "&Имя=" + NAME))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(PETS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pet"
                                + "?Статус=" + FREE
                                + "&Приют=" + ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(PETS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pet"
                                + "?Имя=" + NAME
                                + "&Приют=" + ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(PETS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pet"
                                + "?Тип=" + DOG
                                + "&Статус=" + FREE
                                + "&Имя=" + NAME))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(PETS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pet"
                                + "?Тип=" + DOG
                                + "&Статус=" + FREE
                                + "&Приют=" + ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(PETS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pet"
                                + "?Тип=" + DOG
                                + "&Имя=" + NAME
                                + "&Приют=" + ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(PETS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pet"
                                + "?Статус=" + FREE
                                + "&Имя=" + NAME
                                + "&Приют=" + ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(PETS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pet"
                                + "?Тип=" + DOG
                                + "&Статус=" + FREE
                                + "&Имя=" + NAME
                                + "&Приют=" + ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(PETS)));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForName")
    void getAllByParameters_InvalideInputException(String name) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pet"
                                + "?Имя=" + name))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pet"
                                + "?Тип=" + DOG
                                + "&Имя=" + name))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pet"
                                + "?Статус=" + FREE
                                + "&Имя=" + name))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pet"
                                + "?Имя=" + name
                                + "&Приют=" + ID))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pet"
                                + "?Тип=" + DOG
                                + "&Статус=" + FREE
                                + "&Имя=" + name))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pet"
                                + "?Тип=" + DOG
                                + "&Имя=" + name
                                + "&Приют=" + ID))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pet"
                                + "?Статус=" + FREE
                                + "&Имя=" + name
                                + "&Приют=" + ID))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pet"
                                + "?Тип=" + DOG
                                + "&Статус=" + FREE
                                + "&Имя=" + name
                                + "&Приют=" + ID))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForId")
    void getAllByParameters_InvalideInputException(Long id) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pet"
                                + "?Приют=" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pet"
                                + "?Тип=" + DOG
                                + "&Приют=" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pet"
                                + "?Статус=" + FREE
                                + "&Приют=" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pet"
                                + "?Имя=" + NAME
                                + "&Приют=" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pet"
                                + "?Тип=" + DOG
                                + "&Статус=" + FREE
                                + "&Приют=" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pet"
                                + "?Тип=" + DOG
                                + "&Имя=" + NAME
                                + "&Приют=" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pet"
                                + "?Статус=" + FREE
                                + "&Имя=" + NAME
                                + "&Приют=" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pet"
                                + "?Тип=" + DOG
                                + "&Статус=" + FREE
                                + "&Имя=" + NAME
                                + "&Приют=" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));
    }

    @Test
    void getAll_success() throws Exception {
        when(petService.getAll())
                .thenReturn(PETS);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/pet/all"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(PETS)));
    }

    @Test
    void edit_success() throws Exception {
        getById_success();

        when(shelterRepository.findById(anyLong()))
                .thenReturn(Optional.of(NEW_SHELTER));

        NEW_SHELTER.setId(ID);

        Pet expected = new Pet(CAT, ADOPTED, NEW_NAME, NEW_SHELTER);
        expected.setId(PET.getId());

        when(petRepository.save(any(Pet.class)))
                .thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/pet/" + PET.getId()
                                + "?Тип=" + CAT
                                + "&Статус=" + ADOPTED
                                + "&Имя=" + NEW_NAME
                                + "&Приют=" + NEW_SHELTER.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.type").value(expected.getType().toString()))
                .andExpect(jsonPath("$.status").value(expected.getStatus().toString()))
                .andExpect(jsonPath("$.name").value(expected.getName()))
                .andExpect(jsonPath("$.shelter.id").value(expected.getShelter().getId()))
                .andExpect(jsonPath("$.shelter.type").value(expected.getShelter().getType().toString()))
                .andExpect(jsonPath("$.shelter.address").value(expected.getShelter().getAddress()));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForName")
    void edit_InvalideInputException(String name) throws Exception {
        getById_success();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/pet/" + PET.getId()
                                + "?Имя=" + name))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForId")
    void edit_InvalideInputException(Long id) throws Exception {
        getById_success();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/pet/" + PET.getId()
                                + "?Приют=" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));
    }

    @Test
    void edit_ShelterNotFoundException() throws Exception {
        getPet();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/pet/" + PET.getId()
                                + "?Приют=" + ID))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(NOT_FOUND, SHELTER_NOT_FOUND)));
    }

    @Test
    void edit_PetNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/pet/" + ID))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(NOT_FOUND, PET_NOT_FOUND)));
    }

    @Test
    void edit_DifferentTypesException() throws Exception {
        getById_success();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/pet/" + PET.getId()
                                + "?Тип=" + CAT
                                + "&id" + SHELTER.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, DIFFERENT_TYPES)));
    }

    @Test
    void edit_PetAlreadyAddedException() throws Exception {
        getById_success();
        getAll_success();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/pet/" + PET.getId()
                                + "?Имя=" + NAME))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, PET_ALREADY_ADDED)));
    }

    @Test
    void delete_success() throws Exception {
        getById_success();

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/pet/" + PET.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(PET.getId()))
                .andExpect(jsonPath("$.type").value(PET.getType().toString()))
                .andExpect(jsonPath("$.status").value(PET.getStatus().toString()))
                .andExpect(jsonPath("$.name").value(PET.getName()))
                .andExpect(jsonPath("$.shelter.id").value(SHELTER.getId()))
                .andExpect(jsonPath("$.shelter.type").value(SHELTER.getType().toString()))
                .andExpect(jsonPath("$.shelter.address").value(SHELTER.getAddress()));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForId")
    void delete_InvalideInputException(Long id) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/pet/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));
    }

    @Test
    void delete_PetNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/pet/" + ID))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(NOT_FOUND, PET_NOT_FOUND)));
    }

    private void getShelter() {
        when(shelterRepository.findById(anyLong()))
                .thenReturn(Optional.of(SHELTER));

        SHELTER.setId(ID);
    }

    private void getPet() {
        when(petRepository.findById(anyLong()))
                .thenReturn(Optional.of(PET));

        PET.setId(ID);
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