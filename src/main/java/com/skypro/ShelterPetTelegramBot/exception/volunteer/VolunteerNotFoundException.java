package com.skypro.ShelterPetTelegramBot.exception.volunteer;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpStatusCodeException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class VolunteerNotFoundException extends HttpStatusCodeException {
    public VolunteerNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Волонтер не найден!");
    }
}