package com.skypro.ShelterPetTelegramBot.exception.parent;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpStatusCodeException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ParentNotFoundException extends HttpStatusCodeException {
    public ParentNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Усыновитель не найден!");
    }
}