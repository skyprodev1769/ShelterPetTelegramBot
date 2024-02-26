package com.skypro.ShelterPetTelegramBot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpStatusCodeException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalideNumberException extends HttpStatusCodeException {
    public InvalideNumberException() {
        super(HttpStatus.BAD_REQUEST, "Введен некорректный номер телефона!");
    }
}