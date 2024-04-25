package com.skypro.ShelterPetTelegramBot.exception.pet;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpStatusCodeException;

import static com.skypro.ShelterPetTelegramBot.utils.Exceptions.PET_ALREADY_ADDED;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PetAlreadyAddedException extends HttpStatusCodeException {
    public PetAlreadyAddedException() {
        super(HttpStatus.BAD_REQUEST, PET_ALREADY_ADDED);
    }
}