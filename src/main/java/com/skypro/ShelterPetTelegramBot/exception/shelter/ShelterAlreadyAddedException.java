package com.skypro.ShelterPetTelegramBot.exception.shelter;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpStatusCodeException;

import static com.skypro.ShelterPetTelegramBot.utils.Exceptions.SHELTER_ALREADY_ADDED;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ShelterAlreadyAddedException extends HttpStatusCodeException {
    public ShelterAlreadyAddedException() {
        super(HttpStatus.BAD_REQUEST, SHELTER_ALREADY_ADDED);
    }
}