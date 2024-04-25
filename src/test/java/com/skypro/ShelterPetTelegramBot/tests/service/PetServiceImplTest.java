package com.skypro.ShelterPetTelegramBot.tests.service;

import com.skypro.ShelterPetTelegramBot.exception.pet.PetNotFoundException;
import com.skypro.ShelterPetTelegramBot.model.enums.PetStatus;
import com.skypro.ShelterPetTelegramBot.model.enums.PetType;
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

import static com.skypro.ShelterPetTelegramBot.model.enums.PetStatus.FREE;
import static com.skypro.ShelterPetTelegramBot.model.enums.PetType.DOG;
import static com.skypro.ShelterPetTelegramBot.tests.Utils.*;
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
    void add() {
        getShelter();
        savePet();

        assertEquals(PET,
                petService.add(
                        DOG,
                        FREE,
                        NAME,
                        ID));

        verify(checkService, times(1)).checkName(anyString());
        verify(shelterService, times(1)).getById(anyLong());
        verify(checkService, times(1)).checkTypes(any(PetType.class), any(PetType.class));
        verify(repository, times(1)).findAll();
        verify(checkService, times(1)).checkPetAlreadyAdded(anyCollection(), any(Pet.class));
        verify(repository, times(1)).save(any(Pet.class));
    }

    @Test
    void getById() {
        getPet();

        assertEquals(PET,
                petService.getById(ID));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void getById_PetNotFoundException() {
        when(repository.findById(anyLong()))
                .thenThrow(PetNotFoundException.class);

        assertThrows(PetNotFoundException.class,
                () -> petService.getById(ID));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(repository, times(1)).findById(anyLong());
    }

    @Test
    void getAllByParameters() {
        when(repository.getAllByType(any(PetType.class)))
                .thenReturn(PETS);

        when(repository.getAllByStatus(any(PetStatus.class)))
                .thenReturn(PETS);

        when(repository.getAllByNameContainsIgnoreCase(anyString()))
                .thenReturn(PETS);

        when(repository.getAllByShelterId(anyLong()))
                .thenReturn(PETS);

        when(repository.getAllByTypeAndStatus(any(PetType.class), any(PetStatus.class)))
                .thenReturn(PETS);

        when(repository.getAllByTypeAndNameContainsIgnoreCase(any(PetType.class), anyString()))
                .thenReturn(PETS);

        when(repository.getAllByTypeAndShelterId(any(PetType.class), anyLong()))
                .thenReturn(PETS);

        when(repository.getAllByStatusAndNameContainsIgnoreCase(any(PetStatus.class), anyString()))
                .thenReturn(PETS);

        when(repository.getAllByStatusAndShelterId(any(PetStatus.class), anyLong()))
                .thenReturn(PETS);

        when(repository.getAllByNameContainsIgnoreCaseAndShelterId(anyString(), anyLong()))
                .thenReturn(PETS);

        when(repository.getAllByTypeAndStatusAndNameContainsIgnoreCase(any(PetType.class), any(PetStatus.class), anyString()))
                .thenReturn(PETS);

        when(repository.getAllByTypeAndStatusAndShelterId(any(PetType.class), any(PetStatus.class), anyLong()))
                .thenReturn(PETS);

        when(repository.getAllByTypeAndNameContainsIgnoreCaseAndShelterId(any(PetType.class), anyString(), anyLong()))
                .thenReturn(PETS);

        when(repository.getAllByStatusAndNameContainsIgnoreCaseAndShelterId(any(PetStatus.class), anyString(), anyLong()))
                .thenReturn(PETS);

        when(repository.getAllByTypeAndStatusAndNameContainsIgnoreCaseAndShelterId(any(PetType.class), any(PetStatus.class), anyString(), anyLong()))
                .thenReturn(PETS);

        assertEquals(PETS,
                petService.getAllByParameters(
                        DOG,
                        null,
                        null,
                        null));

        assertEquals(PETS,
                petService.getAllByParameters(
                        null,
                        FREE,
                        null,
                        null));

        assertEquals(PETS,
                petService.getAllByParameters(
                        null,
                        null,
                        NAME,
                        null));

        assertEquals(PETS,
                petService.getAllByParameters(
                        null,
                        null,
                        null,
                        ID));

        assertEquals(PETS,
                petService.getAllByParameters(
                        DOG,
                        FREE,
                        null,
                        null));

        assertEquals(PETS,
                petService.getAllByParameters(
                        DOG,
                        null,
                        NAME,
                        null));

        assertEquals(PETS,
                petService.getAllByParameters(
                        DOG,
                        null,
                        null,
                        ID));

        assertEquals(PETS,
                petService.getAllByParameters(
                        null,
                        FREE,
                        NAME,
                        null));

        assertEquals(PETS,
                petService.getAllByParameters(
                        null,
                        FREE,
                        null,
                        ID));

        assertEquals(PETS,
                petService.getAllByParameters(
                        null,
                        null,
                        NAME,
                        ID));

        assertEquals(PETS,
                petService.getAllByParameters(
                        DOG,
                        FREE,
                        NAME,
                        null));

        assertEquals(PETS,
                petService.getAllByParameters(
                        DOG,
                        FREE,
                        null,
                        ID));

        assertEquals(PETS,
                petService.getAllByParameters(
                        DOG,
                        null,
                        NAME,
                        ID));

        assertEquals(PETS,
                petService.getAllByParameters(
                        null,
                        FREE,
                        NAME,
                        ID));

        assertEquals(PETS,
                petService.getAllByParameters(
                        DOG,
                        FREE,
                        NAME,
                        ID));

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
    void getAll() {
        when(repository.findAll())
                .thenReturn(PETS);

        assertEquals(PETS,
                petService.getAll());

        verify(repository, times(1)).findAll();
    }

    @Test
    void edit() {
        getPet();
        getShelter();
        savePet();

        assertEquals(PET,
                petService.edit(
                        ID,
                        DOG,
                        FREE,
                        NAME,
                        ID));

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
    void delete() {
        getPet();

        assertEquals(PET,
                petService.delete(ID));

        verify(checkService, times(1)).checkValue(anyLong());
        verify(repository, times(1)).findById(anyLong());
        verify(repository, times(1)).delete(any(Pet.class));
    }

    private void getPet() {
        when(repository.findById(anyLong()))
                .thenReturn(Optional.of(PET));
    }

    private void getShelter() {
        when(shelterService.getById(anyLong()))
                .thenReturn(SHELTER);
    }

    private void savePet() {
        when(repository.save(any(Pet.class)))
                .thenReturn(PET);
    }
}