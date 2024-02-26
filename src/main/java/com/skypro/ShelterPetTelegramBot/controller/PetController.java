package com.skypro.ShelterPetTelegramBot.controller;

import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Pet;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Collection;

/**
 * Класс {@link PetController}
 * является контроллером для обработки запросов, связанных с животным
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    private PetService service;

    @ExceptionHandler
    public String handleException(HttpStatusCodeException e) {
        return String.format("Code: %s. Error: %s", e.getStatusCode(), e.getStatusText());
    }

    @PostMapping
    public Pet add(@RequestParam PetType type,
                   @RequestParam String name,
                   @RequestParam Long shelterId) {

        return service.add(type, name, shelterId);
    }

    @GetMapping("{id}")
    public Pet getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public Collection<Pet> getAllByParameters(@RequestParam(required = false) String name,
                                              @RequestParam(required = false) PetType type,
                                              @RequestParam(required = false) Long shelterId) {

        return service.getAllByParameters(name, type, shelterId);
    }

    @GetMapping("/all")
    public Collection<Pet> getAll() {
        return service.getAll();
    }

    @PutMapping("{id}")
    public Pet edit(@PathVariable Long id,
                    @RequestParam(required = false) PetType type,
                    @RequestParam(required = false) String name,
                    @RequestParam(required = false) Long shelterId) {

        return service.edit(id, type, name, shelterId);
    }

    @DeleteMapping("{id}")
    public Pet delete(@PathVariable Long id) {
        return service.delete(id);
    }
}