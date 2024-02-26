package com.skypro.ShelterPetTelegramBot.controller;

import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Volunteer;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.VolunteerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;

import java.util.Collection;

/**
 * Класс {@link VolunteerController}
 * является контроллером для обработки запросов, связанных с волонтером
 */
@RestController
@RequestMapping("/volunteer")
public class VolunteerController {

    @Autowired
    private VolunteerService service;

    @ExceptionHandler
    public String handleException(HttpStatusCodeException e) {
        return String.format("Code: %s. Error: %s", e.getStatusCode(), e.getStatusText());
    }

    @PostMapping
    public Volunteer add(@RequestParam String firstName,
                         @RequestParam String lastName,
                         @RequestParam String phoneNumber,
                         @RequestParam Long shelterId) {

        return service.add(firstName, lastName, phoneNumber, shelterId);
    }

    @GetMapping("{id}")
    public Volunteer getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public Collection<Volunteer> getAllByParameters(@RequestParam(required = false) String firstName,
                                                    @RequestParam(required = false) String lastName,
                                                    @RequestParam(required = false) String phoneNumber,
                                                    @RequestParam(required = false) Long shelterId) {

        return service.getAllByParameters(firstName, lastName, phoneNumber, shelterId);
    }

    @GetMapping("/all")
    public Collection<Volunteer> getAll() {
        return service.getAll();
    }

    @PutMapping("{id}")
    public Volunteer edit(@PathVariable Long id,
                          @RequestParam(required = false) String firstName,
                          @RequestParam(required = false) String lastName,
                          @RequestParam(required = false) String phoneNumber,
                          @RequestParam(required = false) Long shelterId) {

        return service.edit(id, firstName, lastName, phoneNumber, shelterId);
    }

    @DeleteMapping("{id}")
    public Volunteer delete(@PathVariable Long id) {
        return service.delete(id);
    }
}