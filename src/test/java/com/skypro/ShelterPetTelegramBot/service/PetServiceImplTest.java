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

import static com.skypro.ShelterPetTelegramBot.model.entity.enums.PetStatus.FREE;
import static com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType.DOG;
import static com.skypro.ShelterPetTelegramBot.utils.UtilsForPetService.*;
import static com.skypro.ShelterPetTelegramBot.utils.UtilsForShelterService.ID_SHELTER;
import static com.skypro.ShelterPetTelegramBot.utils.UtilsForShelterService.SHELTER;
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

    @Test
    public void add_success() {
        getShelter();
        savePet();

        assertEquals(PET_DOG,
                petService.add(
                        DOG,
                        FREE,
                        NAME,
                        ID_SHELTER));

        verify(checkService, times(1)).checkName(anyString());
        verify(shelterService, times(1)).getById(anyLong());
        verify(checkService, times(1)).checkTypes(any(PetType.class), any(PetType.class));
        verify(repository, times(1)).findAll();
        verify(checkService, times(1)).checkPetAlreadyAdded(anyCollection(), any(Pet.class));
        verify(repository, times(1)).save(any(Pet.class));
    }

    @Test
    public void add_InvalideInputException() {
        checkNameException();

        assertThrows(InvalideInputException.class,
                () -> petService.add(
                        DOG,
                        FREE,
                        INCORRECT_NAME,
                        ID_SHELTER));

        verify(checkService, times(1)).checkName(anyString());
        verify(shelterService, times(0)).getById(anyLong());
        verify(checkService, times(0)).checkTypes(any(PetType.class), any(PetType.class));
        verify(repository, times(0)).findAll();
        verify(checkService, times(0)).checkPetAlreadyAdded(anyCollection(), any(Pet.class));
        verify(repository, times(0)).save(any(Pet.class));
    }

    @Test
    public void add_DifferentTypesException() {
        getShelter();
        checkTypesException();

        assertThrows(DifferentTypesException.class,
                () -> petService.add(
                        DOG,
                        FREE,
                        NAME,
                        ID_SHELTER));

        verify(checkService, times(1)).checkName(anyString());
        verify(shelterService, times(1)).getById(anyLong());
        verify(checkService, times(1)).checkTypes(any(PetType.class), any(PetType.class));
        verify(repository, times(0)).findAll();
        verify(checkService, times(0)).checkPetAlreadyAdded(anyCollection(), any(Pet.class));
        verify(repository, times(0)).save(any(Pet.class));
    }

    @Test
    public void add_PetAlreadyAddedException() {
        getShelter();
        checkAddedException();

        assertThrows(PetAlreadyAddedException.class,
                () -> petService.add(
                        DOG,
                        FREE,
                        NAME,
                        ID_SHELTER));

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

        assertEquals(PET_DOG,
                petService.getById(ID_PET));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    public void getById_InvalideInputException() {
        checkValueException();

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
                () -> petService.getById(ID_PET));

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
                        DOG,
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
                        NAME,
                        null));

        assertEquals(getPets(),
                petService.getAllByParameters(
                        null,
                        null,
                        null,
                        ID_SHELTER));

        assertEquals(getPets(),
                petService.getAllByParameters(
                        DOG,
                        FREE,
                        null,
                        null));

        assertEquals(getPets(),
                petService.getAllByParameters(
                        DOG,
                        null,
                        NAME,
                        null));

        assertEquals(getPets(),
                petService.getAllByParameters(
                        DOG,
                        null,
                        null,
                        ID_SHELTER));

        assertEquals(getPets(),
                petService.getAllByParameters(
                        null,
                        FREE,
                        NAME,
                        null));

        assertEquals(getPets(),
                petService.getAllByParameters(
                        null,
                        FREE,
                        null,
                        ID_SHELTER));

        assertEquals(getPets(),
                petService.getAllByParameters(
                        null,
                        null,
                        NAME,
                        ID_SHELTER));

        assertEquals(getPets(),
                petService.getAllByParameters(
                        DOG,
                        FREE,
                        NAME,
                        null));

        assertEquals(getPets(),
                petService.getAllByParameters(
                        DOG,
                        FREE,
                        null,
                        ID_SHELTER));

        assertEquals(getPets(),
                petService.getAllByParameters(
                        DOG,
                        null,
                        NAME,
                        ID_SHELTER));

        assertEquals(getPets(),
                petService.getAllByParameters(
                        null,
                        FREE,
                        NAME,
                        ID_SHELTER));

        assertEquals(getPets(),
                petService.getAllByParameters(
                        DOG,
                        FREE,
                        NAME,
                        ID_SHELTER));

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
        checkNameException();
        checkValueException();

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
        getShelter();
        savePet();

        assertEquals(PET_DOG,
                petService.edit(
                        ID_PET,
                        DOG,
                        FREE,
                        NAME,
                        ID_SHELTER));

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
        checkNameException();

        assertThrows(InvalideInputException.class,
                () -> petService.edit(
                        ID_PET,
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
        getShelter();
        checkTypesException();

        assertThrows(DifferentTypesException.class,
                () -> petService.edit(
                        ID_PET,
                        DOG,
                        null,
                        null,
                        ID_SHELTER));

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
        checkAddedException();

        assertThrows(PetAlreadyAddedException.class,
                () -> petService.edit(
                        ID_PET,
                        null,
                        null,
                        NAME,
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

        assertEquals(PET_DOG,
                petService.delete(ID_PET));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any(Pet.class));
    }

    private void getPet() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(PET_DOG));
    }

    private void getShelter() {
        when(shelterService.getById(anyLong()))
                .thenReturn(SHELTER);
    }

    private void savePet() {
        when(repository.save(any(Pet.class)))
                .thenReturn(PET_DOG);
    }

    private void checkNameException() {
        when(checkService.checkName(anyString()))
                .thenThrow(InvalideInputException.class);
    }

    private void checkValueException() {
        when(checkService.checkValue(anyLong()))
                .thenThrow(InvalideInputException.class);
    }

    private void checkTypesException() {
        when(checkService.checkTypes(any(PetType.class), any(PetType.class)))
                .thenThrow(DifferentTypesException.class);
    }

    private void checkAddedException() {
        when(checkService.checkPetAlreadyAdded(anyCollection(), any(Pet.class)))
                .thenThrow(PetAlreadyAddedException.class);
    }
}