package com.skypro.ShelterPetTelegramBot.tests.service;

import com.skypro.ShelterPetTelegramBot.exception.ReportNotFoundException;
import com.skypro.ShelterPetTelegramBot.model.enums.ReportStatus;
import com.skypro.ShelterPetTelegramBot.model.repository.ReportRepository;
import com.skypro.ShelterPetTelegramBot.service.impl.entity_service.ReportServiceImpl;
import com.skypro.ShelterPetTelegramBot.service.interfaces.CheckService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static com.skypro.ShelterPetTelegramBot.model.enums.ReportStatus.NOT_VIEWED;
import static com.skypro.ShelterPetTelegramBot.tests.Utils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    @Mock
    private ReportRepository repository;
    @Mock
    private CheckService checkService;
    @InjectMocks
    private ReportServiceImpl reportService;

    @BeforeEach
    void setUp() {
        reportService = new ReportServiceImpl(repository, checkService);
    }

    @Test
    void getById() {
        getReport();

        assertEquals(REPORT,
                reportService.getById(ID));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void getById_ReportNotFoundException() {
        when(repository.findById(anyLong()))
                .thenThrow(ReportNotFoundException.class);

        assertThrows(ReportNotFoundException.class,
                () -> reportService.getById(ID));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void getAllByParameters() {
        when(repository.getAllByDate(any(LocalDate.class)))
                .thenReturn(REPORTS);

        when(repository.getAllByStatus(any(ReportStatus.class)))
                .thenReturn(REPORTS);

        when(repository.getAllByDateAndStatus(any(LocalDate.class), any(ReportStatus.class)))
                .thenReturn(REPORTS);

        assertEquals(REPORTS,
                reportService.getAllByParameters(LocalDate.now(), null));

        assertEquals(REPORTS,
                reportService.getAllByParameters(null, NOT_VIEWED));

        assertEquals(REPORTS,
                reportService.getAllByParameters(LocalDate.now(), NOT_VIEWED));

        verify(repository, times(1)).getAllByDate(any(LocalDate.class));
        verify(repository, times(1)).getAllByStatus(any(ReportStatus.class));
        verify(repository, times(1)).getAllByDateAndStatus(any(LocalDate.class), any(ReportStatus.class));
    }

    @Test
    void getAll() {
        when(repository.findAll())
                .thenReturn(REPORTS);

        assertEquals(REPORTS,
                reportService.getAll());

        verify(repository, times(1)).findAll();
    }

    private void getReport() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(REPORT));
    }
}