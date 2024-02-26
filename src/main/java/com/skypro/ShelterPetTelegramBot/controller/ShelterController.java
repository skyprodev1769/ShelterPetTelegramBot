package com.skypro.ShelterPetTelegramBot.controller;

import com.skypro.ShelterPetTelegramBot.model.entity.enums.PetType;
import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Shelter;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.ShelterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Collection;

/**
 * Класс {@link ShelterController}
 * является контроллером для обработки запросов, связанных с приютом
 */
@RestController
@RequestMapping("/shelter")
public class ShelterController {

    @Autowired
    private ShelterService service;

    @ExceptionHandler
    public String handleException(HttpStatusCodeException e) {
        return String.format("Code: %s. Error: %s", e.getStatusCode(), e.getStatusText());
    }

    @PostMapping
    public Shelter add(@RequestParam PetType type,
                       @RequestParam String address) {

        return service.add(type, address);
    }

    @GetMapping("{id}")
    public Shelter getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public Collection<Shelter> getAllByParameters(@RequestParam(required = false) String address,
                                                  @RequestParam(required = false) PetType type) {

        return service.getAllByParameters(address, type);
    }

    @GetMapping("/all")
    public Collection<Shelter> getAll() {
        return service.getAll();
    }

    @PutMapping("{id}")
    public Shelter edit(@PathVariable Long id,
                        @RequestParam(required = false) PetType type,
                        @RequestParam(required = false) String address) {

        return service.edit(id, type, address);
    }

    @DeleteMapping("{id}")
    public Shelter delete(@PathVariable Long id) {
        return service.delete(id);
    }
}