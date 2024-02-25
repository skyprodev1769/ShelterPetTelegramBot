package com.skypro.ShelterPetTelegramBot.controller;

import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Volunteer;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.VolunteerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Класс {@link VolunteerController}
 * является контроллером для обработки запросов, связанных с животным
 */
@RestController
@RequestMapping("/volunteer")
public class VolunteerController {

    @Autowired
    private VolunteerService service;

    @PostMapping
    public Volunteer add(@RequestParam String firstName,
                         @RequestParam String lastName,
                         @RequestParam String phoneNumber,
                         @RequestParam Long shelterId) {
        return service.add(firstName, lastName, phoneNumber, shelterId);
    }

    @GetMapping("{id}")
    public Volunteer get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping("/all")
    public Collection<Volunteer> getAll() {
        return service.getAll();
    }

    @PutMapping("{id}")
    public Volunteer edit(@PathVariable Long id,
                          @RequestParam String firstName,
                          @RequestParam String lastName,
                          @RequestParam String phoneNumber,
                          @RequestParam Long shelterId) {
        return service.edit(id, firstName, lastName, phoneNumber, shelterId);
    }

    @DeleteMapping("{id}")
    public Volunteer delete(@PathVariable Long id) {
        return service.delete(id);
    }
}