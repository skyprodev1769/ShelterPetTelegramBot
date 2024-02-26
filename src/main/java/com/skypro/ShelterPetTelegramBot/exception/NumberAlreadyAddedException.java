package com.skypro.ShelterPetTelegramBot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpStatusCodeException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NumberAlreadyAddedException extends HttpStatusCodeException {
    public NumberAlreadyAddedException() {
        super(HttpStatus.BAD_REQUEST, "Данный номер телефона уже используется!");
    }
}