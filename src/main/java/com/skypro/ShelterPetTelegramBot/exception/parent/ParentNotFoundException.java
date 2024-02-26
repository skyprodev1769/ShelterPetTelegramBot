package com.skypro.ShelterPetTelegramBot.exception.parent;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpStatusCodeException;

import static com.skypro.ShelterPetTelegramBot.utils.Exceptions.PARENT_NOT_FOUND;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ParentNotFoundException extends HttpStatusCodeException {
    public ParentNotFoundException() {
        super(HttpStatus.NOT_FOUND, PARENT_NOT_FOUND);
    }
}