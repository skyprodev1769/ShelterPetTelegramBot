package com.skypro.ShelterPetTelegramBot.exception.volunteer;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpStatusCodeException;

import static com.skypro.ShelterPetTelegramBot.utils.Exceptions.VOLUNTEER_ALREADY_ADDED;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class VolunteerAlreadyAddedException extends HttpStatusCodeException {
    public VolunteerAlreadyAddedException() {
        super(HttpStatus.BAD_REQUEST, VOLUNTEER_ALREADY_ADDED);
    }
}