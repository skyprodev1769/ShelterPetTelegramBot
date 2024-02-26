package com.skypro.ShelterPetTelegramBot.exception.pet;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpStatusCodeException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PetAlreadyAddedException extends HttpStatusCodeException {
    public PetAlreadyAddedException() {
        super(HttpStatus.BAD_REQUEST, "Животное с такими данными уже было добавлено!");
    }
}