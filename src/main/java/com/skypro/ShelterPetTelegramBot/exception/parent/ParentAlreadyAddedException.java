package com.skypro.ShelterPetTelegramBot.exception.parent;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpStatusCodeException;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ParentAlreadyAddedException extends HttpStatusCodeException {
    public ParentAlreadyAddedException() {
        super(HttpStatus.BAD_REQUEST, "Усыновитель с такими данными уже был добавлен!");
    }
}