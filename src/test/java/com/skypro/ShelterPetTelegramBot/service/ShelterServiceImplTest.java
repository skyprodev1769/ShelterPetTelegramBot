package com.skypro.ShelterPetTelegramBot.service;

import com.skypro.ShelterPetTelegramBot.exception.InvalideInputException;
import com.skypro.ShelterPetTelegramBot.exception.shelter.ShelterAlreadyAddedException;
import com.skypro.ShelterPetTelegramBot.exception.shelter.ShelterNotFoundException;
import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType;
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

import static com.skypro.ShelterPetTelegramBot.utils.UtilsForShelterService.*;
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
    public void add_success() {
        when(checkService.checkAddress(anyString()))
                .thenReturn(true);

        getAll_success();

        when(checkService.checkShelterAlreadyAdded(anyCollection(), any(Shelter.class)))
                .thenReturn(true);

        when(repository.save(any(Shelter.class)))
                .thenReturn(CAT_SHELTER);

        assertEquals(CAT_SHELTER,
                shelterService.add(
                        CAT_SHELTER.getType(),
                        CAT_SHELTER.getAddress()));

        verify(checkService, times(1)).checkAddress(anyString());
        verify(repository, times(2)).findAll();
        verify(checkService, times(1)).checkShelterAlreadyAdded(anyCollection(), any(Shelter.class));
        verify(repository, times(1)).save(any(Shelter.class));
    }

    @Test
    public void add_InvalideInputException() {
        when(checkService.checkAddress(anyString()))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> shelterService.add(
                        CAT_SHELTER.getType(),
                        INCORRECT_ADDRESS));

        verify(checkService, times(1)).checkAddress(anyString());
        verify(repository, times(0)).findAll();
        verify(checkService, times(0)).checkShelterAlreadyAdded(anyCollection(), any(Shelter.class));
        verify(repository, times(0)).save(any(Shelter.class));
    }

    @Test
    public void add_ShelterAlreadyAddedException() {
        when(checkService.checkAddress(anyString()))
                .thenReturn(true);

        getAll_success();

        when(checkService.checkShelterAlreadyAdded(anyCollection(), any(Shelter.class)))
                .thenThrow(ShelterAlreadyAddedException.class);

        assertThrows(ShelterAlreadyAddedException.class,
                () -> shelterService.add(
                        DOG_SHELTER.getType(),
                        DOG_SHELTER.getAddress()));

        verify(checkService, times(1)).checkAddress(anyString());
        verify(repository, times(2)).findAll();
        verify(checkService, times(1)).checkShelterAlreadyAdded(anyCollection(), any(Shelter.class));
        verify(repository, times(0)).save(any(Shelter.class));
    }

    @Test
    public void getById_success() {
        when(checkService.checkValue(anyLong()))
                .thenReturn(true);

        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(DOG_SHELTER));

        DOG_SHELTER.setId(ID_DOG_SHELTER);

        assertEquals(DOG_SHELTER,
                shelterService.getById(DOG_SHELTER.getId()));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    public void getById_InvalideInputException() {
        when(checkService.checkValue(anyLong()))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> shelterService.getById(0L));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(repository, times(0)).findById(anyLong());
    }

    @Test
    public void getById_ShelterNotFoundException() {
        when(checkService.checkValue(anyLong()))
                .thenReturn(true);

        when(repository.findById(anyLong()))
                .thenThrow(ShelterNotFoundException.class);

        assertThrows(ShelterNotFoundException.class,
                () -> shelterService.getById(ID_CAT_SHELTER));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    public void getAllByParameters_WithOnlyType_success() {
        when(repository.getAllByType(any(PetType.class)))
                .thenReturn(getShelters());

        assertEquals(getShelters(),
                shelterService.getAllByParameters(DOG_SHELTER.getType(), null));

        verify(repository, times(1)).getAllByType(any(PetType.class));
    }

    @Test
    public void getAllByParameters_WithOnlyAddress_success() {
        when(checkService.checkAddress(anyString()))
                .thenReturn(true);

        when(repository.getAllByAddressContainsIgnoreCase(anyString()))
                .thenReturn(getShelters());

        assertEquals(getShelters(),
                shelterService.getAllByParameters(null, DOG_SHELTER.getAddress()));

        verify(checkService, times(1)).checkAddress(anyString());
        verify(repository, times(1)).getAllByAddressContainsIgnoreCase(anyString());
    }

    @Test
    public void getAllByParameters_WithOnlyAddress_InvalideInputException() {
        when(checkService.checkAddress(anyString()))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> shelterService.getAllByParameters(null, INCORRECT_ADDRESS));

        verify(checkService, times(1)).checkAddress(anyString());
        verify(repository, times(0)).getAllByAddressContainsIgnoreCase(anyString());
    }

    @Test
    public void getAllByParameters_WithAllParameters_success() {
        when(checkService.checkAddress(anyString()))
                .thenReturn(true);

        when(repository.getAllByTypeAndAddressContainsIgnoreCase(any(PetType.class), anyString()))
                .thenReturn(getShelters());

        assertEquals(getShelters(),
                shelterService.getAllByParameters(DOG_SHELTER.getType(), DOG_SHELTER.getAddress()));

        verify(checkService, times(1)).checkAddress(anyString());
        verify(repository, times(1)).getAllByTypeAndAddressContainsIgnoreCase(any(PetType.class), anyString());
    }

    @Test
    public void getAllByParameters_WithAllParameters_InvalideInputException() {
        when(checkService.checkAddress(anyString()))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> shelterService.getAllByParameters(DOG_SHELTER.getType(), INCORRECT_ADDRESS));

        verify(checkService, times(1)).checkAddress(anyString());
        verify(repository, times(0)).getAllByTypeAndAddressContainsIgnoreCase(any(PetType.class), anyString());
    }

    @Test
    public void getAll_success() {
        when(repository.findAll())
                .thenReturn(getShelters());

        assertEquals(getShelters(),
                shelterService.getAll());

        verify(repository, times(1)).findAll();
    }

    @Test
    public void edit_WithOnlyType_success() {
        getById_success();

        DOG_SHELTER.setId(ID_DOG_SHELTER);
        DOG_SHELTER.setType(CAT_SHELTER.getType());

        when(repository.save(any(Shelter.class)))
                .thenReturn(DOG_SHELTER);

        assertEquals(DOG_SHELTER,
                shelterService.edit(DOG_SHELTER.getId(), CAT_SHELTER.getType(), null));

        verify(checkService, times(2)).checkValue(anyLong());
        verify(repository, times(2)).findById(anyLong());
        verify(checkService, times(0)).checkAddress(anyString());
        verify(checkService, times(0)).checkShelterAlreadyAdded(anyCollection(), any(Shelter.class));
        verify(repository, times(1)).save(any(Shelter.class));
    }

    @Test
    public void edit_WithOnlyAddress_success() {
        getById_success();

        when(checkService.checkAddress(anyString()))
                .thenReturn(true);

        DOG_SHELTER.setId(ID_DOG_SHELTER);
        DOG_SHELTER.setAddress(CAT_SHELTER.getAddress());

        when(checkService.checkShelterAlreadyAdded(anyCollection(), any(Shelter.class)))
                .thenReturn(true);

        when(repository.save(any(Shelter.class)))
                .thenReturn(DOG_SHELTER);

        assertEquals(DOG_SHELTER,
                shelterService.edit(DOG_SHELTER.getId(), null, CAT_SHELTER.getAddress()));

        verify(checkService, times(2)).checkValue(anyLong());
        verify(repository, times(2)).findById(anyLong());
        verify(checkService, times(1)).checkAddress(anyString());
        verify(checkService, times(1)).checkShelterAlreadyAdded(anyCollection(), any(Shelter.class));
        verify(repository, times(1)).save(any(Shelter.class));
    }

    @Test
    public void edit_WithOnlyAddress_InvalideInputException() {
        getById_success();

        when(checkService.checkAddress(anyString()))
                .thenThrow(InvalideInputException.class);

        DOG_SHELTER.setId(ID_DOG_SHELTER);

        assertThrows(InvalideInputException.class,
                () -> shelterService.edit(DOG_SHELTER.getId(), null, INCORRECT_ADDRESS));

        verify(checkService, times(2)).checkValue(anyLong());
        verify(repository, times(2)).findById(anyLong());
        verify(checkService, times(1)).checkAddress(anyString());
        verify(checkService, times(0)).checkShelterAlreadyAdded(anyCollection(), any(Shelter.class));
        verify(repository, times(0)).save(any(Shelter.class));
    }

    @Test
    public void delete_success() {
        getById_success();

        assertEquals(DOG_SHELTER,
                shelterService.delete(DOG_SHELTER.getId()));

        verify(checkService, times(2)).checkValue(anyLong());
        verify(repository, times(2)).findById(anyLong());
        verify(repository, times(1)).delete(any(Shelter.class));
    }
}