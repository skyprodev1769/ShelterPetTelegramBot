package com.skypro.ShelterPetTelegramBot.exception.number;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpStatusCodeException;

import static com.skypro.ShelterPetTelegramBot.utils.Exceptions.NUMBER_ALREADY_ADDED;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NumberAlreadyAddedException extends HttpStatusCodeException {
    public NumberAlreadyAddedException() {
        super(HttpStatus.BAD_REQUEST, NUMBER_ALREADY_ADDED);
    }
}