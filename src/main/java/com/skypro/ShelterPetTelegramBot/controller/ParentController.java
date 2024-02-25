package com.skypro.ShelterPetTelegramBot.controller;

import com.skypro.ShelterPetTelegramBot.model.entity.with_controller.Parent;
import com.skypro.ShelterPetTelegramBot.service.interfaces.entity_service.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Класс {@link ParentController}
 * является контроллером для обработки запросов, связанных с усыновителем
 */
@RestController
@RequestMapping("/parent")
public class ParentController {

    @Autowired
    private ParentService service;

    @PostMapping
    public Parent add(@RequestParam String firstName,
                      @RequestParam String lastName,
                      @RequestParam String phoneNumber,
                      @RequestParam Long volunteerId,
                      @RequestParam Long petId) {
        return service.add(firstName, lastName, phoneNumber, volunteerId, petId);
    }

    @GetMapping("{id}")
    public Parent get(@PathVariable Long id) {
        return service.get(id);
    }

    @GetMapping("/all")
    public Collection<Parent> getAll() {
        return service.getAll();
    }

    @PutMapping("{id}")
    public Parent edit(@PathVariable Long id,
                       @RequestParam String firstName,
                       @RequestParam String lastName,
                       @RequestParam String phoneNumber,
                       @RequestParam Long volunteerId,
                       @RequestParam Long petId) {
        return service.edit(id, firstName, lastName, phoneNumber, volunteerId, petId);
    }

    @DeleteMapping("{id}")
    public Parent delete(@PathVariable Long id) {
        return service.delete(id);
    }
}