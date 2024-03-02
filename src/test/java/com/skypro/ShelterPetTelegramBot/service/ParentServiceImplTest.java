package com.skypro.ShelterPetTelegramBot.service;

import com.skypro.ShelterPetTelegramBot.exception.InvalideInputException;
import com.skypro.ShelterPetTelegramBot.exception.InvalideNumberException;
import com.skypro.ShelterPetTelegramBot.exception.PetStatusException;
import com.skypro.ShelterPetTelegramBot.exception.parent.ParentAlreadyAddedException;
import com.skypro.ShelterPetTelegramBot.exception.parent.ParentNotFoundException;
import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetStatus;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Parent;
import com.skypro.ShelterPetTelegramBot.model.repository.ParentRepository;
import com.skypro.ShelterPetTelegramBot.model.repository.PetRepository;
import com.skypro.ShelterPetTelegramBot.service.impl.entity_service.ParentServiceImpl;
import com.skypro.ShelterPetTelegramBot.service.interfaces.CheckService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.skypro.ShelterPetTelegramBot.utils.UtilsForParentService.*;
import static com.skypro.ShelterPetTelegramBot.utils.UtilsForPetService.NAME;
import static com.skypro.ShelterPetTelegramBot.utils.UtilsForPetService.PET_DOG;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParentServiceImplTest {

    @Mock
    private ParentRepository parentRepository;
    @Mock
    private PetRepository petRepository;
    @Mock
    private CheckService checkService;
    @InjectMocks
    private ParentServiceImpl parentService;

    @BeforeEach
    void setUp() {
        parentService = new ParentServiceImpl(parentRepository, petRepository, checkService);
    }

    @Test
    public void add_success() {
        validatePhoneNumber();
        getPet();
        saveParent();

        assertEquals(PARENT,
                parentService.add(
                        FIRST_NAME,
                        LAST_NAME,
                        PHONE_NUMBER,
                        NAME));

        verify(checkService, times(3)).checkName(anyString());
        verify(checkService, times(1)).checkPhoneNumber(anyString());
        verify(checkService, times(1)).validatePhoneNumber(anyString());
        verify(petRepository, times(1)).getByNameContainsIgnoreCase(anyString());
        verify(checkService, times(1)).checkStatus(any(PetStatus.class));
        verify(parentRepository, times(1)).findAll();
        verify(checkService, times(1)).checkParentAlreadyAdded(anyCollection(), any(Parent.class));
        verify(parentRepository, times(1)).save(any(Parent.class));
    }

    @Test
    public void add_InvalideInputException() {
        checkNameException();

        assertThrows(InvalideInputException.class,
                () -> parentService.add(
                        INCORRECT_STRING,
                        LAST_NAME,
                        PHONE_NUMBER,
                        NAME));

        assertThrows(InvalideInputException.class,
                () -> parentService.add(
                        FIRST_NAME,
                        INCORRECT_STRING,
                        PHONE_NUMBER,
                        NAME));

        assertThrows(InvalideInputException.class,
                () -> parentService.add(
                        FIRST_NAME,
                        LAST_NAME,
                        PHONE_NUMBER,
                        INCORRECT_STRING));

        verify(checkService, times(3)).checkName(anyString());
        verify(checkService, times(0)).checkPhoneNumber(anyString());
        verify(checkService, times(0)).validatePhoneNumber(anyString());
        verify(petRepository, times(0)).getByNameContainsIgnoreCase(anyString());
        verify(checkService, times(0)).checkStatus(any(PetStatus.class));
        verify(parentRepository, times(0)).findAll();
        verify(checkService, times(0)).checkParentAlreadyAdded(anyCollection(), any(Parent.class));
        verify(parentRepository, times(0)).save(any(Parent.class));
    }

    @Test
    public void add_InvalideNumberException() {
        checkPhoneNumberException();

        assertThrows(InvalideNumberException.class,
                () -> parentService.add(
                        FIRST_NAME,
                        LAST_NAME,
                        INCORRECT_STRING,
                        NAME));

        verify(checkService, times(3)).checkName(anyString());
        verify(checkService, times(1)).checkPhoneNumber(anyString());
        verify(checkService, times(0)).validatePhoneNumber(anyString());
        verify(petRepository, times(0)).getByNameContainsIgnoreCase(anyString());
        verify(checkService, times(0)).checkStatus(any(PetStatus.class));
        verify(parentRepository, times(0)).findAll();
        verify(checkService, times(0)).checkParentAlreadyAdded(anyCollection(), any(Parent.class));
        verify(parentRepository, times(0)).save(any(Parent.class));
    }

    @Test
    public void add_PetStatusException() {
        getPet();
        checkStatusException();

        assertThrows(PetStatusException.class,
                () -> parentService.add(
                        FIRST_NAME,
                        LAST_NAME,
                        PHONE_NUMBER,
                        NAME));

        verify(checkService, times(3)).checkName(anyString());
        verify(checkService, times(1)).checkPhoneNumber(anyString());
        verify(checkService, times(1)).validatePhoneNumber(anyString());
        verify(petRepository, times(1)).getByNameContainsIgnoreCase(anyString());
        verify(checkService, times(1)).checkStatus(any(PetStatus.class));
        verify(parentRepository, times(0)).findAll();
        verify(checkService, times(0)).checkParentAlreadyAdded(anyCollection(), any(Parent.class));
        verify(parentRepository, times(0)).save(any(Parent.class));
    }

    @Test
    public void add_ParentAlreadyAddedException() {
        getPet();
        checkAddedException();

        assertThrows(ParentAlreadyAddedException.class,
                () -> parentService.add(
                        FIRST_NAME,
                        LAST_NAME,
                        PHONE_NUMBER,
                        NAME));

        verify(checkService, times(3)).checkName(anyString());
        verify(checkService, times(1)).checkPhoneNumber(anyString());
        verify(checkService, times(1)).validatePhoneNumber(anyString());
        verify(petRepository, times(1)).getByNameContainsIgnoreCase(anyString());
        verify(checkService, times(1)).checkStatus(any(PetStatus.class));
        verify(parentRepository, times(1)).findAll();
        verify(checkService, times(1)).checkParentAlreadyAdded(anyCollection(), any(Parent.class));
        verify(parentRepository, times(0)).save(any(Parent.class));
    }

    @Test
    public void getById_success() {
        getParent();

        assertEquals(PARENT,
                parentService.getById(ID_PARENT));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(parentRepository, times(1)).findById(anyLong());
    }

    @Test
    public void getById_InvalideInputException() {
        when(checkService.checkValue(anyLong()))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> parentService.getById(0L));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(parentRepository, times(0)).findById(anyLong());
    }

    @Test
    public void getById_ParentNotFoundException() {
        when(parentRepository.findById(anyLong()))
                .thenThrow(ParentNotFoundException.class);

        assertThrows(ParentNotFoundException.class,
                () -> parentService.getById(ID_PARENT));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(parentRepository, times(1)).findById(anyLong());
    }

    @Test
    public void getAllByParameters_success() {
        when(parentRepository.getAllByFirstNameContainsIgnoreCase(anyString()))
                .thenReturn(getParents());

        when(parentRepository.getAllByLastNameContainsIgnoreCase(anyString()))
                .thenReturn(getParents());

        when(parentRepository.getAllByPhoneNumberContains(anyString()))
                .thenReturn(getParents());

        when(parentRepository.getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCase(anyString(), anyString()))
                .thenReturn(getParents());

        when(parentRepository.getAllByFirstNameContainsIgnoreCaseAndPhoneNumberContains(anyString(), anyString()))
                .thenReturn(getParents());

        when(parentRepository.getAllByLastNameContainsIgnoreCaseAndPhoneNumberContains(anyString(), anyString()))
                .thenReturn(getParents());

        when(parentRepository.getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCaseAndPhoneNumber(anyString(), anyString(), anyString()))
                .thenReturn(getParents());

        assertEquals(getParents(),
                parentService.getAllByParameters(
                        FIRST_NAME,
                        null,
                        null));

        assertEquals(getParents(),
                parentService.getAllByParameters(
                        null,
                        LAST_NAME,
                        null));

        assertEquals(getParents(),
                parentService.getAllByParameters(
                        null,
                        null,
                        PHONE_NUMBER));

        assertEquals(getParents(),
                parentService.getAllByParameters(
                        FIRST_NAME,
                        LAST_NAME,
                        null));

        assertEquals(getParents(),
                parentService.getAllByParameters(
                        FIRST_NAME,
                        null,
                        PHONE_NUMBER));

        assertEquals(getParents(),
                parentService.getAllByParameters(
                        null,
                        LAST_NAME,
                        PHONE_NUMBER));

        assertEquals(getParents(),
                parentService.getAllByParameters(
                        FIRST_NAME,
                        LAST_NAME,
                        PHONE_NUMBER));

        verify(checkService, times(8)).checkName(anyString());
        verify(parentRepository, times(1)).getAllByFirstNameContainsIgnoreCase(anyString());
        verify(parentRepository, times(1)).getAllByLastNameContainsIgnoreCase(anyString());
        verify(parentRepository, times(1)).getAllByPhoneNumberContains(anyString());
        verify(parentRepository, times(1)).getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCase(anyString(), anyString());
        verify(parentRepository, times(1)).getAllByFirstNameContainsIgnoreCaseAndPhoneNumberContains(anyString(), anyString());
        verify(parentRepository, times(1)).getAllByLastNameContainsIgnoreCaseAndPhoneNumberContains(anyString(), anyString());
        verify(parentRepository, times(1)).getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCaseAndPhoneNumber(anyString(), anyString(), anyString());
    }

    @Test
    public void getAllByParameters_InvalideInputException() {
        checkNameException();

        assertThrows(InvalideInputException.class,
                () -> parentService.getAllByParameters(
                        INCORRECT_STRING,
                        null,
                        null
                ));

        assertThrows(InvalideInputException.class,
                () -> parentService.getAllByParameters(
                        null,
                        INCORRECT_STRING,
                        null
                ));

        verify(checkService, times(2)).checkName(anyString());
        verify(parentRepository, times(0)).getAllByFirstNameContainsIgnoreCase(anyString());
        verify(parentRepository, times(0)).getAllByLastNameContainsIgnoreCase(anyString());
        verify(parentRepository, times(0)).getAllByPhoneNumberContains(anyString());
        verify(parentRepository, times(0)).getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCase(anyString(), anyString());
        verify(parentRepository, times(0)).getAllByFirstNameContainsIgnoreCaseAndPhoneNumberContains(anyString(), anyString());
        verify(parentRepository, times(0)).getAllByLastNameContainsIgnoreCaseAndPhoneNumberContains(anyString(), anyString());
        verify(parentRepository, times(0)).getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCaseAndPhoneNumber(anyString(), anyString(), anyString());
    }

    @Test
    public void getAll_success() {
        when(parentRepository.findAll())
                .thenReturn(getParents());

        assertEquals(getParents(),
                parentService.getAll());

        verify(parentRepository, times(1)).findAll();
    }

    @Test
    public void edit_success() {
        getParent();
        validatePhoneNumber();
        getPet();
        saveParent();

        assertEquals(PARENT,
                parentService.edit(
                        ID_PARENT,
                        FIRST_NAME,
                        LAST_NAME,
                        PHONE_NUMBER,
                        NAME));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(parentRepository, times(1)).findById(anyLong());
        verify(checkService, times(3)).checkName(anyString());
        verify(checkService, times(1)).checkPhoneNumber(anyString());
        verify(checkService, times(1)).validatePhoneNumber(anyString());
        verify(petRepository, times(1)).getByNameContainsIgnoreCase(anyString());
        verify(checkService, times(1)).checkStatus(any(PetStatus.class));
        verify(parentRepository, times(1)).findAll();
        verify(checkService, times(1)).checkParentAlreadyAdded(anyCollection(), any(Parent.class));
        verify(parentRepository, times(1)).save(any(Parent.class));
    }

    @Test
    public void edit_InvalideInputException() {
        getParent();
        checkNameException();

        assertThrows(InvalideInputException.class,
                () -> parentService.edit(
                        ID_PARENT,
                        INCORRECT_STRING,
                        null,
                        null,
                        null));

        assertThrows(InvalideInputException.class,
                () -> parentService.edit(
                        ID_PARENT,
                        null,
                        INCORRECT_STRING,
                        null,
                        null));

        assertThrows(InvalideInputException.class,
                () -> parentService.edit(
                        ID_PARENT,
                        null,
                        null,
                        null,
                        INCORRECT_STRING));

        verify(checkService, times(3)).checkValue(anyLong());
        verify(parentRepository, times(3)).findById(anyLong());
        verify(checkService, times(3)).checkName(anyString());
        verify(checkService, times(0)).checkPhoneNumber(anyString());
        verify(checkService, times(0)).validatePhoneNumber(anyString());
        verify(petRepository, times(0)).getByNameContainsIgnoreCase(anyString());
        verify(checkService, times(0)).checkStatus(any(PetStatus.class));
        verify(parentRepository, times(0)).findAll();
        verify(checkService, times(0)).checkParentAlreadyAdded(anyCollection(), any(Parent.class));
        verify(parentRepository, times(0)).save(any(Parent.class));
    }

    @Test
    public void edit_InvalideNumberException() {
        getParent();
        checkPhoneNumberException();

        assertThrows(InvalideNumberException.class,
                () -> parentService.edit(
                        ID_PARENT,
                        null,
                        null,
                        INCORRECT_STRING,
                        null));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(parentRepository, times(1)).findById(anyLong());
        verify(checkService, times(0)).checkName(anyString());
        verify(checkService, times(1)).checkPhoneNumber(anyString());
        verify(checkService, times(0)).validatePhoneNumber(anyString());
        verify(petRepository, times(0)).getByNameContainsIgnoreCase(anyString());
        verify(checkService, times(0)).checkStatus(any(PetStatus.class));
        verify(parentRepository, times(0)).findAll();
        verify(checkService, times(0)).checkParentAlreadyAdded(anyCollection(), any(Parent.class));
        verify(parentRepository, times(0)).save(any(Parent.class));
    }

    @Test
    public void edit_PetStatusException() {
        getParent();
        getPet();
        checkStatusException();

        assertThrows(PetStatusException.class,
                () -> parentService.edit(
                        ID_PARENT,
                        null,
                        null,
                        null,
                        NAME));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(parentRepository, times(1)).findById(anyLong());
        verify(checkService, times(1)).checkName(anyString());
        verify(checkService, times(0)).checkPhoneNumber(anyString());
        verify(checkService, times(0)).validatePhoneNumber(anyString());
        verify(petRepository, times(1)).getByNameContainsIgnoreCase(anyString());
        verify(checkService, times(1)).checkStatus(any(PetStatus.class));
        verify(parentRepository, times(0)).findAll();
        verify(checkService, times(0)).checkParentAlreadyAdded(anyCollection(), any(Parent.class));
        verify(parentRepository, times(0)).save(any(Parent.class));
    }

    @Test
    public void edit_ParentAlreadyAddedException() {
        getParent();
        checkAddedException();

        assertThrows(ParentAlreadyAddedException.class,
                () -> parentService.edit(
                        ID_PARENT,
                        null,
                        null,
                        PHONE_NUMBER,
                        null));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(parentRepository, times(1)).findById(anyLong());
        verify(checkService, times(0)).checkName(anyString());
        verify(checkService, times(1)).checkPhoneNumber(anyString());
        verify(checkService, times(1)).validatePhoneNumber(anyString());
        verify(petRepository, times(0)).getByNameContainsIgnoreCase(anyString());
        verify(checkService, times(0)).checkStatus(any(PetStatus.class));
        verify(parentRepository, times(1)).findAll();
        verify(checkService, times(1)).checkParentAlreadyAdded(anyCollection(), any(Parent.class));
        verify(parentRepository, times(0)).save(any(Parent.class));
    }

    @Test
    public void delete_success() {
        getParent();

        assertEquals(PARENT,
                parentService.delete(ID_PARENT));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(parentRepository, times(1)).findById(anyLong());
        verify(parentRepository, times(1)).delete(any(Parent.class));
    }

    private void getParent() {
        when(parentRepository.findById(anyLong()))
                .thenReturn(Optional.of(PARENT));
    }

    private void getPet() {
        when(petRepository.getByNameContainsIgnoreCase(anyString()))
                .thenReturn(Optional.of(PET_DOG));
    }

    private void validatePhoneNumber() {
        when(checkService.validatePhoneNumber(anyString()))
                .thenReturn(VALID_PHONE_NUMBER);
    }

    private void saveParent() {
        when(parentRepository.save(any(Parent.class)))
                .thenReturn(PARENT);
    }

    private void checkNameException() {
        when(checkService.checkName(anyString()))
                .thenThrow(InvalideInputException.class);
    }

    private void checkPhoneNumberException() {
        when(checkService.checkPhoneNumber(anyString()))
                .thenThrow(InvalideNumberException.class);
    }

    private void checkStatusException() {
        when(checkService.checkStatus(any(PetStatus.class)))
                .thenThrow(PetStatusException.class);
    }

    private void checkAddedException() {
        when(checkService.checkParentAlreadyAdded(anyCollection(), any(Parent.class)))
                .thenThrow(ParentAlreadyAddedException.class);
    }
}