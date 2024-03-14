package com.skypro.ShelterPetTelegramBot.tests.controller.WebMvcTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skypro.ShelterPetTelegramBot.configuration.BotConfiguration;
import com.skypro.ShelterPetTelegramBot.controller.ParentController;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Parent;
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
import static com.skypro.ShelterPetTelegramBot.tests.Utils.*;
import static com.skypro.ShelterPetTelegramBot.utils.Exceptions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@EnableTransactionManagement(proxyTargetClass = true)
class ParentControllerTest {

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
    private ParentController controller;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void add_success() throws Exception {
        getPet();

        PET.setStatus(FREE);

        when(parentService.add(FIRST_NAME, LAST_NAME, PHONE_NUMBER, PET.getName()))
                .thenReturn(PARENT);

        PARENT.setId(ID);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/parent"
                                + "?Имя=" + FIRST_NAME
                                + "&Фамилия=" + LAST_NAME
                                + "&Номер=" + PHONE_NUMBER
                                + "&Животное=" + PET.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(PARENT.getId()))
                .andExpect(jsonPath("$.firstName").value(PARENT.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(PARENT.getLastName()))
                .andExpect(jsonPath("$.phoneNumber").value(PARENT.getPhoneNumber()))
                .andExpect(jsonPath("$.pet.id").value(PET.getId()))
                .andExpect(jsonPath("$.pet.type").value(PET.getType().toString()))
                .andExpect(jsonPath("$.pet.status").value(PET.getStatus().toString()))
                .andExpect(jsonPath("$.pet.name").value(PET.getName()))
                .andExpect(jsonPath("$.pet.shelter.id").value(SHELTER.getId()))
                .andExpect(jsonPath("$.pet.shelter.type").value(SHELTER.getType().toString()))
                .andExpect(jsonPath("$.pet.shelter.address").value(SHELTER.getAddress()));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForName")
    void add_InvalideInputException(String name) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/parent"
                                + "?Имя=" + name
                                + "&Фамилия=" + LAST_NAME
                                + "&Номер=" + PHONE_NUMBER
                                + "&Животное=" + PET.getName()))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/parent"
                                + "?Имя=" + FIRST_NAME
                                + "&Фамилия=" + name
                                + "&Номер=" + PHONE_NUMBER
                                + "&Животное=" + PET.getName()))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/parent"
                                + "?Имя=" + FIRST_NAME
                                + "&Фамилия=" + LAST_NAME
                                + "&Номер=" + PHONE_NUMBER
                                + "&Животное=" + name))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForPhoneNumber")
    void add_InvalideNumberException(String number) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/parent"
                                + "?Имя=" + FIRST_NAME
                                + "&Фамилия=" + LAST_NAME
                                + "&Номер=" + number
                                + "&Животное=" + PET.getName()))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_NUMBER)));
    }

    @Test
    void add_PetStatusException() throws Exception {
        getPet();

        PET.setStatus(ADOPTED);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/parent"
                                + "?Имя=" + FIRST_NAME
                                + "&Фамилия=" + LAST_NAME
                                + "&Номер=" + PHONE_NUMBER
                                + "&Животное=" + PET.getName()))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, PET_STATUS)));
    }

    @Test
    void add_PetNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/parent"
                                + "?Имя=" + FIRST_NAME
                                + "&Фамилия=" + LAST_NAME
                                + "&Номер=" + PHONE_NUMBER
                                + "&Животное=" + NAME))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(NOT_FOUND, PET_NOT_FOUND)));
    }

    @Test
    void add_ParentAlreadyAddedException() throws Exception {
        getPet();
        getAll_success();

        PET.setStatus(FREE);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/parent"
                                + "?Имя=" + FIRST_NAME
                                + "&Фамилия=" + LAST_NAME
                                + "&Номер=" + PHONE_NUMBER
                                + "&Животное=" + PET.getName()))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, PARENT_ALREADY_ADDED)));
    }

    @Test
    void getById_success() throws Exception {
        getPet();
        getParent();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/parent/" + PARENT.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(PARENT.getId()))
                .andExpect(jsonPath("$.firstName").value(PARENT.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(PARENT.getLastName()))
                .andExpect(jsonPath("$.phoneNumber").value(PARENT.getPhoneNumber()))
                .andExpect(jsonPath("$.pet.id").value(PET.getId()))
                .andExpect(jsonPath("$.pet.type").value(PET.getType().toString()))
                .andExpect(jsonPath("$.pet.status").value(PET.getStatus().toString()))
                .andExpect(jsonPath("$.pet.name").value(PET.getName()))
                .andExpect(jsonPath("$.pet.shelter.id").value(SHELTER.getId()))
                .andExpect(jsonPath("$.pet.shelter.type").value(SHELTER.getType().toString()))
                .andExpect(jsonPath("$.pet.shelter.address").value(SHELTER.getAddress()));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForId")
    void getById_InvalideInputException(Long id) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/parent/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));
    }

    @Test
    void getById_ParentNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/parent/" + ID))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(NOT_FOUND, PARENT_NOT_FOUND)));
    }

    @Test
    void getAllByParameters_success() throws Exception {
        when(parentRepository.getAllByFirstNameContainsIgnoreCase(anyString()))
                .thenReturn(PARENTS);

        when(parentRepository.getAllByLastNameContainsIgnoreCase(anyString()))
                .thenReturn(PARENTS);

        when(parentRepository.getAllByPhoneNumberContains(anyString()))
                .thenReturn(PARENTS);

        when(parentRepository.getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCase(anyString(), anyString()))
                .thenReturn(PARENTS);

        when(parentRepository.getAllByFirstNameContainsIgnoreCaseAndPhoneNumberContains(anyString(), anyString()))
                .thenReturn(PARENTS);

        when(parentRepository.getAllByLastNameContainsIgnoreCaseAndPhoneNumberContains(anyString(), anyString()))
                .thenReturn(PARENTS);

        when(parentRepository.getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCaseAndPhoneNumberContains(anyString(), anyString(), anyString()))
                .thenReturn(PARENTS);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/parent"
                                + "?Имя=" + FIRST_NAME))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(PARENTS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/parent"
                                + "?Фамилия=" + LAST_NAME))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(PARENTS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/parent"
                                + "?Номер=" + NUMBER_FOR_SEARCH))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(PARENTS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/parent"
                                + "?Имя=" + FIRST_NAME
                                + "&Фамилия=" + LAST_NAME))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(PARENTS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/parent"
                                + "?Имя=" + FIRST_NAME
                                + "&Номер=" + NUMBER_FOR_SEARCH))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(PARENTS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/parent"
                                + "?Фамилия=" + LAST_NAME
                                + "&Номер=" + NUMBER_FOR_SEARCH))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(PARENTS)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/parent"
                                + "?Имя=" + FIRST_NAME
                                + "&Фамилия=" + LAST_NAME
                                + "&Номер=" + NUMBER_FOR_SEARCH))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(PARENTS)));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForName")
    void getAllByParameters_InvalideInputException(String name) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/parent"
                                + "?Имя=" + name))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/parent"
                                + "?Фамилия=" + name))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/parent"
                                + "?Имя=" + name
                                + "&Фамилия=" + LAST_NAME))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/parent"
                                + "?Имя=" + FIRST_NAME
                                + "&Фамилия=" + name))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/parent"
                                + "?Имя=" + name
                                + "&Номер=" + NUMBER_FOR_SEARCH))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/parent"
                                + "?Фамилия=" + name
                                + "&Номер=" + NUMBER_FOR_SEARCH))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/parent"
                                + "?Имя=" + name
                                + "&Фамилия=" + LAST_NAME
                                + "&Номер=" + NUMBER_FOR_SEARCH))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/parent"
                                + "?Имя=" + FIRST_NAME
                                + "&Фамилия=" + name
                                + "&Номер=" + NUMBER_FOR_SEARCH))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));
    }

    @Test
    void getAll_success() throws Exception {
        when(parentService.getAll())
                .thenReturn(PARENTS);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/parent/all"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(PARENTS)));
    }

    @Test
    void edit_success() throws Exception {
        getById_success();

        when(petRepository.getByNameContainsIgnoreCase(anyString()))
                .thenReturn(Optional.of(NEW_PET));

        NEW_PET.setStatus(FREE);
        NEW_PET.setId(ID);

        Parent expected = new Parent(NEW_FIRST_NAME, NEW_LAST_NAME, NEW_VALID_PHONE_NUMBER, NEW_PET);
        expected.setId(PARENT.getId());

        when(parentRepository.save(any(Parent.class)))
                .thenReturn(expected);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/parent/" + PARENT.getId()
                                + "?Имя=" + NEW_FIRST_NAME
                                + "&Фамилия=" + NEW_LAST_NAME
                                + "&Номер=" + NEW_PHONE_NUMBER
                                + "&Животное=" + NEW_PET.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(expected.getId()))
                .andExpect(jsonPath("$.firstName").value(expected.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(expected.getLastName()))
                .andExpect(jsonPath("$.phoneNumber").value(expected.getPhoneNumber()))
                .andExpect(jsonPath("$.pet.id").value(expected.getPet().getId()))
                .andExpect(jsonPath("$.pet.type").value(expected.getPet().getType().toString()))
                .andExpect(jsonPath("$.pet.status").value(expected.getPet().getStatus().toString()))
                .andExpect(jsonPath("$.pet.name").value(expected.getPet().getName()))
                .andExpect(jsonPath("$.pet.shelter.id").value(expected.getPet().getShelter().getId()))
                .andExpect(jsonPath("$.pet.shelter.type").value(expected.getPet().getShelter().getType().toString()))
                .andExpect(jsonPath("$.pet.shelter.address").value(expected.getPet().getShelter().getAddress()));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForName")
    void edit_InvalideInputException(String name) throws Exception {
        getById_success();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/parent/" + PARENT.getId()
                                + "?Имя=" + name))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/parent/" + PARENT.getId()
                                + "?Фамилия=" + name))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/parent/" + PARENT.getId()
                                + "?Животное=" + name))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForPhoneNumber")
    void edit_InvalideNumberException(String number) throws Exception {
        getById_success();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/parent/" + PARENT.getId()
                                + "?Номер=" + number))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_NUMBER)));
    }

    @Test
    void edit_PetNotFoundException() throws Exception {
        getParent();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/parent/" + PARENT.getId()
                                + "?Животное=" + NAME))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(NOT_FOUND, PET_NOT_FOUND)));
    }

    @Test
    void edit_ParentNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/parent/" + ID))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(NOT_FOUND, PARENT_NOT_FOUND)));
    }

    @Test
    void edit_PetStatusException() throws Exception {
        getParent();
        getPet();

        PET.setStatus(ADOPTED);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/parent/" + PARENT.getId()
                                + "?Животное=" + PET.getName()))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, PET_STATUS)));
    }

    @Test
    void edit_ParentAlreadyAddedException() throws Exception {
        getById_success();
        getAll_success();

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/parent/" + PARENT.getId()
                                + "?Номер=" + PHONE_NUMBER))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, PARENT_ALREADY_ADDED)));
    }

    @Test
    void delete_success() throws Exception {
        getById_success();

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/parent/" + PARENT.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(PARENT.getId()))
                .andExpect(jsonPath("$.firstName").value(PARENT.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(PARENT.getLastName()))
                .andExpect(jsonPath("$.phoneNumber").value(PARENT.getPhoneNumber()))
                .andExpect(jsonPath("$.pet.id").value(PET.getId()))
                .andExpect(jsonPath("$.pet.type").value(PET.getType().toString()))
                .andExpect(jsonPath("$.pet.status").value(PET.getStatus().toString()))
                .andExpect(jsonPath("$.pet.name").value(PET.getName()))
                .andExpect(jsonPath("$.pet.shelter.id").value(SHELTER.getId()))
                .andExpect(jsonPath("$.pet.shelter.type").value(SHELTER.getType().toString()))
                .andExpect(jsonPath("$.pet.shelter.address").value(SHELTER.getAddress()));
    }

    @ParameterizedTest
    @MethodSource("provideParamsForId")
    void delete_InvalideInputException(Long id) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/parent/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(BAD_REQUEST, INVALIDE_INPUT)));
    }

    @Test
    void delete_VolunteerNotFoundException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/parent/" + ID))
                .andExpect(status().isOk())
                .andExpect(content().string(exception(NOT_FOUND, PARENT_NOT_FOUND)));
    }

    private void getPet() {
        when(petRepository.getByNameContainsIgnoreCase(anyString()))
                .thenReturn(Optional.of(PET));

        PET.setId(ID);
    }

    private void getParent() {
        when(parentRepository.findById(anyLong()))
                .thenReturn(Optional.of(PARENT));

        PARENT.setId(ID);
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