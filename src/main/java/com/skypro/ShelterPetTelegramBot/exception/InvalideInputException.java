package com.skypro.ShelterPetTelegramBot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpStatusCodeException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalideInputException extends HttpStatusCodeException {
    public InvalideInputException() {
        super(HttpStatus.BAD_REQUEST, "Введено некорректное значение!");
    }
}