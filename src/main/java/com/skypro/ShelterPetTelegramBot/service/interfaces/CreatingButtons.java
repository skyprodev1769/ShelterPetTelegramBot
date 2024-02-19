package com.skypro.ShelterPetTelegramBot.service.interfaces;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface CreatingButtons {

    SendMessage createButtonsForChoiceRegistration(Long chatId, String text);

    SendMessage createButtonForRegistration(Long chatId, String text);

    SendMessage createButtonForGetInfoAboutShelter(Long chatId, String text);

    SendMessage createButtonsForGetInfoAboutProcess(Long chatId, String text);

    SendMessage createButtonForCallVolunteer(Long chatId, String text);
}