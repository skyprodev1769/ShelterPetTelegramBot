package com.skypro.ShelterPetTelegramBot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpStatusCodeException;

import static com.skypro.ShelterPetTelegramBot.utils.Exceptions.PET_STATUS;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PetStatusException extends HttpStatusCodeException {
    public PetStatusException() {
        super(HttpStatus.BAD_REQUEST, PET_STATUS);
    }
}