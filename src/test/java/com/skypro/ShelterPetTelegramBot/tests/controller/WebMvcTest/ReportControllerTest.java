package com.skypro.ShelterPetTelegramBot.tests.controller.WebMvcTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skypro.ShelterPetTelegramBot.controller.ReportController;
import com.skypro.ShelterPetTelegramBot.model.enums.ReportStatus;
import com.skypro.ShelterPetTelegramBot.model.repository.*;
import com.skypro.ShelterPetTelegramBot.service.impl.CheckServiceImpl;
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

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

import static com.skypro.ShelterPetTelegramBot.model.enums.ReportStatus.NOT_VIEWED;
import static com.skypro.ShelterPetTelegramBot.tests.Utils.*;
import static com.skypro.ShelterPetTelegramBot.utils.Exceptions.INVALIDE_INPUT;
import static com.skypro.ShelterPetTelegramBot.utils.Exceptions.REPORT_NOT_FOUND;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
public class ReportControllerTest {

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
    private ReportController controller;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getById_success() throws Exception {
        getReport();

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

    private void getReport() {
        when(reportRepository.findById(anyLong()))
                .thenReturn(Optional.of(REPORT));

        REPORT.setId(ID);
    }

    private static Stream<Arguments> provideParamsForId() {
        return Stream.of(
                Arguments.of(Utils.ZERO),
                Arguments.of(Utils.INCORRECT_ID)
        );
    }
}