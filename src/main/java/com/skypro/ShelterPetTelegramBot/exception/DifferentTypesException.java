package com.skypro.ShelterPetTelegramBot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpStatusCodeException;

import static com.skypro.ShelterPetTelegramBot.utils.Exceptions.DIFFERENT_TYPES;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DifferentTypesException extends HttpStatusCodeException {
    public DifferentTypesException() {
        super(HttpStatus.BAD_REQUEST, DIFFERENT_TYPES);
    }
}