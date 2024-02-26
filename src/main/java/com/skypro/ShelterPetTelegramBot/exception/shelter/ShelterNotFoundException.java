package com.skypro.ShelterPetTelegramBot.exception.shelter;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpStatusCodeException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ShelterNotFoundException extends HttpStatusCodeException {
    public ShelterNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Приют не найден!");
    }
}