package com.skypro.ShelterPetTelegramBot.exception.pet;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpStatusCodeException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PetNotFoundException extends HttpStatusCodeException {
    public PetNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Животное не найдено!");
    }
}