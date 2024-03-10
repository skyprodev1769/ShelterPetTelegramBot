package com.skypro.ShelterPetTelegramBot.tests.service;

import com.skypro.ShelterPetTelegramBot.exception.shelter.ShelterNotFoundException;
import com.skypro.ShelterPetTelegramBot.model.enums.PetType;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Shelter;
import com.skypro.ShelterPetTelegramBot.model.repository.ShelterRepository;
import com.skypro.ShelterPetTelegramBot.service.impl.entity_service.ShelterServiceImpl;
import com.skypro.ShelterPetTelegramBot.service.interfaces.CheckService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.skypro.ShelterPetTelegramBot.model.enums.PetType.DOG;
import static com.skypro.ShelterPetTelegramBot.tests.Utils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShelterServiceImplTest {

    @Mock
    private ShelterRepository repository;
    @Mock
    private CheckService checkService;
    @InjectMocks
    private ShelterServiceImpl shelterService;

    @BeforeEach
    void setUp() {
        shelterService = new ShelterServiceImpl(repository, checkService);
    }

    @Test
    void add() {
        saveShelter();

        assertEquals(SHELTER,
                shelterService.add(DOG, ADDRESS));

        verify(checkService, times(1)).checkAddress(anyString());
        verify(repository, times(1)).findAll();
        verify(checkService, times(1)).checkShelterAlreadyAdded(anyCollection(), any(Shelter.class));
        verify(repository, times(1)).save(any(Shelter.class));
    }

    @Test
    void getById() {
        getShelter();

        assertEquals(SHELTER,
                shelterService.getById(ID));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void getById_ShelterNotFoundException() {
        when(repository.findById(anyLong()))
                .thenThrow(ShelterNotFoundException.class);

        assertThrows(ShelterNotFoundException.class,
                () -> shelterService.getById(ID));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void getAllByParameters() {
        when(repository.getAllByType(any(PetType.class)))
                .thenReturn(SHELTERS);

        when(repository.getAllByAddressContainsIgnoreCase(anyString()))
                .thenReturn(SHELTERS);

        when(repository.getAllByTypeAndAddressContainsIgnoreCase(any(PetType.class), anyString()))
                .thenReturn(SHELTERS);

        assertEquals(SHELTERS,
                shelterService.getAllByParameters(DOG, null));

        assertEquals(SHELTERS,
                shelterService.getAllByParameters(null, ADDRESS));

        assertEquals(SHELTERS,
                shelterService.getAllByParameters(DOG, ADDRESS));

        verify(checkService, times(2)).checkAddress(anyString());
        verify(repository, times(1)).getAllByType(any(PetType.class));
        verify(repository, times(1)).getAllByAddressContainsIgnoreCase(anyString());
        verify(repository, times(1)).getAllByTypeAndAddressContainsIgnoreCase(any(PetType.class), anyString());
    }

    @Test
    void getAll() {
        when(repository.findAll())
                .thenReturn(SHELTERS);

        assertEquals(SHELTERS,
                shelterService.getAll());

        verify(repository, times(1)).findAll();
    }

    @Test
    void edit() {
        getShelter();
        saveShelter();

        assertEquals(SHELTER,
                shelterService.edit(ID, DOG, ADDRESS));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(repository, times(1)).findById(anyLong());
        verify(checkService, times(1)).checkAddress(anyString());
        verify(checkService, times(1)).checkShelterAlreadyAdded(anyCollection(), any(Shelter.class));
        verify(repository, times(1)).save(any(Shelter.class));
    }

    @Test
    void delete() {
        getShelter();

        assertEquals(SHELTER,
                shelterService.delete(ID));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any(Shelter.class));
    }

    private void getShelter() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(SHELTER));
    }

    private void saveShelter() {
        when(repository.save(any(Shelter.class)))
                .thenReturn(SHELTER);
    }
}