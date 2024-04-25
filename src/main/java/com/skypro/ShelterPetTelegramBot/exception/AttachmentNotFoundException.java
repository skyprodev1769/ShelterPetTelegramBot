package com.skypro.ShelterPetTelegramBot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpStatusCodeException;

import static com.skypro.ShelterPetTelegramBot.utils.Exceptions.ATTACHMENT_NOT_FOUND;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AttachmentNotFoundException extends HttpStatusCodeException {
    public AttachmentNotFoundException() {
        super(HttpStatus.NOT_FOUND, ATTACHMENT_NOT_FOUND);
    }
}