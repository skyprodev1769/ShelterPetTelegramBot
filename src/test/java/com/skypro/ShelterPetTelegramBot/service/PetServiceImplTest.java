package com.skypro.ShelterPetTelegramBot.service;

import com.skypro.ShelterPetTelegramBot.exception.DifferentTypesException;
import com.skypro.ShelterPetTelegramBot.exception.InvalideInputException;
import com.skypro.ShelterPetTelegramBot.exception.pet.PetAlreadyAddedException;
import com.skypro.ShelterPetTelegramBot.exception.pet.PetNotFoundException;
import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetStatus;
import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Pet;
import com.skypro.ShelterPetTelegramBot.model.repository.PetRepository;
import com.skypro.ShelterPetTelegramBot.service.impl.entity_service.PetServiceImpl;
import com.skypro.ShelterPetTelegramBot.service.interfaces.CheckService;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.ShelterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.skypro.ShelterPetTelegramBot.model.entity.enums.PetStatus.ADOPTED;
import static com.skypro.ShelterPetTelegramBot.model.entity.enums.PetStatus.FREE;
import static com.skypro.ShelterPetTelegramBot.utils.UtilsForPetService.*;
import static com.skypro.ShelterPetTelegramBot.utils.UtilsForShelterService.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetServiceImplTest {

    @Mock
    private PetRepository repository;
    @Mock
    private CheckService checkService;
    @Mock
    private ShelterService shelterService;
    @InjectMocks
    private PetServiceImpl petService;

    @BeforeEach
    void setUp() {
        petService = new PetServiceImpl(repository, checkService, shelterService);
    }

    private void getPet() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(DOG));
    }

    @Test
    public void add_success() {
        when(shelterService.getById(anyLong()))
                .thenReturn(CAT_SHELTER);

        when(repository.save(any(Pet.class)))
                .thenReturn(CAT);

        assertEquals(CAT,
                petService.add(
                        PetType.CAT,
                        FREE,
                        NAME_CAT,
                        ID_CAT_SHELTER));

        verify(checkService, times(1)).checkName(anyString());
        verify(shelterService, times(1)).getById(anyLong());
        verify(checkService, times(1)).checkTypes(any(PetType.class), any(PetType.class));
        verify(repository, times(1)).findAll();
        verify(checkService, times(1)).checkPetAlreadyAdded(anyCollection(), any(Pet.class));
        verify(repository, times(1)).save(any(Pet.class));
    }

    @Test
    public void add_InvalideInputException() {
        when(checkService.checkName(anyString()))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> petService.add(
                        PetType.CAT,
                        FREE,
                        INCORRECT_NAME,
                        ID_CAT_SHELTER));

        verify(checkService, times(1)).checkName(anyString());
        verify(shelterService, times(0)).getById(anyLong());
        verify(checkService, times(0)).checkTypes(any(PetType.class), any(PetType.class));
        verify(repository, times(0)).findAll();
        verify(checkService, times(0)).checkPetAlreadyAdded(anyCollection(), any(Pet.class));
        verify(repository, times(0)).save(any(Pet.class));
    }

    @Test
    public void add_DifferentTypesException() {
        when(shelterService.getById(anyLong()))
                .thenReturn(DOG_SHELTER);

        when(checkService.checkTypes(any(PetType.class), any(PetType.class)))
                .thenThrow(DifferentTypesException.class);

        assertThrows(DifferentTypesException.class,
                () -> petService.add(
                        PetType.CAT,
                        FREE,
                        NAME_CAT,
                        ID_DOG_SHELTER));

        verify(checkService, times(1)).checkName(anyString());
        verify(shelterService, times(1)).getById(anyLong());
        verify(checkService, times(1)).checkTypes(any(PetType.class), any(PetType.class));
        verify(repository, times(0)).findAll();
        verify(checkService, times(0)).checkPetAlreadyAdded(anyCollection(), any(Pet.class));
        verify(repository, times(0)).save(any(Pet.class));
    }

    @Test
    public void add_PetAlreadyAddedException() {
        when(shelterService.getById(anyLong()))
                .thenReturn(CAT_SHELTER);

        when(checkService.checkPetAlreadyAdded(anyCollection(), any(Pet.class)))
                .thenThrow(PetAlreadyAddedException.class);

        assertThrows(PetAlreadyAddedException.class,
                () -> petService.add(
                        PetType.DOG,
                        FREE,
                        NAME_DOG,
                        ID_DOG_SHELTER));

        verify(checkService, times(1)).checkName(anyString());
        verify(shelterService, times(1)).getById(anyLong());
        verify(checkService, times(1)).checkTypes(any(PetType.class), any(PetType.class));
        verify(repository, times(1)).findAll();
        verify(checkService, times(1)).checkPetAlreadyAdded(anyCollection(), any(Pet.class));
        verify(repository, times(0)).save(any(Pet.class));
    }

    @Test
    public void getById_success() {
        getPet();

        assertEquals(DOG,
                petService.getById(ID_DOG));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    public void getById_InvalideInputException() {
        when(checkService.checkValue(anyLong()))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> petService.getById(0L));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(repository, times(0)).findById(anyLong());
    }

    @Test
    public void getById_PetNotFoundException() {
        when(repository.findById(anyLong()))
                .thenThrow(PetNotFoundException.class);

        assertThrows(PetNotFoundException.class,
                () -> petService.getById(ID_CAT));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    public void getAllByParameters_success() {
        when(repository.getAllByType(any(PetType.class)))
                .thenReturn(getPets());

        when(repository.getAllByStatus(any(PetStatus.class)))
                .thenReturn(getPets());

        when(repository.getAllByNameContainsIgnoreCase(anyString()))
                .thenReturn(getPets());

        when(repository.getAllByShelterId(anyLong()))
                .thenReturn(getPets());

        when(repository.getAllByTypeAndStatus(any(PetType.class), any(PetStatus.class)))
                .thenReturn(getPets());

        when(repository.getAllByTypeAndNameContainsIgnoreCase(any(PetType.class), anyString()))
                .thenReturn(getPets());

        when(repository.getAllByTypeAndShelterId(any(PetType.class), anyLong()))
                .thenReturn(getPets());

        when(repository.getAllByStatusAndNameContainsIgnoreCase(any(PetStatus.class), anyString()))
                .thenReturn(getPets());

        when(repository.getAllByStatusAndShelterId(any(PetStatus.class), anyLong()))
                .thenReturn(getPets());

        when(repository.getAllByNameContainsIgnoreCaseAndShelterId(anyString(), anyLong()))
                .thenReturn(getPets());

        when(repository.getAllByTypeAndStatusAndNameContainsIgnoreCase(any(PetType.class), any(PetStatus.class), anyString()))
                .thenReturn(getPets());

        when(repository.getAllByTypeAndStatusAndShelterId(any(PetType.class), any(PetStatus.class), anyLong()))
                .thenReturn(getPets());

        when(repository.getAllByTypeAndNameContainsIgnoreCaseAndShelterId(any(PetType.class), anyString(), anyLong()))
                .thenReturn(getPets());

        when(repository.getAllByStatusAndNameContainsIgnoreCaseAndShelterId(any(PetStatus.class), anyString(), anyLong()))
                .thenReturn(getPets());

        when(repository.getAllByTypeAndStatusAndNameContainsIgnoreCaseAndShelterId(any(PetType.class), any(PetStatus.class), anyString(), anyLong()))
                .thenReturn(getPets());

        assertEquals(getPets(),
                petService.getAllByParameters(
                        PetType.CAT,
                        null,
                        null,
                        null));

        assertEquals(getPets(),
                petService.getAllByParameters(
                        null,
                        FREE,
                        null,
                        null));

        assertEquals(getPets(),
                petService.getAllByParameters(
                        null,
                        null,
                        NAME_CAT,
                        null));

        assertEquals(getPets(),
                petService.getAllByParameters(
                        null,
                        null,
                        null,
                        ID_CAT_SHELTER));

        assertEquals(getPets(),
                petService.getAllByParameters(
                        PetType.CAT,
                        FREE,
                        null,
                        null));

        assertEquals(getPets(),
                petService.getAllByParameters(
                        PetType.CAT,
                        null,
                        NAME_CAT,
                        null));

        assertEquals(getPets(),
                petService.getAllByParameters(
                        PetType.CAT,
                        null,
                        null,
                        ID_CAT_SHELTER));

        assertEquals(getPets(),
                petService.getAllByParameters(
                        null,
                        FREE,
                        NAME_CAT,
                        null));

        assertEquals(getPets(),
                petService.getAllByParameters(
                        null,
                        FREE,
                        null,
                        ID_CAT_SHELTER));

        assertEquals(getPets(),
                petService.getAllByParameters(
                        null,
                        null,
                        NAME_CAT,
                        ID_CAT_SHELTER));

        assertEquals(getPets(),
                petService.getAllByParameters(
                        PetType.CAT,
                        FREE,
                        NAME_CAT,
                        null));

        assertEquals(getPets(),
                petService.getAllByParameters(
                        PetType.CAT,
                        FREE,
                        null,
                        ID_CAT_SHELTER));

        assertEquals(getPets(),
                petService.getAllByParameters(
                        PetType.CAT,
                        null,
                        NAME_CAT,
                        ID_CAT_SHELTER));

        assertEquals(getPets(),
                petService.getAllByParameters(
                        null,
                        FREE,
                        NAME_CAT,
                        ID_CAT_SHELTER));

        assertEquals(getPets(),
                petService.getAllByParameters(
                        PetType.CAT,
                        FREE,
                        NAME_CAT,
                        ID_CAT_SHELTER));

        verify(checkService, times(8)).checkName(anyString());
        verify(checkService, times(8)).checkValue(anyLong());
        verify(repository, times(1)).getAllByType(any(PetType.class));
        verify(repository, times(1)).getAllByStatus(any(PetStatus.class));
        verify(repository, times(1)).getAllByNameContainsIgnoreCase(anyString());
        verify(repository, times(1)).getAllByShelterId(anyLong());
        verify(repository, times(1)).getAllByTypeAndStatus(any(PetType.class), any(PetStatus.class));
        verify(repository, times(1)).getAllByTypeAndNameContainsIgnoreCase(any(PetType.class), anyString());
        verify(repository, times(1)).getAllByTypeAndShelterId(any(PetType.class), anyLong());
        verify(repository, times(1)).getAllByStatusAndNameContainsIgnoreCase(any(PetStatus.class), anyString());
        verify(repository, times(1)).getAllByStatusAndShelterId(any(PetStatus.class), anyLong());
        verify(repository, times(1)).getAllByNameContainsIgnoreCaseAndShelterId(anyString(), anyLong());
        verify(repository, times(1)).getAllByTypeAndStatusAndNameContainsIgnoreCase(any(PetType.class), any(PetStatus.class), anyString());
        verify(repository, times(1)).getAllByTypeAndStatusAndShelterId(any(PetType.class), any(PetStatus.class), anyLong());
        verify(repository, times(1)).getAllByTypeAndNameContainsIgnoreCaseAndShelterId(any(PetType.class), anyString(), anyLong());
        verify(repository, times(1)).getAllByStatusAndNameContainsIgnoreCaseAndShelterId(any(PetStatus.class), anyString(), anyLong());
        verify(repository, times(1)).getAllByTypeAndStatusAndNameContainsIgnoreCaseAndShelterId(any(PetType.class), any(PetStatus.class), anyString(), anyLong());
    }

    @Test
    public void getAllByParameters_InvalideInputException() {
        when(checkService.checkName(anyString()))
                .thenThrow(InvalideInputException.class);

        when(checkService.checkValue(anyLong()))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> petService.getAllByParameters(
                        null,
                        null,
                        INCORRECT_NAME,
                        null));

        assertThrows(InvalideInputException.class,
                () -> petService.getAllByParameters(
                        null,
                        null,
                        null,
                        0L));

        verify(checkService, times(1)).checkName(anyString());
        verify(checkService, times(1)).checkValue(anyLong());
        verify(repository, times(0)).getAllByType(any(PetType.class));
        verify(repository, times(0)).getAllByStatus(any(PetStatus.class));
        verify(repository, times(0)).getAllByNameContainsIgnoreCase(anyString());
        verify(repository, times(0)).getAllByShelterId(anyLong());
        verify(repository, times(0)).getAllByTypeAndStatus(any(PetType.class), any(PetStatus.class));
        verify(repository, times(0)).getAllByTypeAndNameContainsIgnoreCase(any(PetType.class), anyString());
        verify(repository, times(0)).getAllByTypeAndShelterId(any(PetType.class), anyLong());
        verify(repository, times(0)).getAllByStatusAndNameContainsIgnoreCase(any(PetStatus.class), anyString());
        verify(repository, times(0)).getAllByStatusAndShelterId(any(PetStatus.class), anyLong());
        verify(repository, times(0)).getAllByNameContainsIgnoreCaseAndShelterId(anyString(), anyLong());
        verify(repository, times(0)).getAllByTypeAndStatusAndNameContainsIgnoreCase(any(PetType.class), any(PetStatus.class), anyString());
        verify(repository, times(0)).getAllByTypeAndStatusAndShelterId(any(PetType.class), any(PetStatus.class), anyLong());
        verify(repository, times(0)).getAllByTypeAndNameContainsIgnoreCaseAndShelterId(any(PetType.class), anyString(), anyLong());
        verify(repository, times(0)).getAllByStatusAndNameContainsIgnoreCaseAndShelterId(any(PetStatus.class), anyString(), anyLong());
        verify(repository, times(0)).getAllByTypeAndStatusAndNameContainsIgnoreCaseAndShelterId(any(PetType.class), any(PetStatus.class), anyString(), anyLong());
    }

    @Test
    public void getAll_success() {
        when(repository.findAll())
                .thenReturn(getPets());

        assertEquals(getPets(),
                petService.getAll());

        verify(repository, times(1)).findAll();
    }

    @Test
    public void edit_success() {
        getPet();

        when(shelterService.getById(anyLong()))
                .thenReturn(DOG_SHELTER);

        when(repository.save(any(Pet.class)))
                .thenReturn(DOG);

        assertEquals(DOG,
                petService.edit(
                        ID_DOG,
                        PetType.DOG,
                        ADOPTED,
                        NAME_DOG,
                        ID_DOG_SHELTER));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(repository, times(1)).findById(anyLong());
        verify(checkService, times(1)).checkName(anyString());
        verify(shelterService, times(1)).getById(anyLong());
        verify(checkService, times(1)).checkTypes(any(PetType.class), any(PetType.class));
        verify(repository, times(1)).findAll();
        verify(checkService, times(1)).checkPetAlreadyAdded(anyCollection(), any(Pet.class));
        verify(repository, times(1)).save(any(Pet.class));
    }

    @Test
    public void edit_InvalideInputException() {
        getPet();

        when(checkService.checkName(anyString()))
                .thenThrow(InvalideInputException.class);

        assertThrows(InvalideInputException.class,
                () -> petService.edit(
                        ID_DOG,
                        null,
                        null,
                        INCORRECT_NAME,
                        null));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(repository, times(1)).findById(anyLong());
        verify(checkService, times(1)).checkName(anyString());
        verify(shelterService, times(0)).getById(anyLong());
        verify(checkService, times(0)).checkTypes(any(PetType.class), any(PetType.class));
        verify(repository, times(0)).findAll();
        verify(checkService, times(0)).checkPetAlreadyAdded(anyCollection(), any(Pet.class));
        verify(repository, times(0)).save(any(Pet.class));
    }

    @Test
    public void edit_DifferentTypesException() {
        getPet();

        when(shelterService.getById(anyLong()))
                .thenReturn(DOG_SHELTER);

        when(checkService.checkTypes(any(PetType.class), any(PetType.class)))
                .thenThrow(DifferentTypesException.class);

        assertThrows(DifferentTypesException.class,
                () -> petService.edit(
                        ID_DOG,
                        PetType.CAT,
                        null,
                        null,
                        ID_DOG_SHELTER));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(repository, times(1)).findById(anyLong());
        verify(checkService, times(0)).checkName(anyString());
        verify(shelterService, times(1)).getById(anyLong());
        verify(checkService, times(1)).checkTypes(any(PetType.class), any(PetType.class));
        verify(repository, times(0)).findAll();
        verify(checkService, times(0)).checkPetAlreadyAdded(anyCollection(), any(Pet.class));
        verify(repository, times(0)).save(any(Pet.class));
    }

    @Test
    public void edit_PetAlreadyAddedException() {
        getPet();

        when(checkService.checkPetAlreadyAdded(anyCollection(), any(Pet.class)))
                .thenThrow(PetAlreadyAddedException.class);

        assertThrows(PetAlreadyAddedException.class,
                () -> petService.edit(
                        ID_DOG,
                        null,
                        null,
                        NAME_DOG,
                        null));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(repository, times(1)).findById(anyLong());
        verify(checkService, times(1)).checkName(anyString());
        verify(shelterService, times(0)).getById(anyLong());
        verify(checkService, times(0)).checkTypes(any(PetType.class), any(PetType.class));
        verify(repository, times(1)).findAll();
        verify(checkService, times(1)).checkPetAlreadyAdded(anyCollection(), any(Pet.class));
        verify(repository, times(0)).save(any(Pet.class));
    }

    @Test
    public void delete_success() {
        getPet();

        assertEquals(DOG,
                petService.delete(ID_DOG));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any(Pet.class));
    }
}