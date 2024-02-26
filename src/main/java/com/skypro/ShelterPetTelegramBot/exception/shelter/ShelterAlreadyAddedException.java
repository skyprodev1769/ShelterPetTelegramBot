package com.skypro.ShelterPetTelegramBot.exception.shelter;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpStatusCodeException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ShelterAlreadyAddedException extends HttpStatusCodeException {
    public ShelterAlreadyAddedException() {
        super(HttpStatus.BAD_REQUEST, "Приют с такими данными уже был добавлен!");
    }
}