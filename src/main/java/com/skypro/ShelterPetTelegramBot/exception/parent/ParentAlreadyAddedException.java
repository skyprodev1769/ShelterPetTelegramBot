package com.skypro.ShelterPetTelegramBot.exception.parent;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpStatusCodeException;

import static com.skypro.ShelterPetTelegramBot.utils.Exceptions.PARENT_ALREADY_ADDED;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ParentAlreadyAddedException extends HttpStatusCodeException {
    public ParentAlreadyAddedException() {
        super(HttpStatus.BAD_REQUEST, PARENT_ALREADY_ADDED);
    }
}