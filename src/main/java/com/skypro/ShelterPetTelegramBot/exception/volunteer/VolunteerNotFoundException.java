package com.skypro.ShelterPetTelegramBot.exception.volunteer;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpStatusCodeException;

import static com.skypro.ShelterPetTelegramBot.utils.Exceptions.VOLUNTEER_NOT_FOUND;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class VolunteerNotFoundException extends HttpStatusCodeException {
    public VolunteerNotFoundException() {
        super(HttpStatus.NOT_FOUND, VOLUNTEER_NOT_FOUND);
    }
}