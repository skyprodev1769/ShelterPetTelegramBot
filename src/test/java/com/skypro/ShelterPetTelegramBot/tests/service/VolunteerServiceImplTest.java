package com.skypro.ShelterPetTelegramBot.tests.service;

import com.skypro.ShelterPetTelegramBot.exception.volunteer.VolunteerNotFoundException;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Volunteer;
import com.skypro.ShelterPetTelegramBot.model.repository.VolunteerRepository;
import com.skypro.ShelterPetTelegramBot.service.impl.entity_service.VolunteerServiceImpl;
import com.skypro.ShelterPetTelegramBot.service.interfaces.CheckService;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.ShelterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.skypro.ShelterPetTelegramBot.tests.Utils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VolunteerServiceImplTest {

    @Mock
    private VolunteerRepository repository;
    @Mock
    private CheckService checkService;
    @Mock
    private ShelterService shelterService;
    @InjectMocks
    private VolunteerServiceImpl volunteerService;

    @BeforeEach
    void setUp() {
        volunteerService = new VolunteerServiceImpl(repository, checkService, shelterService);
    }

    @Test
    void add() {
        validatePhoneNumber();
        saveVolunteer();

        assertEquals(VOLUNTEER,
                volunteerService.add(
                        FIRST_NAME,
                        LAST_NAME,
                        PHONE_NUMBER,
                        ID));

        verify(checkService, times(2)).checkName(anyString());
        verify(checkService, times(1)).checkPhoneNumber(anyString());
        verify(shelterService, times(1)).getById(anyLong());
        verify(checkService, times(1)).validatePhoneNumber(anyString());
        verify(repository, times(1)).findAll();
        verify(checkService, times(1)).checkVolunteerAlreadyAdded(anyCollection(), any(Volunteer.class));
        verify(repository, times(1)).save(any(Volunteer.class));
    }

    @Test
    void getById() {
        getVolunteer();

        assertEquals(VOLUNTEER,
                volunteerService.getById(ID));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void getById_VolunteerNotFoundException() {
        when(repository.findById(anyLong()))
                .thenThrow(VolunteerNotFoundException.class);

        assertThrows(VolunteerNotFoundException.class,
                () -> volunteerService.getById(ID));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void getAllByParameters() {
        when(repository.getAllByFirstNameContainsIgnoreCase(anyString()))
                .thenReturn(VOLUNTEERS);

        when(repository.getAllByLastNameContainsIgnoreCase(anyString()))
                .thenReturn(VOLUNTEERS);

        when(repository.getAllByPhoneNumberContains(anyString()))
                .thenReturn(VOLUNTEERS);

        when(repository.getAllByShelterId(anyLong()))
                .thenReturn(VOLUNTEERS);

        when(repository.getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCase(anyString(), anyString()))
                .thenReturn(VOLUNTEERS);

        when(repository.getAllByFirstNameContainsIgnoreCaseAndPhoneNumberContains(anyString(), anyString()))
                .thenReturn(VOLUNTEERS);

        when(repository.getAllByFirstNameContainsIgnoreCaseAndShelterId(anyString(), anyLong()))
                .thenReturn(VOLUNTEERS);

        when(repository.getAllByLastNameContainsIgnoreCaseAndPhoneNumberContains(anyString(), anyString()))
                .thenReturn(VOLUNTEERS);

        when(repository.getAllByLastNameContainsIgnoreCaseAndShelterId(anyString(), anyLong()))
                .thenReturn(VOLUNTEERS);

        when(repository.getAllByPhoneNumberContainsAndShelterId(anyString(), anyLong()))
                .thenReturn(VOLUNTEERS);

        when(repository.getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCaseAndPhoneNumberContains(anyString(), anyString(), anyString()))
                .thenReturn(VOLUNTEERS);

        when(repository.getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCaseAndShelterId(anyString(), anyString(), anyLong()))
                .thenReturn(VOLUNTEERS);

        when(repository.getAllByFirstNameContainsIgnoreCaseAndPhoneNumberContainsAndShelterId(anyString(), anyString(), anyLong()))
                .thenReturn(VOLUNTEERS);

        when(repository.getAllByLastNameContainsIgnoreCaseAndPhoneNumberContainsAndShelterId(anyString(), anyString(), anyLong()))
                .thenReturn(VOLUNTEERS);

        when(repository.getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCaseAndPhoneNumberContainsAndShelterId(anyString(), anyString(), anyString(), anyLong()))
                .thenReturn(VOLUNTEERS);

        assertEquals(VOLUNTEERS,
                volunteerService.getAllByParameters(
                        FIRST_NAME,
                        null,
                        null,
                        null));

        assertEquals(VOLUNTEERS,
                volunteerService.getAllByParameters(
                        null,
                        LAST_NAME,
                        null,
                        null));

        assertEquals(VOLUNTEERS,
                volunteerService.getAllByParameters(
                        null,
                        null,
                        VALID_PHONE_NUMBER,
                        null));

        assertEquals(VOLUNTEERS,
                volunteerService.getAllByParameters(
                        null,
                        null,
                        null,
                        ID));

        assertEquals(VOLUNTEERS,
                volunteerService.getAllByParameters(
                        FIRST_NAME,
                        LAST_NAME,
                        null,
                        null));

        assertEquals(VOLUNTEERS,
                volunteerService.getAllByParameters(
                        FIRST_NAME,
                        null,
                        VALID_PHONE_NUMBER,
                        null));

        assertEquals(VOLUNTEERS,
                volunteerService.getAllByParameters(
                        FIRST_NAME,
                        null,
                        null,
                        ID));

        assertEquals(VOLUNTEERS,
                volunteerService.getAllByParameters(
                        null,
                        LAST_NAME,
                        VALID_PHONE_NUMBER,
                        null));

        assertEquals(VOLUNTEERS,
                volunteerService.getAllByParameters(
                        null,
                        LAST_NAME,
                        null,
                        ID));

        assertEquals(VOLUNTEERS,
                volunteerService.getAllByParameters(
                        null,
                        null,
                        VALID_PHONE_NUMBER,
                        ID));

        assertEquals(VOLUNTEERS,
                volunteerService.getAllByParameters(
                        FIRST_NAME,
                        LAST_NAME,
                        VALID_PHONE_NUMBER,
                        null));

        assertEquals(VOLUNTEERS,
                volunteerService.getAllByParameters(
                        FIRST_NAME,
                        LAST_NAME,
                        null,
                        ID));

        assertEquals(VOLUNTEERS,
                volunteerService.getAllByParameters(
                        FIRST_NAME,
                        null,
                        VALID_PHONE_NUMBER,
                        ID));

        assertEquals(VOLUNTEERS,
                volunteerService.getAllByParameters(
                        null,
                        LAST_NAME,
                        VALID_PHONE_NUMBER,
                        ID));

        assertEquals(VOLUNTEERS,
                volunteerService.getAllByParameters(
                        FIRST_NAME,
                        LAST_NAME,
                        VALID_PHONE_NUMBER,
                        ID));

        verify(checkService, times(16)).checkName(anyString());
        verify(checkService, times(8)).checkValue(anyLong());
        verify(repository, times(1)).getAllByFirstNameContainsIgnoreCase(anyString());
        verify(repository, times(1)).getAllByLastNameContainsIgnoreCase(anyString());
        verify(repository, times(1)).getAllByPhoneNumberContains(anyString());
        verify(repository, times(1)).getAllByShelterId(anyLong());
        verify(repository, times(1)).getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCase(anyString(), anyString());
        verify(repository, times(1)).getAllByFirstNameContainsIgnoreCaseAndPhoneNumberContains(anyString(), anyString());
        verify(repository, times(1)).getAllByFirstNameContainsIgnoreCaseAndShelterId(anyString(), anyLong());
        verify(repository, times(1)).getAllByLastNameContainsIgnoreCaseAndPhoneNumberContains(anyString(), anyString());
        verify(repository, times(1)).getAllByLastNameContainsIgnoreCaseAndShelterId(anyString(), anyLong());
        verify(repository, times(1)).getAllByPhoneNumberContainsAndShelterId(anyString(), anyLong());
        verify(repository, times(1)).getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCaseAndPhoneNumberContains(anyString(), anyString(), anyString());
        verify(repository, times(1)).getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCaseAndShelterId(anyString(), anyString(), anyLong());
        verify(repository, times(1)).getAllByFirstNameContainsIgnoreCaseAndPhoneNumberContainsAndShelterId(anyString(), anyString(), anyLong());
        verify(repository, times(1)).getAllByLastNameContainsIgnoreCaseAndPhoneNumberContainsAndShelterId(anyString(), anyString(), anyLong());
        verify(repository, times(1)).getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCaseAndPhoneNumberContainsAndShelterId(anyString(), anyString(), anyString(), anyLong());
    }

    @Test
    void getAll() {
        when(repository.findAll())
                .thenReturn(VOLUNTEERS);

        assertEquals(VOLUNTEERS,
                volunteerService.getAll());

        verify(repository, times(1)).findAll();
    }

    @Test
    void edit() {
        getVolunteer();
        validatePhoneNumber();
        saveVolunteer();

        assertEquals(VOLUNTEER,
                volunteerService.edit(
                        ID,
                        FIRST_NAME,
                        LAST_NAME,
                        PHONE_NUMBER,
                        ID));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(repository, times(1)).findById(anyLong());
        verify(checkService, times(2)).checkName(anyString());
        verify(checkService, times(1)).checkPhoneNumber(anyString());
        verify(shelterService, times(1)).getById(anyLong());
        verify(checkService, times(1)).validatePhoneNumber(anyString());
        verify(repository, times(1)).findAll();
        verify(checkService, times(1)).checkVolunteerAlreadyAdded(anyCollection(), any(Volunteer.class));
        verify(repository, times(1)).save(any(Volunteer.class));
    }

    @Test
    void delete() {
        getVolunteer();

        assertEquals(VOLUNTEER,
                volunteerService.delete(ID));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any(Volunteer.class));
    }

    private void getVolunteer() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(VOLUNTEER));
    }

    private void validatePhoneNumber() {
        when(checkService.validatePhoneNumber(anyString()))
                .thenReturn(VALID_PHONE_NUMBER);
    }

    private void saveVolunteer() {
        when(repository.save(any(Volunteer.class)))
                .thenReturn(VOLUNTEER);
    }
}