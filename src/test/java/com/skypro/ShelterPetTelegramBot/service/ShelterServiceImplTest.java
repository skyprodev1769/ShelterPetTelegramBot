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

import static com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType.CAT;
import static com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType.DOG;
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

    private void getShelter() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(DOG_SHELTER));
    }

    @Test
    public void add_success() {
        when(repository.save(any(Shelter.class)))
                .thenReturn(CAT_SHELTER);

        assertEquals(CAT_SHELTER,
                shelterService.add(CAT, ADDRESS_CAT_SHELTER));

        verify(checkService, times(1)).checkAddress(anyString());
        verify(repository, times(1)).findAll();
        verify(checkService, times(1)).checkShelterAlreadyAdded(anyCollection(), any(Shelter.class));
        verify(repository, times(1)).save(any(Shelter.class));
    }

    @Test
    public void add_InvalideInputException() {
        when(checkService.checkAddress(anyString()))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> shelterService.add(CAT, INCORRECT_ADDRESS));

        verify(checkService, times(1)).checkAddress(anyString());
        verify(repository, times(0)).findAll();
        verify(checkService, times(0)).checkShelterAlreadyAdded(anyCollection(), any(Shelter.class));
        verify(repository, times(0)).save(any(Shelter.class));
    }

    @Test
    public void add_ShelterAlreadyAddedException() {
        when(checkService.checkShelterAlreadyAdded(anyCollection(), any(Shelter.class)))
                .thenThrow(ShelterAlreadyAddedException.class);

        assertThrows(ShelterAlreadyAddedException.class,
                () -> shelterService.add(DOG, ADDRESS_DOG_SHELTER));

        verify(checkService, times(1)).checkAddress(anyString());
        verify(repository, times(1)).findAll();
        verify(checkService, times(1)).checkShelterAlreadyAdded(anyCollection(), any(Shelter.class));
        verify(repository, times(0)).save(any(Shelter.class));
    }

    @Test
    public void getById_success() {
        getShelter();

        assertEquals(DOG_SHELTER,
                shelterService.getById(ID_DOG_SHELTER));

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
        when(repository.findById(anyLong()))
                .thenThrow(ShelterNotFoundException.class);

        assertThrows(ShelterNotFoundException.class,
                () -> shelterService.getById(ID_CAT_SHELTER));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    public void getAllByParameters_success() {
        when(repository.getAllByType(any(PetType.class)))
                .thenReturn(getShelters());

        when(repository.getAllByAddressContainsIgnoreCase(anyString()))
                .thenReturn(getShelters());

        when(repository.getAllByTypeAndAddressContainsIgnoreCase(any(PetType.class), anyString()))
                .thenReturn(getShelters());

        assertEquals(getShelters(),
                shelterService.getAllByParameters(DOG, null));

        assertEquals(getShelters(),
                shelterService.getAllByParameters(null, ADDRESS_DOG_SHELTER));

        assertEquals(getShelters(),
                shelterService.getAllByParameters(DOG, ADDRESS_DOG_SHELTER));

        verify(checkService, times(2)).checkAddress(anyString());
        verify(repository, times(1)).getAllByType(any(PetType.class));
        verify(repository, times(1)).getAllByAddressContainsIgnoreCase(anyString());
        verify(repository, times(1)).getAllByTypeAndAddressContainsIgnoreCase(any(PetType.class), anyString());
    }

    @Test
    public void getAllByParameters_InvalideInputException() {
        when(checkService.checkAddress(anyString()))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> shelterService.getAllByParameters(null, INCORRECT_ADDRESS));

        assertThrows(InvalideInputException.class,
                () -> shelterService.getAllByParameters(DOG, INCORRECT_ADDRESS));

        verify(checkService, times(2)).checkAddress(anyString());
        verify(repository, times(0)).getAllByType(any(PetType.class));
        verify(repository, times(0)).getAllByAddressContainsIgnoreCase(anyString());
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
    public void edit_success() {
        getShelter();

        when(repository.save(any(Shelter.class)))
                .thenReturn(DOG_SHELTER);

        assertEquals(DOG_SHELTER,
                shelterService.edit(ID_DOG_SHELTER, CAT, ADDRESS_CAT_SHELTER));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(repository, times(1)).findById(anyLong());
        verify(checkService, times(1)).checkAddress(anyString());
        verify(checkService, times(1)).checkShelterAlreadyAdded(anyCollection(), any(Shelter.class));
        verify(repository, times(1)).save(any(Shelter.class));
    }

    @Test
    public void edit_InvalideInputException() {
        getShelter();

        when(checkService.checkAddress(anyString()))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> shelterService.edit(ID_DOG_SHELTER, null, INCORRECT_ADDRESS));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(repository, times(1)).findById(anyLong());
        verify(checkService, times(1)).checkAddress(anyString());
        verify(checkService, times(0)).checkShelterAlreadyAdded(anyCollection(), any(Shelter.class));
        verify(repository, times(0)).save(any(Shelter.class));
    }

    @Test
    public void edit_ShelterAlreadyAddedException() {
        getShelter();

        when(checkService.checkShelterAlreadyAdded(anyCollection(), any(Shelter.class)))
                .thenThrow(ShelterAlreadyAddedException.class);

        assertThrows(ShelterAlreadyAddedException.class,
                () -> shelterService.edit(ID_DOG_SHELTER, null, ADDRESS_DOG_SHELTER));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(repository, times(1)).findById(anyLong());
        verify(checkService, times(1)).checkAddress(anyString());
        verify(checkService, times(1)).checkShelterAlreadyAdded(anyCollection(), any(Shelter.class));
        verify(repository, times(0)).save(any(Shelter.class));
    }

    @Test
    public void delete_success() {
        getShelter();

        assertEquals(DOG_SHELTER,
                shelterService.delete(ID_DOG_SHELTER));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any(Shelter.class));
    }
}