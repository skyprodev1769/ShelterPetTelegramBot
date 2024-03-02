package com.skypro.ShelterPetTelegramBot.service;

import com.skypro.ShelterPetTelegramBot.exception.InvalideInputException;
import com.skypro.ShelterPetTelegramBot.exception.InvalideNumberException;
import com.skypro.ShelterPetTelegramBot.exception.volunteer.VolunteerAlreadyAddedException;
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

import static com.skypro.ShelterPetTelegramBot.utils.UtilsForShelterService.ID_SHELTER;
import static com.skypro.ShelterPetTelegramBot.utils.UtilsForVolunteerService.*;
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
    public void add_success() {
        validatePhoneNumber();
        saveVolunteer();

        assertEquals(VOLUNTEER,
                volunteerService.add(
                        FIRST_NAME,
                        LAST_NAME,
                        PHONE_NUMBER,
                        ID_SHELTER));

        verify(checkService, times(2)).checkName(anyString());
        verify(checkService, times(1)).checkPhoneNumber(anyString());
        verify(shelterService, times(1)).getById(anyLong());
        verify(checkService, times(1)).validatePhoneNumber(anyString());
        verify(repository, times(1)).findAll();
        verify(checkService, times(1)).checkVolunteerAlreadyAdded(anyCollection(), any(Volunteer.class));
        verify(repository, times(1)).save(any(Volunteer.class));
    }

    @Test
    public void add_InvalideInputException() {
        checkNameException();

        assertThrows(InvalideInputException.class,
                () -> volunteerService.add(
                        INCORRECT_STRING,
                        LAST_NAME,
                        PHONE_NUMBER,
                        ID_SHELTER));

        assertThrows(InvalideInputException.class,
                () -> volunteerService.add(
                        FIRST_NAME,
                        INCORRECT_STRING,
                        PHONE_NUMBER,
                        ID_SHELTER));

        verify(checkService, times(2)).checkName(anyString());
        verify(checkService, times(0)).checkPhoneNumber(anyString());
        verify(shelterService, times(0)).getById(anyLong());
        verify(checkService, times(0)).validatePhoneNumber(anyString());
        verify(repository, times(0)).findAll();
        verify(checkService, times(0)).checkVolunteerAlreadyAdded(anyCollection(), any(Volunteer.class));
        verify(repository, times(0)).save(any(Volunteer.class));
    }

    @Test
    public void add_InvalideNumberException() {
        checkPhoneNumberException();

        assertThrows(InvalideNumberException.class,
                () -> volunteerService.add(
                        FIRST_NAME,
                        LAST_NAME,
                        INCORRECT_STRING,
                        ID_SHELTER));

        verify(checkService, times(2)).checkName(anyString());
        verify(checkService, times(1)).checkPhoneNumber(anyString());
        verify(shelterService, times(0)).getById(anyLong());
        verify(checkService, times(0)).validatePhoneNumber(anyString());
        verify(repository, times(0)).findAll();
        verify(checkService, times(0)).checkVolunteerAlreadyAdded(anyCollection(), any(Volunteer.class));
        verify(repository, times(0)).save(any(Volunteer.class));
    }

    @Test
    public void add_VolunteerAlreadyAddedException() {
        checkAddedException();

        assertThrows(VolunteerAlreadyAddedException.class,
                () -> volunteerService.add(
                        FIRST_NAME,
                        LAST_NAME,
                        PHONE_NUMBER,
                        ID_SHELTER));

        verify(checkService, times(2)).checkName(anyString());
        verify(checkService, times(1)).checkPhoneNumber(anyString());
        verify(shelterService, times(1)).getById(anyLong());
        verify(checkService, times(1)).validatePhoneNumber(anyString());
        verify(repository, times(1)).findAll();
        verify(checkService, times(1)).checkVolunteerAlreadyAdded(anyCollection(), any(Volunteer.class));
        verify(repository, times(0)).save(any(Volunteer.class));
    }

    @Test
    public void getById_success() {
        getVolunteer();

        assertEquals(VOLUNTEER,
                volunteerService.getById(ID_VOLUNTEER));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    public void getById_InvalideInputException() {
        checkValueException();

        assertThrows(InvalideInputException.class,
                () -> volunteerService.getById(0L));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(repository, times(0)).findById(anyLong());
    }

    @Test
    public void getById_VolunteerNotFoundException() {
        when(repository.findById(anyLong()))
                .thenThrow(VolunteerNotFoundException.class);

        assertThrows(VolunteerNotFoundException.class,
                () -> volunteerService.getById(ID_VOLUNTEER));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    public void getAllByParameters_success() {
        when(repository.getAllByFirstNameContainsIgnoreCase(anyString()))
                .thenReturn(getVolunteers());

        when(repository.getAllByLastNameContainsIgnoreCase(anyString()))
                .thenReturn(getVolunteers());

        when(repository.getAllByPhoneNumberContains(anyString()))
                .thenReturn(getVolunteers());

        when(repository.getAllByShelterId(anyLong()))
                .thenReturn(getVolunteers());

        when(repository.getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCase(anyString(), anyString()))
                .thenReturn(getVolunteers());

        when(repository.getAllByFirstNameContainsIgnoreCaseAndPhoneNumberContains(anyString(), anyString()))
                .thenReturn(getVolunteers());

        when(repository.getAllByFirstNameContainsIgnoreCaseAndShelterId(anyString(), anyLong()))
                .thenReturn(getVolunteers());

        when(repository.getAllByLastNameContainsIgnoreCaseAndPhoneNumberContains(anyString(), anyString()))
                .thenReturn(getVolunteers());

        when(repository.getAllByLastNameContainsIgnoreCaseAndShelterId(anyString(), anyLong()))
                .thenReturn(getVolunteers());

        when(repository.getAllByPhoneNumberContainsAndShelterId(anyString(), anyLong()))
                .thenReturn(getVolunteers());

        when(repository.getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCaseAndPhoneNumber(anyString(), anyString(), anyString()))
                .thenReturn(getVolunteers());

        when(repository.getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCaseAndShelterId(anyString(), anyString(), anyLong()))
                .thenReturn(getVolunteers());

        when(repository.getAllByFirstNameContainsIgnoreCaseAndPhoneNumberContainsAndShelterId(anyString(), anyString(), anyLong()))
                .thenReturn(getVolunteers());

        when(repository.getAllByLastNameContainsIgnoreCaseAndPhoneNumberContainsAndShelterId(anyString(), anyString(), anyLong()))
                .thenReturn(getVolunteers());

        when(repository.getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCaseAndPhoneNumberContainsAndShelterId(anyString(), anyString(), anyString(), anyLong()))
                .thenReturn(getVolunteers());

        assertEquals(getVolunteers(),
                volunteerService.getAllByParameters(
                        FIRST_NAME,
                        null,
                        null,
                        null));

        assertEquals(getVolunteers(),
                volunteerService.getAllByParameters(
                        null,
                        LAST_NAME,
                        null,
                        null));

        assertEquals(getVolunteers(),
                volunteerService.getAllByParameters(
                        null,
                        null,
                        VALID_PHONE_NUMBER,
                        null));

        assertEquals(getVolunteers(),
                volunteerService.getAllByParameters(
                        null,
                        null,
                        null,
                        ID_SHELTER));

        assertEquals(getVolunteers(),
                volunteerService.getAllByParameters(
                        FIRST_NAME,
                        LAST_NAME,
                        null,
                        null));

        assertEquals(getVolunteers(),
                volunteerService.getAllByParameters(
                        FIRST_NAME,
                        null,
                        VALID_PHONE_NUMBER,
                        null));

        assertEquals(getVolunteers(),
                volunteerService.getAllByParameters(
                        FIRST_NAME,
                        null,
                        null,
                        ID_SHELTER));

        assertEquals(getVolunteers(),
                volunteerService.getAllByParameters(
                        null,
                        LAST_NAME,
                        VALID_PHONE_NUMBER,
                        null));

        assertEquals(getVolunteers(),
                volunteerService.getAllByParameters(
                        null,
                        LAST_NAME,
                        null,
                        ID_SHELTER));

        assertEquals(getVolunteers(),
                volunteerService.getAllByParameters(
                        null,
                        null,
                        VALID_PHONE_NUMBER,
                        ID_SHELTER));

        assertEquals(getVolunteers(),
                volunteerService.getAllByParameters(
                        FIRST_NAME,
                        LAST_NAME,
                        VALID_PHONE_NUMBER,
                        null));

        assertEquals(getVolunteers(),
                volunteerService.getAllByParameters(
                        FIRST_NAME,
                        LAST_NAME,
                        null,
                        ID_SHELTER));

        assertEquals(getVolunteers(),
                volunteerService.getAllByParameters(
                        FIRST_NAME,
                        null,
                        VALID_PHONE_NUMBER,
                        ID_SHELTER));

        assertEquals(getVolunteers(),
                volunteerService.getAllByParameters(
                        null,
                        LAST_NAME,
                        VALID_PHONE_NUMBER,
                        ID_SHELTER));

        assertEquals(getVolunteers(),
                volunteerService.getAllByParameters(
                        FIRST_NAME,
                        LAST_NAME,
                        VALID_PHONE_NUMBER,
                        ID_SHELTER));

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
        verify(repository, times(1)).getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCaseAndPhoneNumber(anyString(), anyString(), anyString());
        verify(repository, times(1)).getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCaseAndShelterId(anyString(), anyString(), anyLong());
        verify(repository, times(1)).getAllByFirstNameContainsIgnoreCaseAndPhoneNumberContainsAndShelterId(anyString(), anyString(), anyLong());
        verify(repository, times(1)).getAllByLastNameContainsIgnoreCaseAndPhoneNumberContainsAndShelterId(anyString(), anyString(), anyLong());
        verify(repository, times(1)).getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCaseAndPhoneNumberContainsAndShelterId(anyString(), anyString(), anyString(), anyLong());
    }

    @Test
    public void getAllByParameters_InvalideInputException() {
        checkNameException();
        checkValueException();

        assertThrows(InvalideInputException.class,
                () -> volunteerService.getAllByParameters(
                        INCORRECT_STRING,
                        null,
                        null,
                        null));

        assertThrows(InvalideInputException.class,
                () -> volunteerService.getAllByParameters(
                        null,
                        INCORRECT_STRING,
                        null,
                        null));

        assertThrows(InvalideInputException.class,
                () -> volunteerService.getAllByParameters(
                        null,
                        null,
                        null,
                        0L));

        verify(checkService, times(2)).checkName(anyString());
        verify(checkService, times(1)).checkValue(anyLong());
        verify(repository, times(0)).getAllByFirstNameContainsIgnoreCase(anyString());
        verify(repository, times(0)).getAllByLastNameContainsIgnoreCase(anyString());
        verify(repository, times(0)).getAllByPhoneNumberContains(anyString());
        verify(repository, times(0)).getAllByShelterId(anyLong());
        verify(repository, times(0)).getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCase(anyString(), anyString());
        verify(repository, times(0)).getAllByFirstNameContainsIgnoreCaseAndPhoneNumberContains(anyString(), anyString());
        verify(repository, times(0)).getAllByFirstNameContainsIgnoreCaseAndShelterId(anyString(), anyLong());
        verify(repository, times(0)).getAllByLastNameContainsIgnoreCaseAndPhoneNumberContains(anyString(), anyString());
        verify(repository, times(0)).getAllByLastNameContainsIgnoreCaseAndShelterId(anyString(), anyLong());
        verify(repository, times(0)).getAllByPhoneNumberContainsAndShelterId(anyString(), anyLong());
        verify(repository, times(0)).getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCaseAndPhoneNumber(anyString(), anyString(), anyString());
        verify(repository, times(0)).getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCaseAndShelterId(anyString(), anyString(), anyLong());
        verify(repository, times(0)).getAllByFirstNameContainsIgnoreCaseAndPhoneNumberContainsAndShelterId(anyString(), anyString(), anyLong());
        verify(repository, times(0)).getAllByLastNameContainsIgnoreCaseAndPhoneNumberContainsAndShelterId(anyString(), anyString(), anyLong());
        verify(repository, times(0)).getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCaseAndPhoneNumberContainsAndShelterId(anyString(), anyString(), anyString(), anyLong());
    }

    @Test
    public void getAll_success() {
        when(repository.findAll())
                .thenReturn(getVolunteers());

        assertEquals(getVolunteers(),
                volunteerService.getAll());

        verify(repository, times(1)).findAll();
    }

    @Test
    public void edit_success() {
        getVolunteer();
        validatePhoneNumber();
        saveVolunteer();

        assertEquals(VOLUNTEER,
                volunteerService.edit(
                        ID_VOLUNTEER,
                        FIRST_NAME,
                        LAST_NAME,
                        PHONE_NUMBER,
                        ID_SHELTER));

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
    public void edit_InvalideInputException() {
        getVolunteer();
        checkNameException();

        assertThrows(InvalideInputException.class,
                () -> volunteerService.edit(
                        ID_VOLUNTEER,
                        INCORRECT_STRING,
                        null,
                        null,
                        null));

        assertThrows(InvalideInputException.class,
                () -> volunteerService.edit(
                        ID_VOLUNTEER,
                        null,
                        INCORRECT_STRING,
                        null,
                        null));

        verify(checkService, times(2)).checkValue(anyLong());
        verify(repository, times(2)).findById(anyLong());
        verify(checkService, times(2)).checkName(anyString());
        verify(checkService, times(0)).checkPhoneNumber(anyString());
        verify(shelterService, times(0)).getById(anyLong());
        verify(checkService, times(0)).validatePhoneNumber(anyString());
        verify(repository, times(0)).findAll();
        verify(checkService, times(0)).checkVolunteerAlreadyAdded(anyCollection(), any(Volunteer.class));
        verify(repository, times(0)).save(any(Volunteer.class));
    }

    @Test
    public void edit_InvalideNumberException() {
        getVolunteer();
        checkPhoneNumberException();

        assertThrows(InvalideNumberException.class,
                () -> volunteerService.edit(
                        ID_VOLUNTEER,
                        null,
                        null,
                        INCORRECT_STRING,
                        null));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(repository, times(1)).findById(anyLong());
        verify(checkService, times(0)).checkName(anyString());
        verify(checkService, times(1)).checkPhoneNumber(anyString());
        verify(shelterService, times(0)).getById(anyLong());
        verify(checkService, times(0)).validatePhoneNumber(anyString());
        verify(repository, times(0)).findAll();
        verify(checkService, times(0)).checkVolunteerAlreadyAdded(anyCollection(), any(Volunteer.class));
        verify(repository, times(0)).save(any(Volunteer.class));
    }

    @Test
    public void edit_VolunteerAlreadyAddedException() {
        getVolunteer();
        checkAddedException();

        assertThrows(VolunteerAlreadyAddedException.class,
                () -> volunteerService.edit(
                        ID_VOLUNTEER,
                        null,
                        null,
                        PHONE_NUMBER,
                        null));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(repository, times(1)).findById(anyLong());
        verify(checkService, times(0)).checkName(anyString());
        verify(checkService, times(1)).checkPhoneNumber(anyString());
        verify(shelterService, times(0)).getById(anyLong());
        verify(checkService, times(1)).validatePhoneNumber(anyString());
        verify(repository, times(1)).findAll();
        verify(checkService, times(1)).checkVolunteerAlreadyAdded(anyCollection(), any(Volunteer.class));
        verify(repository, times(0)).save(any(Volunteer.class));
    }

    @Test
    public void delete_success() {
        getVolunteer();

        assertEquals(VOLUNTEER,
                volunteerService.delete(ID_VOLUNTEER));

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

    private void checkNameException() {
        when(checkService.checkName(anyString()))
                .thenThrow(InvalideInputException.class);
    }

    private void checkPhoneNumberException() {
        when(checkService.checkPhoneNumber(anyString()))
                .thenThrow(InvalideNumberException.class);
    }

    private void checkAddedException() {
        when(checkService.checkVolunteerAlreadyAdded(anyCollection(), any(Volunteer.class)))
                .thenThrow(VolunteerAlreadyAddedException.class);
    }

    private void checkValueException() {
        when(checkService.checkValue(anyLong()))
                .thenThrow(InvalideInputException.class);
    }
}