package com.skypro.ShelterPetTelegramBot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpStatusCodeException;

import static com.skypro.ShelterPetTelegramBot.utils.Exceptions.RECIPIENT_NOT_FOUND;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RecipientNotFoundException extends HttpStatusCodeException {
    public RecipientNotFoundException() {
        super(HttpStatus.NOT_FOUND, RECIPIENT_NOT_FOUND);
    }
}