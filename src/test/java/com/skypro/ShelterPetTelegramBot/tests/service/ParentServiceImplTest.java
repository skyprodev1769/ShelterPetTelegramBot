package com.skypro.ShelterPetTelegramBot.tests.service;

import com.skypro.ShelterPetTelegramBot.exception.parent.ParentNotFoundException;
import com.skypro.ShelterPetTelegramBot.model.enums.PetStatus;
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

import static com.skypro.ShelterPetTelegramBot.tests.Utils.*;
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
    void add() {
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
    void getById() {
        getParent();

        assertEquals(PARENT,
                parentService.getById(ID));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(parentRepository, times(1)).findById(anyLong());
    }

    @Test
    void getById_ParentNotFoundException() {
        when(parentRepository.findById(anyLong()))
                .thenThrow(ParentNotFoundException.class);

        assertThrows(ParentNotFoundException.class,
                () -> parentService.getById(ID));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(parentRepository, times(1)).findById(anyLong());
    }

    @Test
    void getAllByParameters() {
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

        assertEquals(PARENTS,
                parentService.getAllByParameters(
                        FIRST_NAME,
                        null,
                        null));

        assertEquals(PARENTS,
                parentService.getAllByParameters(
                        null,
                        LAST_NAME,
                        null));

        assertEquals(PARENTS,
                parentService.getAllByParameters(
                        null,
                        null,
                        PHONE_NUMBER));

        assertEquals(PARENTS,
                parentService.getAllByParameters(
                        FIRST_NAME,
                        LAST_NAME,
                        null));

        assertEquals(PARENTS,
                parentService.getAllByParameters(
                        FIRST_NAME,
                        null,
                        PHONE_NUMBER));

        assertEquals(PARENTS,
                parentService.getAllByParameters(
                        null,
                        LAST_NAME,
                        PHONE_NUMBER));

        assertEquals(PARENTS,
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
        verify(parentRepository, times(1)).getAllByFirstNameContainsIgnoreCaseAndLastNameContainsIgnoreCaseAndPhoneNumberContains(anyString(), anyString(), anyString());
    }

    @Test
    void getAll() {
        when(parentRepository.findAll())
                .thenReturn(PARENTS);

        assertEquals(PARENTS,
                parentService.getAll());

        verify(parentRepository, times(1)).findAll();
    }

    @Test
    void edit() {
        getParent();
        validatePhoneNumber();
        getPet();
        saveParent();

        assertEquals(PARENT,
                parentService.edit(
                        ID,
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
    void delete() {
        getParent();

        assertEquals(PARENT,
                parentService.delete(ID));

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
                .thenReturn(Optional.of(PET));
    }

    private void validatePhoneNumber() {
        when(checkService.validatePhoneNumber(anyString()))
                .thenReturn(VALID_PHONE_NUMBER);
    }

    private void saveParent() {
        when(parentRepository.save(any(Parent.class)))
                .thenReturn(PARENT);
    }
}