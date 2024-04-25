package com.skypro.ShelterPetTelegramBot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpStatusCodeException;

import static com.skypro.ShelterPetTelegramBot.utils.Exceptions.REPORT_NOT_FOUND;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ReportNotFoundException extends HttpStatusCodeException {
    public ReportNotFoundException() {
        super(HttpStatus.NOT_FOUND, REPORT_NOT_FOUND);
    }
}